package org.javex;

import org.javex.functions.date.*;
import org.javex.functions.logical.*;
import org.javex.functions.math.*;
import org.javex.functions.string.*;
import org.javex.utils.*;
import java.util.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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
        List expected = new ArrayList();
        expected.add(null);
        assertNull(expression.getExternalFunctions());
    }

    @Test
    public void testExpressionString() {
        Expression expression = new Expression("test");
        assertEquals("test", expression.expression);
    }

    @Test
    public void testDoEvaluationStack() {
        expression.stack = new ExpressionStack();

        expression.stack.add("1");
        expression.stack.add("2");
        expression.stack.add("*");
        expression.stack.add("3");
        expression.stack.add("+");
        expression.stack.add("4");
        expression.stack.add("/");

        expression.doEvaluationStack();

        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.add("1");
        expectedStack.add("2");
        expectedStack.add("3");
        expectedStack.add("*");
        expectedStack.add("4");
        expectedStack.add("/");
        expectedStack.add("+");

        assertEquals(expectedStack, expression.stack);
    }

    @Test
    public void testPutFunction() {
        FunctionExpression f = new AndFunction();
        Map map = new TreeMap();

        assertNull(Expression.reservedFunctions);
        expression.putFunction(f, map);
        assertEquals(f, map.get(f));
    }

    @Test
    public void testGetReservedFunctions() {
        assertNull(Expression.getReservedFunctions(""));
    }

    @Test
    public void testIsReservedFunction() {
        assertFalse(expression.isReservedFunction(""));
    }

    @Test
    public void testIsLogicalFunction() {
        assertFalse(expression.isLogicalFunction(""));
    }

    @Test
    public void testIsMathFunction() {
        assertFalse(expression.isMathFunction(""));
    }

    @Test
    public void testIsDateFunction() {
        assertFalse(expression.isDateFunction(""));
    }

    @Test
    public void testIsStringFunction() {
        assertFalse(expression.isStringFunction(""));
    }

    @Test
    public void testGetExternalFunction() {
        assertNull(expression.getExternalFunction(""));
    }

    @Test
    public void testIsExternalFunction() {
        assertFalse(expression.isExternalFunction(""));
    }

    @Test
    public void testGetFunction() {
        assertNull(expression.getFunction(""));
    }

    @Test
    public void testIsFunction() {
        assertFalse(expression.isFunction(""));
    }

    @Test
    public void testExpression() {
        assertNull(expression.expression);
    }

    @Test
    public void testPrepare() {
        expression.stack = new ExpressionStack();
        expression.stack.add("1");
        expression.stack.add("2");
        expression.stack.add("*");
        expression.stack.add("3");
        expression.stack.add("+");
        expression.stack.add("4");
        expression.stack.add("/");

        expression.prepare();

        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.add("1");
        expectedStack.add("2");
        expectedStack.add("3");
        expectedStack.add("*");
        expectedStack.add("4");
        expectedStack.add("/");
        expectedStack.add("+");

        assertTrue(expression.prepared);
        assertEquals(expectedStack, expression.stack);
    }

    @Test
    public void testGetStackCopy() {
        expression.stack = new ExpressionStack();
        expression.stack.add("1");
        expression.stack.add("2");
        expression.stack.add("*");

        ExpressionStack stackCopy = expression.getStackCopy();

        assertEquals(expression.stack, stackCopy);
        assertFalse(expression.stack == stackCopy);
    }

    @Test(expected = Exception.class)
    public void testEvaluate() throws Exception {
        expression.evaluate();
    }

    @Test
    public void testTokenize() {
        expression.expression = "1 + 2";
        expression.tokenize();

        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.add("1");
        expectedStack.add("+");
        expectedStack.add("2");

        assertEquals(expectedStack, expression.stack);
    }
}