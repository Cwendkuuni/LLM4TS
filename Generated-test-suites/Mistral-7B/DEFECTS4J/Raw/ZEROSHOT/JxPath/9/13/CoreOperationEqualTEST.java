package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualTest;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual_true() {
        Expression arg1 = ExpressionFactory.createLiteral(1);
        Expression arg2 = ExpressionFactory.createLiteral(1);

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());

        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_false() {
        Expression arg1 = ExpressionFactory.createLiteral(1);
        Expression arg2 = ExpressionFactory.createLiteral(2);

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());

        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_null_arg1() {
        Expression arg1 = ExpressionFactory.createNull();
        Expression arg2 = ExpressionFactory.createLiteral(1);

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());

        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_null_arg2() {
        Expression arg1 = ExpressionFactory.createLiteral(1);
        Expression arg2 = ExpressionFactory.createNull();

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());

        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_equal_strings() {
        Expression arg1 = ExpressionFactory.createString("test");
        Expression arg2 = ExpressionFactory.createString("test");

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());

        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_not_equal_strings() {
        Expression arg1 = ExpressionFactory.createString("test");
        Expression arg2 = ExpressionFactory.createString("anotherTest");

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());

        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_equal_numbers() {
        Expression arg1 = ExpressionFactory.createLiteral(1.0);
        Expression arg2 = ExpressionFactory.createLiteral(1.0);

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());

        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationEqual_not_equal_numbers() {
        Expression arg1 = ExpressionFactory.createLiteral(1.0);
        Expression arg2 = ExpressionFactory.createLiteral(2.0);

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());

        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationEqual_not_equal_types() {
        Expression arg1 = ExpressionFactory.createLiteral(1);
        Expression arg2 = ExpressionFactory.createString("test");

        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(new EvalContext());

        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }
}