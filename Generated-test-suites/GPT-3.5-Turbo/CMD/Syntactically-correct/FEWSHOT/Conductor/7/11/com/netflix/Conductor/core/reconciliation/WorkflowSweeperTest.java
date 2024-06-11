package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;

public class WorkflowSweeperTest {

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Mock
    private ConductorProperties properties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private WorkflowModel workflowModel;

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync("testWorkflowId");
        assertNotNull(future);
    }

    @Test
    public void testSweep() {
        Mockito.when(workflowExecutor.decide(Mockito.anyString())).thenReturn(workflowModel);
        workflowSweeper.sweep("testWorkflowId");
        Mockito.verify(queueDAO).remove(Mockito.eq("DECIDER_QUEUE"), Mockito.eq("testWorkflowId"));
    }

    @Test
    public void testUnack() {
        Mockito.when(workflowModel.getTasks()).thenReturn(new ArrayList<>());
        workflowSweeper.unack(workflowModel, 60L);
        Mockito.verify(queueDAO, Mockito.never()).setUnackTimeout(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long offsetTimeout = 45L;
        long result = workflowSweeper.workflowOffsetWithJitter(offsetTimeout);
        assertTrue(result >= 30 && result <= 60);
    }

    @Test
    public void testWorkflowSweeperConstructor() {
        WorkflowSweeper sweeper = new WorkflowSweeper(workflowExecutor, Optional.ofNullable(workflowRepairService), properties, queueDAO);
        assertNotNull(sweeper);
    }

    @Test
    public void testSweepWithNotFoundException() {
        Mockito.doThrow(new NotFoundException("test")).when(queueDAO).remove(Mockito.anyString(), Mockito.anyString());
        workflowSweeper.sweep("nonExistingWorkflowId");
        Mockito.verify(queueDAO, Mockito.times(1)).remove(Mockito.eq("DECIDER_QUEUE"), Mockito.eq("nonExistingWorkflowId"));
    }

    @Test
    public void testSweepWithException() {
        Mockito.doThrow(new RuntimeException("test")).when(workflowExecutor).decide(Mockito.anyString());
        workflowSweeper.sweep("testWorkflowId");
        // Verify that an error is logged
    }
}