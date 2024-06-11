package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
        WorkflowRepairService workflowRepairService = mock(WorkflowRepairService.class);
        ConductorProperties properties = mock(ConductorProperties.class);
        QueueDAO queueDAO = mock(QueueDAO.class);

        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        assertNotNull(future);
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";
        workflowSweeper.sweep(workflowId);
        // Verify that necessary methods are called
        verify(workflowSweeper.workflowExecutor, times(1)).decide(workflowId);
        verify(workflowSweeper.queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        long workflowOffsetTimeout = 60;
        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);
        // Verify that necessary methods are called
        verify(workflowSweeper.queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), 60000); // 60 seconds to milliseconds
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        // Verify that the result is within the expected range
        assertTrue(result >= 30 && result <= 60);
    }
}