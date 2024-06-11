
package com.netflix.conductor.core.reconciliation;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        workflowSweeper = new WorkflowSweeper(
                mock(WorkflowExecutor.class),
                mock(WorkflowRepairService.class),
                new ConductorProperties(),
                mock(QueueDAO.class)
        );
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync("workflowId");
        assertNotNull(future);
    }

    @Test
    public void testSweep() {
        workflowSweeper.sweep("workflowId");
        // Verify that the appropriate methods are being called
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowSweeper.unack(workflowModel, 30);
        // Add test case to verify the behavior of unack method
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long result = workflowSweeper.workflowOffsetWithJitter(45);
        // Add test case to verify the behavior of workflowOffsetWithJitter method
    }
}
