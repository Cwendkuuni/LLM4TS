package org.javex;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

    private Expression expression;

    @Before
    public void setUp() {
        expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
    }

    @Test
    public void testGetExternalFunctions() {
        assertNotNull(expression.getExternalFunctions());
    }

    @Test
    public void testExpression() {
        Expression expression = new Expression("test");
        assertNotNull(expression.getStackCopy());
    }

    @Test
    public void testDoEvaluationStack() {
        expression.prepare();
        expression.doEvaluationStack();
        assertNotNull(expression.getStackCopy());
    }

    @Test
    public void testGetReservedFunctions() {
        assertNotNull(Expression.getReservedFunctions("+"));
    }

    @Test
    public void testIsReservedFunction() {
        assertTrue(expression.isReservedFunction("+"));
        assertFalse(expression.isReservedFunction("test"));
    }

    @Test
    public void testIsLogicalFunction() {
        assertTrue(expression.isLogicalFunction("and"));
        assertFalse(expression.isLogicalFunction("test"));
    }

    @Test
    public void testIsMathFunction() {
        assertTrue(expression.isMathFunction("+"));
        assertFalse(expression.isMathFunction("test"));
    }

    @Test
    public void testIsDateFunction() {
        assertTrue(expression.isDateFunction("dateadd"));
        assertFalse(expression.isDateFunction("test"));
    }

    @Test
    public void testIsStringFunction() {
        assertTrue(expression.isStringFunction("startswith"));
        assertFalse(expression.isStringFunction("test"));
    }

    @Test
    public void testGetExternalFunction() {
        assertNull(expression.getExternalFunction("test"));
    }

    @Test
    public void testIsExternalFunction() {
        assertFalse(expression.isExternalFunction("test"));
    }

    @Test
    public void testGetFunction() {
        assertNotNull(expression.getFunction("+"));
    }

    @Test
    public void testIsFunction() {
        assertTrue(expression.isFunction("+"));
        assertFalse(expression.isFunction("test"));
    }

    @Test
    public void testExpression() {
        new Expression();
    }

    @Test
    public void testPrepare() {
        expression.prepare();
        assertTrue(expression.prepared);
    }

    @Test
    public void testGetStackCopy() {
        assertNotNull(expression.getStackCopy());
    }

    @Test
    public void testEvaluate() throws Exception {
        assertNull(expression.evaluate());
    }

    @Test
    public void testTokenize() {
        expression.tokenize();
        assertNotNull(expression.getStackCopy());
    }
}