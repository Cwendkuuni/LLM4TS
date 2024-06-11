package org.javex;

import org.junit.*;
import static org.junit.Assert.*;

import org.javex.functions.logical.*;

import java.util.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression() {};
        List externalFunctions = new ArrayList<>();
        externalFunctions.add(new AndFunction());
        expression.externalFunctions = externalFunctions;
        
        List result = expression.getExternalFunctions();
        
        assertEquals(externalFunctions, result);
    }

    @Test
    public void testExpressionConstructor() {
        String s = "1 + 2";
        Expression expression = new Expression(s) {};
        
        assertEquals(s, expression.expression);
        assertNotNull(expression.stack);
        assertNull(expression.externalFunctions);
    }

    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression("1 + 2") {};
        expression.stack.add("1");
        expression.stack.add("+");
        expression.stack.add("2");
        
        expression.doEvaluationStack();
        ExpressionStack result = expression.stack;
        
        ExpressionStack expected = new ExpressionStack();
        expected.add("1");
        expected.add("2");
        expected.add("+");
        assertEquals(expected, result);
    }

    @Test
    public void testGetReservedFunctions() {
        String simbol = "+";
        
        FunctionExpression result = Expression.getReservedFunctions(simbol);
        
        assertNotNull(result);
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression() {};
        String simbol = "+";
        
        boolean result = expression.isReservedFunction(simbol);
        
        assertTrue(result);
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression() {};
        String simbol = "and";
        
        boolean result = expression.isLogicalFunction(simbol);
        
        assertTrue(result);
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression() {};
        String simbol = "sqrt";
        
        boolean result = expression.isMathFunction(simbol);
        
        assertTrue(result);
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression() {};
        String simbol = "date";
        
        boolean result = expression.isDateFunction(simbol);
        
        assertTrue(result);
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression() {};
        String simbol = "concat";
        
        boolean result = expression.isStringFunction(simbol);
        
        assertTrue(result);
    }

    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression() {};
        expression.externalFunctions = new ArrayList<>();
        FunctionExpression function = new AndFunction();
        expression.externalFunctions.add(function);
        String simbol = "and";
        
        FunctionExpression result = expression.getExternalFunction(simbol);
        
        assertEquals(function, result);
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression() {};
        expression.externalFunctions = new ArrayList<>();
        expression.externalFunctions.add(new AndFunction());
        String simbol = "and";
        
        boolean result = expression.isExternalFunction(simbol);
        
        assertTrue(result);
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression() {};
        FunctionExpression reservedFunction = new AndFunction();
        FunctionExpression externalFunction = new GenericFunctionExpression("customFunction");
        expression.externalFunctions = new ArrayList<>();
        expression.externalFunctions.add(externalFunction);
        String simbol = "and";
        
        FunctionExpression result = expression.getFunction(simbol);
        
        assertEquals(reservedFunction, result);
        
        simbol = "customFunction";
        result = expression.getFunction(simbol);
        
        assertEquals(externalFunction, result);
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression() {};
        FunctionExpression reservedFunction = new AndFunction();
        FunctionExpression externalFunction = new GenericFunctionExpression("customFunction");
        expression.externalFunctions = new ArrayList<>();
        expression.externalFunctions.add(externalFunction);
        String simbol = "and";
        
        boolean result = expression.isFunction(simbol);
        
        assertTrue(result);
        
        simbol = "customFunction";
        result = expression.isFunction(simbol);
        
        assertTrue(result);
    }

    @Test
    public void testExpressionConstructor2() {
        Expression expression = new Expression() {};
        
        assertFalse(expression.prepared);
    }

    @Test
    public void testStack() {
        Expression expression = new Expression() {};
        String c = "+";
        Stack stack = new Stack();
        stack.push("2");
        
        expression.stack(c, stack);
        Object result = stack.peek();
        
        assertEquals("2+", result);
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression() {};
        expression.expression = "1 + 2";
        
        expression.prepare();
        
        assertTrue(expression.prepared);
        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.add("1");
        expectedStack.add("2");
        expectedStack.add("+");
        assertEquals(expectedStack, expression.stack);
    }

    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression() {};
        expression.stack = new ExpressionStack(Arrays.asList("1", "+", "2"));
        
        ExpressionStack result = expression.getStackCopy();
        
        ExpressionStack expected = new ExpressionStack(Arrays.asList("1", "+", "2"));
        assertEquals(expected, result);
    }

    @Test
    public void testEvaluate() throws Exception {
        Expression expression = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return 3;
            }
        };
        
        Object result = expression.evaluate();
        
        assertEquals(3, result);
    }

    @Test
    public void testTokenize() {
        Expression expression = new Expression() {};
        expression.expression = "1 + 2";
        
        expression.tokenize();
        ExpressionStack result = expression.stack;
        
        ExpressionStack expected = new ExpressionStack();
        expected.add("1");
        expected.add("+");
        expected.add("2");
        assertEquals(expected, result);
    }
}