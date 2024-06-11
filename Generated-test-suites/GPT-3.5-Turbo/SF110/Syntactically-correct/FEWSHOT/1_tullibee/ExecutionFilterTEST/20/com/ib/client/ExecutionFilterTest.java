package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExecutionFilterTest {
    @Test
    public void testConstructorWithParams() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT1", "2020-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT1", filter.m_acctCode);
        assertEquals("2020-01-01", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STOCK", filter.m_secType);
        assertEquals("NASDAQ", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }
    
    @Test
    public void testConstructorWithoutParams() {
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
    public void testEqualsSameObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT1", "2020-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertTrue(filter.equals(filter));
    }
    
    @Test
    public void testEqualsNullObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT1", "2020-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertFalse(filter.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT1", "2020-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertFalse(filter.equals("test"));
    }
    
    @Test
    public void testEqualsEqualObjects() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2020-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2020-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertTrue(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2020-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT1", "2020-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsDifferentAcctCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2020-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT2", "2020-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsDifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2020-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2020-01-02", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsDifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2020-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2020-01-01", "GOOG", "STOCK", "NASDAQ", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsDifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2020-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2020-01-01", "AAPL", "OPTION", "NASDAQ", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsDifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2020-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2020-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsDifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "2020-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "2020-01-01", "AAPL", "STOCK", "NASDAQ", "SELL");
        
        assertFalse(filter1.equals(filter2));
    }
}