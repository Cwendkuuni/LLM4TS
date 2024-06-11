package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "TIME1", "SYMBOL1", "SEC1", "EXCH1", "SIDE1");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "TIME1", "SYMBOL1", "SEC1", "EXCH1", "SIDE1");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT2", "TIME2", "SYMBOL2", "SEC2", "EXCH2", "SIDE2");

        // Test equality with equal objects
        assertTrue(filter1.equals(filter2));
        assertTrue(filter2.equals(filter1));

        // Test equality with different objects
        assertFalse(filter1.equals(filter3));
        assertFalse(filter3.equals(filter1));

        // Test equality with null
        assertFalse(filter1.equals(null));
    }
}