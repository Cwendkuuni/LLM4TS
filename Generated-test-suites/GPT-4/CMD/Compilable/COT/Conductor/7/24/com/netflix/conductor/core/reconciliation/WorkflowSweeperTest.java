package com.netflix.conductor.core.reconciliation;
import org.junit.Test;

import static org.junit.Assert.*;

public class WorkflowSweeperTest {

    /* your arrangement for testing WorkflowSweeper here */

    @Test
    public void testWorkflowSweeper() {
        /* basic test cases here */
    }

    @Test(expected = NullPointerException.class)
    public void sweepNullTest() {
        /* test for edge case 1 here */
    }

    @Test
    public void sweepNotFoundTest() {
        /* test for edge case 2 here */
    }

    @Test(expected = RuntimeException.class)  // replace RuntimeException with the realistic exception type
    public void sweepExceptionTest() {
        /* test for edge case 3 here */
    }
}