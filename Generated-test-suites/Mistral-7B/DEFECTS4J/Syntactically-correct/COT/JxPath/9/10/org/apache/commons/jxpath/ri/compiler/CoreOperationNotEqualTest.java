package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual_boolean_boolean() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(true), new ConstantExpression(false));
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_boolean_boolean_true() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(false), new ConstantExpression(true));
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqual_invalid_arguments() {
        new CoreOperationNotEqual(null, new ConstantExpression(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqual_invalid_arguments_2() {
        new CoreOperationNotEqual(new ConstantExpression(true), null);
    }

    @Test
    public void testCoreOperationNotEqual_int_int() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(2));
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_int_int_not_equal() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(2), new ConstantExpression(1));
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_String_String() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression("apple"), new ConstantExpression("orange"));
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_String_String_not_equal() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression("orange"), new ConstantExpression("apple"));
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqual_invalid_argument_types() {
        new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression("apple"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqual_invalid_argument_types_2() {
        new CoreOperationNotEqual(new ConstantExpression("apple"), new ConstantExpression(1));
    }
}