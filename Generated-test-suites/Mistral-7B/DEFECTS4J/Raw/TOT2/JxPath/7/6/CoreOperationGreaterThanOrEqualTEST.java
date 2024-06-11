package org.apache.commons.jxpath.ri.compiler;

import junit.framework.Assert;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.LiteralExpression;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new LiteralExpression("5.0");
        Expression arg2 = new LiteralExpression("5.0");
        CoreOperationGreaterThanOrEqual coreOp = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Assert.assertTrue(coreOp.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValueWithInts() {
        Expression arg1 = new LiteralExpression("5");
        Expression arg2 = new LiteralExpression("5");
        CoreOperationGreaterThanOrEqual coreOp = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Assert.assertTrue(coreOp.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValueWithNaNAndInfinity() {
        Expression arg1 = new LiteralExpression("NaN");
        Expression arg2 = new LiteralExpression("Infinity");
        CoreOperationGreaterThanOrEqual coreOp = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Assert.assertFalse(coreOp.computeValue(context).equals(Boolean.TRUE));
        Assert.assertNull(coreOp.computeValue(context));
    }

    @Test(expected = RuntimeException.class)
    public void testComputeValueWithNullArguments() {
        Expression arg1 = null;
        Expression arg2 = new LiteralExpression("5.0");
        CoreOperationGreaterThanOrEqual coreOp = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOp.computeValue(context);
    }

    // ... Add more test cases for other methods if necessary
}