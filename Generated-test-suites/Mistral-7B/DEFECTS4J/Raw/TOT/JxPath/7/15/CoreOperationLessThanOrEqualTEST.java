package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.nodetest.AbstractNodeTestSupport;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationLessThanOrEqualTest extends AbstractNodeTestSupport {

    @Test
    public void testLessThanOrEqualWithDoubleValues() {
        // Test with valid double values
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new LiteralExpression(new DoubleNodeInfo(2.5)),
                new LiteralExpression(new DoubleNodeInfo(3.0)));
        EvalContext context = new EvalContext();
        context.setInfoSet(createInfoSet(new NodeInfo(null, 0, null, 0, null, null)));
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationLessThanOrEqual(
                new LiteralExpression(new DoubleNodeInfo(1.5)),
                new LiteralExpression(new DoubleNodeInfo(3.0)));
        context.reset();
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationLessThanOrEqual(
                new LiteralExpression(new DoubleNodeInfo(1.5)),
                new LiteralExpression(new DoubleNodeInfo(1.5)));
        context.reset();
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationLessThanOrEqual(
                new LiteralExpression(new DoubleNodeInfo(1.5)),
                new LiteralExpression(new DoubleNodeInfo(1.5)));
        context.reset();
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        // Test with edges cases
        expression = new CoreOperationLessThanOrEqual(
                new LiteralExpression(new DoubleNodeInfo(0.0)),
                new LiteralExpression(new DoubleNodeInfo(0.0)));
        context.reset();
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationLessThanOrEqual(
                new LiteralExpression(new DoubleNodeInfo(0.0)),
                new LiteralExpression(new DoubleNodeInfo(1.0)));
        context.reset();
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationLessThanOrEqual(
                new LiteralExpression(new DoubleNodeInfo(Double.MIN_VALUE)),
                new LiteralExpression(new DoubleNodeInfo(Double.MIN_VALUE)));
        context.reset();
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationLessThanOrEqual(
                new LiteralExpression(new DoubleNodeInfo(Double.MIN_VALUE)),
                new LiteralExpression(new DoubleNodeInfo(Double.MIN_VALUE + 1.0)));
        context.reset();
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        expression = new CoreOperationLessThanOrEqual(
                new LiteralExpression(new DoubleNodeInfo(Double.MAX_VALUE)),
                new LiteralExpression(new DoubleNodeInfo(Double.MAX_VALUE)));
        context.reset();
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationLessThanOrEqual(
                new LiteralExpression(new DoubleNodeInfo(Double.MAX_VALUE)),
                new LiteralExpression(new DoubleNodeInfo(Double.MAX_VALUE - 1.0)));
        context.reset();
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThanOrEqualWithNullValues() {
        // Test with null values
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new LiteralExpression(new DoubleNodeInfo(null)),
                new LiteralExpression(new DoubleNodeInfo(1.0)));
        EvalContext context = new EvalContext();
        context.setInfoSet(createInfoSet(new NodeInfo(null, 0, null, 0, null, null)));
        Object result = expression.computeValue(context);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLessThanOrEqualWithInvalidArguments() {
        // Test with invalid arguments
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new LiteralExpression(new IntegerNodeInfo(1)),
                new LiteralExpression(new StringNodeInfo("abc")));
    }
}