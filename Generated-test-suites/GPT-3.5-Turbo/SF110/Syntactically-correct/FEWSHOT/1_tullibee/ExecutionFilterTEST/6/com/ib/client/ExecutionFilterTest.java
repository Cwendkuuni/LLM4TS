package com.ib.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        assertEquals(0, filter.m_clientId);
        assertEquals(null, filter.m_acctCode);
        assertEquals(null, filter.m_time);
        assertEquals(null, filter.m_symbol);
        assertEquals(null, filter.m_secType);
        assertEquals(null, filter.m_exchange);
        assertEquals(null, filter.m_side);

        filter = new ExecutionFilter(1, "ACCT1", "2021-01-01", "AAPL", "STK", "NYSE", "BUY");
        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT1", filter.m_acctCode);
        assertEquals("2021-01-01", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STK", filter.m_secType);
        assertEquals("NYSE", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }

    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2021-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2021-01-01", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT2", "2021-01-02", "GOOGL", "OPT", "NASDAQ", "SELL");

        assertEquals(filter1, filter2);
        assertEquals(filter2, filter1);
        assertEquals(filter1, filter1);
        assertEquals(filter2, filter2);
        assertEquals(filter3, filter3);
    }
}