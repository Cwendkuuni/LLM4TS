package com.ib.client;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {
    
    @Test
    public void testExecutionFilterConstructor1() {
        ExecutionFilter filter = new ExecutionFilter();
        assertEquals(0, filter.m_clientId);
    }
    
    @Test
    public void testExecutionFilterConstructor2() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT123", filter.m_acctCode);
        assertEquals("10:00", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STOCK", filter.m_secType);
        assertEquals("NYSE", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }
    
    @Test
    public void testEqualsMethod_sameObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertTrue(filter.equals(filter));
    }
    
    @Test
    public void testEqualsMethod_equalObjects() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertTrue(filter1.equals(filter2));
    }
    
    @Test
    public void testEqualsMethod_notEqualObjects() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT456", "12:00", "GOOG", "STOCK", "NASDAQ", "SELL");
        assertFalse(filter1.equals(filter2));
    }

}