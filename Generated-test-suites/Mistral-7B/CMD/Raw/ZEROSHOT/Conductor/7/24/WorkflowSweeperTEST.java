package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.springframework.scheduling.annotation.AsyncResult;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;

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
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.ofNullable(new WorkflowModel()));

        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);

        result.join();

        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepWithNotFoundException() {
        String workflowId = "testWorkflowId";

        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.empty());

        workflowSweeper.sweep(workflowId);

        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verify(monitors).error(eq(WorkflowSweeper.class.getSimpleName()), eq("sweep"));
    }

    @Test
    public void testSweepWithException() {
        String workflowId = "testWorkflowId";
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);

        WorkflowModel workflowModel = new WorkflowModel();

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.of(workflowModel));

        when(workflowExecutor.decide(workflowId)).thenThrow(new RuntimeException("Test Exception"));

        workflowSweeper.sweep(workflowId);

        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verify(monitors).error(eq(WorkflowSweeper.class.getSimpleName()), eq("sweep"));
    }

    @Test
    public void testVerifyAndRepairWorkflowTasks() {
        String workflowId = "testWorkflowId";

        WorkflowModel workflowModel = new WorkflowModel();

        when(properties.getAppId()).thenReturn("testAppId");

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.of(workflowModel));

        List<TaskModel> tasks = Arrays.asList(
                new TaskModel(),
                new TaskModel(),
                new TaskModel());

        workflowModel.setTasks(tasks);

        when(workflowRepairService.verifyAndRepairWorkflowTasks(workflowId)).thenReturn();

        workflowSweeper.sweep(workflowId);

        verify(workflowRepairService).verifyAndRepairWorkflowTasks(workflowId);
    }

    @Test
    public void testUnack() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = new WorkflowModel();

        when(properties.getAppId()).thenReturn("testAppId");

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.of(workflowModel));

        TaskModel taskModel1 = new TaskModel();
        taskModel1.setStatus(Status.IN_PROGRESS);

        TaskModel taskModel2 = new TaskModel();
        taskModel2.setStatus(Status.SCHEDULED);

        List<TaskModel> tasks = Arrays.asList(taskModel1, taskModel2);

        workflowModel.setTasks(tasks);

        when(workflowModel.getWorkflowDefinition()).thenReturn(new TaskDef());

        long workflowOffsetTimeout = 10L;

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        verify(queueDAO).setUnackTimeout(
                eq(DECIDER_QUEUE),
                eq(workflowModel.getWorkflowId()),
                eq(workflowOffsetTimeout * 1000));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 10L;

        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertFalse(result <= (workflowOffsetTimeout - 3));
        assertFalse(result >= (workflowOffsetTimeout + 3));
    }

    @Test
    public void testDecideReturnsNull() {
        String workflowId = "testWorkflowId";

        when(properties.getAppId()).thenReturn("testAppId");

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.ofNullable(null));

        workflowSweeper.sweep(workflowId);

        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testDecideReturnsWorkflowModel() {
        String workflowId = "testWorkflowId";

        when(properties.getAppId()).thenReturn("testAppId");

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(Optional.of(new WorkflowModel()));

        workflowSweeper.sweep(workflowId);

        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
    }
}