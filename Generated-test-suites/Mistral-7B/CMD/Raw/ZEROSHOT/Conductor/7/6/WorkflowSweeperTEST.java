package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.metrics.Monitors;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.model.WorkflowModel.WorkflowDefinition;

public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflowId";
    private static final String WORKFLOW_NAME = "workflowName";
    private static final long WORKFLOW_OFFSET_TIMEOUT = 45;
    private static final long UNACK_TIMEOUT = 60000;
    private static final long RANDOM_JITTER = 12;
    private static final int WAIT_TO_RETRY_ATTEMPTS = 2;

    @Captor
    ArgumentCaptor<Long> unackTimeoutCaptor;

    private ConductorProperties properties;
    private QueueDAO queueDAO;
    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;
    private Monitors monitors;
    private WorkflowSweeper sweeper;

    @Before
    public void setup() {
        properties = mock(ConductorProperties.class);
        queueDAO = mock(QueueDAO.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        workflowRepairService = mock(WorkflowRepairService.class);
        monitors = mock(Monitors.class);
        sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties,
                queueDAO);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweeperAsyncShouldCallSweeperMethod() {
        when(properties.getWorkflowOffsetTimeout()).thenReturn(WORKFLOW_OFFSET_TIMEOUT);
        when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(true);

        sweeper.sweepAsync(WORKFLOW_ID);

        verify(sweeper, times(1)).sweep(WORKFLOW_ID);
    }

    @Test
    public void testSweeperShouldInitializeLogger() {
        WorkflowModel workflowModel = new WorkflowModel();
        sweeper.sweep(WORKFLOW_ID);

        verify(LOGGER, times(1)).info("WorkflowSweeper initialized.");
    }

    @Test
    public void testSweeperShouldRemoveWorkflowFromQueue() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setStatus(Status.RUNNING);

        when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(true);

        sweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweeperShouldNotRemoveWorkflowIfNotFound() {
        when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(false);

        sweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(LOGGER, times(1)).info("Workflow NOT found for id:{}. Removed it from decider queue",
                WORKFLOW_ID);
    }

    @Test
    public void testSweeperShouldNotRemoveWorkflowWithTerminalStatus() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setStatus(Status.SUCCEEDED);

        when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(false);

        sweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, never()).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(LOGGER, times(1)).debug("Running sweeper for workflow {}", WORKFLOW_ID);
    }

    @Test
    public void testSweeperShouldVerifyAndRepairTasks() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);

        TaskModel taskModel1 = new TaskModel();
        TaskModel taskModel2 = new TaskModel();

        taskModel1.setTaskId("taskId1");
        taskModel1.setStatus(Status.FAILED);
        taskModel1.setTaskType(TaskType.TASK_TYPE_WAIT);

        taskModel2.setTaskId("taskId2");
        taskModel2.setStatus(Status.FAILED);
        taskModel2.setTaskType(TaskType.TASK_TYPE_HUMAN);

        List<TaskModel> tasks = new ArrayList<>();
        tasks.add(taskModel1);
        tasks.add(taskModel2);
        workflowModel.setTasks(tasks);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);
        when(workflowRepairService.verifyAndRepairWorkflowTasks(WORKFLOW_ID)).thenReturn(null);

        sweeper.sweep(WORKFLOW_ID);

        verify(workflowRepairService, times(1)).verifyAndRepairWorkflowTasks(WORKFLOW_ID);
    }

    @Test
    public void testUnackShouldSetUnackTimeoutForTaskWithResponseTimeout() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("taskId");
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setResponseTimeoutSeconds(10);

        when(workflowModel.getTasks()).thenReturn(Arrays.asList(taskModel));
        when(workflowModel.getWorkflowDefinition()).thenReturn(new WorkflowDefinition());

        sweeper.unack(workflowModel, WORKFLOW_OFFSET_TIMEOUT);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID,
                unackTimeoutCaptor.capture());

        long unackTimeout = unackTimeoutCaptor.getValue();

        assertTrue(unackTimeout > WORKFLOW_OFFSET_TIMEOUT);
        assertTrue(unackTimeout <= (WORKFLOW_OFFSET_TIMEOUT + RANDOM_JITTER));
    }

    @Test
    public void testUnackShouldSetUnackTimeoutForTaskWithNoResponseTimeout() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("taskId");
        taskModel.setStatus(Status.IN_PROGRESS);

        when(workflowModel.getTasks()).thenReturn(Arrays.asList(taskModel));
        when(workflowModel.getWorkflowDefinition()).thenReturn(new WorkflowDefinition());

        sweeper.unack(workflowModel, WORKFLOW_OFFSET_TIMEOUT);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID,
                unackTimeoutCaptor.capture());

        long unackTimeout = unackTimeoutCaptor.getValue();

        assertTrue(unackTimeout > WORKFLOW_OFFSET_TIMEOUT);
        assertTrue(unackTimeout <= (WORKFLOW_OFFSET_TIMEOUT + RANDOM_JITTER));
    }

    @Test
    public void testUnackShouldSetUnackTimeoutForScheduledTask() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("taskId");
        taskModel.setStatus(Status.SCHEDULED);

        when(workflowModel.getTasks()).thenReturn(Arrays.asList(taskModel));
        when(workflowModel.getWorkflowDefinition()).thenReturn(new WorkflowDefinition());

        sweeper.unack(workflowModel, WORKFLOW_OFFSET_TIMEOUT);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID,
                unackTimeoutCaptor.capture());

        long unackTimeout = unackTimeoutCaptor.getValue();

        assertTrue(unackTimeout > WORKFLOW_OFFSET_TIMEOUT);
        assertTrue(unackTimeout <= (WORKFLOW_OFFSET_TIMEOUT + RANDOM_JITTER));
    }

    @Test
    public void testUnackShouldNotSetUnackTimeoutForCompletedWorkflow() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setStatus(Status.SUCCEEDED);

        sweeper.unack(workflowModel, WORKFLOW_OFFSET_TIMEOUT);

        verify(queueDAO, never()).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong());
    }

    @Test
    public void testUnackShouldNotSetUnackTimeoutForFailedWorkflow() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setStatus(Status.FAILED);

        sweeper.unack(workflowModel, WORKFLOW_OFFSET_TIMEOUT);

        verify(queueDAO, never()).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitterShouldReturnWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;

        long workflowOffsetWithJitter = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(workflowOffsetWithJitter > workflowOffsetTimeout);
        assertTrue(workflowOffsetWithJitter <= (workflowOffsetTimeout + RANDOM_JITTER));
    }
}