package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

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
    }
    
    @Test
    public void testParameterizedConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        
        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT123", filter.m_acctCode);
        assertEquals("2021-01-01", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STOCK", filter.m_secType);
        assertEquals("NYSE", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }
    
    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT123", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        
        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
    }
    
    // Edge cases and exception testing
    
    // Test equals with null object
    @Test
    public void testEqualsWithNull() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        
        assertFalse(filter.equals(null));
    }
    
    // Test equals with same instance
    @Test
    public void testEqualsWithSameInstance() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        
        assertTrue(filter.equals(filter));
    }
    
    // Test equals with different class object
    @Test
    public void testEqualsWithDifferentClassObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        String otherObject = "test";
        
        assertFalse(filter.equals(otherObject));
    }
    
    // Test equals with different clientId
    @Test
    public void testEqualsWithDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT123", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    // Test equals with different acctCode
    @Test
    public void testEqualsWithDifferentAcctCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT456", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    // Test equals with different time
    @Test
    public void testEqualsWithDifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    // Test equals with different symbol
    @Test
    public void testEqualsWithDifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "GOOGL", "STOCK", "NYSE", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    // Test equals with different secType
    @Test
    public void testEqualsWithDifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "OPTION", "NYSE", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    // Test equals with different exchange
    @Test
    public void testEqualsWithDifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertFalse(filter1.equals(filter2));
    }
    
    // Test equals with different side
    @Test
    public void testEqualsWithDifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2021-01-01", "AAPL", "STOCK", "NYSE", "SELL");
        
        assertFalse(filter1.equals(filter2));
    }
    
}