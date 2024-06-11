package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExecutionFilterTest {
    @Test
    public void testExecutionFilterDefaultConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        assertEquals(0, filter.m_clientId);
        assertEquals(null, filter.m_acctCode);
        assertEquals(null, filter.m_time);
        assertEquals(null, filter.m_symbol);
        assertEquals(null, filter.m_secType);
        assertEquals(null, filter.m_exchange);
        assertEquals(null, filter.m_side);
    }
    
    @Test
    public void testExecutionFilterParameterizedConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT001", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT001", filter.m_acctCode);
        assertEquals("2021-01-01", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STOCK", filter.m_secType);
        assertEquals("NASDAQ", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }
    
    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT001", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT001", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT001", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
    }
}