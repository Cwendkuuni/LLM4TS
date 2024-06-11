package com.ib.client;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testConstructor() {
        ExecutionFilter filter = new ExecutionFilter();
        assertEquals(0, filter.m_clientId);
    }

    @Test
    public void testEquals_sameObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertTrue(filter.equals(filter));
    }

    @Test
    public void testEquals_equalObjects() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testEquals_notEqualObjects() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT", "2020-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEquals_nullObject() {
        ExecutionFilter filter = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertFalse(filter.equals(null));
    }

}