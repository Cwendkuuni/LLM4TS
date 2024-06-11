package org.javex;

import org.javex.utils.*;
import org.javex.functions.logical.*;
import org.javex.functions.math.*;
import org.javex.functions.date.*;
import org.javex.functions.string.*;
import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("2 + 2");
        List<FunctionExpression> expected = null;
        List<FunctionExpression> result = expression.getExternalFunctions();
        assertEquals(expected, result);
    }

    @Test
    public void testExpressionConstructor() {
        Expression expression = new Expression("2 + 2");
        String expected = "2 + 2";
        String result = expression.expression;
        assertEquals(expected, result);
    }

    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression("2 + 2");
        expression.stack = new ExpressionStack();
        expression.stack.add("2");
        expression.stack.add("+");
        expression.stack.add("2");
        expression.doEvaluationStack();
        ExpressionStack expected = new ExpressionStack();
        expected.add("2");
        expected.add("2");
        expected.add("+");
        ExpressionStack result = expression.stack;
        assertEquals(expected, result);
    }

    @Test
    public void testPutFunction() {
        FunctionExpression function = new AndFunction();
        Map<FunctionExpression, FunctionExpression> map = new TreeMap<>();
        Expression.putFunction(function, map);
        Map<FunctionExpression, FunctionExpression> expected = new TreeMap<>();
        expected.put(function, function);
        Map<FunctionExpression, FunctionExpression> result = map;
        assertEquals(expected, result);
    }

    @Test
    public void testGetReservedFunctions() {
        String symbol = "and";
        FunctionExpression expected = new AndFunction();
        FunctionExpression result = Expression.getReservedFunctions(symbol);
        assertEquals(expected, result);
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression();
        String symbol = "and";
        boolean expected = true;
        boolean result = expression.isReservedFunction(symbol);
        assertEquals(expected, result);
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression();
        String symbol = "and";
        boolean expected = true;
        boolean result = expression.isLogicalFunction(symbol);
        assertEquals(expected, result);
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression();
        String symbol = "+";
        boolean expected = true;
        boolean result = expression.isMathFunction(symbol);
        assertEquals(expected, result);
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression();
        String symbol = "date";
        boolean expected = false;
        boolean result = expression.isDateFunction(symbol);
        assertEquals(expected, result);
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression();
        String symbol = "substring";
        boolean expected = true;
        boolean result = expression.isStringFunction(symbol);
        assertEquals(expected, result);
    }

    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression();
        expression.externalFunctions = new ArrayList<>();
        expression.externalFunctions.add(new ToLowerFunction());
        expression.externalFunctions.add(new ToUpperFunction());
        String symbol = "tolower";
        FunctionExpression expected = new ToLowerFunction();
        FunctionExpression result = expression.getExternalFunction(symbol);
        assertEquals(expected, result);
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression();
        expression.externalFunctions = new ArrayList<>();
        expression.externalFunctions.add(new ToLowerFunction());
        expression.externalFunctions.add(new ToUpperFunction());
        String symbol = "tolower";
        boolean expected = true;
        boolean result = expression.isExternalFunction(symbol);
        assertEquals(expected, result);
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression();
        expression.externalFunctions = new ArrayList<>();
        expression.externalFunctions.add(new ToLowerFunction());
        expression.externalFunctions.add(new ToUpperFunction());
        String symbol = "tolower";
        FunctionExpression expected = new ToLowerFunction();
        FunctionExpression result = expression.getFunction(symbol);
        assertEquals(expected, result);
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression();
        expression.externalFunctions = new ArrayList<>();
        expression.externalFunctions.add(new ToLowerFunction());
        expression.externalFunctions.add(new ToUpperFunction());
        String symbol = "tolower";
        boolean expected = true;
        boolean result = expression.isFunction(symbol);
        assertEquals(expected, result);
    }

    @Test
    public void testExpressionConstructor2() {
        Expression expression = new Expression();
        boolean expected = false;
        boolean result = expression.prepared;
        assertEquals(expected, result);
    }

    @Test
    public void testStack() {
        Expression expression = new Expression();
        String c = "a";
        Stack<String> stack = new Stack<>();
        stack.push("b");
        expression.stack(c, stack);
        String expected = "ba";
        String result = stack.peek();
        assertEquals(expected, result);
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression("2 + 2");
        expression.prepare();
        boolean expected = true;
        boolean result = expression.prepared;
        assertEquals(expected, result);
    }

    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression("2 + 2");
        expression.stack = new ExpressionStack();
        expression.stack.add("2");
        expression.stack.add("+");
        expression.stack.add("2");
        ExpressionStack expected = new ExpressionStack();
        expected.add("2");
        expected.add("+");
        expected.add("2");
        ExpressionStack result = expression.getStackCopy();
        assertEquals(expected, result);
    }

    @Test
    public void testEvaluate() throws Exception {
        Expression expression = new Expression("2 + 2");
        Object expected = null;
        Object result = expression.evaluate();
        assertEquals(expected, result);
    }

    @Test
    public void testTokenize() {
        Expression expression = new Expression();
        expression.expression = "2 + 2";
        expression.tokenize();
        ExpressionStack expected = new ExpressionStack();
        expected.add("2");
        expected.add("+");
        expected.add("2");
        ExpressionStack result = expression.stack;
        assertEquals(expected, result);
    }

}