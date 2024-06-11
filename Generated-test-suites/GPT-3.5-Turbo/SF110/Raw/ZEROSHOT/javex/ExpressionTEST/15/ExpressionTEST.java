package org.javex.test;

import org.junit.Test;
import org.javex.Expression;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("2 + 3");
        assertNotNull(expression.getExternalFunctions());
    }

    @Test
    public void testExpressionConstructor() {
        Expression expression = new Expression("2 + 3");
        assertNotNull(expression.stack);
        assertEquals("2 + 3", expression.expression);
    }

    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression("2 + 3");
        expression.stack = new ExpressionStack();
        expression.stack.add("2");
        expression.stack.add("+");
        expression.stack.add("3");
        expression.doEvaluationStack();
        assertEquals(3, expression.stack.size());
    }

    @Test
    public void testGetReservedFunctions() {
        assertNotNull(Expression.reservedFunctions);
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("2 + 3");
        assertTrue(expression.isReservedFunction("+"));
        assertFalse(expression.isReservedFunction("8"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("2 + 3");
        assertTrue(expression.isLogicalFunction("AND"));
        assertFalse(expression.isLogicalFunction("+"));
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("2 + 3");
        assertTrue(expression.isMathFunction("+"));
        assertFalse(expression.isMathFunction("AND"));
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("2 + 3");
        assertTrue(expression.isDateFunction("DATEADD"));
        assertFalse(expression.isDateFunction("+"));
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("2 + 3");
        assertTrue(expression.isStringFunction("LENGTH"));
        assertFalse(expression.isStringFunction("+"));
    }

    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression("2 + 3");
        expression.externalFunctions.add(new CustomFunction());
        assertNotNull(expression.getExternalFunction("CUSTOM"));
        assertNull(expression.getExternalFunction("LENGTH"));
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression("2 + 3");
        expression.externalFunctions.add(new CustomFunction());
        assertTrue(expression.isExternalFunction("CUSTOM"));
        assertFalse(expression.isExternalFunction("LENGTH"));
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression("2 + 3");
        assertNotNull(expression.getFunction("+"));
        assertNull(expression.getFunction("LENGTH"));
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression("2 + 3");
        assertTrue(expression.isFunction("+"));
        assertFalse(expression.isFunction("LENGTH"));
    }

    @Test
    public void testExpressionConstructor2() {
        Expression expression = new Expression();
        assertFalse(expression.prepared);
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression("2 + 3");
        expression.prepare();
        assertTrue(expression.prepared);
    }

    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression("2 + 3");
        expression.stack = new ExpressionStack();
        expression.stack.add("2");
        expression.stack.add("+");
        expression.stack.add("3");
        ExpressionStack copy = expression.getStackCopy();
        assertNotSame(expression.stack, copy);
        assertEquals(expression.stack.size(), copy.size());
    }

    @Test
    public void testEvaluate() {
        Expression expression = new Expression("2 + 3");
        try {
            assertNotNull(expression.evaluate());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testTokenize() {
        Expression expression = new Expression("2 + 3");
        expression.tokenize();
        assertEquals(3, expression.stack.size());
    }
}