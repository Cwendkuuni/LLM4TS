package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.netflix.conductor.metrics.Monitors;

public class WorkflowSweeperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowSweeper.class);
    private static final String WORKFLOW_ID = "workflowId";
    private static final String WORKFLOW_NAME = "workflowName";
    private static final long WORKFLOW_OFFSET_TIMEOUT = 45L;

    private ConductorProperties properties;
    private WorkflowExecutor workflowExecutor;
    private WorkflowSweeper workflowSweeper;
    private QueueDAO queueDAO;
    private Monitors monitors;

    @Before
    public void setUp() {
        properties = mock(ConductorProperties.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        queueDAO = mock(QueueDAO.class);
        monitors = mock(Monitors.class);

        when(properties.getAppId()).thenReturn("appId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(WORKFLOW_OFFSET_TIMEOUT);

        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        when(workflowExecutor.decide(anyString())).thenReturn(new WorkflowModel());

        workflowSweeper = new WorkflowSweeper(
                workflowExecutor, null, properties, queueDAO, monitors);
    }

    @After
    public void tearDown() {
        workflowSweeper = null;
        properties = null;
        workflowExecutor = null;
        queueDAO = null;
        monitors = null;
    }

    @Test
    public void testSweepAsync() throws InterruptedException {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
        assertTrue(future.join());
    }

    @Test
    public void testSweep() {
        WorkflowModel workflowModel = new WorkflowModel();
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);

        // Verify no workflow was found and removed from decider queue
        verifyNoWorkflowFoundAndRemovedFromDeciderQueue();
    }

    @Test
    public void testSweep_WorkflowFound_TerminalStatus() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(Status.FINISHED);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);

        verifyWorkflowFoundAndRemovedFromDeciderQueue();
        verifyNoUnackTimesRecorded();
    }

    @Test
    public void testSweep_WorkflowFound_InProgressStatus() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(Status.IN_PROGRESS);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);

        verifyWorkflowFoundAndUnackTimeoutSet();
    }

    @Test
    public void testSweep_WorkflowFound_ScheduledStatus() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(Status.SCHEDULED);
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);

        verifyWorkflowFoundAndUnackTimeoutSet();
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();

        when(workflowModel.getTasks()).thenReturn(Arrays.asList(taskModel));

        workflowSweeper.unack(workflowModel, WORKFLOW_OFFSET_TIMEOUT);

        long expectedUnackTimeout = WORKFLOW_OFFSET_TIMEOUT + 1;
        verify(queueDAO).setUnackTimeout(anyString(), anyString(), expectedUnackTimeout * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45L;

        long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        long expectedJitterRange = workflowOffsetTimeout / 3;
        long expectedJitter = jitter % (2 * expectedJitterRange + 1) - expectedJitterRange;

        assertTrue(jitter > workflowOffsetTimeout - expectedJitterRange);
        assertTrue(jitter < workflowOffsetTimeout + expectedJitterRange);
    }

    @Test
    public void testSweep_WorkflowNotFound() {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenThrow(new NotFoundException(""));

        workflowSweeper.sweep(WORKFLOW_ID);

        verifyNoWorkflowFoundAndRemovedFromDeciderQueue();
    }

    @Test
    public void testSweep_ExceptionThrown() {
        WorkflowModel workflowModel = new WorkflowModel();
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        when(monitors::error).thenReturn(mock(RuntimeException.class));

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(monitors).error(anyString(), any(RuntimeException.class));
    }

    private void verifyNoWorkflowFoundAndRemovedFromDeciderQueue() {
        verify(queueDAO).remove(anyString(), WORKFLOW_ID);
    }

    private void verifyWorkflowFoundAndRemovedFromDeciderQueue() {
        verifyNoMoreInteractions(workflowExecutor);
        verifyNoMoreInteractions(queueDAO);
        verify(workflowExecutor).decide(WORKFLOW_ID);
        verify(queueDAO).remove(anyString(), WORKFLOW_ID);
    }

    private void verifyWorkflowFoundAndUnackTimeoutSet() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel = new TaskModel();
        List<TaskModel> tasks = Collections.singletonList(taskModel);

        when(workflowModel.getTasks()).thenReturn(tasks);
        when(taskModel.getStatus()).thenReturn(Status.IN_PROGRESS);

        workflowSweeper.sweep(WORKFLOW_ID);

        long expectedUnackTimeout = WORKFLOW_OFFSET_TIMEOUT + 1;
        verify(queueDAO).setUnackTimeout(anyString(), WORKFLOW_ID.toString(), expectedUnackTimeout * 1000);
    }

    private void verifyNoUnackTimesRecorded() {
        verify(monitors, never()).recordUnackTime(WORKFLOW_NAME, anyLong());
    }

    private void verify(QueueDAO queueDAO, int interactions) {
        Mockito.verify(queueDAO, Mockito.times(interactions)).setUnackTimeout(anyString(), anyString(), anyLong());
    }

    private void verify(QueueDAO queueDAO, int interactions, String methodName) {
        Mockito.verify(queueDAO, Mockito.times(interactions)).setUnackTimeout(anyString(), anyString(), anyLong());
        Mockito.verify(monitors, Mockito.times(1)).error(anyString(), any(RuntimeException.class));
        Mockito.verify(LOGGER, Mockito.times(1)).warn(anyString(), anyString());

        if (interactions > 0) {
            Mockito.verify(queueDAO).remove(anyString(), WORKFLOW_ID);
        }

        if (methodName != null) {
            Mockito.verify(monitors, Mockito.times(1)).recordUnackTime(WORKFLOW_NAME, anyLong());
        }
    }

    private void verifyNoMoreInteractions(Object obj) {
        Mockito.verifyNoMoreInteractions(obj);
    }
}