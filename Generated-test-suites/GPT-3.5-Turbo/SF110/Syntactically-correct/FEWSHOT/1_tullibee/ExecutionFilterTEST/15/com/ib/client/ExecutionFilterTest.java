package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExecutionFilterTest {
    @Test
    public void testConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        assertEquals(0, filter.m_clientId);
        assertNull(filter.m_acctCode);
        assertNull(filter.m_time);
        assertNull(filter.m_symbol);
        assertNull(filter.m_secType);
        assertNull(filter.m_exchange);
        assertNull(filter.m_side);
        
        int clientId = 1;
        String acctCode = "ACCT123";
        String time = "20220101";
        String symbol = "AAPL";
        String secType = "STOCK";
        String exchange = "NASDAQ";
        String side = "BUY";
        
        filter = new ExecutionFilter(clientId, acctCode, time, symbol, secType, exchange, side);
        assertEquals(clientId, filter.m_clientId);
        assertEquals(acctCode, filter.m_acctCode);
        assertEquals(time, filter.m_time);
        assertEquals(symbol, filter.m_symbol);
        assertEquals(secType, filter.m_secType);
        assertEquals(exchange, filter.m_exchange);
        assertEquals(side, filter.m_side);
    }
    
    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "20220101", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "20220101", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT123", "20220101", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter4 = new ExecutionFilter(1, "ACCT456", "20220101", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter5 = new ExecutionFilter(1, "ACCT123", "20220102", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter6 = new ExecutionFilter(1, "ACCT123", "20220101", "AAPL", "OPTION", "NASDAQ", "BUY");
        ExecutionFilter filter7 = new ExecutionFilter(1, "ACCT123", "20220101", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter8 = new ExecutionFilter(1, "ACCT123", "20220101", "AAPL", "STOCK", "NASDAQ", "SELL");
        
        assertTrue(filter1.equals(filter1));
        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
        assertFalse(filter1.equals(filter4));
        assertFalse(filter1.equals(filter5));
        assertFalse(filter1.equals(filter6));
        assertFalse(filter1.equals(filter7));
        assertFalse(filter1.equals(filter8));
    }
}