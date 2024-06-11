
package com.netflix.conductor.core.reconciliation;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Mockito.*;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class WorkflowSweeperTest {
    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;
    private ConductorProperties properties;
    private QueueDAO queueDAO;

    @Before
    public void setup(){
        properties = mock(ConductorProperties.class);
        queueDAO = mock(QueueDAO.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        workflowRepairService = mock(WorkflowRepairService.class);
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), 
                                              properties, queueDAO);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "123";
        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);
        assertNotNull(result);
    }

    @Test
    public void testSweep() {
        String workflowId = "123";
        workflowSweeper.sweep(workflowId);
        verify(workflowExecutor).decide(workflowId);
    }

    @Test
    public void testUnack() {
        WorkflowModel testWorkflowModel = mock(WorkflowModel.class);
        workflowSweeper.unack(testWorkflowModel, 1000L);
        verify(queueDAO).setUnackTimeout(DECIDER_QUEUE, testWorkflowModel.getWorkflowId(), 1000L*1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        Long timeout = 45L;
        Long jitteredTimeout = workflowSweeper.workflowOffsetWithJitter(timeout);
        assertTrue(jitteredTimeout <= 60L);
        assertTrue(jitteredTimeout >= 30L);
    }

    @Test(expected = NotFoundException.class)
    public void testSweep_throwNotFoundException() {
        String invalidWorkflowId = "invalid";
        when(workflowExecutor.decide(invalidWorkflowId)).thenThrow(new NotFoundException());
        workflowSweeper.sweep(invalidWorkflowId);
    }

    @Test(expected = Exception.class)
    public void testSweep_throwException() {
        String invalidWorkflowId = "invalid";
        when(workflowExecutor.decide(invalidWorkflowId)).thenThrow(new Exception());
        workflowSweeper.sweep(invalidWorkflowId);
    }
}
