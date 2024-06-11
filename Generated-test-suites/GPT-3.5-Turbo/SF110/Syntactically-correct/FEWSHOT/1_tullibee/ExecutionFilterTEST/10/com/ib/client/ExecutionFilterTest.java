package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExecutionFilterTest {
    @Test
    public void testConstructorWithParameters() {
        ExecutionFilter filter = new ExecutionFilter(1, "account", "time", "symbol", "secType", "exchange", "side");
        assertEquals(1, filter.m_clientId);
        assertEquals("account", filter.m_acctCode);
        assertEquals("time", filter.m_time);
        assertEquals("symbol", filter.m_symbol);
        assertEquals("secType", filter.m_secType);
        assertEquals("exchange", filter.m_exchange);
        assertEquals("side", filter.m_side);
    }

    @Test
    public void testConstructorWithoutParameters() {
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
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "account", "time", "symbol", "secType", "exchange", "side");
        ExecutionFilter filter2 = new ExecutionFilter(1, "account", "time", "symbol", "secType", "exchange", "side");
        ExecutionFilter filter3 = new ExecutionFilter(2, "account", "time", "symbol", "secType", "exchange", "side");

        assertTrue(filter1.equals(filter1));
        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
        assertFalse(filter1.equals(null));
        assertFalse(filter1.equals(new Object()));
    }
}