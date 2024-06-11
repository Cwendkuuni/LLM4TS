package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.runtime.Context;
import org.apache.commons.jxpath.ri.test.TestSupport;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    private CoreOperationCompare arg1;
    private CoreOperationCompare arg2;
    private CoreOperationNotEqual notEqual;

    @Before
    public void setUp() {
        arg1 = new CoreOperationCompare(new ConstantExpression(1L), new ConstantExpression(1L));
        arg2 = new CoreOperationCompare(new ConstantExpression(2L), new ConstantExpression(1L));
        notEqual = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test
    public void testCoreOperationNotEqual_equal() {
        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);
        Assert.assertFalse(result.toString().equals("true"));
    }

    @Test
    public void testCoreOperationNotEqual_notEqual() {
        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);
        Assert.assertTrue(result.toString().equals("true"));
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationNotEqual_arg1_not_comparable_to_arg2() {
        arg1 = new ConstantExpression("abc");
        notEqual = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationNotEqual_arg2_not_comparable_to_arg1() {
        arg2 = new ConstantExpression(new Object() {});
        notEqual = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqual_null_arg() {
        arg1 = null;
        notEqual = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test
    public void testCoreOperationNotEqual_args_of_same_value() {
        arg1 = new ConstantExpression(3L);
        notEqual = new CoreOperationNotEqual(arg1, arg1);
        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);
        Assert.assertFalse(result.toString().equals("true"));
    }

}