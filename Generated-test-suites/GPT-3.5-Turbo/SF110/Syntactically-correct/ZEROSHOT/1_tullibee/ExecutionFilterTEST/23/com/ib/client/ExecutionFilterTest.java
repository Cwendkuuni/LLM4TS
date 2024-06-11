package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testEquals() {
        // Test if two ExecutionFilters are equal
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "10:00", "AAPL", "STOCK", "NASDAQ", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT1", "10:00", "AAPL", "STOCK", "NASDAQ", "BUY");
        assertTrue(filter1.equals(filter2));

        // Test if two ExecutionFilters are not equal
        ExecutionFilter filter3 = new ExecutionFilter(2, "ACCT2", "12:00", "GOOG", "STOCK", "NYSE", "SELL");
        assertFalse(filter1.equals(filter3));
    }

}