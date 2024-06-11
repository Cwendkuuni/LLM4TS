package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "2021-01-01", "IBM", "STOCK", "NYSE", "BUY");

        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT123", filter.m_acctCode);
        assertEquals("2021-01-01", filter.m_time);
        assertEquals("IBM", filter.m_symbol);
        assertEquals("STOCK", filter.m_secType);
        assertEquals("NYSE", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }

    @Test
    public void testExecutionFilterEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "IBM", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "IBM", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT123", "2021-01-01", "IBM", "STOCK", "NYSE", "BUY");

        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
        assertFalse(filter2.equals(null));
        assertTrue(filter1.equals(filter1));
    }

    @Test
    public void testExecutionFilterEqualsWithNull() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "2021-01-01", "IBM", "STOCK", "NYSE", "BUY");

        assertFalse(filter.equals(null));
    }
}