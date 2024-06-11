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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.WorkflowExecutor;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.SchedulerConfiguration;
import com.netflix.conductor.core.execution.WorkflowExecutorTestUtil;
import com.netflix.conductor.core.metrics.Monitors;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.model.WorkflowModelTestUtil;

import static com.netflix.conductor.common.metadata.tasks.TaskType.TASK_TYPE_WAIT;
import static com.netflix.conductor.common.metadata.tasks.TaskType.TASK_TYPE_HUMAN;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private Monitors monitors;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        WorkflowContext.set(new WorkflowContext("testAppId"));

        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);
        when(properties.getSchedulerConfiguration()).thenReturn(new SchedulerConfiguration());

        when(queueDAO.remove(DECIDER_QUEUE, anyString())).thenAnswer(invocation -> {});
    }

    @Test
    public void testSweepWithValidWorkflowId() {
        WorkflowModel workflowModel = WorkflowModelTestUtil.createWorkflowModelWithTasks();

        when(workflowExecutor.decide(workflowModel.getWorkflowId())).thenReturn(workflowModel);

        workflowSweeper.sweep(workflowModel.getWorkflowId());

        verify(workflowExecutor).decide(workflowModel.getWorkflowId());
        verify(queueDAO).remove(DECIDER_QUEUE, workflowModel.getWorkflowId());
        verify(monitors).recordUnackTime("testWorkflowName", anyLong());
    }

    @Test
    public void testSweepWithNotFoundWorkflowId() {
        String workflowId = "nonExistentWorkflowId";
        doThrow(new NotFoundException("Workflow not found for id: " + workflowId))
                .when(workflowExecutor).decide(workflowId);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verify(monitors).error(WorkflowSweeper.class.getSimpleName(), "sweep");
        verify(log).info("Workflow NOT found for id:{}. Removed it from decider queue", workflowId);
    }

    @Test
    public void testSweepWithException() {
        String workflowId = "exceptionalWorkflowId";
        doThrow(new RuntimeException("Exception occurred while sweeping workflow id: " + workflowId))
                .when(workflowExecutor).decide(workflowId);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verify(monitors).error(WorkflowSweeper.class.getSimpleName(), "sweep");
        verify(log).error("Error running sweep for " + workflowId, any(Throwable.class));
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "asyncWorkflowId";

        CompletableFuture<Void> voidFuture = new CompletableFuture<>();
        when(workflowExecutor.sweep(workflowId)).thenReturn(voidFuture);

        workflowSweeper.sweepAsync(workflowId);

        verify(workflowExecutor).sweep(workflowId);
    }

    @Test
    public void testUnackWithWorkflow() {
        WorkflowModel workflowModel = WorkflowModelTestUtil.createWorkflowModelWithTasks();

        when(workflowExecutor.decide(workflowModel.getWorkflowId())).thenReturn(workflowModel);

        workflowSweeper.unack(workflowModel);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), anyLong());

        TaskModel taskModel = workflowModel.getTasks().get(0);
        when(taskModel.getTaskDefinition().isPresent()).thenReturn(false);

        workflowSweeper.unack(workflowModel);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), anyLong());

        taskModel.setTaskDefinition(mock(TaskDef.class));
        when(taskModel.getTaskDefinition().get().getPollTimeoutSeconds()).thenReturn(10L);

        workflowSweeper.unack(workflowModel);

        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), 21L);
    }

    @Test
    public void testUnackWithNullWorkflowModel() {
        workflowSweeper.unack(null);

        verify(queueDAO, never()).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;

        long jitter = 10;
        long expectedWorkflowOffset = workflowOffsetTimeout + jitter;

        when(properties.getWorkflowOffsetTimeout()).thenReturn(workflowOffsetTimeout);

        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertEquals(expectedWorkflowOffset, result);
    }
}