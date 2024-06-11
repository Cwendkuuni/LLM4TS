package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.Path;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValueWithBothArgumentsDouble() {
        Expression arg1 = new LiteralExpression(new Double(10.5));
        Expression arg2 = new LiteralExpression(new Double(5.0));
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Path("/"));
        assertTrue(op.computeValue(context).booleanValue());

        arg1 = new LiteralExpression(new Double(5.0));
        assertFalse(op.computeValue(context).booleanValue());

        arg1 = new LiteralExpression(new Double(10.5));
        arg2 = new LiteralExpression(new Double(10.5));
        assertTrue(op.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueWithMixedArguments() {
        Expression arg1 = new LiteralExpression(new Double(10.5));
        Expression arg2 = new LiteralExpression(new Integer(5));
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Path("/"));
        assertTrue(op.computeValue(context).booleanValue());

        arg1 = new LiteralExpression(new Integer(5));
        assertFalse(op.computeValue(context).booleanValue());

        arg1 = new LiteralExpression(new Double(10.5));
        arg2 = new LiteralExpression(new Double(10.5));
        assertTrue(op.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueWithNullArguments() {
        Expression arg1 = new LiteralExpression(null);
        Expression arg2 = new LiteralExpression(new Double(5.0));
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Path("/"));
        assertNull(op.computeValue(context));

        arg1 = new LiteralExpression(new Double(5.0));
        arg2 = new LiteralExpression(null);
        assertFalse(op.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueWithNaNArguments() {
        Expression arg1 = new LiteralExpression(Double.NaN);
        Expression arg2 = new LiteralExpression(new Double(5.0));
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Path("/"));
        assertFalse(op.computeValue(context).booleanValue());

        arg1 = new LiteralExpression(new Double(5.0));
        arg2 = new LiteralExpression(Double.NaN);
        assertFalse(op.computeValue(context).booleanValue());

        arg1 = new LiteralExpression(Double.NaN);
        arg2 = new LiteralExpression(Double.NaN);
        assertNull(op.computeValue(context));
    }

    @Test
    public void testComputeValueWithIncompatibleTypes() {
        Expression arg1 = new LiteralExpression(new Integer(10));
        Expression arg2 = new LiteralExpression(new String("5"));
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new Path("/"));
        assertFalse(op.computeValue(context).booleanValue());
    }
}