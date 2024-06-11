package com.ib.client;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCTCODE", "TIME", "SYMBOL", "SECTYPE", "EXCHANGE", "SIDE");

        assertEquals(1, filter.m_clientId);
        assertEquals("ACCTCODE", filter.m_acctCode);
        assertEquals("TIME", filter.m_time);
        assertEquals("SYMBOL", filter.m_symbol);
        assertEquals("SECTYPE", filter.m_secType);
        assertEquals("EXCHANGE", filter.m_exchange);
        assertEquals("SIDE", filter.m_side);
    }

    @Test
    public void testExecutionFilterEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCTCODE", "TIME", "SYMBOL", "SECTYPE", "EXCHANGE", "SIDE");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCTCODE", "TIME", "SYMBOL", "SECTYPE", "EXCHANGE", "SIDE");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCTCODE", "TIME", "SYMBOL", "SECTYPE", "EXCHANGE", "SIDE");
        ExecutionFilter filter4 = new ExecutionFilter(1, "DIFFERENT", "TIME", "SYMBOL", "SECTYPE", "EXCHANGE", "SIDE");
        ExecutionFilter filter5 = new ExecutionFilter(1, "ACCTCODE", "DIFFERENT", "SYMBOL", "SECTYPE", "EXCHANGE", "SIDE");
        ExecutionFilter filter6 = new ExecutionFilter(1, "ACCTCODE", "TIME", "SYMBOL", "DIFFERENT", "EXCHANGE", "SIDE");
        ExecutionFilter filter7 = new ExecutionFilter(1, "ACCTCODE", "TIME", "SYMBOL", "SECTYPE", "DIFFERENT", "SIDE");
        ExecutionFilter filter8 = new ExecutionFilter(1, "ACCTCODE", "TIME", "SYMBOL", "SECTYPE", "EXCHANGE", "DIFFERENT");

        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
        assertFalse(filter1.equals(filter4));
        assertFalse(filter1.equals(filter5));
        assertFalse(filter1.equals(filter6));
        assertFalse(filter1.equals(filter7));
        assertFalse(filter1.equals(filter8));
    }
}