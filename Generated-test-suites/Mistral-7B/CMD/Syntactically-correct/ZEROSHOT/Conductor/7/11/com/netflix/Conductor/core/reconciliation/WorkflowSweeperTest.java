package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.Status;

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

    private WorkflowModel workflowModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(properties.getAppId()).thenReturn("testAppId");
        when(properties.getWorkflowOffsetTimeout()).thenReturn(5L);
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, anyString())).thenReturn(0L);
        when(queueDAO.remove(DECIDER_QUEUE, anyString())).thenReturn(true);
        when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);
        when(workflowModel.getStatus()).thenReturn(Status.RUNNING);
        when(workflowModel.getTasks()).thenReturn(Collections.singletonList(taskModel()));
        when(monitors.recordUnackTime(anyString(), anyLong())).thenReturn(true);
        when(monitors.error(anyString(), any())).thenReturn(true);
    }

    @After
    public void teardown() {
        workflowModel = null;
        reset(workflowExecutor, workflowRepairService, properties, queueDAO, monitors);
    }

    @Test
    public void testSweepAsync() throws Exception {
        String workflowId = UUID.randomUUID().toString();
        workflowSweeper.sweepAsync(workflowId).get();
        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void testSweep() throws Exception {
        String workflowId = UUID.randomUUID().toString();
        workflowSweeper.sweep(workflowId);

        verify(workflowSweeper, times(1)).sweep(workflowId);

        // Verify and repair tasks in the workflow.
        if (workflowRepairService != null) {
            verify(workflowRepairService, times(1)).verifyAndRepairWorkflowTasks(workflowId);
        }

        // Run the workflow to get a valid workflowModel
        WorkflowModel expectedWorkflowModel = workflowExecutor.decide(workflowId);

        // Check if workflow exists
        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO, times(1)).get(DECIDER_QUEUE, workflowId);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);

        // Check if workflow is terminal
        assertFalse(expectedWorkflowModel.getStatus().isTerminal());

        // Check unack timeout
        long workflowOffsetTimeout = workflowSweeper.properties.getWorkflowOffsetTimeout().getSeconds();
        long postponeDurationSeconds = workflowSweeper.unack(expectedWorkflowModel, workflowOffsetTimeout);
        long unackTimeout = queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowId);
        assertEquals(postponeDurationSeconds * 1000, unackTimeout);

        // Check if monitors.recordUnackTime is called
        verify(monitors, times(1)).recordUnackTime(expectedWorkflowModel.getWorkflowName(), anyLong());

        // Check if monitors.error is called
        verify(monitors, times(1)).error(WorkflowSweeper.class.getSimpleName(), any());

        // Check if workflow is removed from decider queue
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testNotFoundException() throws Exception {
        String workflowId = UUID.randomUUID().toString();
        doThrow(new NotFoundException("Workflow not found")).when(workflowExecutor).decide(workflowId);
        workflowSweeper.sweep(workflowId);

        verify(workflowSweeper, times(1)).sweep(workflowId);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
        verify(loggedStatement(), times(1)).info(
                "Workflow NOT found for id:{}. Removed it from decider queue", workflowId);
    }

    @Test
    public void testException() throws Exception {
        String workflowId = UUID.randomUUID().toString();
        doThrow(new Exception("Exception occurred")).when(workflowExecutor).decide(workflowId);
        workflowSweeper.sweep(workflowId);

        verify(workflowSweeper, times(1)).sweep(workflowId);
        verify(monitors, times(1)).error(WorkflowSweeper.class.getSimpleName(), "sweep");
        verify(loggedStatement(), times(1)).error(
                "Error running sweep for " + workflowId, (Exception) eq(Exception.class));
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("testWorkflowId");
        workflowModel.setStatus(Status.RUNNING);
        workflowModel.setTasks(Collections.singletonList(taskModel()));

        long workflowOffsetTimeout = workflowSweeper.properties.getWorkflowOffsetTimeout().getSeconds();
        long postponeDurationSeconds = workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        assertEquals(workflowOffsetTimeout + 1, postponeDurationSeconds);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45L;
        long expectedWorkflowTimeout = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(expectedWorkflowTimeout >= workflowOffsetTimeout - 15);
        assertTrue(expectedWorkflowTimeout <= workflowOffsetTimeout + 15);
    }
}