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
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.test.TestUtils;

public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflow-1";
    @Mock
    private WorkflowExecutor workflowExecutor;
    @Mock
    private WorkflowRepairService workflowRepairService;
    @Mock
    private QueueDAO queueDAO;
    @Mock
    private Monitors monitors;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(1000L);
        when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(null);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(new WorkflowModel());
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException("Workflow not found"));
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(new WorkflowModel().setStatus(Status.RUNNING));
        when(workflowRepairService.verifyAndRepairWorkflowTasks(WORKFLOW_ID)).thenReturn(Collections.emptyList());
        when(monitors.recordUnackTime(anyString(), anyLong())).thenReturn(null);
    }

    @Test
    public void testSweep_WorkflowNotFound_RemovedFromQueue() {
        workflowSweeper.sweep(WORKFLOW_ID);
        verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweep_WorkflowFound_RepairedTasks() {
        WorkflowModel workflowModel = new WorkflowModel().setWorkflowName("test-workflow");
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);
        List<TaskModel> tasks = Arrays.asList(
                new TaskModel().setTaskId("task-1").setStatus(Status.IN_PROGRESS),
                new TaskModel().setTaskId("task-2").setStatus(Status.SCHEDULED),
                new TaskModel().setTaskId("task-3").setStatus(Status.FAILED)
        );
        when(workflowModel.getTasks()).thenReturn(tasks);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(workflowRepairService).verifyAndRepairWorkflowTasks(WORKFLOW_ID);
    }

    @Test
    public void testSweep_WorkflowRunning_UnackTimeoutSet() {
        WorkflowModel workflowModel = new WorkflowModel().setWorkflowName("test-workflow").setStatus(Status.RUNNING);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);

        long workflowOffsetTimeout = 45L;
        long postponeDurationSeconds = 50L;
        long expectedUnackTimeout = workflowOffsetTimeout + postponeDurationSeconds;

        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowModel.getWorkflowId(), expectedUnackTimeout * 1000);
    }

    @Test(expected = IllegalStateException.class)
    public void testSweep_WorkflowNotFound_ThrowsIllegalStateException() {
        workflowSweeper.sweep(WORKFLOW_ID);
        verify(queueDAO, never()).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(workflowRepairService, never()).verifyAndRepairWorkflowTasks(WORKFLOW_ID);
        workflowSweeper.sweep(WORKFLOW_ID);
    }

    @Test
    public void testUnack_TaskInProgress_UnackTimeoutSet() {
        TaskModel taskModel = new TaskModel().setTaskId("task-1").setStatus(Status.IN_PROGRESS);
        WorkflowModel workflowModel = new WorkflowModel().setTasks(Collections.singletonList(taskModel));

        workflowSweeper.unack(workflowModel, 45L);

        long expectedUnackTimeout = 46L;
        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowModel.getWorkflowId(), expectedUnackTimeout * 1000);
    }

    @Test
    public void testUnack_TaskScheduled_UnackTimeoutSet() {
        TaskModel taskModel = new TaskModel().setTaskId("task-1").setStatus(Status.SCHEDULED);
        WorkflowModel workflowModel = new WorkflowModel().setTasks(Collections.singletonList(taskModel));

        workflowSweeper.unack(workflowModel, 45L);

        long expectedUnackTimeout = 46L;
        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowModel.getWorkflowId(), expectedUnackTimeout * 1000);
    }

    @Test
    public void testUnack_WorkflowNotFound_UnackTimeoutSet() {
        WorkflowModel workflowModel = new WorkflowModel().setWorkflowName("test-workflow");

        workflowSweeper.unack(workflowModel, 45L);

        long expectedUnackTimeout = 46L;
        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowModel.getWorkflowId(), expectedUnackTimeout * 1000);
    }
}