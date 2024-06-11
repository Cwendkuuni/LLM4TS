
package com.ib.client.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.ib.client.ExecutionFilter;

public class ExecutionFilterTest {
    
    ExecutionFilter executionFilter;
    
    @Before
    public void setUp() {
        executionFilter = new ExecutionFilter();
    }
    
    @Test
    public void testEquals_WhenObjectIsNull_ReturnsFalse() {
        Object other = null;
        assertFalse(executionFilter.equals(other));
    }

    @Test
    public void testEquals_WhenObjectIsSameInstance_ReturnsTrue() {
        assertTrue(executionFilter.equals(executionFilter));
    }
    
    @Test
    public void testEquals_WhenObjectIsDifferentClass_ReturnsFalse() {
        Object other = new Object();
        assertFalse(executionFilter.equals(other));
    }
    
    @Test
    public void testEquals_WhenObjectHasDifferentClientId_ReturnsFalse() {
        ExecutionFilter other = new ExecutionFilter(1, "code", "time", "symbol", "type", "exchange", "side");
        assertFalse(executionFilter.equals(other));
    }
    
    @Test
    public void testEquals_WhenObjectHasDifferentAcctCode_ReturnsFalse() {
        ExecutionFilter other = new ExecutionFilter(0, "different", "time", "symbol", "type", "exchange", "side");
        assertFalse(executionFilter.equals(other));
    }
    
    @Test
    public void testEquals_WhenObjectHasDifferentTime_ReturnsFalse() {
        ExecutionFilter other = new ExecutionFilter(0, "code", "different", "symbol", "type", "exchange", "side");
        assertFalse(executionFilter.equals(other));
    }
    
    @Test
    public void testEquals_WhenObjectHasDifferentSymbol_ReturnsFalse() {
        ExecutionFilter other = new ExecutionFilter(0, "code", "time", "different","type", "exchange", "side");
        assertFalse(executionFilter.equals(other));
    }
    
    @Test
    public void testEquals_WhenObjectHasDifferentSecType_ReturnsFalse() {
        ExecutionFilter other = new ExecutionFilter(0, "code", "time", "symbol", "different", "exchange", "side");
        assertFalse(executionFilter.equals(other));
    }
    
    @Test
    public void testEquals_WhenObjectHasDifferentExchange_ReturnsFalse() {
        ExecutionFilter other = new ExecutionFilter(0, "code", "time", "symbol", "type", "different", "side");
        assertFalse(executionFilter.equals(other));
    }
    
    @Test
    public void testEquals_WhenObjectHasDifferentSide_ReturnsFalse() {
        ExecutionFilter other = new ExecutionFilter(0, "code", "time", "symbol", "type", "exchange", "different");
        assertFalse(executionFilter.equals(other));
    }
    
    @Test
    public void testEquals_WhenObjectHasSameValues_ReturnsTrue() {
        ExecutionFilter other = new ExecutionFilter(0, "code", "time", "symbol", "type", "exchange", "side");
        assertTrue(executionFilter.equals(other));
    }
}

