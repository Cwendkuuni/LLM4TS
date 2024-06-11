package com.netflix.conductor.core.reconciliation;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper = new WorkflowSweeper();

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 15;
        long jitteredValue = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        
        // Check if calculated value is in the expected range
        assertTrue(jitteredValue >= 10 && jitteredValue <= 20);
    }
}