package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {
    @Test
    public void testDefaultConstructor() {
        ExecutionFilter executionFilter = new ExecutionFilter();
        assertEquals(0, executionFilter.m_clientId);
        assertNull(executionFilter.m_acctCode);
        assertNull(executionFilter.m_time);
        assertNull(executionFilter.m_symbol);
        assertNull(executionFilter.m_secType);
        assertNull(executionFilter.m_exchange);
        assertNull(executionFilter.m_side);
    }

    @Test
    public void testParameterizedConstructor() {
        ExecutionFilter executionFilter = new ExecutionFilter(1, "ACCT", "TIME", "SYMBOL", "SEC_TYPE", "EXCHANGE", "SIDE");
        assertEquals(1, executionFilter.m_clientId);
        assertEquals("ACCT", executionFilter.m_acctCode);
        assertEquals("TIME", executionFilter.m_time);
        assertEquals("SYMBOL", executionFilter.m_symbol);
        assertEquals("SEC_TYPE", executionFilter.m_secType);
        assertEquals("EXCHANGE", executionFilter.m_exchange);
        assertEquals("SIDE", executionFilter.m_side);
    }

    @Test
    public void testEquals() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT", "TIME", "SYMBOL", "SEC_TYPE", "EXCHANGE", "SIDE");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT", "TIME", "SYMBOL", "SEC_TYPE", "EXCHANGE", "SIDE");
        ExecutionFilter executionFilter3 = new ExecutionFilter(2, "ACCT", "TIME", "SYMBOL", "SEC_TYPE", "EXCHANGE", "SIDE");

        assertTrue(executionFilter1.equals(executionFilter2));
        assertFalse(executionFilter1.equals(executionFilter3));
    }
}