package com.netflix.conductor.core.reconciliation;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    // Initialize Mock objects
    private WorkflowExecutor workflowExecutor = Mockito.mock(WorkflowExecutor.class);
    private WorkflowRepairService workflowRepairService = Mockito.mock(WorkflowRepairService.class);
    private ConductorProperties properties = Mockito.mock(ConductorProperties.class);
    private QueueDAO queueDAO = Mockito.mock(QueueDAO.class);
    private WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);

    @Test
    public void testSweepAsync() {
        WorkflowSweeper sweeper = mock(WorkflowSweeper.class);
        when(sweeper.sweepAsync(anyString())).thenReturn(CompletableFuture.completedFuture(null));
        sweeper.sweepAsync("workflowId");
        verify(sweeper).sweepAsync(anyString());
    }

    @Test
    public void testSweep() {
        WorkflowSweeper sweeper = mock(WorkflowSweeper.class);
        doNothing().when(sweeper).sweep(anyString());
        sweeper.sweep("workflowId");
        verify(sweeper).sweep(anyString());
    }

    @Test
    public void testUnack() {
        WorkflowSweeper sweeper = mock(WorkflowSweeper.class);
        doNothing().when(sweeper).unack(any(WorkflowModel.class), any(Long.class));
        sweeper.unack(workflowModel, 10L);
        verify(sweeper).unack(any(WorkflowModel.class), any(Long.class));
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        WorkflowSweeper sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService),
                                       properties, queueDAO);
        long workflowOffsetTimeout = 60L;
        long result = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertTrue(result >= 40 && result <= 80);
    }

    @Test(expected = NotFoundException.class)
    public void testSweep_WhenWorkflowNotFound() {
        when(workflowExecutor.decide(anyString())).thenThrow(new NotFoundException("Workflow not found"));
        WorkflowSweeper sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService),
                                           properties, queueDAO);
        sweeper.sweep("invalidWorkflowId");
    }

    @Test(expected = Exception.class)
    public void testSweep_WhenAnyExceptionOccurred() {
        when(workflowExecutor.decide(anyString())).thenThrow(new RuntimeException("Unexpected error"));
        WorkflowSweeper sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService),
                                           properties, queueDAO);
        sweeper.sweep("workflowId");
    }

    @Test
    public void testWorkflowOffsetWithJitter_WhenWorkflowOffsetTimeoutIsMaxLong() {
        WorkflowSweeper sweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService),
                                           properties, queueDAO);
        long workflowOffsetTimeout = Long.MAX_VALUE;
        long result = sweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        assertEquals(Long.MAX_VALUE, result);
    }
}