package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testDefaultConstructor() {
        ExecutionFilter executionFilter = new ExecutionFilter();
        assertEquals(0, executionFilter.m_clientId);
    }

    @Test
    public void testParameterConstructor() {
        ExecutionFilter executionFilter = new ExecutionFilter(1, "code", "time", "symbol", "type", "exchange", "side");
        assertEquals(1, executionFilter.m_clientId);
        assertEquals("code", executionFilter.m_acctCode);
        assertEquals("time", executionFilter.m_time);
        assertEquals("symbol", executionFilter.m_symbol);
        assertEquals("type", executionFilter.m_secType);
        assertEquals("exchange", executionFilter.m_exchange);
        assertEquals("side", executionFilter.m_side);
    }

    @Test
    public void testEquals() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "code", "time", "symbol", "type", "exchange", "side");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "code", "time", "symbol", "type", "exchange", "side");
        ExecutionFilter executionFilter3 = new ExecutionFilter(2, "code", "time", "symbol", "type", "exchange", "side");
        assertTrue(executionFilter1.equals(executionFilter2));
        assertFalse(executionFilter1.equals(executionFilter3));
    }
}