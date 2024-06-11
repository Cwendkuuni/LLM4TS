package com.ib.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertEquals(true, filter1.equals(filter2));
        
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT", "2020-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertEquals(false, filter1.equals(filter3));
        
        ExecutionFilter filter4 = new ExecutionFilter(1, "acct", "2020-01-01", "AAPL", "STOCK", "NYSE", "BUY");
        assertEquals(false, filter1.equals(filter4));
        
        ExecutionFilter filter5 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "EQTY", "NYSE", "BUY");
        assertEquals(false, filter1.equals(filter5));
        
        ExecutionFilter filter6 = new ExecutionFilter(1, "ACCT", "2020-01-02", "AAPL", "STOCK", "NYSE", "BUY");
        assertEquals(false, filter1.equals(filter6));
        
        ExecutionFilter filter7 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STOCK", "CME", "BUY");
        assertEquals(false, filter1.equals(filter7));
        
        ExecutionFilter filter8 = new ExecutionFilter(1, "ACCT", "2020-01-01", "AAPL", "STOCK", "NYSE", "SELL");
        assertEquals(false, filter1.equals(filter8));
    }
}