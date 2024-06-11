package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual_whenBothArgsAreSame_shouldReturnTrue() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_whenBothArgsAreDifferentTypesButEqual_shouldReturnTrue() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression("1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_whenBothArgsAreNumbersButNotEqual_shouldReturnFalse() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(2);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_whenArg1IsNull_shouldThrowIllegalArgumentException() {
        Expression arg2 = new ConstantExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(null, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_whenArg2IsNull_shouldThrowIllegalArgumentException() {
        Expression arg1 = new ConstantExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, null);
    }

    @Test
    public void testCoreOperationEqual_whenArg1IsNumberAndArg2IsStringButTheyAreEqual_shouldReturnTrue() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression("1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_whenArg1IsStringAndArg2IsNumberButTheyAreEqual_shouldReturnTrue() {
        Expression arg1 = new ConstantExpression("1");
        Expression arg2 = new ConstantExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_whenArg1IsNumberAndArg2IsBooleanAndTheyAreEqual_shouldReturnTrue() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new BooleanConstantExpression(true);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_whenArg1IsBooleanAndArg2IsNumberAndTheyAreNotEqual_shouldReturnFalse() {
        Expression arg1 = new BooleanConstantExpression(true);
        Expression arg2 = new ConstantExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_whenArg1IsBooleanAndArg2IsStringAndTheyAreNotEqual_shouldReturnFalse() {
        Expression arg1 = new BooleanConstantExpression(true);
        Expression arg2 = new ConstantExpression("1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_whenArg1IsStringAndArg2IsBooleanAndTheyAreNotEqual_shouldReturnFalse() {
        Expression arg1 = new ConstantExpression("1");
        Expression arg2 = new BooleanConstantExpression(true);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_whenArg1IsStringAndArg2IsStringButTheyAreNotEqual_shouldReturnFalse() {
        Expression arg1 = new ConstantExpression("1");
        Expression arg2 = new ConstantExpression("2");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_whenArg1IsEmptyStringAndArg2IsAnEmptyString_shouldReturnTrue() {
        Expression arg1 = new ConstantExpression("");
        Expression arg2 = new ConstantExpression("");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_whenArg1IsEmptyStringAndArg2IsANonEmptyString_shouldReturnFalse() {
        Expression arg1 = new ConstantExpression("");
        Expression arg2 = new ConstantExpression("1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_whenArg1IsANonEmptyStringAndArg2IsAnEmptyString_shouldReturnFalse() {
        Expression arg1 = new ConstantExpression("1");
        Expression arg2 = new ConstantExpression("");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }
}