package org.javex;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression() {};
        assertNull(expression.getExternalFunctions());
    }
    
    @Test
    public void testConstructorWithExpressionString() {
        Expression expression = new Expression("1 + 2");
        assertNotNull(expression);
    }
    
    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression() {};
        expression.stack.push("1");
        expression.stack.push("+");
        expression.stack.push("2");
        expression.doEvaluationStack();
        assertEquals("1 2 +", expression.stack.toString());
    }
    
    @Test
    public void testGetReservedFunctions() {
        FunctionExpression function = Expression.getReservedFunctions("and");
        assertNotNull(function);
    }
    
    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression() {};
        assertTrue(expression.isReservedFunction("and"));
        assertFalse(expression.isReservedFunction("test"));
    }
    
    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression() {};
        assertTrue(expression.isLogicalFunction("and"));
        assertFalse(expression.isLogicalFunction("test"));
    }
    
    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression() {};
        assertTrue(expression.isMathFunction("power"));
        assertFalse(expression.isMathFunction("test"));
    }
    
    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression() {};
        assertTrue(expression.isDateFunction("dateAdd"));
        assertFalse(expression.isDateFunction("test"));
    }
    
    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression() {};
        assertTrue(expression.isStringFunction("contains"));
        assertFalse(expression.isStringFunction("test"));
    }
    
    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression() {};
        assertNull(expression.getExternalFunction("test"));
    }
    
    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression() {};
        assertFalse(expression.isExternalFunction("test"));
    }
    
    @Test
    public void testGetFunction() {
        Expression expression = new Expression() {};
        assertNotNull(expression.getFunction("and"));
        assertNull(expression.getFunction("test"));
    }
    
    @Test
    public void testIsFunction() {
        Expression expression = new Expression() {};
        assertTrue(expression.isFunction("and"));
        assertFalse(expression.isFunction("test"));
    }
    
    @Test
    public void testPrepare() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        expression.expression = "1 + 2";
        expression.prepare();
        assertTrue(expression.prepared);
    }
    
    @Test
    public void testEvaluate() {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        try {
            assertNull(expression.evaluate());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testTokenize() {
        Expression expression = new Expression() {};
        expression.expression = "1 + 2";
        expression.tokenize();
        assertEquals("[1, +, 2]", expression.stack.toString());
    }
}