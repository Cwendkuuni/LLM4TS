package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT001", "2020-01-01", "AAPL", "STK", "NASDAQ", "BUY");

        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT001", filter.m_acctCode);
        assertEquals("2020-01-01", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STK", filter.m_secType);
        assertEquals("NASDAQ", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }

    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT001", "2020-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT001", "2020-01-01", "AAPL", "STK", "NASDAQ", "BUY");

        assertEquals(filter1, filter2);
    }

    @Test
    public void testEquals_Null() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT001", "2020-01-01", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = null;

        assertFalse(filter1.equals(filter2));
    }
}