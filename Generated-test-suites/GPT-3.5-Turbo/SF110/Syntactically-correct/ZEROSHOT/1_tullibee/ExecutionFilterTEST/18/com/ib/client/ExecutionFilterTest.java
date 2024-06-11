package com.ib.client;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter();
        ExecutionFilter filter2 = new ExecutionFilter();

        // Test when both objects are empty
        assertEquals(filter1, filter2);

        // Test when clientIds are different
        filter1.m_clientId = 1;
        assertNotEquals(filter1, filter2);

        // Test when acctCodes are different
        filter2.m_acctCode = "ABC123";
        assertNotEquals(filter1, filter2);

        // Test when times are different
        filter1.m_time = "2022-01-01 10:00:00";
        assertNotEquals(filter1, filter2);

        // Test when symbols are different
        filter2.m_symbol = "AAPL";
        assertNotEquals(filter1, filter2);

        // Test when secTypes are different
        filter1.m_secType = "STK";
        assertNotEquals(filter1, filter2);

        // Test when exchanges are different
        filter2.m_exchange = "NASDAQ";
        assertNotEquals(filter1, filter2);

        // Test when sides are different
        filter1.m_side = "BUY";
        assertNotEquals(filter1, filter2);

        // Test when all attributes are the same
        filter2.m_clientId = 1;
        filter2.m_acctCode = "ABC123";
        filter2.m_time = "2022-01-01 10:00:00";
        filter2.m_symbol = "AAPL";
        filter2.m_secType = "STK";
        filter2.m_exchange = "NASDAQ";
        filter2.m_side = "BUY";
        assertEquals(filter1, filter2);
    }
}