package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

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
        int clientId = 1;
        String acctCode = "ACCT123";
        String time = "2021-01-01";
        String symbol = "AAPL";
        String secType = "STOCK";
        String exchange = "NASDAQ";
        String side = "BUY";
        
        ExecutionFilter filter = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);
        assertEquals(clientId, filter.m_clientId);
        assertEquals(acctCode, filter.m_acctCode);
        assertEquals(time, filter.m_time);
        assertEquals(symbol, filter.m_symbol);
        assertEquals(secType, filter.m_secType);
        assertEquals(exchange, filter.m_exchange);
        assertEquals(side, filter.m_side);
    }
    
    @Test
    public void testEqualsSameObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertTrue(filter.equals(filter));
    }
    
    @Test
    public void testEqualsNullObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertFalse(filter.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertFalse(filter.equals("Not an ExecutionFilter object"));
    }
    
    @Test
    public void testEqualsEqualObjects() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertTrue(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsUnequalClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsUnequalAcctCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT456", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsUnequalTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2022-01-02", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsUnequalSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "GOOG", "STOCK", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsUnequalSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "OPTION", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsUnequalExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsUnequalSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "SELL");
        assertFalse(filter1.equals(filter2));
    }

}