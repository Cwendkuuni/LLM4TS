
package com.ib.client;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExecutionFilterTest {
    
    private ExecutionFilter executionFilter;
    
    @Before
    public void setUp() {
        executionFilter = new ExecutionFilter();
    }
    
    @Test
    public void testExecutionFilterDefaultConstructor() {
        assertNotNull(executionFilter);
        assertEquals(0, executionFilter.m_clientId);
    }
    
    @Test
    public void testExecutionFilterParameterizedConstructor() {
        ExecutionFilter executionFilter = new ExecutionFilter(1, "ACCT", "12:00", "IBM", "STOCK", "NYSE", "BUY");
        
        assertEquals(1, executionFilter.m_clientId);
        assertEquals("ACCT", executionFilter.m_acctCode);
        assertEquals("12:00", executionFilter.m_time);
        assertEquals("IBM", executionFilter.m_symbol);
        assertEquals("STOCK", executionFilter.m_secType);
        assertEquals("NYSE", executionFilter.m_exchange);
        assertEquals("BUY", executionFilter.m_side);
    }
    
    @Test
    public void testEqualsNullObject() {
        Object object = null;
        
        assertFalse(executionFilter.equals(object));
    }
    
    @Test
    public void testEqualsSameObject() {
        assertTrue(executionFilter.equals(executionFilter));
    }
    
    @Test
    public void testEqualsDifferentClassObject() {
        Object object = new Object();
        
        assertFalse(executionFilter.equals(object));
    }
    
    @Test
    public void testEqualsDifferentClientId() {
        ExecutionFilter otherFilter = new ExecutionFilter(1, "ACCT", "12:00", "IBM", "STOCK", "NYSE", "BUY");
        executionFilter.m_clientId = 2;
        
        assertFalse(executionFilter.equals(otherFilter));
    }
    
    @Test
    public void testEqualsDifferentAcctCode() {
        ExecutionFilter otherFilter = new ExecutionFilter(1, "ACCT", "12:00", "IBM", "STOCK", "NYSE", "BUY");
        executionFilter.m_acctCode = "DIFFERENT";
        
        assertFalse(executionFilter.equals(otherFilter));
    }
    
    @Test
    public void testEqualsDifferentTime() {
        ExecutionFilter otherFilter = new ExecutionFilter(1, "ACCT", "12:00", "IBM", "STOCK", "NYSE", "BUY");
        executionFilter.m_time = "10:00";
        
        assertFalse(executionFilter.equals(otherFilter));
    }
    
    @Test
    public void testEqualsDifferentSymbol() {
        ExecutionFilter otherFilter = new ExecutionFilter(1, "ACCT", "12:00", "IBM", "STOCK", "NYSE", "BUY");
        executionFilter.m_symbol = "GOOG";
        
        assertFalse(executionFilter.equals(otherFilter));
    }
    
    @Test
    public void testEqualsDifferentSecType() {
        ExecutionFilter otherFilter = new ExecutionFilter(1, "ACCT", "12:00", "IBM", "STOCK", "NYSE", "BUY");
        executionFilter.m_secType = "FUTURE";
        
        assertFalse(executionFilter.equals(otherFilter));
    }
    
    @Test
    public void testEqualsDifferentExchange() {
        ExecutionFilter otherFilter = new ExecutionFilter(1, "ACCT", "12:00", "IBM", "STOCK", "NYSE", "BUY");
        executionFilter.m_exchange = "NASDAQ";
        
        assertFalse(executionFilter.equals(otherFilter));
    }
    
    @Test
    public void testEqualsDifferentSide() {
        ExecutionFilter otherFilter = new ExecutionFilter(1, "ACCT", "12:00", "IBM", "STOCK", "NYSE", "BUY");
        executionFilter.m_side = "SELL";
        
        assertFalse(executionFilter.equals(otherFilter));
    }
    
    @Test
    public void testEqualsEqualObjects() {
        ExecutionFilter otherFilter = new ExecutionFilter(1, "ACCT", "12:00", "IBM", "STOCK", "NYSE", "BUY");
        
        assertTrue(executionFilter.equals(otherFilter));
    }
    
    @Test
    public void testEqualsIgnoreCase() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "Acct", "12:00", "Ibm", "Stock", "NYS", "Buy");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct", "12:00", "ibm", "stock", "nys", "buy");
        
        assertTrue(filter1.equals(filter2));
    }
}
