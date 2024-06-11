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
    public void testEquals_nullObject_returnsFalse() {
        assertFalse(executionFilter.equals(null));
    }
    
    @Test
    public void testEquals_sameObject_returnsTrue() {
        assertTrue(executionFilter.equals(executionFilter));
    }
    
    @Test
    public void testEquals_differentObjectWithSameFields_returnsTrue() {
        ExecutionFilter other = new ExecutionFilter();
        other.m_clientId = executionFilter.m_clientId;
        other.m_acctCode = executionFilter.m_acctCode;
        other.m_time = executionFilter.m_time;
        other.m_symbol = executionFilter.m_symbol;
        other.m_secType = executionFilter.m_secType;
        other.m_exchange = executionFilter.m_exchange;
        other.m_side = executionFilter.m_side;
        
        assertTrue(executionFilter.equals(other));
    }
    
    @Test
    public void testEquals_differentObjectWithDifferentFields_returnsFalse() {
        ExecutionFilter other = new ExecutionFilter();
        other.m_clientId = 1;
        other.m_acctCode = "TEST";
        other.m_time = "12:00";
        other.m_symbol = "AAPL";
        other.m_secType = "STOCK";
        other.m_exchange = "NASDAQ";
        other.m_side = "BUY";
        
        assertFalse(executionFilter.equals(other));
    }
}