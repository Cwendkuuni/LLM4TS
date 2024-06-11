package org.javex;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class ExpressionTest {
    
    private Expression expression;
    
    @Test
    public void testGetExternalFunctions() {
        expression = new Expression("test");
        List externalFunctions = expression.getExternalFunctions();
        assertNotNull(externalFunctions);
        assertTrue(externalFunctions.isEmpty());
    }
    
    @Test
    public void testDoEvaluationStack() {
        expression = new Expression("test");
        try {
            expression.prepare();
            expression.doEvaluationStack();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertNotNull(expression.getStackCopy());
    }
    
    @Test
    public void testGetReservedFunctions() {
        FunctionExpression func = Expression.getReservedFunctions("and");
        assertNotNull(func);
    }
    
    @Test
    public void testIsReservedFunction() {
        expression = new Expression("test");
        assertTrue(expression.isReservedFunction("and"));
    }
    
    @Test
    public void testIsLogicalFunction() {
        expression = new Expression("test");
        assertTrue(expression.isLogicalFunction("and"));
    }
    
    @Test
    public void testIsMathFunction() {
        expression = new Expression("test");
        assertTrue(expression.isMathFunction("+"));
    }
    
    @Test
    public void testIsDateFunction() {
        expression = new Expression("test");
        assertTrue(expression.isDateFunction("date"));
    }
    
    @Test
    public void testIsStringFunction() {
        expression = new Expression("test");
        assertTrue(expression.isStringFunction("string"));
    }
    
    @Test
    public void testIsExternalFunction() {
        expression = new Expression("test");
        assertTrue(expression.isExternalFunction("external"));
    }
    
    @Test
    public void testIsFunction() {
        expression = new Expression("test");
        assertTrue(expression.isFunction("function"));
    }
    
    @Test
    public void testPrepare() {
        expression = new Expression("test");
        try {
            expression.prepare();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertTrue(expression.prepared);
    }
    
    @Test
    public void testEvaluate() {
        expression = new Expression("test");
        try {
            expression.prepare();
            Object result = expression.evaluate();
            assertNotNull(result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}