package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.util.concurrent.CompletableFuture;

import org.junit.Test;

public class WorkflowSweeperTest {

    private final WorkflowExecutor workflowExecutor = mock(WorkflowExecutor.class);
    private final WorkflowRepairService workflowRepairService = mock(WorkflowRepairService.class);
    private final QueueDAO queueDAO = mock(QueueDAO.class);

    private final WorkflowSweeper workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> result = workflowSweeper.sweepAsync("test_workflow_id");
        assertNotNull(result);
    }

    @Test
    public void testSweep() {
        workflowSweeper.sweep("test_workflow_id");
        // Add appropriate assertions here
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("test_workflow_id");
        workflowModel.setTasks(new ArrayList<>());
        workflowSweeper.unack(workflowModel, 60);
        // Add appropriate assertions here
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long result = workflowSweeper.workflowOffsetWithJitter(45);
        // Add appropriate assertions here
    }
}