package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExecutionFilterTest {

    @Test
    public void testEqualsWithSameObject() {
        ExecutionFilter filter = new ExecutionFilter();
        assertTrue(filter.equals(filter));
    }

    @Test
    public void testEqualsWithNullObject() {
        ExecutionFilter filter = new ExecutionFilter();
        assertFalse(filter.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        ExecutionFilter filter = new ExecutionFilter();
        assertFalse(filter.equals(new Object()));
    }

    @Test
    public void testEqualsWithDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter();
        ExecutionFilter filter2 = new ExecutionFilter(1, "", "", "", "", "", "");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentAcctCode() {
        ExecutionFilter filter1 = new ExecutionFilter();
        ExecutionFilter filter2 = new ExecutionFilter(0, "123", "", "", "", "", "");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter();
        ExecutionFilter filter2 = new ExecutionFilter(0, "", "10:00", "", "", "", "");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter();
        ExecutionFilter filter2 = new ExecutionFilter(0, "", "", "AAPL", "", "", "");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter();
        ExecutionFilter filter2 = new ExecutionFilter(0, "", "", "", "STOCK", "", "");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter();
        ExecutionFilter filter2 = new ExecutionFilter(0, "", "", "", "", "NYSE", "");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithDifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter();
        ExecutionFilter filter2 = new ExecutionFilter(0, "", "", "", "", "", "BUY");
        assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithEqualObjects() {
        ExecutionFilter filter1 = new ExecutionFilter();
        ExecutionFilter filter2 = new ExecutionFilter();
        assertTrue(filter1.equals(filter2));
    }

    @Test
    public void testEqualsWithSameValues() {
        ExecutionFilter filter1 = new ExecutionFilter(1, "123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        ExecutionFilter filter2 = new ExecutionFilter(1, "123", "10:00", "AAPL", "STOCK", "NYSE", "BUY");
        assertTrue(filter1.equals(filter2));
    }

}