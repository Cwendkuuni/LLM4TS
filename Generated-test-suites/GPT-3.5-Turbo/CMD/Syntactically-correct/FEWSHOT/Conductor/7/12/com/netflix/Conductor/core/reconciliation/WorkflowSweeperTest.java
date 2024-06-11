package com.netflix.conductor.core.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;

    @Before
    public void setUp() {
        workflowSweeper = new WorkflowSweeper(mock(WorkflowExecutor.class), Optional.empty(), mock(ConductorProperties.class), mock(QueueDAO.class));
    }

    @Test
    public void testSweepAsync() {
        CompletableFuture<Void> result = workflowSweeper.sweepAsync("workflowId");
        assertNotNull(result);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        // Assuming the jitter is within range [30, 60]
        assertTrue(result >= 30 && result <= 60);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowModel.getWorkflowId()).thenReturn("workflowId");
        when(workflowModel.getTasks()).thenReturn(new ArrayList<>());

        WorkflowContext workflowContext = new WorkflowContext("appId");
        WorkflowContext.set(workflowContext);

        workflowSweeper.unack(workflowModel, 60);

        // Perform assertions
    }

    // Add more test cases for other methods in WorkflowSweeper class

}