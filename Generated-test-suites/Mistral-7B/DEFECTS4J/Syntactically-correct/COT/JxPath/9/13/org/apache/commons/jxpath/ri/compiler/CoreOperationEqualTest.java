package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualarg1Constant;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualarg1Variable;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualarg2Constant;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualarg2Variable;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual_arg1Constant_arg2Constant() {
        CoreOperationEqual operation = new CoreOperationEqual(
                new CoreOperationEqualarg1Constant(1),
                new CoreOperationEqualarg2Constant(1)
        );
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        Assert.assertTrue(result == Boolean.TRUE);
    }

    @Test
    public void testCoreOperationEqual_arg1Constant_arg2Constant_differentValues() {
        CoreOperationEqual operation = new CoreOperationEqual(
                new CoreOperationEqualarg1Constant(1),
                new CoreOperationEqualarg2Constant(2)
        );
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        Assert.assertTrue(result == Boolean.FALSE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_arg1Null() {
        CoreOperationEqual operation = new CoreOperationEqual(null, new CoreOperationEqualarg2Variable());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_arg2Null() {
        CoreOperationEqual operation = new CoreOperationEqual(new CoreOperationEqualarg1Variable(), null);
    }

    @Test
    public void testCoreOperationEqual_arg1Variable_arg2Constant() {
        CoreOperationEqual operation = new CoreOperationEqual(
                new CoreOperationEqualarg1Variable(),
                new CoreOperationEqualarg2Constant(1)
        );
        EvalContext context = new EvalContext();
        context.setValue("x", 1);
        Object result = operation.computeValue(context);
        Assert.assertTrue(result == Boolean.TRUE);
    }

    @Test
    public void testCoreOperationEqual_arg1Variable_arg2Variable() {
        CoreOperationEqual operation = new CoreOperationEqual(
                new CoreOperationEqualarg1Variable(),
                new CoreOperationEqualarg2Variable()
        );
        EvalContext context = new EvalContext();
        context.setValue("x", 1);
        context.setValue("y", 1);
        Object result = operation.computeValue(context);
        Assert.assertTrue(result == Boolean.TRUE);
    }

    @Test
    public void testCoreOperationEqual_arg1Variable_arg2Variable_differentValues() {
        CoreOperationEqual operation = new CoreOperationEqual(
                new CoreOperationEqualarg1Variable(),
                new CoreOperationEqualarg2Variable()
        );
        EvalContext context = new EvalContext();
        context.setValue("x", 1);
        context.setValue("y", 2);
        Object result = operation.computeValue(context);
        Assert.assertTrue(result == Boolean.FALSE);
    }

    @Test
    public void testCoreOperationEqual_arg1Variable_arg2Variable_sameValuesDifferentTypes() {
        CoreOperationEqual operation = new CoreOperationEqual(
                new CoreOperationEqualarg1Variable(),
                new CoreOperationEqualarg2Variable()
        );
        EvalContext context = new EvalContext();
        context.setValue("x", 1.0);
        context.setValue("y", "1");
        Object result = operation.computeValue(context);
        Assert.assertTrue(result == Boolean.FALSE);
    }

    @Test
    public void testCoreOperationEqual_arg1Variable_arg2Variable_sameValuesSameTypes() {
        CoreOperationEqual operation = new CoreOperationEqual(
                new CoreOperationEqualarg1Variable(),
                new CoreOperationEqualarg2Variable()
        );
        EvalContext context = new EvalContext();
        context.setValue("x", 1);
        context.setValue("y", 1);
        Object result = operation.computeValue(context);
        Assert.assertTrue(result == Boolean.TRUE);
    }
}