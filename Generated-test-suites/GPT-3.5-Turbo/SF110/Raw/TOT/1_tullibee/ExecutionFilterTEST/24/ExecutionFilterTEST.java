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
        ExecutionFilter filter = new ExecutionFilter(1, "ABC", "2022-01-01", "IBM", "STK", "NYSE", "BUY");
        assertEquals(1, filter.m_clientId);
        assertEquals("ABC", filter.m_acctCode);
        assertEquals("2022-01-01", filter.m_time);
        assertEquals("IBM", filter.m_symbol);
        assertEquals("STK", filter.m_secType);
        assertEquals("NYSE", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }
    
    @Test
    public void testEqualsSameObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ABC", "2022-01-01", "IBM", "STK", "NYSE", "BUY");
        assertTrue(filter.equals(filter));
    }
    
    @Test
    public void testEqualsNullObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ABC", "2022-01-01", "IBM", "STK", "NYSE", "BUY");
        assertFalse(filter.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        ExecutionFilter filter = new ExecutionFilter(1, "ABC", "2022-01-01", "IBM", "STK", "NYSE", "BUY");
        assertFalse(filter.equals("not a filter object"));
    }
    
    @Test
    public void testEqualsEqualFilter() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ABC", "2022-01-01", "IBM", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ABC", "2022-01-01", "IBM", "STK", "NYSE", "BUY");
        assertTrue(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ABC", "2022-01-01", "IBM", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ABC", "2022-01-01", "IBM", "STK", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsDifferentAcctCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ABC", "2022-01-01", "IBM", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "XYZ", "2022-01-01", "IBM", "STK", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsDifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ABC", "2022-01-01", "IBM", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ABC", "2022-01-02", "IBM", "STK", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsDifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ABC", "2022-01-01", "IBM", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ABC", "2022-01-01", "AAPL", "STK", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsDifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ABC", "2022-01-01", "IBM", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ABC", "2022-01-01", "IBM", "OPT", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsDifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ABC", "2022-01-01", "IBM", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ABC", "2022-01-01", "IBM", "STK", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsDifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ABC", "2022-01-01", "IBM", "STK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ABC", "2022-01-01", "IBM", "STK", "NYSE", "SELL");
        assertFalse(filter1.equals(filter2));
    }
    
}