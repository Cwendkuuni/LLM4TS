package com.ib.client;

import org.junit.Assert;
import org.junit.Test;

public class ExecutionFilterTest {
    
    @Test
    public void testEquals() {
        // Given
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct1", "time1", "symbol1", "secType1", "exchange1", "side1");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct1", "time1", "symbol1", "secType1", "exchange1", "side1");
        
        // When
        boolean result = filter1.equals(filter2);
        
        // Then
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEquals_WhenDifferentClientId_ReturnsFalse() {
        // Given
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct1", "time1", "symbol1", "secType1", "exchange1", "side1");
        ExecutionFilter filter2 = new ExecutionFilter(2, "acct1", "time1", "symbol1", "secType1", "exchange1", "side1");
        
        // When
        boolean result = filter1.equals(filter2);
        
        // Then
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEquals_WhenDifferentAcctCode_ReturnsFalse() {
        // Given
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct1", "time1", "symbol1", "secType1", "exchange1", "side1");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct2", "time1", "symbol1", "secType1", "exchange1", "side1");
        
        // When
        boolean result = filter1.equals(filter2);
        
        // Then
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEquals_WhenDifferentTime_ReturnsFalse() {
        // Given
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct1", "time1", "symbol1", "secType1", "exchange1", "side1");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct1", "time2", "symbol1", "secType1", "exchange1", "side1");
        
        // When
        boolean result = filter1.equals(filter2);
        
        // Then
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEquals_WhenDifferentSymbol_ReturnsFalse() {
        // Given
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct1", "time1", "symbol1", "secType1", "exchange1", "side1");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct1", "time1", "symbol2", "secType1", "exchange1", "side1");
        
        // When
        boolean result = filter1.equals(filter2);
        
        // Then
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEquals_WhenDifferentSecType_ReturnsFalse() {
        // Given
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct1", "time1", "symbol1", "secType1", "exchange1", "side1");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct1", "time1", "symbol1", "secType2", "exchange1", "side1");
        
        // When
        boolean result = filter1.equals(filter2);
        
        // Then
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEquals_WhenDifferentExchange_ReturnsFalse() {
        // Given
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct1", "time1", "symbol1", "secType1", "exchange1", "side1");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct1", "time1", "symbol1", "secType1", "exchange2", "side1");
        
        // When
        boolean result = filter1.equals(filter2);
        
        // Then
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEquals_WhenDifferentSide_ReturnsFalse() {
        // Given
        ExecutionFilter filter1 = new ExecutionFilter(1, "acct1", "time1", "symbol1", "secType1", "exchange1", "side1");
        ExecutionFilter filter2 = new ExecutionFilter(1, "acct1", "time1", "symbol1", "secType1", "exchange1", "side2");
        
        // When
        boolean result = filter1.equals(filter2);
        
        // Then
        Assert.assertFalse(result);
    }
}