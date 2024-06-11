package com.ib.client;

import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {

    @Test
    public void testEqualsSameObject() {
        ExecutionFilter filter = new ExecutionFilter();
        Assert.assertTrue(filter.equals(filter));
    }

    @Test
    public void testEqualsNullObject() {
        ExecutionFilter filter = new ExecutionFilter();
        Assert.assertFalse(filter.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        ExecutionFilter filter = new ExecutionFilter();
        Object obj = new Object();
        Assert.assertFalse(filter.equals(obj));
    }

    @Test
    public void testEqualsDifferentClientId() {
        ExecutionFilter filter1 = new ExecutionFilter();
        ExecutionFilter filter2 = new ExecutionFilter(1, "ACCT", "TIME", "SYMBOL", "TYPE", "EXCHANGE", "SIDE");
        Assert.assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsDifferentAcctCode() {
        ExecutionFilter filter1 = new ExecutionFilter(0, "ACCT1", "TIME", "SYMBOL", "TYPE", "EXCHANGE", "SIDE");
        ExecutionFilter filter2 = new ExecutionFilter(0, "ACCT2", "TIME", "SYMBOL", "TYPE", "EXCHANGE", "SIDE");
        Assert.assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsDifferentTime() {
        ExecutionFilter filter1 = new ExecutionFilter(0, "ACCT", "TIME1", "SYMBOL", "TYPE", "EXCHANGE", "SIDE");
        ExecutionFilter filter2 = new ExecutionFilter(0, "ACCT", "TIME2", "SYMBOL", "TYPE", "EXCHANGE", "SIDE");
        Assert.assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsDifferentSymbol() {
        ExecutionFilter filter1 = new ExecutionFilter(0, "ACCT", "TIME", "SYMBOL1", "TYPE", "EXCHANGE", "SIDE");
        ExecutionFilter filter2 = new ExecutionFilter(0, "ACCT", "TIME", "SYMBOL2", "TYPE", "EXCHANGE", "SIDE");
        Assert.assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsDifferentSecType() {
        ExecutionFilter filter1 = new ExecutionFilter(0, "ACCT", "TIME", "SYMBOL", "TYPE1", "EXCHANGE", "SIDE");
        ExecutionFilter filter2 = new ExecutionFilter(0, "ACCT", "TIME", "SYMBOL", "TYPE2", "EXCHANGE", "SIDE");
        Assert.assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsDifferentExchange() {
        ExecutionFilter filter1 = new ExecutionFilter(0, "ACCT", "TIME", "SYMBOL", "TYPE", "EXCHANGE1", "SIDE");
        ExecutionFilter filter2 = new ExecutionFilter(0, "ACCT", "TIME", "SYMBOL", "TYPE", "EXCHANGE2", "SIDE");
        Assert.assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsDifferentSide() {
        ExecutionFilter filter1 = new ExecutionFilter(0, "ACCT", "TIME", "SYMBOL", "TYPE", "EXCHANGE", "SIDE1");
        ExecutionFilter filter2 = new ExecutionFilter(0, "ACCT", "TIME", "SYMBOL", "TYPE", "EXCHANGE", "SIDE2");
        Assert.assertFalse(filter1.equals(filter2));
    }

    @Test
    public void testEqualsSameProperties() {
        ExecutionFilter filter1 = new ExecutionFilter(0, "ACCT", "TIME", "SYMBOL", "TYPE", "EXCHANGE", "SIDE");
        ExecutionFilter filter2 = new ExecutionFilter(0, "ACCT", "TIME", "SYMBOL", "TYPE", "EXCHANGE", "SIDE");
        Assert.assertTrue(filter1.equals(filter2));
    }
}