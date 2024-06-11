package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExecutionFilterTest {
    @Test
    public void testConstructorAndGetters() {
        ExecutionFilter executionFilter = new ExecutionFilter();
        assertEquals(0, executionFilter.m_clientId);
        assertNull(executionFilter.m_acctCode);
        assertNull(executionFilter.m_time);
        assertNull(executionFilter.m_symbol);
        assertNull(executionFilter.m_secType);
        assertNull(executionFilter.m_exchange);
        assertNull(executionFilter.m_side);
        
        ExecutionFilter executionFilter = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        assertEquals(1, executionFilter.m_clientId);
        assertEquals("acctCode", executionFilter.m_acctCode);
        assertEquals("time", executionFilter.m_time);
        assertEquals("symbol", executionFilter.m_symbol);
        assertEquals("secType", executionFilter.m_secType);
        assertEquals("exchange", executionFilter.m_exchange);
        assertEquals("side", executionFilter.m_side);
    }
    
    @Test
    public void testEquals() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        ExecutionFilter executionFilter3 = new ExecutionFilter(2, "acctCode", "time", "symbol", "secType", "exchange", "side");
        
        assertTrue(executionFilter1.equals(executionFilter1));
        assertTrue(executionFilter1.equals(executionFilter2));
        assertTrue(executionFilter2.equals(executionFilter1));
        assertFalse(executionFilter1.equals(executionFilter3));
        assertFalse(executionFilter3.equals(executionFilter1));
        
        assertFalse(executionFilter1.equals(null));
        assertFalse(executionFilter1.equals("string"));
    }
    
    @Test
    public void testEqualsWithDifferentValues() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        
        ExecutionFilter executionFilter3 = new ExecutionFilter(2, "acctCode", "time", "symbol", "secType", "exchange", "side");
        ExecutionFilter executionFilter4 = new ExecutionFilter(1, "acctCode2", "time", "symbol", "secType", "exchange", "side");
        ExecutionFilter executionFilter5 = new ExecutionFilter(1, "acctCode", "time2", "symbol", "secType", "exchange", "side");
        ExecutionFilter executionFilter6 = new ExecutionFilter(1, "acctCode", "time", "symbol2", "secType", "exchange", "side");
        ExecutionFilter executionFilter7 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType2", "exchange", "side");
        ExecutionFilter executionFilter8 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange2", "side");
        ExecutionFilter executionFilter9 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side2");
        
        assertTrue(executionFilter1.equals(executionFilter2));
        assertFalse(executionFilter1.equals(executionFilter3));
        assertFalse(executionFilter1.equals(executionFilter4));
        assertFalse(executionFilter1.equals(executionFilter5));
        assertFalse(executionFilter1.equals(executionFilter6));
        assertFalse(executionFilter1.equals(executionFilter7));
        assertFalse(executionFilter1.equals(executionFilter8));
        assertFalse(executionFilter1.equals(executionFilter9));
        assertFalse(executionFilter1.equals(executionFilter1));
        
        assertFalse(executionFilter1.equals(null));
        assertFalse(executionFilter1.equals("string"));
    }
    
    @Test
    public void testHashCode() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        ExecutionFilter executionFilter3 = new ExecutionFilter(2, "acctCode", "time", "symbol", "secType", "exchange", "side");
        
        assertEquals(executionFilter1.hashCode(), executionFilter2.hashCode());
        assertNotEquals(executionFilter1.hashCode(), executionFilter3.hashCode());
    }
}