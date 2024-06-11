package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual_false() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreConstantExpression(1);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_true() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreConstantExpression(2);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_nullArg1() {
        Expression arg1 = null;
        Expression arg2 = new CoreConstantExpression(1);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertNull(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_nullArg2() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = null;

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_nullBoth() {
        Expression arg1 = null;
        Expression arg2 = null;

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertNull(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_arg1PrimitiveType() {
        Expression arg1 = new CoreNumberExpression(1);
        Expression arg2 = new CoreConstantExpression("1");

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_arg2PrimitiveType() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreNumberExpression(1);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_arg1BooleanType() {
        Expression arg1 = new CoreBooleanExpression(true);
        Expression arg2 = new CoreConstantExpression(1);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqual_arg2BooleanType() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreBooleanExpression(false);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));
    }
}