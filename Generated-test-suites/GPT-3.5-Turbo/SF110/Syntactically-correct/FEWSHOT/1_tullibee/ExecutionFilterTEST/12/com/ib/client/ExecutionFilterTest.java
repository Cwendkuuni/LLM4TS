package com.ib.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExecutionFilterTest {
    @Test
    public void testConstructor() {
        ExecutionFilter filter = new ExecutionFilter(0, null, null, null, null, null, null);
        assertEquals(0, filter.m_clientId);
        assertEquals(null, filter.m_acctCode);
        assertEquals(null, filter.m_time);
        assertEquals(null, filter.m_symbol);
        assertEquals(null, filter.m_secType);
        assertEquals(null, filter.m_exchange);
        assertEquals(null, filter.m_side);
    }

    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(0, "ACCT1", "10:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(0, "ACCT1", "10:00", "AAPL", "STK", "NYSE", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(1, "ACCT2", "09:00", "GOOG", "OPT", "NASDAQ", "SELL");

        assertEquals(true, filter1.equals(filter1));
        assertEquals(true, filter1.equals(filter2));
        assertEquals(false, filter1.equals(filter3));
    }
}