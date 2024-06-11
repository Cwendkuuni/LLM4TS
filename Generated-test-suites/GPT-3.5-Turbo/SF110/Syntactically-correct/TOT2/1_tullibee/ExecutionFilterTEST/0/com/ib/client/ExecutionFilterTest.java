package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "09:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT123", filter.m_acctCode);
        assertEquals("09:00", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STOCK", filter.m_secType);
        assertEquals("NYSE", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }

    @Test
    public void testExecutionFilterEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "09:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "09:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterNotEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "09:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT456", "10:00", "GOOG", "STOCK", "NASDAQ", "SELL");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testExecutionFilterNullEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "09:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(filter1.equals(null));
    }

    @Test
    public void testExecutionFilterSameObjectEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "09:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertTrue(filter1.equals(filter1));
    }

    @Test
    public void testExecutionFilterNotInstanceOfEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "09:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(filter1.equals(new Object()));
    }

}