package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.QueueDAO;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;

public class WorkflowSweeperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowSweeper.class);

    private final ConductorProperties properties = mock(ConductorProperties.class);
    private final WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
    private final WorkflowRepairService workflowRepairService = mock(WorkflowRepairService.class);
    private final QueueDAO queueDAO = new QueueDAOImpl();

    private WorkflowSweeper workflowSweeper;

    @Captor
    private ArgumentCaptor<String> workflowIdCaptor;

    @Before
    public void setup() {
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);

        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        WorkflowContext.set(workflowContext);

        this.workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync("testWorkflowId");
        future.join();
        verify(workflowExecutor).decide("testWorkflowId");
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";
        TaskModel taskModel = createTaskModel();
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setTasks(Arrays.asList(taskModel));

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO).remove(eq(DECIDER_QUEUE), eq(workflowId));
        verify(queueDAO).setUnackTimeout(
                eq(DECIDER_QUEUE), eq(workflowId), anyLong());

        if (workflowModel.getStatus() != null && workflowModel.getStatus().isTerminal()) {
            verify(queueDAO, never()).setUnackTimeout(
                    eq(DECIDER_QUEUE), eq(workflowId), anyLong());
        }
    }

    @Test
    public void testSweepNotFound() {
        String workflowId = "testWorkflowId";

        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException());

        workflowSweeper.sweep(workflowId);

        verify(queueDAO).remove(eq(DECIDER_QUEUE), eq(workflowId));
        verify(LOGGER).info(
                "Workflow NOT found for id:{}. Removed it from decider queue", workflowId);
    }

    @Test
    public void testSweepException() {
        String workflowId = "testWorkflowId";

        WorkflowModel workflowModel = new WorkflowModel();

        when(workflowExecutor.decide(workflowId)).thenThrow(new Exception());

        workflowSweeper.sweep(workflowId);

        verify(Monitors).error(eq(WorkflowSweeper.class.getSimpleName()), "sweep");
        verify(LOGGER).error(
                "Error running sweep for " + workflowId, any(Exception.class));
        verify(queueDAO).setUnackTimeout(
                eq(DECIDER_QUEUE), eq(workflowId), anyLong());
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = createTaskModel();
        workflowModel.setTasks(Arrays.asList(taskModel));

        long workflowOffsetTimeout = 5L;

        when(workflowModel.getTasks().get(0).getStatus()).thenReturn(Status.IN_PROGRESS);
        when(workflowModel.getTasks().get(0).getTaskType()).thenReturn(TaskType.TASK_TYPE_NORMAL);

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        verify(queueDAO).setUnackTimeout(
                eq(DECIDER_QUEUE), eq(workflowModel.getWorkflowId()), eq(6L * 1000L));
    }

    @Test
    public void testUnackWaitTask() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = createTaskModel();
        workflowModel.setTasks(Arrays.asList(taskModel));

        long workflowOffsetTimeout = 5L;

        when(workflowModel.getTasks().get(0).getStatus()).thenReturn(Status.IN_PROGRESS);
        when(workflowModel.getTasks().get(0).getTaskType()).thenReturn(TaskType.TASK_TYPE_WAIT);

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        verify(queueDAO).setUnackTimeout(
                eq(DECIDER_QUEUE), eq(workflowModel.getWorkflowId()), eq(6L * 1000L));
    }

    @Test
    public void testUnackScheduledTask() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = createTaskModel();
        workflowModel.setTasks(Arrays.asList(taskModel));

        long workflowOffsetTimeout = 5L;

        when(workflowModel.getTasks().get(0).getStatus()).thenReturn(Status.SCHEDULED);

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        verify(queueDAO).setUnackTimeout(
                eq(DECIDER_QUEUE), eq(workflowModel.getWorkflowId()), eq(6L * 1000L));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 5L;

        long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(jitter >= (workflowOffsetTimeout / 3) && jitter <= (2 * (workflowOffsetTimeout / 3) + 1));
    }

    private TaskModel createTaskModel() {
        return new TaskModel();
    }
}