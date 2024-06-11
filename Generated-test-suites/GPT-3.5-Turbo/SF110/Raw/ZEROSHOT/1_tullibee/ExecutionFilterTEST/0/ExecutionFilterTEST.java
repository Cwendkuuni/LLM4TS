package com.ib.client;

import static org.junit.Assert.*;
import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testEquals() {
        // Test case 1: Object is null
        ExecutionFilter filter1 = new ExecutionFilter();
        boolean result1 = filter1.equals(null);
        assertFalse(result1);
        
        // Test case 2: Object is the same instance
        ExecutionFilter filter2 = new ExecutionFilter();
        boolean result2 = filter2.equals(filter2);
        assertTrue(result2);
        
        // Test case 3: Object is different instance with same values
        ExecutionFilter filter3A = new ExecutionFilter(1, "ACCT", "12:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter3B = new ExecutionFilter(1, "ACCT", "12:00", "AAPL", "STOCK", "NYSE", "BUY");
        boolean result3 = filter3A.equals(filter3B);
        assertTrue(result3);
        
        // Test case 4: Object is different instance with different values
        ExecutionFilter filter4A = new ExecutionFilter(1, "ACCT", "12:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter4B = new ExecutionFilter(2, "ACCT", "12:00", "AAPL", "STOCK", "NYSE", "BUY");
        boolean result4 = filter4A.equals(filter4B);
        assertFalse(result4);
    }
}