package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ExecutionFilterTest {
    private ExecutionFilter executionFilter;
    
    @Before
    public void setUp() {
        executionFilter = new ExecutionFilter();
    }
    
    @Test
    public void testConstructorWithParameters() {
        ExecutionFilter executionFilter = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        
        assertEquals(1, executionFilter.m_clientId);
        assertEquals("acctCode", executionFilter.m_acctCode);
        assertEquals("time", executionFilter.m_time);
        assertEquals("symbol", executionFilter.m_symbol);
        assertEquals("secType", executionFilter.m_secType);
        assertEquals("exchange", executionFilter.m_exchange);
        assertEquals("side", executionFilter.m_side);
    }
    
    @Test
    public void testEquals() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        ExecutionFilter executionFilter3 = new ExecutionFilter(2, "acctCode", "time", "symbol", "secType", "exchange", "side");
        
        assertTrue(executionFilter1.equals(executionFilter2));
        assertFalse(executionFilter1.equals(executionFilter3));
    }
}