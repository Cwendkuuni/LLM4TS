package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT1", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        
        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT1", filter.m_acctCode);
        assertEquals("10:00", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STOCK", filter.m_secType);
        assertEquals("NYSE", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }
    
    @Test
    public void testExecutionFilterEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT2", "09:00", "GOOG", "OPTION", "NASDAQ", "SELL");
        
        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
    }
}