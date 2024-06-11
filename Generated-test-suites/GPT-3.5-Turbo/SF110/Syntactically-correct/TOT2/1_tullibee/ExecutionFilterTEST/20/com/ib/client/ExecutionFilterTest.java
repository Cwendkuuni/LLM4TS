package com.ib.client;
import static org.junit.Assert.*;
import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterConstructor1() {
        ExecutionFilter filter = new ExecutionFilter();
        assertEquals(0, filter.m_clientId);
    }
    
    @Test
    public void testExecutionFilterConstructor2() {
        ExecutionFilter filter = new ExecutionFilter(123, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertEquals(123, filter.m_clientId);
        assertEquals("ACCT123", filter.m_acctCode);
        assertEquals("2022-01-01", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STOCK", filter.m_secType);
        assertEquals("NYSE", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }
    
    @Test
    public void testEqualsSameObject() {
        ExecutionFilter filter = new ExecutionFilter(123, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertTrue(filter.equals(filter));
    }
    
    @Test
    public void testEqualsNullObject() {
        ExecutionFilter filter = new ExecutionFilter(123, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(filter.equals(null));
    }
    
    @Test
    public void testEqualsDifferentObject() {
        ExecutionFilter filter1 = new ExecutionFilter(123, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(124, "ACCT124", "2022-01-02", "GOOG", "STOCK", "NASDAQ", "SELL");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsSameAttributes() {
        ExecutionFilter filter1 = new ExecutionFilter(123, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(123, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertTrue(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsDifferentAttributes() {
        ExecutionFilter filter1 = new ExecutionFilter(123, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(123, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "SELL");
        assertFalse(filter1.equals(filter2));
    }
}