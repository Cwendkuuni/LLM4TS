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
    }

    @Test
    public void testParameterizedConstructor() {
        ExecutionFilter filter = new ExecutionFilter(10, "ACCT", "2021-01-01", "SYMBOL", "STOCK", "NYSE", "BUY");

        assertEquals(10, filter.m_clientId);
        assertEquals("ACCT", filter.m_acctCode);
        assertEquals("2021-01-01", filter.m_time);
        assertEquals("SYMBOL", filter.m_symbol);
        assertEquals("STOCK", filter.m_secType);
        assertEquals("NYSE", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }

    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT", "2021-01-01", "SYMBOL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT", "2021-01-01", "SYMBOL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT", "2021-01-01", "SYMBOL", "STOCK", "NYSE", "BUY");

        assertTrue(filter1.equals(filter1)); // Same object
        assertTrue(filter1.equals(filter2)); // Same values
        assertFalse(filter1.equals(null)); // Null comparison
        assertFalse(filter1.equals("string")); // Different class
        assertFalse(filter1.equals(filter3)); // Different clientId
        assertFalse(filter1.equals(new ExecutionFilter())); // Different values
    }
}