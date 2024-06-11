package com.netflix.conductor.core.reconciliation;

import static com.netflix.conductor.core.config.SchedulerConfiguration.SWEEPER_EXECUTOR_NAME;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.metrics.NoOpMonitors;
import com.netflix.conductor.metrics.Monitors;

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
    private Logger logger;
    @Mock
    private Monitors monitors;

    private TaskModel taskModel;
    private List<TaskModel> tasks;
    private WorkflowModel workflowModel;
    private WorkflowSweeper sweeper;
    private ArgumentCaptor<WorkflowContext> workflowContextCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
        sweeper.setLogger(logger);
        sweeper.setMonitors(monitors);

        taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(new TaskDef("TaskType"));

        tasks = Arrays.asList(taskModel, new TaskModel());
        workflowModel = new WorkflowModel("WorkflowName", Status.RUNNING, Collections.singletonList(tasks), null, null);
        workflowContextCaptor = ArgumentCaptor.forClass(WorkflowContext.class);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(workflowExecutor, workflowRepairService, queueDAO, properties, logger, monitors);
    }

    @Test
    public void testSweep() throws Exception {
        when(workflowExecutor.decide(eq("workflowId"))).thenReturn(workflowModel);
        sweeper.sweep("workflowId");

        verify(workflowExecutor).decide(eq("workflowId"));
        verify(queueDAO).remove(eq(DECIDER_QUEUE), eq("workflowId"));

        if (workflowRepairService != null) {
            verify(workflowRepairService).verifyAndRepairWorkflowTasks(eq("workflowId"));
        }

        if (workflowModel != null && workflowModel.getStatus().isTerminal()) {
            return;
        }

        verify(queueDAO, times(1)).setUnackTimeout(
                eq(DECIDER_QUEUE), eq("workflowId"), anyLong());
    }

    @Test
    public void testSweep_NotFound() throws Exception {
        when(workflowExecutor.decide(eq("workflowId"))).thenThrow(new NotFoundException("Workflow not found"));
        sweeper.sweep("workflowId");

        verify(queueDAO).remove(eq(DECIDER_QUEUE), eq("workflowId"));
        verify(logger).info(
                "Workflow NOT found for id:{}. Removed it from decider queue", eq("workflowId"), isA(NotFoundException.class));
    }

    @Test
    public void testSweep_Exception() throws Exception {
        when(workflowExecutor.decide(eq("workflowId"))).thenThrow(new RuntimeException("Error deciding workflow"));
        sweeper.sweep("workflowId");

        verify(queueDAO).setUnackTimeout(
                eq(DECIDER_QUEUE), eq("workflowId"), anyLong());
        verify(monitors).error(eq(WorkflowSweeper.class.getSimpleName()), eq("sweep"));
        verify(logger).error("Error running sweep for " + eq("workflowId"), isA(RuntimeException.class));
    }

    @Test
    public void testUnack() {
        sweeper.unack(workflowModel, 1000L);

        verify(queueDAO).setUnackTimeout(
                eq(DECIDER_QUEUE), eq(workflowModel.getWorkflowId()), eq(1001L));
    }

    @Test
    public void testUnack_TaskInProgress() {
        when(taskModel.getStatus()).thenReturn(Status.IN_PROGRESS);
        sweeper.unack(workflowModel, 1000L);

        verify(queueDAO).setUnackTimeout(
                eq(DECIDER_QUEUE), eq(workflowModel.getWorkflowId()), eq(1001L));
    }

    @Test
    public void testUnack_TaskScheduled() {
        when(taskModel.getStatus()).thenReturn(Status.SCHEDULED);
        sweeper.unack(workflowModel, 1000L);

        verify(queueDAO).setUnackTimeout(
                eq(DECIDER_QUEUE), eq(workflowModel.getWorkflowId()), eq(1001L));
    }

    @Test
    public void testUnack_WorkflowTimedOut() {
        when(workflowModel.getWorkflowDefinition()).thenReturn(new TaskDef("WorkflowDefinition"));
        when(workflowModel.getWorkflowDefinition().getTimeoutSeconds()).thenReturn(50L);
        sweeper.unack(workflowModel, 1000L);

        verify(queueDAO).setUnackTimeout(
                eq(DECIDER_QUEUE), eq(workflowModel.getWorkflowId()), eq(51L));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 50L;

        long workflowOffset = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(workflowOffset >= (workflowOffsetTimeout - 16L));
        assertTrue(workflowOffset <= (workflowOffsetTimeout + 16L));
    }
}