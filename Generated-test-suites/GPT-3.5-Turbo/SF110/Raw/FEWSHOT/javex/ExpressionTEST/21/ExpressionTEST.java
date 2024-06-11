package org.javex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ExpressionTest {
    @Test
    public void testGetExternalFunctions() {
        Expression expression = new ExpressionImpl("");
        assertNull(expression.getExternalFunctions());
    }
    
    @Test
    public void testIsReservedFunction() {
        Expression expression = new ExpressionImpl("");
        assertEquals(false, expression.isReservedFunction("foo"));
    }
    
    @Test
    public void testIsLogicalFunction() {
        Expression expression = new ExpressionImpl("");
        assertEquals(false, expression.isLogicalFunction("foo"));
    }
    
    @Test
    public void testIsMathFunction() {
        Expression expression = new ExpressionImpl("");
        assertEquals(false, expression.isMathFunction("foo"));
    }
    
    @Test
    public void testIsDateFunction() {
        Expression expression = new ExpressionImpl("");
        assertEquals(false, expression.isDateFunction("foo"));
    }
    
    @Test
    public void testIsStringFunction() {
        Expression expression = new ExpressionImpl("");
        assertEquals(false, expression.isStringFunction("foo"));
    }
    
    @Test
    public void testGetExternalFunction() {
        Expression expression = new ExpressionImpl("");
        assertNull(expression.getExternalFunction("foo"));
    }
    
    @Test
    public void testIsExternalFunction() {
        Expression expression = new ExpressionImpl("");
        assertEquals(false, expression.isExternalFunction("foo"));
    }
    
    @Test
    public void testGetFunction() {
        Expression expression = new ExpressionImpl("");
        assertNull(expression.getFunction("foo"));
    }
    
    @Test
    public void testIsFunction() {
        Expression expression = new ExpressionImpl("");
        assertEquals(false, expression.isFunction("foo"));
    }
    
    private class ExpressionImpl extends Expression {
        ExpressionImpl(String s) {
            super(s);
        }
        
        @Override
        public Object evaluate() throws Exception {
            return null;
        }
    }
}