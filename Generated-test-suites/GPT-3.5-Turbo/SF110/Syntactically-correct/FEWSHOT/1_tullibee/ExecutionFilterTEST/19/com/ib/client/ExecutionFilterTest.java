
package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExecutionFilterTest {
    
    @Test
    public void testConstructorWithParameters() {
        ExecutionFilter executionFilter = new ExecutionFilter(1, "ACCT001", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertEquals(1, executionFilter.m_clientId);
        assertEquals("ACCT001", executionFilter.m_acctCode);
        assertEquals("2022-01-01", executionFilter.m_time);
        assertEquals("AAPL", executionFilter.m_symbol);
        assertEquals("STOCK", executionFilter.m_secType);
        assertEquals("NASDAQ", executionFilter.m_exchange);
        assertEquals("BUY", executionFilter.m_side);
    }
    
    @Test
    public void testConstructorWithDefaultClientId() {
        ExecutionFilter executionFilter = new ExecutionFilter();
        
        assertEquals(0, executionFilter.m_clientId);
    }
    
    @Test
    public void testEqualsAndHashCode() {
        ExecutionFilter executionFilter1 = new ExecutionFilter(1, "ACCT001", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter executionFilter2 = new ExecutionFilter(1, "ACCT001", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter executionFilter3 = new ExecutionFilter(2, "ACCT002", "2022-01-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        
        assertEquals(executionFilter1, executionFilter2);
        assertEquals(executionFilter1.hashCode(), executionFilter2.hashCode());
        
        assertEquals(false, executionFilter1.equals(executionFilter3));
    }
}

