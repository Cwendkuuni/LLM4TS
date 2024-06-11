package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExecutionFilterTest {

    @Test
    public void testEquals() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "acctCode1", "time1", "symbol1", "secType1", "exchange1", "side1");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acctCode1", "time1", "symbol1", "secType1", "exchange1", "side1");
        ExecutionFilter filter3 = new ExecutionFilter(2, "acctCode2", "time2", "symbol2", "secType2", "exchange2", "side2");

        // Test equal filters
        assertEquals(true, filter1.equals(filter2));

        // Test non-equal filters
        assertEquals(false, filter1.equals(filter3));
    }
}