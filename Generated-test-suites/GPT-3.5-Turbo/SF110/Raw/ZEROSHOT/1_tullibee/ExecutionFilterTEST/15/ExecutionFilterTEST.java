package com.ib.client;

import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testDefaultConstructor() {
        ExecutionFilter executionFilter = new ExecutionFilter();
        Assert.assertEquals(0, executionFilter.m_clientId);
        Assert.assertNull(executionFilter.m_acctCode);
        Assert.assertNull(executionFilter.m_time);
        Assert.assertNull(executionFilter.m_symbol);
        Assert.assertNull(executionFilter.m_secType);
        Assert.assertNull(executionFilter.m_exchange);
        Assert.assertNull(executionFilter.m_side);
    }

    @Test
    public void testParameterizedConstructor() {
        int clientId = 1;
        String acctCode = "ACCT";
        String time = "TIME";
        String symbol = "SYMBOL";
        String secType = "SEC_TYPE";
        String exchange = "EXCHANGE";
        String side = "SIDE";

        ExecutionFilter executionFilter = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);
        Assert.assertEquals(clientId, executionFilter.m_clientId);
        Assert.assertEquals(acctCode, executionFilter.m_acctCode);
        Assert.assertEquals(time, executionFilter.m_time);
        Assert.assertEquals(symbol, executionFilter.m_symbol);
        Assert.assertEquals(secType, executionFilter.m_secType);
        Assert.assertEquals(exchange, executionFilter.m_exchange);
        Assert.assertEquals(side, executionFilter.m_side);
    }

    @Test
    public void testEqualsWithNullObject() {
        ExecutionFilter executionFilter = new ExecutionFilter();
        boolean result = executionFilter.equals(null);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualsWithSameObject() {
        ExecutionFilter executionFilter = new ExecutionFilter();
        boolean result = executionFilter.equals(executionFilter);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualsWithDifferentObject() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT", "TIME", "SYMBOL", "SEC_TYPE", "EXCHANGE", "SIDE");
        ExecutionFilter executionFilter2 = new ExecutionFilter(2, "ACCT", "TIME", "SYMBOL", "SEC_TYPE", "EXCHANGE", "SIDE");
        boolean result = executionFilter1.equals(executionFilter2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualsWithEqualObject() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT", "TIME", "SYMBOL", "SEC_TYPE", "EXCHANGE", "SIDE");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "acct", "time", "symbol", "sec_type", "exchange", "side");
        boolean result = executionFilter1.equals(executionFilter2);
        Assert.assertTrue(result);
    }

}