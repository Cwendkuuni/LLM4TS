package com.ib.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExecutionFilterTest {
  
    private ExecutionFilter executionFilter;
  
    @Before
    public void setUp() {
        executionFilter = new ExecutionFilter();
    }
  
    @Test
    public void testEquals_SameObject_ReturnsTrue() {
        Assert.assertTrue(executionFilter.equals(executionFilter));
    }
  
    @Test
    public void testEquals_NullObject_ReturnsFalse() {
        Assert.assertFalse(executionFilter.equals(null));
    }
  
    @Test
    public void testEquals_DifferentObject_ReturnsFalse() {
        Object obj = new Object();
        Assert.assertFalse(executionFilter.equals(obj));
    }
  
    @Test
    public void testEquals_EqualExecutionFilter_ReturnsTrue() {
        ExecutionFilter other = new ExecutionFilter(0, "", "", "", "", "", "");
        Assert.assertTrue(executionFilter.equals(other));
    }
  
    @Test
    public void testEquals_DifferentClientId_ReturnsFalse() {
        ExecutionFilter other = new ExecutionFilter(1, "", "", "", "", "", "");
        Assert.assertFalse(executionFilter.equals(other));
    }
  
    @Test
    public void testEquals_DifferentAcctCode_ReturnsFalse() {
        ExecutionFilter other = new ExecutionFilter(0, "ABC", "", "", "", "", "");
        Assert.assertFalse(executionFilter.equals(other));
    }
  
    @Test
    public void testEquals_DifferentTime_ReturnsFalse() {
        ExecutionFilter other = new ExecutionFilter(0, "", "10:00", "", "", "", "");
        Assert.assertFalse(executionFilter.equals(other));
    }
  
    @Test
    public void testEquals_DifferentSymbol_ReturnsFalse() {
        ExecutionFilter other = new ExecutionFilter(0, "", "", "AAPL", "", "", "");
        Assert.assertFalse(executionFilter.equals(other));
    }
  
    @Test
    public void testEquals_DifferentSecType_ReturnsFalse() {
        ExecutionFilter other = new ExecutionFilter(0, "", "", "", "STK", "", "");
        Assert.assertFalse(executionFilter.equals(other));
    }
  
    @Test
    public void testEquals_DifferentExchange_ReturnsFalse() {
        ExecutionFilter other = new ExecutionFilter(0, "", "", "", "", "NYSE", "");
        Assert.assertFalse(executionFilter.equals(other));
    }
  
    @Test
    public void testEquals_DifferentSide_ReturnsFalse() {
        ExecutionFilter other = new ExecutionFilter(0, "", "", "", "", "", "BUY");
        Assert.assertFalse(executionFilter.equals(other));
    }
}