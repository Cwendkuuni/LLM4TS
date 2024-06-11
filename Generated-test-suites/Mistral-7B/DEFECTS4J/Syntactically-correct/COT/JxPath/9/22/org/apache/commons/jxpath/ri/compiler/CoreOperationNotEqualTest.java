package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.CompilerTest;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;

/**
 * Test cases for CoreOperationNotEqual class.
 *
 * @author junit-test-generator
 */
public class CoreOperationNotEqualTest {

    @SuppressWarnings("unchecked")
    private static <T> Expression<T> createExpression(Class<T> clazz, Object value) {
        return (Expression<T>) new ConstantExpression<>(value);
    }

    /**
     * Test method for {@link CoreOperationNotEqual#CoreOperationNotEqual(Expression, Expression)}.
     */
    @Test
    public void testConstructor() {
        Expression<Object> arg1 = createExpression(Object.class, "value1");
        Expression<Object> arg2 = createExpression(Object.class, "value2");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertEquals("!=" + arg1 + " " + arg2, coreOperationNotEqual.getSymbol());
    }

    /**
     * Test method for {@link CoreOperationNotEqual#computeValue(EvalContext)}.
     */
    @Test
    public void testComputeValue_whenBothArgsAreSame_thenReturnFalse() {
        Expression<Number> arg1 = createExpression(Number.class, 1);
        Expression<Number> arg2 = arg1;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertFalse("Expected false, but got: " + result, result instanceof Boolean);
        assertTrue("Expected false, but got: " + result.booleanValue(), result.booleanValue() == false);
    }

    /**
     * Test method for {@link CoreOperationNotEqual#computeValue(EvalContext)} when arg1 equals arg2.
     */
    @Test
    public void testComputeValue_whenArg1EqualsArg2_thenReturnFalse() {
        Expression<String> arg1 = createExpression(String.class, "value1");
        Expression<String> arg2 = arg1;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertFalse("Expected false, but got: " + result, result instanceof Boolean);
        assertTrue("Expected false, but got: " + result.booleanValue(), result.booleanValue() == false);
    }

    /**
     * Test method for {@link CoreOperationNotEqual#computeValue(EvalContext)} when arg1 is null and arg2 is not null.
     */
    @Test
    public void testComputeValue_whenArg1IsNullAndArg2IsNotNull_thenReturnTrue() {
        Expression<String> arg1 = null;
        Expression<String> arg2 = createExpression(String.class, "value1");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue("Expected true, but got: " + result, result instanceof Boolean);
        assertTrue("Expected true, but got: " + result.booleanValue(), result.booleanValue() == true);
    }

    /**
     * Test method for {@link CoreOperationNotEqual#computeValue(EvalContext)} when arg1 is not null and arg2 is null.
     */
    @Test
    public void testComputeValue_whenArg1IsNotNullAndArg2IsNull_thenReturnTrue() {
        Expression<String> arg1 = createExpression(String.class, "value1");
        Expression<String> arg2 = null;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue("Expected true, but got: " + result, result instanceof Boolean);
        assertTrue("Expected true, but got: " + result.booleanValue(), result.booleanValue() == true);
    }

    /**
     * Test method for {@link CoreOperationNotEqual#computeValue(EvalContext)} when arg1 is null and arg2 is also null.
     */
    @Test
    public void testComputeValue_whenBothArgsAreNull_thenReturnTrue() {
        Expression<String> arg1 = null;
        Expression<String> arg2 = null;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue("Expected true, but got: " + result, result instanceof Boolean);
        assertTrue("Expected true, but got: " + result.booleanValue(), result.booleanValue() == true);
    }

    /**
     * Test method for {@link CoreOperationNotEqual#computeValue(EvalContext)} with different data types.
     */
    @Test
    public void testComputeValue_withDifferentDataTypes_thenReturnCorrectResult() {
        Expression<Number> arg1 = createExpression(Number.class, 1);
        Expression<String> arg2 = createExpression(String.class, "value1");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue("Expected true, but got: " + result, result instanceof Boolean);
        assertTrue("Expected true, but got: " + result.booleanValue(), result.booleanValue() == true);
    }

    /**
     * Test method for {@link CoreOperationNotEqual#getSymbol()} with null arg1.
     */
    @Test(expected = NullPointerException.class)
    public void testGetSymbol_whenArg1IsNull_thenThrowNullPointerException() {
        Expression<String> arg1 = null;
        Expression<String> arg2 = createExpression(String.class, "value1");
        new CoreOperationNotEqual(arg1, arg2).getSymbol();
    }

    /**
     * Test method for {@link CoreOperationNotEqual#getSymbol()} with null arg2.
     */
    @Test(expected = NullPointerException.class)
    public void testGetSymbol_whenArg2IsNull_thenThrowNullPointerException() {
        Expression<String> arg1 = createExpression(String.class, "value1");
        Expression<String> arg2 = null;
        new CoreOperationNotEqual(arg1, arg2).getSymbol();
    }

    /**
     * Test method for {@link CoreOperationNotEqual#getSymbol()} with valid arguments.
     */
    @Test
    public void testGetSymbol_withValidArguments_thenReturnExpectedSymbol() {
        Expression<Number> arg1 = createExpression(Number.class, 1);
        Expression<Number> arg2 = createExpression(Number.class, 2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        String symbol = coreOperationNotEqual.getSymbol();
        assertEquals("!=", symbol);
    }
}