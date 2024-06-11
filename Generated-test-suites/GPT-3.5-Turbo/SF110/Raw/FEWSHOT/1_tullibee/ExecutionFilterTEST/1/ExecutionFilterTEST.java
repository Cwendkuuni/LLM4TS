package com.ib.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExecutionFilterTest {
    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        assertEquals(1, filter.m_clientId);
        assertEquals("acctCode", filter.m_acctCode);
        assertEquals("time", filter.m_time);
        assertEquals("symbol", filter.m_symbol);
        assertEquals("secType", filter.m_secType);
        assertEquals("exchange", filter.m_exchange);
        assertEquals("side", filter.m_side);
    }
    
    @Test
    public void testExecutionFilterEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        assertEquals(true, filter1.equals(filter2));
    }
}