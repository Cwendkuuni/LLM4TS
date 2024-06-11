package com.netflix.conductor.core.reconciliation.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
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
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;

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
    private Logger logger;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(10L);
        when(queueDAO.remove(DECIDER_QUEUE, anyString())).thenReturn(null);
    }

    @After
    public void tearDown() {
        reset(properties, workflowExecutor, workflowRepairService, queueDAO, logger);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowId)).thenReturn(1L);
        workflowSweeper.sweepAsync(workflowId);
        verify(workflowSweeper).sweep(workflowId);
    }

    @Test
    public void testSweep_WorkflowNotFound() {
        String workflowId = "testWorkflowId";
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowId)).thenReturn(1L);
        doThrow(new NotFoundException("Test exception")).when(workflowExecutor).decide(workflowId);
        workflowSweeper.sweep(workflowId);
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verify(logger).info("Workflow NOT found for id:{}. Removed it from decider queue", workflowId);
    }

    @Test
    public void testSweep_ExceptionDuringSweep() {
        String workflowId = "testWorkflowId";
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowId)).thenReturn(1L);
        doThrow(new RuntimeException("Test exception")).when(workflowSweeper).sweep(workflowId);
        workflowSweeper.sweep(workflowId);
        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowId, workflowSweeper.workflowOffsetWithJitter(10L) * 1000);
        Monitors.error(WorkflowSweeper.class.getSimpleName(), "sweep");
        verify(logger).error("Error running sweep for " + workflowId, any(RuntimeException.class));
    }

    @Test
    public void testSweep_WorkflowNotFoundAndRepairServicePresent() {
        String workflowId = "testWorkflowId";
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowId)).thenReturn(1L);
        doThrow(new NotFoundException("Test exception")).when(workflowExecutor).decide(workflowId);
        when(workflowRepairService.verifyAndRepairWorkflowTasks(workflowId)).thenReturn(Collections.emptyList());
        workflowSweeper.sweep(workflowId);
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
        verify(logger).info(
                "Workflow NOT found for id:{}. Removed it from decider queue", workflowId);
    }

    @Test
    public void testSweep_NoWorkflowTerminalStatus() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(Status.RUNNING);
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowId)).thenReturn(1L);
        when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenReturn(null);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);
        doNothing().when(workflowExecutor).decide(workflowId);
        workflowSweeper.sweep(workflowId);
        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflowId, workflowSweeper.workflowOffsetWithJitter(10L) * 1000);
    }

    @Test
    public void testSweep_WorkflowTerminalStatus() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(Status.COMPLETED);
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowId)).thenReturn(1L);
        when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenReturn(null);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);
        workflowSweeper.sweep(workflowId);
        verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testUnack_TaskInProgress() {
        WorkflowModel workflow = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        workflow.setTasks(Arrays.asList(taskModel));
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, workflow.getWorkflowId())).thenReturn(1L);
        long postponeDurationSeconds = 5L;
        workflowSweeper.unack(workflow, 10L);
        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflow.getWorkflowId(), postponeDurationSeconds * 1000);
    }

    @Test
    public void testUnack_TaskScheduled() {
        WorkflowModel workflow = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.SCHEDULED);
        workflow.setTasks(Arrays.asList(taskModel));
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, workflow.getWorkflowId())).thenReturn(1L);
        long postponeDurationSeconds = 5L;
        workflowSweeper.unack(workflow, 10L);
        verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, workflow.getWorkflowId(), postponeDurationSeconds * 1000);
    }

    @Test
    public void testUnack_TaskCompleted() {
        WorkflowModel workflow = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.COMPLETED);
        workflow.setTasks(Arrays.asList(taskModel));
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, workflow.getWorkflowId())).thenReturn(1L);
        long postponeDurationSeconds = 5L;
        workflowSweeper.unack(workflow, 10L);
        verify(queueDAO, never()).setUnackTimeout(
                DECIDER_QUEUE, workflow.getWorkflowId(), postponeDurationSeconds * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 10L;
        long jitter = 5L;
        long expectedWorkflowOffset = 15L;
        long actualWorkflowOffset = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertEquals(expectedWorkflowOffset, actualWorkflowOffset);
    }
}