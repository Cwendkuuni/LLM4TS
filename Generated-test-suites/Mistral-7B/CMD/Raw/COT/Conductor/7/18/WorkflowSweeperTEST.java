package com.netflix.conductor.core.reconciliation.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.WorkflowModel;
import com.netflix.conductor.core.reconciliation.QueueDAO;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.metrics.Monitors;

public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private Monitors monitors;

    @Mock
    private TaskDef taskDef;

    @Mock
    private ConductorProperties properties;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("appId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);
        when(queueDAO.remove(DECIDER_QUEUE, anyString())).thenReturn(true);
        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, anyString(), anyLong())).thenReturn(true);
        when(workflowExecutor.decide(anyString())).thenReturn(new WorkflowModel());
    }

    @Test
    public void testSweepAsync() throws InterruptedException, ExecutionException {
        workflowSweeper.sweepAsync("workflowId");
        verify(workflowSweeper).sweep("workflowId");
    }

    @Test
    public void testSweep_WorkflowFoundAndTerminal() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(Status.TERMINALED);
        when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);

        workflowSweeper.sweep("workflowId");

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, "workflowId");
    }

    @Test
    public void testSweep_WorkflowNotFound() {
        doThrow(new NotFoundException("Workflow not found"))
                .when(workflowExecutor).decide(anyString());

        workflowSweeper.sweep("workflowId");

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, "workflowId");
        verify(monitors, times(1)).error(WORKFLOW_SWEEPER_CLASS, "sweep");
    }

    @Test
    public void testSweep_EXceptionDuringSweep() {
        doThrow(new RuntimeException("Exception during sweep"))
                .when(workflowSweeper)
                .sweep("workflowId");

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, "workflowId");
        verify(monitors, times(1)).error(WORKFLOW_SWEEPER_CLASS, "sweep");
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        List<TaskModel> tasks = Arrays.asList(taskModel);
        workflowModel.setTasks(tasks);

        when(taskModel.getStatus()).thenReturn(Status.IN_PROGRESS);
        when(taskModel.getTaskType()).thenReturn(TaskType.TASK_TYPE_WAIT);

        workflowSweeper.unack(workflowModel, 5L);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), 6L * 1000);
    }

    @Test
    public void testUnack_NoTasksInWorkflow() {
        WorkflowModel workflowModel = new WorkflowModel();

        workflowSweeper.unack(workflowModel, 5L);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), 5L * 1000);
    }

    @Test
    public void testUnack_NoTasksWithInProgressStatus() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        List<TaskModel> tasks = Arrays.asList(taskModel);
        workflowModel.setTasks(tasks);

        when(taskModel.getStatus()).thenReturn(Status.COMPLETED);

        workflowSweeper.unack(workflowModel, 5L);

        verifyZeroInteractions(queueDAO);
    }

    @Test
    public void testUnack_TaskWithNoResponseTimeout() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        List<TaskModel> tasks = Arrays.asList(taskModel);
        workflowModel.setTasks(tasks);

        when(taskModel.getStatus()).thenReturn(Status.IN_PROGRESS);
        when(taskModel.getTaskType()).thenReturn(TaskType.TASK_TYPE_NONE);

        workflowSweeper.unack(workflowModel, 5L);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), 5L * 1000);
    }

    @Test
    public void testUnack_TaskWithResponseTimeout() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        List<TaskModel> tasks = Arrays.asList(taskModel);
        workflowModel.setTasks(tasks);

        when(taskModel.getStatus()).thenReturn(Status.IN_PROGRESS);
        when(taskModel.getTaskType()).thenReturn(TaskType.TASK_TYPE_PROCESS);
        when(taskModel.getResponseTimeoutSeconds()).thenReturn(10L);

        workflowSweeper.unack(workflowModel, 5L);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), 11L * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter_ZeroWorkflowOffsetTimeout() {
        long workflowOffsetTimeout = 0L;

        long workflowOffset = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(workflowOffset >= 0);
    }

    @Test
    public void testWorkflowOffsetWithJitter_NonZeroWorkflowOffsetTimeout() {
        long workflowOffsetTimeout = 5L;

        long workflowOffset = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(workflowOffset >= 2L && workflowOffset <= 8L);
    }
}