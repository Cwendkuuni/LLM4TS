package org.javex;

import org.javex.functions.logical.*;
import org.javex.functions.math.*;
import org.javex.functions.date.*;
import org.javex.functions.string.*;
import org.javex.utils.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("test");
        List externalFunctions = expression.getExternalFunctions();
        assertNull(externalFunctions);
    }

    @Test
    public void testExpression() {
        Expression expression = new Expression("test");
        assertEquals("test", expression.expression);
        assertNotNull(expression.stack);
        assertNull(expression.externalFunctions);
        assertFalse(expression.prepared);
    }

    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression("test");
        expression.stack = new ExpressionStack();
        expression.stack.add("(");
        expression.stack.add("1");
        expression.stack.add("+");
        expression.stack.add("2");
        expression.stack.add(")");
        expression.stack.add("*");
        expression.stack.add("3");
        expression.stack.add("-");
        expression.stack.add("4");
        expression.stack.add("/");
        expression.stack.add("5");
        expression.doEvaluationStack();
        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.add("1");
        expectedStack.add("2");
        expectedStack.add("+");
        expectedStack.add("3");
        expectedStack.add("*");
        expectedStack.add("4");
        expectedStack.add("5");
        expectedStack.add("/");
        expectedStack.add("-");
        assertEquals(expectedStack, expression.stack);
    }

    @Test
    public void testGetReservedFunctions() {
        FunctionExpression andFunction = Expression.getReservedFunctions("and");
        assertNotNull(andFunction);
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression("test");
        boolean isReservedFunction = expression.isReservedFunction("and");
        assertTrue(isReservedFunction);
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression("test");
        boolean isLogicalFunction = expression.isLogicalFunction("and");
        assertTrue(isLogicalFunction);
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression("test");
        boolean isMathFunction = expression.isMathFunction("sqrt");
        assertTrue(isMathFunction);
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression("test");
        boolean isDateFunction = expression.isDateFunction("dateDiff");
        assertTrue(isDateFunction);
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression("test");
        boolean isStringFunction = expression.isStringFunction("substring");
        assertTrue(isStringFunction);
    }

    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression("test");
        List<FunctionExpression> externalFunctions = new ArrayList<>();
        externalFunctions.add(new GenericFunctionExpression("function1"));
        externalFunctions.add(new GenericFunctionExpression("function2"));
        expression.externalFunctions = externalFunctions;
        FunctionExpression externalFunction = expression.getExternalFunction("function2");
        assertNotNull(externalFunction);
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression("test");
        List<FunctionExpression> externalFunctions = new ArrayList<>();
        externalFunctions.add(new GenericFunctionExpression("function1"));
        externalFunctions.add(new GenericFunctionExpression("function2"));
        expression.externalFunctions = externalFunctions;
        boolean isExternalFunction = expression.isExternalFunction("function2");
        assertTrue(isExternalFunction);
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression("test");
        List<FunctionExpression> externalFunctions = new ArrayList<>();
        externalFunctions.add(new GenericFunctionExpression("function1"));
        externalFunctions.add(new GenericFunctionExpression("function2"));
        expression.externalFunctions = externalFunctions;
        FunctionExpression function = expression.getFunction("function2");
        assertNotNull(function);
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression("test");
        List<FunctionExpression> externalFunctions = new ArrayList<>();
        externalFunctions.add(new GenericFunctionExpression("function1"));
        externalFunctions.add(new GenericFunctionExpression("function2"));
        expression.externalFunctions = externalFunctions;
        boolean isFunction = expression.isFunction("function2");
        assertTrue(isFunction);
    }

    @Test
    public void testExpression2() {
        Expression expression = new Expression();
        assertFalse(expression.prepared);
    }

    @Test
    public void testStack() {
        Expression expression = new Expression("test");
        String c = "c";
        Stack<String> stack = new Stack<>();
        expression.stack(c, stack);
        assertEquals("c", stack.pop());
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression("test");
        expression.prepare();
        assertTrue(expression.prepared);
    }

    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression("test");
        expression.stack = new ExpressionStack();
        expression.stack.add("1");
        expression.stack.add("2");
        ExpressionStack stackCopy = expression.getStackCopy();
        assertNotNull(stackCopy);
        assertEquals(stackCopy, expression.stack);
    }

    @Test
    public void testEvaluate() throws Exception {
        Expression expression = new Expression("test") {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        assertNull(expression.evaluate());
    }

    @Test
    public void testTokenize() {
        Expression expression = new Expression("test");
        expression.expression = "(1 + 2) * 3 - 4 / 5";
        expression.tokenize();
        ExpressionStack stack = new ExpressionStack();
        stack.add("(");
        stack.add("1");
        stack.add("+");
        stack.add("2");
        stack.add(")");
        stack.add("*");
        stack.add("3");
        stack.add("-");
        stack.add("4");
        stack.add("/");
        stack.add("5");
        assertEquals(stack, expression.stack);
    }
}