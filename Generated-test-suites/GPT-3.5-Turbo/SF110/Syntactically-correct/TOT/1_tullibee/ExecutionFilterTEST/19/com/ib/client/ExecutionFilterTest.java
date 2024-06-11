package com.ib.client;

import static org.junit.Assert.*;
import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testEquals() {
        // Typical use case
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT123", "09:00", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT123", "09:00", "AAPL", "STK", "NASDAQ", "BUY");
        assertTrue(filter1.equals(filter2));
        
        // Edge case: different client id
        ExecutionFilter filter3 = new ExecutionFilter(1, "ACCT123", "09:00", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter4 = new ExecutionFilter(2, "ACCT123", "09:00", "AAPL", "STK", "NASDAQ", "BUY");
        assertFalse(filter3.equals(filter4));
        
        // Edge case: different account code
        ExecutionFilter filter5 = new ExecutionFilter(1, "ACCT123", "09:00", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter6 = new ExecutionFilter(1, "ACCT456", "09:00", "AAPL", "STK", "NASDAQ", "BUY");
        assertFalse(filter5.equals(filter6));
        
        // Edge case: different time
        ExecutionFilter filter7 = new ExecutionFilter(1, "ACCT123", "09:00", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter8 = new ExecutionFilter(1, "ACCT123", "10:00", "AAPL", "STK", "NASDAQ", "BUY");
        assertFalse(filter7.equals(filter8));
        
        // Edge case: different symbol
        ExecutionFilter filter9 = new ExecutionFilter(1, "ACCT123", "09:00", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter10 = new ExecutionFilter(1, "ACCT123", "09:00", "MSFT", "STK", "NASDAQ", "BUY");
        assertFalse(filter9.equals(filter10));
        
        // Edge case: different security type
        ExecutionFilter filter11 = new ExecutionFilter(1, "ACCT123", "09:00", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter12 = new ExecutionFilter(1, "ACCT123", "09:00", "AAPL", "OPT", "NASDAQ", "BUY");
        assertFalse(filter11.equals(filter12));
        
        // Edge case: different exchange
        ExecutionFilter filter13 = new ExecutionFilter(1, "ACCT123", "09:00", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter14 = new ExecutionFilter(1, "ACCT123", "09:00", "AAPL", "STK", "NYSE", "BUY");
        assertFalse(filter13.equals(filter14));
        
        // Edge case: different side
        ExecutionFilter filter15 = new ExecutionFilter(1, "ACCT123", "09:00", "AAPL", "STK", "NASDAQ", "BUY");
        ExecutionFilter filter16 = new ExecutionFilter(1, "ACCT123", "09:00", "AAPL", "STK", "NASDAQ", "SELL");
        assertFalse(filter15.equals(filter16));
        
        // Error scenario: object is null
        ExecutionFilter filter17 = new ExecutionFilter(1, "ACCT123", "09:00", "AAPL", "STK", "NASDAQ", "BUY");
        assertFalse(filter17.equals(null));
        
        // Error scenario: different object type
        ExecutionFilter filter18 = new ExecutionFilter(1, "ACCT123", "09:00", "AAPL", "STK", "NASDAQ", "BUY");
        String str = "ExecutionFilter";
        assertFalse(filter18.equals(str));
    }
}