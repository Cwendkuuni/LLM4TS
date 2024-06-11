package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExecutionFilterTest {
    
    @Test
    public void testConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        assertEquals(0, filter.m_clientId);
        assertNull(filter.m_acctCode);
        assertNull(filter.m_time);
        assertNull(filter.m_symbol);
        assertNull(filter.m_secType);
        assertNull(filter.m_exchange);
        assertNull(filter.m_side);
        
        ExecutionFilter filterWithParams = new ExecutionFilter(1, "ACCT01", "2020-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        assertEquals(1, filterWithParams.m_clientId);
        assertEquals("ACCT01", filterWithParams.m_acctCode);
        assertEquals("2020-01-01", filterWithParams.m_time);
        assertEquals("AAPL", filterWithParams.m_symbol);
        assertEquals("STK", filterWithParams.m_secType);
        assertEquals("NASDAQ", filterWithParams.m_exchange);
        assertEquals("BUY", filterWithParams.m_side);
    }
    
    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT01", "2020-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT01", "2020-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT01", "2020-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter4 = new ExecutionFilter(1, "ACCT02", "2020-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter5 = new ExecutionFilter(1, "ACCT01", "2020-01-01", "AAPL", "OPT", "NASDAQ", "BUY");
        ExecutionFilter filter6 = new ExecutionFilter(1, "ACCT01", "2020-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter7 = new ExecutionFilter(1, "ACCT01", "2020-01-01", "AAPL", "STK", "NASDAQ", "SELL");
        
        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
        assertFalse(filter1.equals(filter4));
        assertFalse(filter1.equals(filter5));
        assertFalse(filter1.equals(filter6));
        assertFalse(filter1.equals(filter7));
    }
}