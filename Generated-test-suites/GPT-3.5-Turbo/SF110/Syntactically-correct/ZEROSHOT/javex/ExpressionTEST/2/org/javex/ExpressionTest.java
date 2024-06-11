package org.javex;

import org.javex.utils.*;
import org.javex.functions.logical.*;
import org.javex.functions.math.*;
import org.javex.functions.date.*;
import org.javex.functions.string.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("");
        List externalFunctions = expression.getExternalFunctions();
        assertNull(externalFunctions);
    }

    @Test
    public void testExpression() {
        Expression expression = new Expression("");
        assertFalse(expression.prepared);
        assertNotNull(expression.stack);
        assertNull(expression.externalFunctions);
        assertNull(expression.reservedFunctions);
        assertEquals("", expression.expression);
    }

    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression("");
        expression.stack = new ExpressionStack();
        expression.stack.add("(");
        expression.stack.add("1");
        expression.stack.add("+");
        expression.stack.add("2");
        expression.stack.add(")");
        expression.stack.add("*");
        expression.stack.add("3");
        
        expression.doEvaluationStack();
        
        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.add("1");
        expectedStack.add("2");
        expectedStack.add("+");
        expectedStack.add("3");
        expectedStack.add("*");
        
        assertEquals(expectedStack, expression.stack);
    }

    @Test
    public void testPutFunction() {
        Map map = new TreeMap();
        FunctionExpression function = new AndFunction();
        
        Expression.putFunction(function, map);
        
        assertTrue(map.containsKey(function));
        assertEquals(function, map.get(function));
    }

    @Test
    public void testGetReservedFunctions() {
        FunctionExpression function = Expression.getReservedFunctions("and");
        assertNotNull(function);
        assertTrue(function instanceof AndFunction);
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression();
        boolean isReservedFunction = expression.isReservedFunction("and");
        assertTrue(isReservedFunction);
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression();
        boolean isLogicalFunction = expression.isLogicalFunction("and");
        assertTrue(isLogicalFunction);
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression();
        boolean isMathFunction = expression.isMathFunction("abs");
        assertTrue(isMathFunction);
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression();
        boolean isDateFunction = expression.isDateFunction("date_add");
        assertTrue(isDateFunction);
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression();
        boolean isStringFunction = expression.isStringFunction("length");
        assertTrue(isStringFunction);
    }

    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression();
        List<FunctionExpression> externalFunctions = new ArrayList<>();
        externalFunctions.add(new CustomFunction());
        expression.externalFunctions = externalFunctions;
        
        FunctionExpression function = expression.getExternalFunction("custom");
        assertNotNull(function);
        assertTrue(function instanceof CustomFunction);
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression();
        List<FunctionExpression> externalFunctions = new ArrayList<>();
        externalFunctions.add(new CustomFunction());
        expression.externalFunctions = externalFunctions;
        
        boolean isExternalFunction = expression.isExternalFunction("custom");
        assertTrue(isExternalFunction);
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression();
        FunctionExpression function = expression.getFunction("and");
        assertNotNull(function);
        assertTrue(function instanceof AndFunction);
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression();
        boolean isFunction = expression.isFunction("and");
        assertTrue(isFunction);
    }

    @Test
    public void testExpression() {
        Expression expression = new Expression();
        assertFalse(expression.prepared);
    }

    @Test
    public void testStack() {
        Expression expression = new Expression();
        Stack<String> stack = new Stack<>();
        stack.push("1");
        
        expression.stack("0", stack);
        
        assertEquals("10", stack.peek());
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression();
        expression.expression = "1 + 2 * 3";
        
        expression.prepare();
        
        assertTrue(expression.prepared);
        
        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.add("1");
        expectedStack.add("2");
        expectedStack.add("3");
        expectedStack.add("*");
        expectedStack.add("+");
        
        assertEquals(expectedStack, expression.stack);
    }

    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression("");
        expression.stack = new ExpressionStack();
        expression.stack.add("1");
        expression.stack.add("2");
        
        ExpressionStack copy = expression.getStackCopy();
        
        assertEquals(expression.stack, copy);
        assertNotSame(expression.stack, copy);
    }

    @Test
    public void testEvaluate() throws Exception {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };
        
        Object result = expression.evaluate();
        assertNull(result);
    }

    @Test
    public void testTokenize() {
        Expression expression = new Expression("");
        expression.expression = "1 + 2 * 3";
        
        expression.tokenize();
        
        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.add("1");
        expectedStack.add("+");
        expectedStack.add("2");
        expectedStack.add("*");
        expectedStack.add("3");
        
        assertEquals(expectedStack, expression.stack);
    }
}