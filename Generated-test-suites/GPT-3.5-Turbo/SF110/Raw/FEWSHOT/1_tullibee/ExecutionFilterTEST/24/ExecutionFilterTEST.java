package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "code", "time", "symbol", "type", "exchange", "side");
        assertEquals(1, filter.m_clientId);
        assertEquals("code", filter.m_acctCode);
        assertEquals("time", filter.m_time);
        assertEquals("symbol", filter.m_symbol);
        assertEquals("type", filter.m_secType);
        assertEquals("exchange", filter.m_exchange);
        assertEquals("side", filter.m_side);
    }

    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "code", "time", "symbol", "type", "exchange", "side");
        ExecutionFilter filter2 = new ExecutionFilter(1, "code", "time", "symbol", "type", "exchange", "side");
        assertEquals(filter1, filter2);

        ExecutionFilter filter3 = new ExecutionFilter(2, "code", "time", "symbol", "type", "exchange", "side");
        assertNotEquals(filter1, filter3);

        ExecutionFilter filter4 = new ExecutionFilter(1, "code2", "time", "symbol", "type", "exchange", "side");
        assertNotEquals(filter1, filter4);

        ExecutionFilter filter5 = new ExecutionFilter(1, "code", "time2", "symbol", "type", "exchange", "side");
        assertNotEquals(filter1, filter5);

        ExecutionFilter filter6 = new ExecutionFilter(1, "code", "time", "symbol2", "type", "exchange", "side");
        assertNotEquals(filter1, filter6);

        ExecutionFilter filter7 = new ExecutionFilter(1, "code", "time", "symbol", "type2", "exchange", "side");
        assertNotEquals(filter1, filter7);

        ExecutionFilter filter8 = new ExecutionFilter(1, "code", "time", "symbol", "type", "exchange2", "side");
        assertNotEquals(filter1, filter8);

        ExecutionFilter filter9 = new ExecutionFilter(1, "code", "time", "symbol", "type", "exchange", "side2");
        assertNotEquals(filter1, filter9);
    }
}