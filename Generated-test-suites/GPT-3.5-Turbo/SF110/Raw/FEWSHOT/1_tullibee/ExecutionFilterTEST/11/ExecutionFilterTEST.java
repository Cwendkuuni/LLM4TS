package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExecutionFilterTest {
    
    @Test
    public void testConstructorAndGetters() {
        ExecutionFilter filter = new ExecutionFilter(1, "AC123", "2021-01-01 10:00:00", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertEquals(1, filter.m_clientId);
        assertEquals("AC123", filter.m_acctCode);
        assertEquals("2021-01-01 10:00:00", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STOCK", filter.m_secType);
        assertEquals("NASDAQ", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }
    
    @Test
    public void testEquals_sameObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "AC123", "2021-01-01 10:00:00", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertTrue(filter.equals(filter));
    }
    
    @Test
    public void testEquals_nullObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "AC123", "2021-01-01 10:00:00", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertFalse(filter.equals(null));
    }
    
    @Test
    public void testEquals_differentClass() {
        ExecutionFilter filter = new ExecutionFilter(1, "AC123", "2021-01-01 10:00:00", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertFalse(filter.equals("string"));
    }
    
    @Test
    public void testEquals_sameValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2021-01-01 10:00:00", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "AC123", "2021-01-01 10:00:00", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertTrue(filter1.equals(filter2));
    }
    
    @Test
    public void testEquals_differentValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "AC123", "2021-01-01 10:00:00", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "AC456", "2021-01-01 12:00:00", "IBM", "STOCK", "NYSE", "SELL");
        assertFalse(filter1.equals(filter2));
    }
}