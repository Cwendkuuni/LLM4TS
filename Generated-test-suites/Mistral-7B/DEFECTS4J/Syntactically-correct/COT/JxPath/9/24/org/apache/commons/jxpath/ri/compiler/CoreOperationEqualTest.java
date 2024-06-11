package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualTest;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;

/**
 * Test for CoreOperationEqual class.
 */
public class CoreOperationEqualTest {

    @org.junit.Test
    public void testConstructor() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertSame(CoreOperationEqual.class, coreOperationEqual.getClass());
    }

    @org.junit.Test
    public void testComputeValueWhenBothArgumentsAreSameInstance() {
        Expression arg1 = new Expression();
        Expression arg2 = arg1;
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @org.junit.Test
    public void testComputeValueWhenBothArgumentsAreEqual() {
        Expression arg1 = new Expression(1);
        Expression arg2 = new Expression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @org.junit.Test
    public void testComputeValueWhenBothArgumentsAreDifferentInstancesButEqualValue() {
        Expression arg1 = new Expression(1);
        Expression arg2 = new Expression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @org.junit.Test
    public void testComputeValueWhenBothArgumentsAreDifferentInstancesAndHaveDifferentValues() {
        Expression arg1 = new Expression(1);
        Expression arg2 = new Expression(2);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @org.junit.Test
    public void testComputeValueWhenArg1IsNull() {
        Expression arg1 = new Expression(null);
        Expression arg2 = new Expression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @org.junit.Test
    public void testComputeValueWhenArg2IsNull() {
        Expression arg1 = new Expression(1);
        Expression arg2 = new Expression(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @org.junit.Test
    public void testComputeValueWhenArg1IsNumberAndArg2IsString() {
        Expression arg1 = new Expression(1);
        Expression arg2 = new Expression("1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @org.junit.Test
    public void testComputeValueWhenArg1IsStringAndArg2IsNumber() {
        Expression arg1 = new Expression("1");
        Expression arg2 = new Expression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @org.junit.Test
    public void testComputeValueWhenArg1IsStringAndArg2IsAnotherString() {
        Expression arg1 = new Expression("1");
        Expression arg2 = new Expression("2");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @org.junit.Test
    public void testComputeValueWhenArg1IsBooleanAndArg2IsBooleanTrue() {
        Expression arg1 = new Expression(Boolean.TRUE);
        Expression arg2 = new Expression(Boolean.TRUE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @org.junit.Test
    public void testComputeValueWhenArg1IsBooleanAndArg2IsBooleanFalse() {
        Expression arg1 = new Expression(Boolean.TRUE);
        Expression arg2 = new Expression(Boolean.FALSE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @org.junit.Test
    public void testComputeValueWhenArg1IsBooleanAndArg2IsNumber() {
        Expression arg1 = new Expression(Boolean.TRUE);
        Expression arg2 = new Expression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertNull(result);
    }

    @org.junit.Test
    public void testComputeValueWhenArg1IsNumberAndArg2IsBooleanTrue() {
        Expression arg1 = new Expression(1);
        Expression arg2 = new Expression(Boolean.TRUE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertNull(result);
    }

    @org.junit.Test
    public void testComputeValueWhenArg1IsNumberAndArg2IsBooleanFalse() {
        Expression arg1 = new Expression(1);
        Expression arg2 = new Expression(Boolean.FALSE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertNull(result);
    }

    @org.junit.Test
    public void testComputeValueWhenArg1IsNumberAndArg2IsString() {
        Expression arg1 = new Expression(1);
        Expression arg2 = new Expression("1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @org.junit.Test
    public void testComputeValueWhenArg1IsStringAndArg2IsNumber() {
        Expression arg1 = new Expression("1");
        Expression arg2 = new Expression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @org.junit.Test
    public void testComputeValueWhenArg1IsStringAndArg2IsAnotherString() {
        Expression arg1 = new Expression("1");
        Expression arg2 = new Expression("2");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testComputeValueWhenArg1AndArg2AreBothNull() {
        Expression arg1 = new Expression(null);
        Expression arg2 = new Expression(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testComputeValueWhenArg1IsNumberAndArg2IsNull() {
        Expression arg1 = new Expression(1);
        Expression arg2 = new Expression(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testComputeValueWhenArg1IsStringAndArg2IsNull() {
        Expression arg1 = new Expression("1");
        Expression arg2 = new Expression(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testComputeValueWhenArg1IsBooleanAndArg2IsNull() {
        Expression arg1 = new Expression(Boolean.TRUE);
        Expression arg2 = new Expression(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testComputeValueWhenArg1IsBooleanAndArg2IsAnotherBooleanWithDifferentType() {
        Expression arg1 = new Expression(Boolean.TRUE);
        Expression arg2 = new Expression(new String("true"));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testComputeValueWhenArg1IsNumberAndArg2IsAnotherNumberWithDifferentType() {
        Expression arg1 = new Expression(1);
        Expression arg2 = new Expression(new Double(1.5));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());
    }
}