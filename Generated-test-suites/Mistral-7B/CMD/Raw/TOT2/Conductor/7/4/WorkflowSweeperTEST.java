package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.model.WorkflowModel.WorkflowDefinition;
import com.netflix.conductor.test.TestUtils;

import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;

@SuppressWarnings("unchecked")
public class WorkflowSweeperTest {

    private static final Logger LOGGER = Mockito.mock(Logger.class);
    private static final Random RANDOM = new Random();

    private static final String WORKFLOW_ID = "123";
    private static final String WORKFLOW_NAME = "testWorkflow";
    private static final long WORKFLOW_OFFSET_TIMEOUT = 45000;

    private WorkflowSweeper sweeper;
    private ConductorProperties properties;
    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;
    private QueueDAO queueDAO;

    @Before
    public void setUp() {
        properties = Mockito.mock(ConductorProperties.class);
        queueDAO = Mockito.mock(QueueDAO.class);
        workflowExecutor = Mockito.mock(WorkflowExecutor.class);
        workflowRepairService = Mockito.mock(WorkflowRepairService.class);

        sweeper = new WorkflowSweeper(
                workflowExecutor,
                Optional.of(workflowRepairService),
                properties,
                queueDAO);

        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(WORKFLOW_OFFSET_TIMEOUT);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(new WorkflowModel());

        when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(true);
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong())).thenReturn(true);
    }

    @After
    public void tearDown() {
        reset(properties, queueDAO, workflowExecutor, workflowRepairService);
    }

    @Test
    public void testSweepAsync_WorkflowFound() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setId(WORKFLOW_ID);
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        workflowModel.setStatus(Status.RUNNING);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        CompletableFuture<Void> result = sweeper.sweepAsync(WORKFLOW_ID);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(queueDAO, times(1)).setUnackTimeout(
                DECIDER_QUEUE, WORKFLOW_ID, anyLong());
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(workflowExecutor);
        verify(LOGGER, times(1)).debug("Running sweeper for workflow {}", WORKFLOW_ID);

        result.join();
    }

    @Test
    public void testSweepAsync_WorkflowNotFound() {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException("Workflow not found"));

        CompletableFuture<Void> result = sweeper.sweepAsync(WORKFLOW_ID);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(LOGGER, times(1)).info(
                "Workflow NOT found for id:{}. Removed it from decider queue", WORKFLOW_ID);
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(workflowExecutor);
        verifyNoMoreInteractions(LOGGER);

        result.join();
    }

    @Test
    public void testSweep_WorkflowFound() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setId(WORKFLOW_ID);
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        workflowModel.setStatus(Status.RUNNING);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        sweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(queueDAO, times(1)).setUnackTimeout(
                DECIDER_QUEUE, WORKFLOW_ID, anyLong());
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(workflowExecutor);
        verify(LOGGER, times(1)).debug("Running sweeper for workflow {}", WORKFLOW_ID);
    }

    @Test
    public void testSweep_WorkflowNotFound() {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException("Workflow not found"));

        sweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(LOGGER, times(1)).info(
                "Workflow NOT found for id:{}. Removed it from decider queue", WORKFLOW_ID);
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(workflowExecutor);
        verifyNoMoreInteractions(LOGGER);
    }

    @Test
    public void testUnack_TasksWithResponseTimeout() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setId(WORKFLOW_ID);
        workflowModel.setWorkflowName(WORKFLOW_NAME);

        WorkflowDefinition workflowDefinition = new WorkflowDefinition();
        workflowDefinition.setTimeoutSeconds(WORKFLOW_OFFSET_TIMEOUT);
        workflowModel.setWorkflowDefinition(workflowDefinition);

        List<TaskModel> tasks = Arrays.asList(
                new TaskModel()
                        .setId("1")
                        .setTaskType(TaskType.TASK_TYPE_EXEC)
                        .setStatus(Status.IN_PROGRESS)
                        .setResponseTimeoutSeconds(10000),
                new TaskModel()
                        .setId("2")
                        .setTaskType(TaskType.TASK_TYPE_EXEC)
                        .setStatus(Status.SCHEDULED)
                        .setResponseTimeoutSeconds(5000),
                new TaskModel()
                        .setId("3")
                        .setTaskType(TaskType.TASK_TYPE_EXEC)
                        .setStatus(Status.COMPLETED)
        );
        workflowModel.setTasks(tasks);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        long postponeDurationSeconds = sweeper.unack(workflowModel, WORKFLOW_OFFSET_TIMEOUT);

        verify(queueDAO, times(1)).setUnackTimeout(
                DECIDER_QUEUE, WORKFLOW_ID, postponeDurationSeconds * 1000);
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(workflowExecutor);

        TaskModel taskModel = workflowModel.getTasks().get(0);
        assertEquals(115000, taskModel.getUnackTimeout());
    }

    @Test
    public void testUnack_TasksWithWaitTimeout() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setId(WORKFLOW_ID);
        workflowModel.setWorkflowName(WORKFLOW_NAME);

        WorkflowDefinition workflowDefinition = new WorkflowDefinition();
        workflowDefinition.setTimeoutSeconds(WORKFLOW_OFFSET_TIMEOUT);
        workflowModel.setWorkflowDefinition(workflowDefinition);

        List<TaskModel> tasks = Arrays.asList(
                new TaskModel()
                        .setId("1")
                        .setTaskType(TaskType.TASK_TYPE_WAIT)
                        .setStatus(Status.IN_PROGRESS)
                        .setWaitTimeout(10000)
        );
        workflowModel.setTasks(tasks);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        long postponeDurationSeconds = sweeper.unack(workflowModel, WORKFLOW_OFFSET_TIMEOUT);

        verify(queueDAO, times(1)).setUnackTimeout(
                DECIDER_QUEUE, WORKFLOW_ID, postponeDurationSeconds * 1000);
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(workflowExecutor);

        TaskModel taskModel = workflowModel.getTasks().get(0);
        assertEquals(115000, taskModel.getUnackTimeout());
    }

    @Test
    public void testUnack_TasksWithoutTimeout() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setId(WORKFLOW_ID);
        workflowModel.setWorkflowName(WORKFLOW_NAME);

        WorkflowDefinition workflowDefinition = new WorkflowDefinition();
        workflowDefinition.setTimeoutSeconds(WORKFLOW_OFFSET_TIMEOUT);
        workflowModel.setWorkflowDefinition(workflowDefinition);

        List<TaskModel> tasks = Arrays.asList(
                new TaskModel()
                        .setId("1")
                        .setTaskType(TaskType.TASK_TYPE_EXEC)
                        .setStatus(Status.IN_PROGRESS)
        );
        workflowModel.setTasks(tasks);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        long postponeDurationSeconds = sweeper.unack(workflowModel, WORKFLOW_OFFSET_TIMEOUT);

        verify(queueDAO, times(1)).setUnackTimeout(
                DECIDER_QUEUE, WORKFLOW_ID, postponeDurationSeconds * 1000);
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(workflowExecutor);

        TaskModel taskModel = workflowModel.getTasks().get(0);
        assertEquals(WORKFLOW_OFFSET_TIMEOUT + 1, taskModel.getUnackTimeout());
    }

    @Test
    public void testWorkflowOffsetWithJitter_ExpectedValue() {
        long workflowOffsetTimeout = 45000;

        long result = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(result >= (workflowOffsetTimeout - 15000));
        assertTrue(result <= (workflowOffsetTimeout + 15000));
    }
}