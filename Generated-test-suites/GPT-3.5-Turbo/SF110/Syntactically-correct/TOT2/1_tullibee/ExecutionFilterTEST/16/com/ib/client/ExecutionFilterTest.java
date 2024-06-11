package com.ib.client;
import static org.junit.Assert.*;
import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterDefaultConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        assertEquals(0, filter.m_clientId);
    }

    @Test
    public void testExecutionFilterParameterizedConstructor() {
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
    public void testEqualsSameObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertTrue(filter.equals(filter));
    }

    @Test
    public void testEqualsDifferentObject() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testEqualsDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsDifferentAcctCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT456", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsNullObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(filter.equals(null));
    }

    @Test
    public void testEqualsDifferentObjectClass() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(filter.equals("not a filter"));
    }

}