package org.javex;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("1 + 2");
        assertEquals(null, expression.getExternalFunctions());
    }

    @Test
    public void testExpression() {
        Expression expression = new Expression("1 + 2");
        assertEquals(false, expression.prepared);
        assertEquals("1 + 2", expression.expression);
    }

    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression("1 + 2");
        // Testing goes here...
    }

    @Test
    public void testGetReservedFunctions() {
        assertEquals(null, Expression.getReservedFunctions("abc"));
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("1 + 2");
        assertEquals(false, expression.isReservedFunction("abc"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("1 + 2");
        assertEquals(false, expression.isLogicalFunction("abc"));
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("1 + 2");
        assertEquals(false, expression.isMathFunction("abc"));
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("1 + 2");
        assertEquals(false, expression.isDateFunction("abc"));
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("1 + 2");
        assertEquals(false, expression.isStringFunction("abc"));
    }

    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression("1 + 2");
        assertEquals(null, expression.getExternalFunction("abc"));
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression("1 + 2");
        assertEquals(false, expression.isExternalFunction("abc"));
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression("1 + 2");
        assertEquals(null, expression.getFunction("abc"));
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression("1 + 2");
        assertEquals(false, expression.isFunction("abc"));
    }

    @Test
    public void testExpression() {
        Expression expression = new Expression();
        assertEquals(false, expression.prepared);
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression("1 + 2");
        // Testing goes here...
    }

    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression("1 + 2");
        // Testing goes here...
    }

    @Test
    public void testEvaluate() {
        Expression expression = new Expression("1 + 2");
        // Testing goes here...
    }

    @Test
    public void testTokenize() {
        Expression expression = new Expression("1 + 2");
        // Testing goes here...
    }
}