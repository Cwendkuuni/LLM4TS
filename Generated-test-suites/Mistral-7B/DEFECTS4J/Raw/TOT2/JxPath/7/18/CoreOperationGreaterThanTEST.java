 package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.LiteralExpression;
import org.apache.commons.jxpath.ri.core.CoreOperationAdd;
import org.apache.commons.jxpath.ri.core.CoreOperationGreaterThan;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        // Test Case 1
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(0.5);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        Assert.assertTrue(coreOperationGreaterThan.computeValue(new EvalContext()).booleanValue());

        // Test Case 2
        arg1 = new LiteralExpression(0.5);
        arg2 = new LiteralExpression(1.0);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        Assert.assertFalse(coreOperationGreaterThan.computeValue(new EvalContext()).booleanValue());

        // Test Case 3
        arg1 = new LiteralExpression(-3.0);
        arg2 = new LiteralExpression(-2.0);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        Assert.assertTrue(coreOperationGreaterThan.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanWithInvalidArguments() {
        // Test Case 1
        Expression arg1 = new LiteralExpression("1.0");
        Expression arg2 = new CoreOperationAdd(new LiteralExpression(1.0), new LiteralExpression(1.0));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        // Test Case 2
        coreOperationGreaterThan.computeValue(new EvalContext());
    }

    @Test
    public void testCoreOperationGreaterThanWithEqualValues() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        Assert.assertFalse(coreOperationGreaterThan.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testComputeValue() {
        // Test Case 1
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(0.5);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        Assert.assertTrue(coreOperationGreaterThan.computeValue(new EvalContext()).booleanValue());

        // Test Case 2
        arg1 = new LiteralExpression("1.0");
        arg2 = new CoreOperationAdd(new LiteralExpression(1.0), new LiteralExpression(1.0));
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        coreOperationGreaterThan.computeValue(new EvalContext());

        // Test Case 3
        Expression arg3 = new LiteralExpression(Double.NaN);
        Expression arg4 = new LiteralExpression(0.0);
        CoreOperationGreaterThan coreOperationGreaterThanNaN = new CoreOperationGreaterThan(arg3, arg4);
        Assert.assertFalse(coreOperationGreaterThanNaN.computeValue(new EvalContext()).booleanValue());

        // Test Case 4
        Expression arg5 = new LiteralExpression(Double.POSITIVE_INFINITY);
        Expression arg6 = new LiteralExpression(0.0);
        CoreOperationGreaterThan coreOperationGreaterThanInf = new CoreOperationGreaterThan(arg5, arg6);
        Assert.assertTrue(coreOperationGreaterThanInf.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new LiteralExpression(1.0), new LiteralExpression(0.5));
        Assert.assertEquals(">", coreOperationGreaterThan.getSymbol());
    }

}
