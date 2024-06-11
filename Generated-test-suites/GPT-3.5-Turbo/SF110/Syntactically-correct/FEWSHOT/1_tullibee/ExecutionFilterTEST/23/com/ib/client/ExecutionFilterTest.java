package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExecutionFilterTest {
    @Test
    public void testDefaultConstructor() {
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
        ExecutionFilter filter = new ExecutionFilter(1, "A123", "2022-01-01", "AAPL", "STK", "NYSE", "Buy");
        assertEquals(1, filter.m_clientId);
        assertEquals("A123", filter.m_acctCode);
        assertEquals("2022-01-01", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STK", filter.m_secType);
        assertEquals("NYSE", filter.m_exchange);
        assertEquals("Buy", filter.m_side);
    }
    
    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "A123", "2022-01-01", "AAPL", "STK", "NYSE", "Buy");
        ExecutionFilter filter2 = new ExecutionFilter(1, "a123", "2022-01-01", "aapl", "stk", "nyse", "buy");
        ExecutionFilter filter3 = new ExecutionFilter(2, "A123", "2022-01-01", "AAPL", "STK", "NYSE", "Sell");

        assertTrue(filter1.equals(filter1)); // same object
        assertTrue(filter1.equals(filter2)); // case-insensitive strings
        assertFalse(filter1.equals(filter3)); // different client id
    }
}