
package org.javex;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.javex.functions.logical.AndFunction;
import org.javex.utils.FunctionExpression;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExpressionTest {
    private Expression expression;

    @Before
    public void setup() {
        expression = new Expression("1 + 2");
    }

    @Test
    public void testGetExternalFunctions() {
        List<FunctionExpression> externalFunctions = Arrays.asList(
                mock(FunctionExpression.class),
                mock(FunctionExpression.class),
                mock(FunctionExpression.class)
        );

        expression.externalFunctions = externalFunctions;

        assertEquals(externalFunctions, expression.getExternalFunctions());
    }

    @Test
    public void testPrepare_ValidInput_UpdatesStack() {
        expression.prepare();

        ExpressionStack expectedStack = new ExpressionStack();
        expectedStack.add("1");
        expectedStack.add("2");
        expectedStack.add("+");

        assertEquals(expectedStack, expression.stack);
    }

    @Test
    public void testEvaluate_Expression_Addition_ReturnsCorrectResult() throws Exception {
        expression.prepare();

        Object result = expression.evaluate();

        assertEquals(3, result);
    }

    @Test(expected = Exception.class)
    public void testEvaluate_Expression_InvalidExpression_ThrowsException() throws Exception {
        expression = new Expression("1 +");

        expression.prepare();

        expression.evaluate();
    }

    @Test
    public void testIsReservedFunction_ValidReservedFunction_ReturnsTrue() {
        assertTrue(expression.isReservedFunction("and"));
    }

    @Test
    public void testIsReservedFunction_InvalidReservedFunction_ReturnsFalse() {
        assertFalse(expression.isReservedFunction("invalidFunction"));
    }

    @Test
    public void testIsLogicalFunction_ValidLogicalFunction_ReturnsTrue() {
        assertTrue(expression.isLogicalFunction("and"));
    }

    @Test
    public void testIsLogicalFunction_InvalidLogicalFunction_ReturnsFalse() {
        assertFalse(expression.isLogicalFunction("invalidFunction"));
    }

    @Test
    public void testIsMathFunction_ValidMathFunction_ReturnsTrue() {
        assertTrue(expression.isMathFunction("sqrt"));
    }

    @Test
    public void testIsMathFunction_InvalidMathFunction_ReturnsFalse() {
        assertFalse(expression.isMathFunction("invalidFunction"));
    }

    @Test
    public void testIsDateFunction_ValidDateFunction_ReturnsTrue() {
        assertTrue(expression.isDateFunction("dateAdd"));
    }

    @Test
    public void testIsDateFunction_InvalidDateFunction_ReturnsFalse() {
        assertFalse(expression.isDateFunction("invalidFunction"));
    }

    @Test
    public void testIsStringFunction_ValidStringFunction_ReturnsTrue() {
        assertTrue(expression.isStringFunction("startsWith"));
    }

    @Test
    public void testIsStringFunction_InvalidStringFunction_ReturnsFalse() {
        assertFalse(expression.isStringFunction("invalidFunction"));
    }

    @Test
    public void testIsExternalFunction_ValidExternalFunction_ReturnsTrue() {
        FunctionExpression externalFunction = mock(FunctionExpression.class);
        when(externalFunction.getSimbol()).thenReturn("externalFunction");

        expression.externalFunctions = Arrays.asList(externalFunction);

        assertTrue(expression.isExternalFunction("externalFunction"));
    }

    @Test
    public void testIsExternalFunction_InvalidExternalFunction_ReturnsFalse() {
        assertFalse(expression.isExternalFunction("invalidFunction"));
    }

    @Test
    public void testGetFunction_ValidReservedFunction_ReturnsFunction() {
        FunctionExpression expectedFunction = new AndFunction();

        Expression.reservedFunctions.put(expectedFunction, expectedFunction);

        FunctionExpression result = expression.getFunction("and");

        assertEquals(expectedFunction, result);
    }

    @Test
    public void testGetFunction_ValidExternalFunction_ReturnsFunction() {
        FunctionExpression expectedFunction = mock(FunctionExpression.class);
        when(expectedFunction.getSimbol()).thenReturn("externalFunction");

        expression.externalFunctions = Arrays.asList(expectedFunction);

        FunctionExpression result = expression.getFunction("externalFunction");

        assertEquals(expectedFunction, result);
    }

    @Test
    public void testGetFunction_InvalidFunction_ReturnsNull() {
        FunctionExpression result = expression.getFunction("invalidFunction");

        assertNull(result);
    }

    @Test
    public void testIsFunction_ValidFunction_ReturnsTrue() {
        FunctionExpression function = new AndFunction();

        Expression.reservedFunctions.put(function, function);

        assertTrue(expression.isFunction("and"));
    }

    @Test
    public void testIsFunction_ValidExternalFunction_ReturnsTrue() {
        FunctionExpression function = mock(FunctionExpression.class);
        when(function.getSimbol()).thenReturn("externalFunction");

        expression.externalFunctions = Arrays.asList(function);

        assertTrue(expression.isFunction("externalFunction"));
    }

    @Test
    public void testIsFunction_InvalidFunction_ReturnsFalse() {
        assertFalse(expression.isFunction("invalidFunction"));
    }

    @Test
    public void testStack_ValidInput_UpdatesStack() {
        Stack<String> stack = new Stack<>();
        stack.push("1");
        stack.push("+");

        expression.stack("+", stack);

        Stack<String> expectedStack = new Stack<>();
        expectedStack.push("1+");
        
        assertEquals(expectedStack, stack);
    }
}
