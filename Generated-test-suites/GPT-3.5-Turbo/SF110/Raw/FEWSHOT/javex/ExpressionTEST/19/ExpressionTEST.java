package org.javex;

import static org.junit.Assert.*;
import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression();
        assertNull(expression.getExternalFunctions());
    }

    @Test
    public void testExpression() {
        Expression expression = new Expression("2 + 3");
        assertNotNull(expression);
        assertEquals("2 + 3", expression.expression);
        assertNotNull(expression.stack);
        assertNull(expression.externalFunctions);
        assertFalse(expression.prepared);
    }

    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression();
        expression.stack = new ExpressionStack();
        expression.stack.add("2");
        expression.stack.add("+");
        expression.stack.add("3");
        expression.doEvaluationStack();
        assertEquals("2 3 +", expression.stack.toString());
    }

    @Test
    public void testGetReservedFunctions() {
        String simbol = "+";
        FunctionExpression result = Expression.getReservedFunctions(simbol);
        assertNotNull(result);
    }

    @Test
    public void testIsReservedFunction() {
        Expression expression = new Expression();
        assertTrue(expression.isReservedFunction("+"));
        assertFalse(expression.isReservedFunction("!"));
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expression = new Expression();
        assertTrue(expression.isLogicalFunction("and"));
        assertFalse(expression.isLogicalFunction("+"));
    }

    @Test
    public void testIsMathFunction() {
        Expression expression = new Expression();
        assertTrue(expression.isMathFunction("+"));
        assertFalse(expression.isMathFunction("and"));
    }

    @Test
    public void testIsDateFunction() {
        Expression expression = new Expression();
        assertTrue(expression.isDateFunction("dateAdd"));
        assertFalse(expression.isDateFunction("and"));
    }

    @Test
    public void testIsStringFunction() {
        Expression expression = new Expression();
        assertTrue(expression.isStringFunction("startsWith"));
        assertFalse(expression.isStringFunction("and"));
    }

    @Test
    public void testGetExternalFunction() {
        Expression expression = new Expression();
        expression.externalFunctions = new ArrayList<FunctionExpression>();
        FunctionExpression function1 = new FunctionExpression();
        function1.setSimbol("startsWith");
        FunctionExpression function2 = new FunctionExpression();
        function2.setSimbol("substring");
        expression.externalFunctions.add(function1);
        expression.externalFunctions.add(function2);
        FunctionExpression result = expression.getExternalFunction("startsWith");
        assertNotNull(result);
        assertEquals("startsWith", result.getSimbol());

        result = expression.getExternalFunction("substring");
        assertNotNull(result);
        assertEquals("substring", result.getSimbol());

        result = expression.getExternalFunction("contains");
        assertNull(result);
    }

    @Test
    public void testIsExternalFunction() {
        Expression expression = new Expression();
        expression.externalFunctions = new ArrayList<FunctionExpression>();
        FunctionExpression function1 = new FunctionExpression();
        function1.setSimbol("startsWith");
        FunctionExpression function2 = new FunctionExpression();
        function2.setSimbol("substring");
        expression.externalFunctions.add(function1);
        expression.externalFunctions.add(function2);
        assertTrue(expression.isExternalFunction("startsWith"));
        assertTrue(expression.isExternalFunction("substring"));
        assertFalse(expression.isExternalFunction("contains"));
    }

    @Test
    public void testGetFunction() {
        Expression expression = new Expression();
        assertTrue(expression.getFunction("+") instanceof PlusFunction);
        assertTrue(expression.getFunction("startsWith") instanceof StartsWithFunction);
        assertNull(expression.getFunction("!"));
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression();
        assertTrue(expression.isFunction("+"));
        assertTrue(expression.isFunction("startsWith"));
        assertFalse(expression.isFunction("!="));
    }

    @Test
    public void testExpressionConstructor() {
        Expression expression = new Expression();
        assertFalse(expression.prepared);
    }

    @Test
    public void testStack() {
        Expression expression = new Expression();
        Stack<String> stack = new Stack<String>();
        stack.push("a");
        expression.stack("b", stack);
        assertEquals("ab", stack.peek());
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression();
        expression.expression = "2 + 3";
        expression.prepare();
        assertTrue(expression.prepared);
        assertNotNull(expression.stack);
        assertEquals("2 3 +", expression.stack.toString());
    }

    @Test
    public void testGetStackCopy() {
        Expression expression = new Expression();
        expression.stack = new ExpressionStack();
        expression.stack.add("2");
        expression.stack.add("+");
        expression.stack.add("3");
        ExpressionStack stackCopy = expression.getStackCopy();
        assertEquals(expression.stack.toString(), stackCopy.toString());
    }

    @Test
    public void testEvaluate() throws Exception {
        // Test implementation specific to the evaluate method
    }

    @Test
    public void testTokenize() {
        Expression expression = new Expression();
        expression.expression = "2 + 3";
        expression.tokenize();
        assertEquals("2 3 +", expression.stack.toString());
    }
}