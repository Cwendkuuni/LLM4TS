package com.ib.client;

import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testDefaultConstructor() {
        ExecutionFilter executionFilter = new ExecutionFilter();
        Assert.assertEquals(0, executionFilter.m_clientId);
    }
    
    @Test
    public void testParameterizedConstructor() {
        int clientId = 10;
        String acctCode = "ACCT123";
        String time = "10:00 AM";
        String symbol = "AAPL";
        String secType = "STOCK";
        String exchange = "NASDAQ";
        String side = "BUY";
        
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
    public void testEqualsMethod() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(10, "ACCT123", "10:00 AM", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(10, "ACCT123", "10:00 AM", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter executionFilter3 = new ExecutionFilter(20, "ACCT456", "12:00 PM", "GOOG", "STOCK", "NYSE", "SELL");
        
        Assert.assertTrue(executionFilter1.equals(executionFilter2));
        Assert.assertFalse(executionFilter1.equals(executionFilter3));
    }
}