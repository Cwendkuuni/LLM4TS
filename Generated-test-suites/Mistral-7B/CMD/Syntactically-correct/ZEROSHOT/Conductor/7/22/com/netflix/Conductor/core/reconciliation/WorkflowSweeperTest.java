package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertNotNull;
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

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.WorkflowModel.Status;
import com.netflix.conductor.test.IntegrationTestUtils;

public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflowId";
    private static final String WORKFLOW_NAME = "workflowName";
    private static final String APPLICATION_ID = "applicationId";

    private ConductorProperties properties;
    private WorkflowExecutor workflowExecutor;
    private WorkflowSweeper workflowSweeper;
    private QueueDAO queueDAO;
    private Monitors monitors;
    private WorkflowModel workflowModel;

    @Before
    public void setup() {
        properties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        queueDAO = mock(QueueDAO.class);
        monitors = mock(Monitors.class);
        workflowSweeper = new WorkflowSweeper(workflowExecutor, null, properties, queueDAO);
        when(properties.getAppId()).thenReturn(APPLICATION_ID);
        workflowModel = IntegrationTestUtils.createWorkflowModel();
    }

    @Test
    public void testSweepAsync() throws Exception {
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(WORKFLOW_ID);
        result.join();
        assertNotNull(result);
    }

    @Test
    public void testSweep_WorkflowNotFound() throws Exception {
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(null);

        workflowSweeper.sweep(WORKFLOW_ID);

        // Verify and repair tasks in the workflow was not called since the workflow was not found.
        Mockito.verifyNoMoreInteractions(workflowRepairService);
        Mockito.verifyNoMoreInteractions(workflowExecutor);
        Mockito.verifyNoMoreInteractions(queueDAO);
        Mockito.verifyNoMoreInteractions(monitors);
    }

    @Test
    public void testSweep_WorkflowFound() throws Exception {
        WorkflowContext workflowContext = new WorkflowContext(APPLICATION_ID);
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(workflowModel);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);

        // Verify and repair tasks in the workflow was called.
        Mockito.verify(workflowRepairService).verifyAndRepairWorkflowTasks(WORKFLOW_ID);
        Mockito.verify(workflowExecutor).decide(WORKFLOW_ID);

        // Unack was called with correct workflow offset timeout.
        long workflowOffsetTimeout = 1000L;
        long expectedPostponeDurationSeconds = 1010L;
        when(properties.getWorkflowOffsetTimeout()).thenReturn(workflowOffsetTimeout);
        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);
        Mockito.verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, WORKFLOW_ID, expectedPostponeDurationSeconds * 1000);
    }

    @Test
    public void testSweep_WorkflowTerminal() throws Exception {
        WorkflowModel workflowModelTerminal = IntegrationTestUtils.createWorkflowModelWithTerminalStatus();
        when(queueDAO.get(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(workflowModelTerminal);

        workflowSweeper.sweep(WORKFLOW_ID);

        // Verify and repair tasks in the workflow was not called since the workflow has terminal status.
        Mockito.verifyNoMoreInteractions(workflowRepairService);
        Mockito.verifyNoMoreInteractions(workflowExecutor);
        Mockito.verifyNoMoreInteractions(queueDAO);
        Mockito.verifyNoMoreInteractions(monitors);

        // QueueDAO remove method was called.
        Mockito.verify(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testUnack() throws Exception {
        long workflowOffsetTimeout = 1000L;
        long expectedPostponeDurationSeconds = 1010L;
        when(properties.getWorkflowOffsetTimeout()).thenReturn(workflowOffsetTimeout);

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        Mockito.verify(queueDAO).setUnackTimeout(
                DECIDER_QUEUE, WORKFLOW_ID, expectedPostponeDurationSeconds * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45L;
        long jitter = 12;
        long expectedJitterRange = workflowOffsetTimeout / 3;
        long expectedJitter = workflowOffsetTimeout + jitter;

        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assert (result >= (expectedJitterRange - jitter)) && (result <= (expectedJitterRange + jitter));
    }
}