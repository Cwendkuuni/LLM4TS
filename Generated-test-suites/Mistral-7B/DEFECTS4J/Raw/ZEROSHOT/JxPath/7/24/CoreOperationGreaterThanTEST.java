package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Value;
import org.junit.Test;

/**
 * Test file for CoreOperationGreaterThan class.
 */
public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        // Test with valid double values
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new LiteralExpression(new Value(5.5, new NodeInfo(new InfoSet(), 1, 1, ""))),
                new LiteralExpression(new Value(2.5, new NodeInfo(new InfoSet(), 2, 1, ""))));
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue("Expected true, but received: " + result, result instanceof Boolean && ((Boolean) result).booleanValue());

        // Test with invalid double values
        coreOperationGreaterThan = new CoreOperationGreaterThan(
                new LiteralExpression(new Value(Double.NaN, new NodeInfo(new InfoSet(), 1, 1, ""))),
                new LiteralExpression(new Value(Double.NaN, new NodeInfo(new InfoSet(), 2, 1, ""))));
        context = new EvalContext();
        result = coreOperationGreaterThan.computeValue(context);
        assertFalse("Expected false, but received: " + result, result instanceof Boolean);

        // Test with negative double values
        coreOperationGreaterThan = new CoreOperationGreaterThan(
                new LiteralExpression(new Value(-1.5, new NodeInfo(new InfoSet(), 1, 1, ""))),
                new LiteralExpression(new Value(-2.5, new NodeInfo(new InfoSet(), 2, 1, ""))));
        context = new EvalContext();
        result = coreOperationGreaterThan.computeValue(context);
        assertFalse("Expected false, but received: " + result, result instanceof Boolean);

        coreOperationGreaterThan = new CoreOperationGreaterThan(
                new LiteralExpression(new Value(-1.5, new NodeInfo(new InfoSet(), 1, 1, ""))),
                new LiteralExpression(new Value(-2.5, new NodeInfo(new InfoSet(), 2, 1, ""))));
        context = new EvalContext();
        result = coreOperationGreaterThan.computeValue(context);
        assertTrue("Expected false, but received: " + result, !result instanceof Boolean);

        coreOperationGreaterThan = new CoreOperationGreaterThan(
                new LiteralExpression(new Value(-5.5, new NodeInfo(new InfoSet(), 1, 1, ""))),
                new LiteralExpression(new Value(-6.5, new NodeInfo(new InfoSet(), 2, 1, ""))));
        context = new EvalContext();
        result = coreOperationGreaterThan.computeValue(context);
        assertFalse("Expected false, but received: " + result, result instanceof Boolean);

        coreOperationGreaterThan = new CoreOperationGreaterThan(
                new LiteralExpression(new Value(-5.5, new NodeInfo(new InfoSet(), 1, 1, ""))),
                new LiteralExpression(new Value(-6.5, new NodeInfo(new InfoSet(), 2, 1, ""))));
        context = new EvalContext();
        result = coreOperationGreaterThan.computeValue(context);
        assertTrue("Expected false, but received: " + result, !result instanceof Boolean);

        // Test with valid integer values
        coreOperationGreaterThan = new CoreOperationGreaterThan(
                new LiteralExpression(new Value(5, new NodeInfo(new InfoSet(), 1, 1, ""))),
                new LiteralExpression(new Value(2, new NodeInfo(new InfoSet(), 2, 1, ""))));
        context = new EvalContext();
        result = coreOperationGreaterThan.computeValue(context);
        assertTrue("Expected true, but received: " + result, result instanceof Boolean && ((Boolean) result).booleanValue());

        // Test with invalid integer values
        coreOperationGreaterThan = new CoreOperationGreaterThan(
                new LiteralExpression(new Value(Integer.MIN_VALUE, new NodeInfo(new InfoSet(), 1, 1, ""))),
                new LiteralExpression(new Value(Integer.MIN_VALUE, new NodeInfo(new InfoSet(), 2, 1, ""))));
        context = new EvalContext();
        result = coreOperationGreaterThan.computeValue(context);
        assertFalse("Expected false, but received: " + result, result instanceof Boolean);

        // Test with negative integer values
        coreOperationGreaterThan = new CoreOperationGreaterThan(
                new LiteralExpression(new Value(-5, new NodeInfo(new InfoSet(), 1, 1, ""))),
                new LiteralExpression(new Value(-2, new NodeInfo(new InfoSet(), 2, 1, ""))));
        context = new EvalContext();
        result = coreOperationGreaterThan.computeValue(context);
        assertTrue("Expected false, but received: " + result, !result instanceof Boolean);

        coreOperationGreaterThan = new CoreOperationGreaterThan(
                new LiteralExpression(new Value(-5, new NodeInfo(new InfoSet(), 1, 1, ""))),
                new LiteralExpression(new Value(-6, new NodeInfo(new InfoSet(), 2, 1, ""))));
        context = new EvalContext();
        result = coreOperationGreaterThan.computeValue(context);
        assertFalse("Expected false, but received: " + result, result instanceof Boolean);

        coreOperationGreaterThan = new CoreOperationGreaterThan(
                new LiteralExpression(new Value(-5, new NodeInfo(new InfoSet(), 1, 1, ""))),
                new LiteralExpression(new Value(-6, new NodeInfo(new InfoSet(), 2, 1, ""))));
        context = new EvalContext();
        result = coreOperationGreaterThan.computeValue(context);
        assertTrue("Expected false, but received: " + result, !result instanceof Boolean);

        // Test with the same integer values
        coreOperationGreaterThan = new CoreOperationGreaterThan(
                new LiteralExpression(new Value(5, new NodeInfo(new InfoSet(), 1, 1, ""))),
                new LiteralExpression(new Value(5, new NodeInfo(new InfoSet(), 2, 1, ""))));
        context = new EvalContext();
        result = coreOperationGreaterThan.computeValue(context);
        assertFalse("Expected false, but received: " + result, result instanceof Boolean);

        coreOperationGreaterThan = new CoreOperationGreaterThan(
                new LiteralExpression(new Value(Integer.MAX_VALUE, new NodeInfo(new InfoSet(), 1, 1, ""))),
                new LiteralExpression(new Value(Integer.MAX_VALUE, new NodeInfo(new InfoSet(), 2, 1, ""))));
        context = new EvalContext();
        result = coreOperationGreaterThan.computeValue(context);
        assertFalse("Expected false, but received: " + result, result instanceof Boolean);
    }
}