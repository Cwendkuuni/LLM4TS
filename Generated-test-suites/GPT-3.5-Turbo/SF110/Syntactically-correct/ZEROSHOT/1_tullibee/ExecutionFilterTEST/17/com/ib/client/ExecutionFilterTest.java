package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testEquals_returnsFalse_whenPassedNull() {
        ExecutionFilter filter = new ExecutionFilter();
        assertFalse(filter.equals(null));
    }
    
    @Test
    public void testEquals_returnsTrue_whenPassedSameObject() {
        ExecutionFilter filter = new ExecutionFilter();
        assertTrue(filter.equals(filter));
    }
    
    @Test
    public void testEquals_returnsTrue_whenPassedEqualObject() {
        ExecutionFilter filter1 = new ExecutionFilter();
        ExecutionFilter filter2 = new ExecutionFilter();
        assertTrue(filter1.equals(filter2));
    }
    
    @Test
    public void testEquals_returnsFalse_whenPassedDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT", "TIME", "SYM", "SEC", "EX", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(2, "ACCT", "TIME", "SYM", "SEC", "EX", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEquals_returnsFalse_whenPassedDifferentAcctCode() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT1", "TIME", "SYM", "SEC", "EX", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT2", "TIME", "SYM", "SEC", "EX", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEquals_returnsFalse_whenPassedDifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT", "TIME1", "SYM", "SEC", "EX", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT", "TIME2", "SYM", "SEC", "EX", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEquals_returnsFalse_whenPassedDifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT", "TIME", "SYM1", "SEC", "EX", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT", "TIME", "SYM2", "SEC", "EX", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEquals_returnsFalse_whenPassedDifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT", "TIME", "SYM", "SEC1", "EX", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT", "TIME", "SYM", "SEC2", "EX", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEquals_returnsFalse_whenPassedDifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT", "TIME", "SYM", "SEC", "EX1", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT", "TIME", "SYM", "SEC", "EX2", "BUY");
        assertFalse(filter1.equals(filter2));
    }
    
    @Test
    public void testEquals_returnsFalse_whenPassedDifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "ACCT", "TIME", "SYM", "SEC", "EX", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT", "TIME", "SYM", "SEC", "EX", "SELL");
        assertFalse(filter1.equals(filter2));
    }
}