package com.ib.client;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExecutionFilterTest {
    
    private ExecutionFilter executionFilter;
    private ExecutionFilter executionFilter2;

    @Before
    public void setUp() {
        executionFilter = new ExecutionFilter();
        executionFilter2 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
    }

    @Test
    public void testEquals() {
        // Test equal objects
        assertTrue(executionFilter.equals(executionFilter));
        assertTrue(executionFilter2.equals(executionFilter2));
        
        // Test different objects with same attributes
        ExecutionFilter executionFilter3 = new ExecutionFilter();
        executionFilter3.m_clientId = 1;
        assertTrue(executionFilter2.equals(executionFilter3));
        
        // Test different objects with different attributes
        ExecutionFilter executionFilter4 = new ExecutionFilter(2, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(executionFilter2.equals(executionFilter4));
        
        // Test null object
        assertFalse(executionFilter2.equals(null));
        
        // Test different class object
        assertFalse(executionFilter2.equals("not an ExecutionFilter object"));
    }
}