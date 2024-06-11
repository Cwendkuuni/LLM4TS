
package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testEquals_SameObject_ReturnsTrue() {
        ExecutionFilter filter = new ExecutionFilter();
        assertTrue(filter.equals(filter));
    }

    @Test
    public void testEquals_NullInput_ReturnsFalse() {
        ExecutionFilter filter = new ExecutionFilter();
        assertFalse(filter.equals(null));
    }

    @Test
    public void testEquals_DifferentObject_ReturnsFalse() {
        ExecutionFilter filter = new ExecutionFilter();
        Object obj = new Object();
        assertFalse(filter.equals(obj));
    }

    @Test
    public void testEquals_SameValues_ReturnsTrue() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT", "TIME", "SYM", "SEC", "EXCH", "SIDE");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT", "TIME", "SYM", "SEC", "EXCH", "SIDE");
        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testEquals_DifferentClientId_ReturnsFalse() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT", "TIME", "SYM", "SEC", "EXCH", "SIDE");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT", "TIME", "SYM", "SEC", "EXCH", "SIDE");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEquals_NullAcctCode_ReturnsFalse() {
        ExecutionFilter filter1 = new ExecutionFilter(1, null, "TIME", "SYM", "SEC", "EXCH", "SIDE");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT", "TIME", "SYM", "SEC", "EXCH", "SIDE");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEquals_DifferentTime_ReturnsFalse() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT", "TIME1", "SYM", "SEC", "EXCH", "SIDE");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT", "TIME2", "SYM", "SEC", "EXCH", "SIDE");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEquals_IgnoreCase_ReturnsTrue() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT", "TIME", "SYM", "sec", "EXCH", "side");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT", "TIME", "SYM", "SEC", "EXCH", "SIDE");
        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testEquals_DifferentExchange_ReturnsFalse() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT", "TIME", "SYM", "SEC", "EXCH1", "SIDE");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT", "TIME", "SYM", "SEC", "EXCH2", "SIDE");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEquals_DifferentSide_ReturnsFalse() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT", "TIME", "SYM", "SEC", "EXCH", "SIDE1");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT", "TIME", "SYM", "SEC", "EXCH", "SIDE2");
        assertFalse(filter1.equals(filter2));
    }
}
