package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testEquals() {
        // Test case 1: p_other is null
        ExecutionFilter filter1 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        boolean result1 = filter1.equals(null);
        assertFalse(result1);

        // Test case 2: p_other is the same object reference
        ExecutionFilter filter2 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        boolean result2 = filter2.equals(filter2);
        assertTrue(result2);

        // Test case 3: all attributes are equal
        ExecutionFilter filter3 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        ExecutionFilter filter4 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        boolean result3 = filter3.equals(filter4);
        assertTrue(result3);

        // Test case 4: different client ID
        ExecutionFilter filter5 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side");
        ExecutionFilter filter6 = new ExecutionFilter(2, "acctCode", "time", "symbol", "secType", "exchange", "side");
        boolean result4 = filter5.equals(filter6);
        assertFalse(result4);

        // Test case 5: different account code
        ExecutionFilter filter7 = new ExecutionFilter(1, "acctCode1", "time", "symbol", "secType", "exchange", "side");
        ExecutionFilter filter8 = new ExecutionFilter(1, "acctCode2", "time", "symbol", "secType", "exchange", "side");
        boolean result5 = filter7.equals(filter8);
        assertFalse(result5);

        // Test case 6: different time
        ExecutionFilter filter9 = new ExecutionFilter(1, "acctCode", "time1", "symbol", "secType", "exchange", "side");
        ExecutionFilter filter10 = new ExecutionFilter(1, "acctCode", "time2", "symbol", "secType", "exchange", "side");
        boolean result6 = filter9.equals(filter10);
        assertFalse(result6);

        // Test case 7: different symbol
        ExecutionFilter filter11 = new ExecutionFilter(1, "acctCode", "time", "symbol1", "secType", "exchange", "side");
        ExecutionFilter filter12 = new ExecutionFilter(1, "acctCode", "time", "symbol2", "secType", "exchange", "side");
        boolean result7 = filter11.equals(filter12);
        assertFalse(result7);

        // Test case 8: different security type
        ExecutionFilter filter13 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType1", "exchange", "side");
        ExecutionFilter filter14 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType2", "exchange", "side");
        boolean result8 = filter13.equals(filter14);
        assertFalse(result8);

        // Test case 9: different exchange
        ExecutionFilter filter15 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange1", "side");
        ExecutionFilter filter16 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange2", "side");
        boolean result9 = filter15.equals(filter16);
        assertFalse(result9);

        // Test case 10: different side
        ExecutionFilter filter17 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side1");
        ExecutionFilter filter18 = new ExecutionFilter(1, "acctCode", "time", "symbol", "secType", "exchange", "side2");
        boolean result10 = filter17.equals(filter18);
        assertFalse(result10);
    }
}