package org.javex;

import static org.junit.Assert.*;
import org.junit.Test;

public class ExpressionTest {
    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression();
        assertNull(expression.getExternalFunctions());
        
        expression = new Expression("5 + 3");
        assertNull(expression.getExternalFunctions());
    }
    
    @Test
    public void testGetFunction() {
        Expression expression = new Expression();
        assertNull(expression.getFunction("+"));
        
        expression = new Expression("5 + 3");
        assertNull(expression.getFunction("+"));
    }
    
    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression();
        assertFalse(expression.isExternalFunction("+"));
        
        expression = new Expression("5 + 3");
        assertFalse(expression.isExternalFunction("+"));
    }
    
    @Test
    public void testIsFunction() {
        Expression expression = new Expression();
        assertFalse(expression.isFunction("+"));
        
        expression = new Expression("5 + 3");
        assertFalse(expression.isFunction("+"));
    }
    
    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression();
        assertFalse(expression.isLogicalFunction("and"));
        
        expression = new Expression("5 + 3");
        assertFalse(expression.isLogicalFunction("and"));
    }
    
    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression();
        assertFalse(expression.isMathFunction("+"));
        
        expression = new Expression("5 + 3");
        assertFalse(expression.isMathFunction("+"));
    }
    
    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression();
        assertFalse(expression.isDateFunction("+"));
        
        expression = new Expression("5 + 3");
        assertFalse(expression.isDateFunction("+"));
    }
    
    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression();
        assertFalse(expression.isStringFunction("+"));
        
        expression = new Expression("5 + 3");
        assertFalse(expression.isStringFunction("+"));
    }
    
    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression();
        assertFalse(expression.isReservedFunction("+"));
        
        expression = new Expression("5 + 3");
        assertFalse(expression.isReservedFunction("+"));
    }
    
    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression();
        assertNull(expression.getExternalFunction("toUpperCase"));
        
        expression = new Expression("5 + 3");
        assertNull(expression.getExternalFunction("toUpperCase"));
    }
    
    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression();
        assertFalse(expression.isExternalFunction("toUpperCase"));
        
        expression = new Expression("5 + 3");
        assertFalse(expression.isExternalFunction("toUpperCase"));
    }
    
    @Test
    public void testGetFunction() {
        Expression expression = new Expression();
        assertNull(expression.getFunction("length"));
        
        expression = new Expression("5 + 3");
        assertNull(expression.getFunction("length"));
    }
    
    @Test
    public void testIsFunction() {
        Expression expression = new Expression();
        assertFalse(expression.isFunction("length"));
        
        expression = new Expression("5 + 3");
        assertFalse(expression.isFunction("length"));
    }
    
    @Test
    public void testPrepare() {
        Expression expression = new Expression();
        expression.prepare();
        assertTrue(expression.prepared);
        
        expression = new Expression("5 + 3");
        expression.prepare();
        assertTrue(expression.prepared);
    }
    
    @Test
    public void testEvaluate() {
        Expression expression = new Expression();
        try {
            expression.evaluate();
            fail("Expected Exception was not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
        
        expression = new Expression("5 + 3");
        try {
            expression.evaluate();
            fail("Expected Exception was not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }
}