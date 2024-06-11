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
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "09:00:00", "AAPL", "STK", "NYSE", "BUY");
        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT123", filter.m_acctCode);
        assertEquals("09:00:00", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STK", filter.m_secType);
        assertEquals("NYSE", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }

    @Test
    public void testEqualsWithEqualExecutionFilters() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "09:00:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "09:00:00", "AAPL", "STK", "NYSE", "BUY");
        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "09:00:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT123", "09:00:00", "AAPL", "STK", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentAcctCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "09:00:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT456", "09:00:00", "AAPL", "STK", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "09:00:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "10:00:00", "AAPL", "STK", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "09:00:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "09:00:00", "IBM", "STK", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "09:00:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "09:00:00", "AAPL", "OPT", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "09:00:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "09:00:00", "AAPL", "STK", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "09:00:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "09:00:00", "AAPL", "STK", "NYSE", "SELL");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithNullExecutionFilter() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "09:00:00", "AAPL", "STK", "NYSE", "BUY");
        assertFalse(filter1.equals(null));
    }

    @Test
    public void testEqualsWithSameExecutionFilter() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "09:00:00", "AAPL", "STK", "NYSE", "BUY");
        assertTrue(filter1.equals(filter1));
    }
}