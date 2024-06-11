package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.Test;

/**
 * Test cases for CoreOperationGreaterThanOrEqual class.
 */
public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValueWithDoubleValues() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new DoubleValue(5.0)),
                new ConstantExpression(new DoubleValue(3.0)));

        EvalContext context = new EvalContext(new InfoSet(), new NodeInfo(new Object()));
        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InfoSet(), new NodeInfo(new Object()));
        op = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new DoubleValue(3.0)),
                new ConstantExpression(new DoubleValue(5.0)));

        result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new InfoSet(), new NodeInfo(new Object()));
        op = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new DoubleValue(5.0)),
                new ConstantExpression(new DoubleValue(5.0)));

        result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithNodeInfoValues() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new NodeInfo(new DoubleValue(5.0))),
                new ConstantExpression(new NodeInfo(new DoubleValue(3.0))));

        EvalContext context = new EvalContext(new InfoSet(), new NodeInfo(new Object()));
        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InfoSet(), new NodeInfo(new Object()));
        op = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new NodeInfo(new DoubleValue(3.0))),
                new ConstantExpression(new NodeInfo(new DoubleValue(5.0))));

        result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new InfoSet(), new NodeInfo(new Object()));
        op = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new NodeInfo(new DoubleValue(5.0))),
                new ConstantExpression(new NodeInfo(new DoubleValue(5.0))));

        result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithMixedValues() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new DoubleValue(5.0)),
                new VariableExpression("x"));

        EvalContext context = new EvalContext(new InfoSet(), new NodeInfo(new Object()));
        context.setVariableValue("x", new DoubleValue(3.0));
        Object result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context.setVariableValue("x", new DoubleValue(5.5));
        result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context.setVariableValue("x", new DoubleValue(4.5));
        result = op.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new DoubleValue(5.0)),
                new ConstantExpression(new DoubleValue(3.0)));

        assertEquals(">=", op.getSymbol());
    }

    @Test
    public void testComputeValueWithNullContext() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new DoubleValue(5.0)),
                new ConstantExpression(new DoubleValue(3.0)));

        assertNull(op.computeValue(null));
    }
}