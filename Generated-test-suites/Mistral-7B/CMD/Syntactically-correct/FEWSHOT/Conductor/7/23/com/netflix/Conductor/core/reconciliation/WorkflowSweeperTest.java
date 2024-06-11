package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.metrics.Monitors;

public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowSweeper.class);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void teardown() {
        verifyNoMoreInteractions(workflowExecutor, queueDAO, properties, workflowRepairService);
    }

    @Test
    public void testSweepAsync() throws Exception {
        String workflowId = "testWorkflowId";
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowId)).thenReturn(0L);

        workflowSweeper.sweepAsync(workflowId);

        verify(workflowSweeper, times(1)).sweep(workflowId);
        verify(queueDAO, times(1)).setUnackTimeout(
                DECIDER_QUEUE, workflowId, (int) properties.getWorkflowOffsetTimeout().getSeconds() * 1000);
    }

    @Test
    public void testSweepWithWorkflowNotFound() throws Exception {
        String workflowId = "testWorkflowId";

        WorkflowModel workflow = null;
        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException("Workflow not found"));

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepWithWorkflowTerminal() throws Exception {
        String workflowId = "testWorkflowId";
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(Status.COMPLETED);

        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepWithWorkflowRepairService() throws Exception {
        String workflowId = "testWorkflowId";
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(Status.FAILED);

        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);
        when(workflowRepairService.verifyAndRepairWorkflowTasks(workflowId)).thenReturn(true);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, never()).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepWithWorkflowInProgress() throws Exception {
        String workflowId = "testWorkflowId";
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(Status.IN_PROGRESS);

        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);

        workflowSweeper.sweep(workflowId);

        long workflowOffsetTimeout =
                properties.getWorkflowOffsetTimeout().getSeconds();
        long postponeDurationSeconds = 1;
        verify(queueDAO, times(1)).setUnackTimeout(
                DECIDER_QUEUE, workflowId, postponeDurationSeconds * 1000);
    }

    @Test
    public void testSweepWithWorkflowScheduled() throws Exception {
        String workflowId = "testWorkflowId";
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(Status.SCHEDULED);
        TaskModel taskModel = new TaskModel();

        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);
        when(taskModel.getStatus()).thenReturn(Status.SCHEDULED);
        when(workflow.getTasks()).thenReturn(Collections.singletonList(taskModel));

        workflowSweeper.sweep(workflowId);

        long workflowOffsetTimeout =
                properties.getWorkflowOffsetTimeout().getSeconds();
        long postponeDurationSeconds = workflowOffsetTimeout + 1;
        verify(queueDAO, times(1)).setUnackTimeout(
                DECIDER_QUEUE, workflowId, postponeDurationSeconds * 1000);
    }

    @Test
    public void testSweepWithWorkflowFailed() throws Exception {
        String workflowId = "testWorkflowId";
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(Status.FAILED);
        TaskModel taskModel = new TaskModel();

        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);
        when(taskModel.getStatus()).thenReturn(Status.FAILED);
        when(taskModel.getTaskDefinition()).thenReturn(mock(TaskDef.class));

        workflowSweeper.sweep(workflowId);

        long workflowOffsetTimeout =
                properties.getWorkflowOffsetTimeout().getSeconds();
        long postponeDurationSeconds = workflowOffsetTimeout + 1;
        verify(queueDAO, times(1)).setUnackTimeout(
                DECIDER_QUEUE, workflowId, postponeDurationSeconds * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() throws Exception {
        long workflowOffsetTimeout = 45;

        long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(jitter >= (workflowOffsetTimeout - 15));
        assertTrue(jitter <= (workflowOffsetTimeout + 15));
    }
}