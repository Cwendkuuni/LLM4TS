package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.AsyncResult;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;

@SpringBootTest
public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private Monitors monitors;

    @Mock
    private Optional<WorkflowRepairService> workflowRepairService;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    private static final String WORKFLOW_ID = "workflowId";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweepAsync() {
        // Given
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.of(WORKFLOW_ID));
        when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(true);

        // When
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(WORKFLOW_ID);

        // Then
        verify(queueDAO, times(1)).get(DECIDER_QUEUE, WORKFLOW_ID);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
        assertNull(result.join());
    }

    @Test
    public void testSweepWithNotFoundException() {
        // Given
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.empty());

        // When
        workflowSweeper.sweep(WORKFLOW_ID);

        // Then
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(logBackender, times(1)).info(
                "Workflow NOT found for id:{}. Removed it from decider queue", WORKFLOW_ID);
    }

    @Test
    public void testSweepWithException() {
        // Given
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.of(WORKFLOW_ID));
        when(workflowExecutor.decide(WORKFLOW_ID))
                .thenThrow(new RuntimeException("Test Exception"));

        // When
        workflowSweeper.sweep(WORKFLOW_ID);

        // Then
        verify(queueDAO, times(1)).get(DECIDER_QUEUE, WORKFLOW_ID);
        verify(queueDAO, times(1)).setUnackTimeout(
                DECIDER_QUEUE, WORKFLOW_ID, workflowOffsetWithJitter(30L) * 1000);
        Monitors.error(eq(WorkflowSweeper.class), eq("sweep"), isA(RuntimeException.class));
        verify(logBackender, times(1)).error("Error running sweep for " + WORKFLOW_ID,
                isA(RuntimeException.class));
    }

    @Test
    public void testSweepWithWorkflow() {
        // Given
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId(WORKFLOW_ID);
        workflow.setWorkflowName("Test Workflow");
        workflow.setStatus(Status.RUNNING);
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.of(WORKFLOW_ID));
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflow);

        // When
        workflowSweeper.sweep(WORKFLOW_ID);

        // Then
        verify(queueDAO, times(1)).get(DECIDER_QUEUE, WORKFLOW_ID);
        verify(queueDAO, times(1)).setUnackTimeout(
                DECIDER_QUEUE, WORKFLOW_ID, workflowOffsetWithJitter(30L) * 1000);
        verify(monitors, times(1)).recordUnackTime("Test Workflow", anyLong());
    }

    @Test
    public void testUnack() {
        // Given
        TaskModel taskModel = new TaskModel();
        taskModel.setWorkflowId(WORKFLOW_ID);
        taskModel.setTaskId("taskId");
        taskModel.setTaskName("Test Task");
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(new TaskType("Test Task Type"));
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setTasks(Arrays.asList(taskModel));

        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(Optional.of(WORKFLOW_ID));
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        // When
        workflowSweeper.unack(workflowModel, 30L);

        // Then
        verify(queueDAO, times(1)).setUnackTimeout(
                DECIDER_QUEUE, WORKFLOW_ID, 31L * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        // Given
        long workflowOffsetTimeout = 30L;

        // When
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        // Then
        assertTrue(result >= 20L && result <= 40L);
    }
}