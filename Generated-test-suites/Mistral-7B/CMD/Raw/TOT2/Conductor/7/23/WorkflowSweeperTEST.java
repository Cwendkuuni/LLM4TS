package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.Async;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.daos.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.metrics.Monitors;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAOTest;
import com.netflix.conductor.model.TaskModel.Status;
import com.netflix.conductor.model.WorkflowModelTest;

@SuppressWarnings("resultswithbettervalues")
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
    private WorkflowSweeper sweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweepAsyncMethod() {
        String workflowId = "test-workflow-id";
        sweeper.sweepAsync(workflowId);
        verify(sweeper).sweep(workflowId);
    }

    @Test
    public void testSweepMethod_WorkflowFoundAndTerminal() {
        String workflowId = "test-workflow-id";
        WorkflowModel workflow = WorkflowModelTest.createValidWorkflowModel();
        when(queueDAO.read(DECIDER_QUEUE, workflowId)).thenReturn(workflow);
        when(workflow.getStatus()).thenReturn(Status.TERMINAL);

        sweeper.sweep(workflowId);

        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verifyNoMoreInteractions(workflowExecutor, workflowRepairService, monitors);
    }

    @Test
    public void testSweepMethod_WorkflowNotFound() {
        String workflowId = "test-workflow-id";
        when(queueDAO.read(DECIDER_QUEUE, workflowId)).thenThrow(new NotFoundException());

        sweeper.sweep(workflowId);

        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verify(monitors).error(eq(WorkflowSweeper.class.getName()), eq("sweep"), any(Throwable.class));
    }

    @Test
    public void testSweepMethod_ExceptionThrown() {
        String workflowId = "test-workflow-id";
        WorkflowModel workflow = WorkflowModelTest.createValidWorkflowModel();
        when(queueDAO.read(DECIDER_QUEUE, workflowId)).thenReturn(workflow);

        when(workflowExecutor.decide(workflowId))
                .thenThrow(new RuntimeException("Test exception thrown"));

        sweeper.sweep(workflowId);

        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verify(monitors).error(eq(WorkflowSweeper.class.getName()), eq("sweep"), any(Throwable.class));
    }

    @Test
    public void testUnackMethodWithTaskInProgress() {
        String workflowId = "test-workflow-id";
        WorkflowModel workflow = WorkflowModelTest.createValidWorkflowModel();
        List<TaskModel> tasks = Arrays.asList(
                TaskModelTest.createValidTaskModel("task1", Status.IN_PROGRESS),
                TaskModelTest.createValidTaskModel("task2", Status.COMPLETED));
        workflow.setTasks(tasks);

        long workflowOffsetTimeout = 45 * 1000L;

        sweeper.unack(workflow, workflowOffsetTimeout);

        verify(queueDAO).setUnackTimeout(
                eq(DECIDER_QUEUE), eq(workflowId), eq(workflowOffsetTimeout * 1000L));
    }

    @Test
    public void testUnackMethodWithTaskScheduled() {
        String workflowId = "test-workflow-id";
        WorkflowModel workflow = WorkflowModelTest.createValidWorkflowModel();
        List<TaskModel> tasks = Arrays.asList(
                TaskModelTest.createValidTaskModel("task1", Status.SCHEDULED),
                TaskModelTest.createValidTaskModel("task2", Status.COMPLETED));
        workflow.setTasks(tasks);

        long workflowOffsetTimeout = 45 * 1000L;

        sweeper.unack(workflow, workflowOffsetTimeout);

        Optional<TaskDef> taskDefinition = TaskModelTest.createValidTaskDef("task1");
        when(tasks.get(0).getTaskDefinition()).thenReturn(taskDefinition.get());
        when(taskDefinition.get().getPollTimeoutSeconds()).thenReturn(15L);

        sweeper.unack(workflow, workflowOffsetTimeout);

        verify(queueDAO).setUnackTimeout(
                eq(DECIDER_QUEUE), eq(workflowId), eq(workflowOffsetTimeout * 1000L + 15 * 1000L));
    }

    @Test
    public void testWorkflowOffsetWithJitterMethod() {
        long workflowOffsetTimeout = 45 * 1000L;

        long expectedMin = workflowOffsetTimeout - (workflowOffsetTimeout / 3);
        long expectedMax = workflowOffsetTimeout + (workflowOffsetTimeout / 3);

        long jitter = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(jitter >= expectedMin);
        assertTrue(jitter <= expectedMax);
    }

    @Test
    public void testSweepMethod_WorkflowNotFoundWithUnackTimeout() {
        String workflowId = "test-workflow-id";

        CompletableFuture<Void> future = new CompletableFuture<>();
        when(queueDAO.read(DECIDER_QUEUE, workflowId)).thenReturn(null);

        sweeper.sweep(workflowId);

        verify(queueDAO).setUnackTimeout(
                eq(DECIDER_QUEUE), eq(workflowId), anyLong());
        verifyNoMoreInteractions(workflowExecutor, workflowRepairService, monitors);
    }
}