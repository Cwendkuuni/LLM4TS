package com.ib.client;
import static org.junit.Assert.*;
import org.junit.Test;

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
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "2021-09-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT123", filter.m_acctCode);
        assertEquals("2021-09-01", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STOCK", filter.m_secType);
        assertEquals("NASDAQ", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }

    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2021-09-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2021-09-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testNotEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2021-09-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "DIFFERENT", "2021-09-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testNullInput() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "2021-09-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertFalse(filter.equals(null));
    }

    @Test
    public void testSameObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "2021-09-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertTrue(filter.equals(filter));
    }

    @Test(expected = ClassCastException.class)
    public void testDifferentObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "2021-09-01", "AAPL", "STOCK", "NASDAQ", "BUY");
        filter.equals(new Object());
    }
}