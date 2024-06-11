package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for CoreOperationGreaterThanOrEqual class.
 */
public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(5.5)),
                new ConstantExpression(new Double(6.5)));

        EvalContext context = new EvalContext(new InfoSet());
        context.pushNode(new NodeInfo(1, "root", null, null));

        Object result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithFalseResult() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(5.5)),
                new ConstantExpression(new Double(6.5)));

        EvalContext context = new EvalContext(new InfoSet());
        context.pushNode(new NodeInfo(1, "root", null, null));

        Object result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanOrEqualWithInvalidArgumentType() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Integer(5)),
                new ConstantExpression(new Double(6.5)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanOrEqualWithNullArguments() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(null, null);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithZeroAndPositiveNumber() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(0.0)),
                new ConstantExpression(new Double(5.5)));

        EvalContext context = new EvalContext(new InfoSet());
        context.pushNode(new NodeInfo(1, "root", null, null));

        Object result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNegativeNumberAndPositiveNumber() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(-1.0)),
                new ConstantExpression(new Double(5.5)));

        EvalContext context = new EvalContext(new InfoSet());
        context.pushNode(new NodeInfo(1, "root", null, null));

        Object result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithEqualNumbers() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(5.5)),
                new ConstantExpression(new Double(5.5)));

        EvalContext context = new EvalContext(new InfoSet());
        context.pushNode(new NodeInfo(1, "root", null, null));

        Object result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithEqualNumbersAndNaN() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(new Double(5.5)),
                new ConstantExpression(new Double(Double.NaN)));

        EvalContext context = new EvalContext(new InfoSet());
        context.pushNode(new NodeInfo(1, "root", null, null));

        Object result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
}