package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testExecutionFilterDefaultConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        assertEquals(0, filter.m_clientId);
    }

    @Test
    public void testExecutionFilterParameterizedConstructor() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertEquals(1, filter.m_clientId);
        assertEquals("ACCT123", filter.m_acctCode);
        assertEquals("2022-01-01", filter.m_time);
        assertEquals("AAPL", filter.m_symbol);
        assertEquals("STOCK", filter.m_secType);
        assertEquals("NYSE", filter.m_exchange);
        assertEquals("BUY", filter.m_side);
    }

    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT456", "2023-01-01", "GOOG", "STOCK", "NASDAQ", "SELL");

        assertTrue(filter1.equals(filter2));
        assertFalse(filter1.equals(filter3));
    }

    @Test
    public void testEqualsWithNull() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(filter.equals(null));
    }

    @Test
    public void testEqualsWithSameObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertTrue(filter.equals(filter));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(filter.equals("Not an ExecutionFilter"));
    }

    @Test
    public void testEqualsWithDifferentValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT456", "2022-01-01", "AAPL", "STOCK", "NYSE", "BUY");

        assertFalse(filter1.equals(filter2));
    }

}