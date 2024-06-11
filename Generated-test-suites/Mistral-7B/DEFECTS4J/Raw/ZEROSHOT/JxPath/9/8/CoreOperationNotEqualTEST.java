package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;
import org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_whenBothArgsNull_thenReturnFalse() {
        Expression arg1 = null;
        Expression arg2 = null;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        Assert.assertFalse(result instanceof Boolean);
    }

    @Test
    public void testComputeValue_whenArg1IsNull_andArg2IsNumber_thenReturnTrue() {
        Expression arg1 = null;
        Expression arg2 = new CoreNumber(5);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_whenArg1IsNumber_andArg2IsNull_thenReturnTrue() {
        Expression arg1 = new CoreNumber(5);
        Expression arg2 = null;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_whenArg1IsNumber_andArg2IsNumber_thenReturnFalse() {
        Expression arg1 = new CoreNumber(5);
        Expression arg2 = new CoreNumber(5);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_whenArg1IsString_andArg2IsString_thenReturnFalse() {
        Expression arg1 = new CoreString("hello");
        Expression arg2 = new CoreString("hello");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_whenArg1IsString_andArg2IsNumber_thenThrowException() {
        Expression arg1 = new CoreString("hello");
        Expression arg2 = new CoreNumber(5);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        try {
            coreOperationNotEqual.computeValue(context);
            fail("Expected an exception to be thrown.");
        } catch (ClassCastException e) {
            // expected
        }
    }

    @Test
    public void testComputeValue_whenArg1IsNumber_andArg2IsString_thenThrowException() {
        Expression arg1 = new CoreNumber(5);
        Expression arg2 = new CoreString("hello");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        try {
            coreOperationNotEqual.computeValue(context);
            fail("Expected an exception to be thrown.");
        } catch (ClassCastException e) {
            // expected
        }
    }
}