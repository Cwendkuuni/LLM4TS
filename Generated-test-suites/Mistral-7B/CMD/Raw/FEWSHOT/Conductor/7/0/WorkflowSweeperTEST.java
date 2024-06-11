package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.SchedulerConfiguration;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;

@SuppressWarnings("unchecked")
public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private ConductorProperties properties;

    @Mock
    private Monitors monitors;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @VisibleForTesting
    private static final String WORKFLOW_ID = "workflowID";
    @VisibleForTesting
    private static final String WORKFLOW_NAME = "workflowName";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(60L);
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);
        when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong())).thenReturn(true);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(workflowExecutor, workflowRepairService, queueDAO, monitors);
    }

    @Test
    public void testSweepAsync() {
        workflowSweeper.sweepAsync(WORKFLOW_ID).join();
        verify(workflowSweeper).sweep(WORKFLOW_ID);
    }

    @Test
    public void testSweep_NotFoundException() {
        doThrow(new NotFoundException("Test Exception")).when(queueDAO).get(DECIDER_QUEUE, WORKFLOW_ID);
        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(monitors).error(WorkflowSweeper.class.getSimpleName(), "sweep");
        verify(logFactory).info("Workflow NOT found for id:{}. Removed it from decider queue", WORKFLOW_ID,
                argThat(isA(NotFoundException.class)));
    }

    @Test
    public void testSweep_Exception() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        List<TaskModel> tasks = Arrays.asList(new TaskModel());
        workflowModel.setTasks(tasks);

        doThrow(new RuntimeException("Test Exception")).when(workflowExecutor).decide(WORKFLOW_ID);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, argThat(gt(0L)));
        verify(monitors).error(WorkflowSweeper.class.getSimpleName(), "sweep");
        verify(logFactory).warn("Error running sweep for " + WORKFLOW_ID, argThat(isA(RuntimeException.class)));
    }

    @Test
    public void testSweep_WorkflowFound() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        List<TaskModel> tasks = Arrays.asList(new TaskModel(), new TaskModel());
        workflowModel.setTasks(tasks);

        WorkflowContext workflowContext = new WorkflowContext("testAppId");

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(workflowRepairService).verifyAndRepairWorkflowTasks(WORKFLOW_ID);
        verify(workflowModel).getStatus();
        verify(workflowModel).getStatus().isTerminal();
        if (workflowModel.getStatus().isTerminal()) {
            verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
        } else {
            long workflowOffsetTimeout = workflowSweeper.workflowOffsetWithJitter(60L);
            verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, workflowOffsetTimeout * 1000);
        }
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        workflowModel.getTasks().add(taskModel);

        long workflowOffsetTimeout = 60L;

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        Optional<TaskDef> taskDefinition = Optional.of(new TaskDef());
        when(taskModel.getTaskDefinition()).thenReturn(taskDefinition);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, argThat(gt(0L)));
    }

    @Test
    public void testUnack_TaskTypeWait() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        workflowModel.getTasks().add(taskModel);

        long workflowOffsetTimeout = 60L;

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        Optional<TaskDef> taskDefinition = Optional.of(new TaskDef());
        when(taskModel.getTaskDefinition()).thenReturn(taskDefinition);

        long expectedTimeout = workflowOffsetTimeout + 1;
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, expectedTimeout * 1000);
    }

    @Test
    public void testUnack_TaskTypeHuman() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_HUMAN);
        workflowModel.getTasks().add(taskModel);

        long workflowOffsetTimeout = 60L;

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        Optional<TaskDef> taskDefinition = Optional.of(new TaskDef());
        when(taskModel.getTaskDefinition()).thenReturn(taskDefinition);

        long expectedTimeout = workflowOffsetTimeout + 1;
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, expectedTimeout * 1000);
    }

    @Test
    public void testUnack_TaskTypeNormal() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.SCHEDULED);
        workflowModel.getTasks().add(taskModel);

        long workflowOffsetTimeout = 60L;

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        WorkflowDefinition workflowDefinition = new WorkflowDefinition();
        workflowDefinition.setTimeoutSeconds(60L);
        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDefinition);

        long expectedTimeout = workflowOffsetTimeout + 1;
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, expectedTimeout * 1000);
    }

    @Test
    public void testUnack_NoTaskDefinition() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        taskModel.setStatus(Status.SCHEDULED);
        workflowModel.getTasks().add(taskModel);

        long workflowOffsetTimeout = 60L;

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        WorkflowDefinition workflowDefinition = new WorkflowDefinition();
        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDefinition);

        long expectedTimeout = workflowOffsetTimeout + 1;
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, expectedTimeout * 1000);
    }
}