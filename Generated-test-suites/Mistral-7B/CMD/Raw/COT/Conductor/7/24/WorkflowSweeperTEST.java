package com.netflix.conductor.core.reconciliation.test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;

import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;

@SuppressWarnings("unchecked")
public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties properties;

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
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        workflowSweeper.sweepAsync(workflowId);
        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";

        // Given
        WorkflowModel workflow = new WorkflowModel();
        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);

        // When
        workflowSweeper.sweep(workflowId);

        // Then
        verify(queueDAO, times(1)).remove(eq(DECIDER_QUEUE), eq(workflowId));

        // Test for workflow not found
        doThrow(new NotFoundException("Workflow not found")).when(workflowExecutor)
                .decide(workflowId);
        workflowSweeper.sweep(workflowId);
        verify(queueDAO, times(1)).remove(eq(DECIDER_QUEUE), eq(workflowId));
        verify(logFactory, times(1)).info("Workflow NOT found for id:{}. Removed it from decider queue",
                workflowId, anyString());

        // Test for exception in sweep
        doThrow(new Exception("Test exception in sweep"))
                .when(workflowSweeper)
                .sweep(workflowId);
        workflowSweeper.sweep(workflowId);
        verify(monitors, times(1)).error(eq(WorkflowSweeper.class.getSimpleName()),
                eq("sweep"), any(Throwable.class));
        verify(logFactory, times(1)).error("Error running sweep for " + workflowId,
                any(Throwable.class));
    }

    @Test
    public void testUnack() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);

        // Given
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(new TaskType());
        workflowModel.setTasks(new TaskModel[]{taskModel});

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        // When
        workflowSweeper.sweep(workflowId);

        // Then
        long workflowOffsetTimeout = 45L;
        Long postponeDuration = workflowSweeper.unack(workflowModel, workflowOffsetTimeout);
        assertEquals(46L, postponeDuration.longValue());

        // Test for workflow not found
        doThrow(new NotFoundException("Workflow not found"))
                .when(workflowExecutor)
                .decide(workflowId);
        workflowSweeper.sweep(workflowId);
        verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId),
                eq(45L * 1000L));

        // Test for task not found
        taskModel.setStatus(Status.FAILED);
        workflowModel.setTasks(new TaskModel[]{taskModel});
        workflowSweeper.sweep(workflowId);
        verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId),
                eq(workflowOffsetTimeout * 1000L));

        // Test for wait and human tasks
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(new TaskType().setTaskType("WAIT"));
        workflowModel.setTasks(new TaskModel[]{taskModel});
        workflowSweeper.sweep(workflowId);
        verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId),
                eq((workflowOffsetTimeout + 1) * 1000L));

        taskModel.setTaskType(new TaskType().setTaskType("HUMAN"));
        workflowModel.setTasks(new TaskModel[]{taskModel});
        workflowSweeper.sweep(workflowId);
        verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId),
                eq((workflowOffsetTimeout + 1) * 1000L));

        // Test for scheduled tasks
        taskModel.setStatus(Status.SCHEDULED);
        taskModel.setTaskDefinition(new TaskDef());
        workflowModel.setTasks(new TaskModel[]{taskModel});
        workflowSweeper.sweep(workflowId);
        verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId),
                eq((workflowModel.getWorkflowDefinition().getTimeoutSeconds() + 1) * 1000L));

        // Test for workflow definition not present
        taskModel.setTaskDefinition(null);
        workflowModel.setTasks(new TaskModel[]{taskModel});
        workflowSweeper.sweep(workflowId);
        verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq(workflowId),
                eq((workflowModel.getWorkflowDefinition().getTimeoutSeconds() + 1) * 1000L));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45L;

        // Given
        long jitter = new Random().nextInt((int) (2 * workflowOffsetTimeout / 3 + 1))
                - (workflowOffsetTimeout / 3);

        // When
        long workflowOffset = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        // Then
        assertTrue(workflowOffset >= (workflowOffsetTimeout - workflowOffsetTimeout / 3)
                && workflowOffset <= (workflowOffsetTimeout + workflowOffsetTimeout / 3));
    }
}