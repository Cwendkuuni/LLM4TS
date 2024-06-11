package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.test.TestUtils;

public class WorkflowSweeperTest {

    private static final Logger LOGGER = Mockito.mock(Logger.class);
    private static final String WORKFLOW_ID = "workflowId";
    private static final String WORKFLOW_NAME = "workflowName";
    private static final long WORKFLOW_OFFSET_TIMEOUT = 60;

    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor workflowExecutor;
    private QueueDAO queueDAO;
    private ConductorProperties conductorProperties;
    private Monitors monitors;
    private WorkflowRepairService workflowRepairService;

    @Before
    public void setup() {
        workflowExecutor = mock(WorkflowExecutor.class);
        queueDAO = mock(QueueDAO.class);
        conductorProperties = mock(ConductorProperties.class);
        monitors = mock(Monitors.class);
        workflowRepairService = mock(WorkflowRepairService.class);

        when(conductorProperties.getAppId()).thenReturn("appId");
        when(conductorProperties.getWorkflowOffsetTimeout()).thenReturn(WORKFLOW_OFFSET_TIMEOUT);
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);

        workflowSweeper =
                new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), conductorProperties,
                        queueDAO);
    }

    @Test
    public void testSweepAsync_callsSweepMethod() {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
        workflowSweeper.sweep(WORKFLOW_ID);
        Mockito.verify(workflowSweeper).sweep(WORKFLOW_ID);
        future.join();
    }

    @Test
    public void testSweep_noWorkflowFound_removesFromDeciderQueue() {
        WorkflowModel workflowModel = TestUtils.getWorkflowModel("workflowName", Status.STARTED);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException(""));

        workflowSweeper.sweep(WORKFLOW_ID);
        Mockito.verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweep_workflowNotFound_returns() {
        WorkflowModel workflowModel = TestUtils.getWorkflowModel("workflowName", Status.STARTED);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException(""));

        workflowSweeper.sweep(WORKFLOW_ID);
        assertNull(workflowSweeper.workflow);
    }

    @Test
    public void testSweep_workflowTerminal_returns() {
        TaskModel taskModel = TestUtils.getTaskModel(Status.IN_PROGRESS);
        WorkflowModel workflowModel =
                TestUtils.getWorkflowModel("workflowName", Status.TERMINATED, Arrays.asList(taskModel));
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);
        assertNull(workflowSweeper.workflow);
    }

    @Test
    public void testSweep_unackTasks() {
        TaskModel inProgressTaskModel = TestUtils.getTaskModel(Status.IN_PROGRESS);
        long postponeDurationSeconds = 10;

        WorkflowModel workflowModel =
                TestUtils.getWorkflowModel("workflowName", Status.STARTED, Arrays.asList(inProgressTaskModel));
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);

        Mockito.verify(queueDAO)
                .setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, postponeDurationSeconds * 1000);
    }

    @Test
    public void testSweep_workflowNotFound_unackTimeout() {
        WorkflowModel workflowModel = TestUtils.getWorkflowModel("workflowName", Status.STARTED);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException(""));

        workflowSweeper.sweep(WORKFLOW_ID);

        Mockito.verify(queueDAO)
                .setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, WORKFLOW_OFFSET_TIMEOUT * 1000);
    }

    @Test
    public void testUnack_taskInProgress_setsUnackTimeout() {
        TaskModel taskModel = TestUtils.getTaskModel(Status.IN_PROGRESS);
        long postponeDurationSeconds = 10;

        WorkflowModel workflowModel =
                TestUtils.getWorkflowModel("workflowName", Status.STARTED, Arrays.asList(taskModel));
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);

        Mockito.verify(queueDAO)
                .setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, postponeDurationSeconds * 1000);
    }

    @Test
    public void testUnack_taskScheduled_setsUnackTimeout() {
        TaskModel taskModel = TestUtils.getTaskModel(Status.SCHEDULED);
        long postponeDurationSeconds = 10;

        WorkflowModel workflowModel =
                TestUtils.getWorkflowModel("workflowName", Status.STARTED, Arrays.asList(taskModel));
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);

        Mockito.verify(queueDAO)
                .setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, postponeDurationSeconds * 1000);
    }

    @Test
    public void testUnack_workflowTerminal_doesNotCallSetUnackTimeout() {
        TaskModel taskModel = TestUtils.getTaskModel(Status.IN_PROGRESS);
        WorkflowModel workflowModel =
                TestUtils.getWorkflowModel("workflowName", Status.TERMINATED, Arrays.asList(taskModel));
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);

        Mockito.verify(queueDAO, Mockito.never())
                .setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, Mockito.anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter_returnsExpectedValue() {
        long workflowOffsetTimeout = 60;

        long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertFalse(jitter <= workflowOffsetTimeout / 3);
        assertFalse(jitter > workflowOffsetTimeout * 1.5);
    }
}