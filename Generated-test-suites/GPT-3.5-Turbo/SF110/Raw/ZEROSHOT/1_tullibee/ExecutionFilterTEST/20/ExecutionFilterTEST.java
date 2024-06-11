package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT1", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter4 = new ExecutionFilter(1, "ACCT2", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter5 = new ExecutionFilter(1, "ACCT1", "10:00", "AAPL", "FUTURE", "NYSE", "BUY");
        ExecutionFilter filter6 = new ExecutionFilter(1, "ACCT1", "10:00", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter7 = new ExecutionFilter(1, "ACCT1", "10:00", "AAPL", "STOCK", "NYSE", "SELL");
        ExecutionFilter filter8 = null;
        
        assertEquals(filter1, filter1);
        assertEquals(filter1, filter2);
        assertNotEquals(filter1, filter3);
        assertNotEquals(filter1, filter4);
        assertNotEquals(filter1, filter5);
        assertNotEquals(filter1, filter6);
        assertNotEquals(filter1, filter7);
        assertNotEquals(filter1, filter8);
    }
}