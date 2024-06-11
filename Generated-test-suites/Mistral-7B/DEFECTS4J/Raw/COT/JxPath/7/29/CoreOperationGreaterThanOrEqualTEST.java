package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for CoreOperationGreaterThanOrEqual class.
 */
public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(5.0),
                new LiteralExpression(5.0));
        Assert.assertTrue(operation.computeValue(new EvalContext(new InfoSet())).equals(Boolean.TRUE));

        operation = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(5.0),
                new LiteralExpression(4.0));
        Assert.assertTrue(operation.computeValue(new EvalContext(new InfoSet())).equals(Boolean.TRUE));

        operation = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(4.0),
                new LiteralExpression(5.0));
        Assert.assertFalse(operation.computeValue(new EvalContext(new InfoSet())).equals(Boolean.TRUE));
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqualWithInvalidArgs() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new CoreOperationRelationalExpression(),
                new LiteralExpression(5.0));
        operation.computeValue(new EvalContext(new InfoSet()));
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqualWithInvalidArgs2() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(5.0),
                new CoreOperationRelationalExpression());
        operation.computeValue(new EvalContext(new InfoSet()));
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNaN() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(Double.NaN),
                new LiteralExpression(Double.NaN));
        Assert.assertTrue(operation.computeValue(new EvalContext(new InfoSet())).equals(Boolean.TRUE));
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNegativeNumbers() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(-3.0),
                new LiteralExpression(-2.0));
        Assert.assertTrue(operation.computeValue(new EvalContext(new InfoSet())).equals(Boolean.TRUE));

        operation = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(-3.0),
                new LiteralExpression(-4.0));
        Assert.assertFalse(operation.computeValue(new EvalContext(new InfoSet())).equals(Boolean.TRUE));
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithZero() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(0.0),
                new LiteralExpression(0.0));
        Assert.assertTrue(operation.computeValue(new EvalContext(new InfoSet())).equals(Boolean.TRUE));

        operation = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(0.0),
                new LiteralExpression(-1.0));
        Assert.assertTrue(operation.computeValue(new EvalContext(new InfoSet())).equals(Boolean.FALSE));

        operation = new CoreOperationGreaterThanOrEqual(
                new LiteralExpression(-1.0),
                new LiteralExpression(0.0));
        Assert.assertFalse(operation.computeValue(new EvalContext(new InfoSet())).equals(Boolean.FALSE));
    }
}