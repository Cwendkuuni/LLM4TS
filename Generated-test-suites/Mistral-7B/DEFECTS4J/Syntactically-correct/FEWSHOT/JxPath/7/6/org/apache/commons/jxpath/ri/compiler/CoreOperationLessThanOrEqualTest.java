package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new ConstantExpression(1.0), new ConstantExpression(2.0));

        assertTrue(op.computeValue(new EvalContext()).booleanValue());

        op = new CoreOperationLessThanOrEqual(new ConstantExpression(1.0), new ConstantExpression(1.0));

        assertTrue(op.computeValue(new EvalContext()).booleanValue());

        op = new CoreOperationLessThanOrEqual(new ConstantExpression(2.0), new ConstantExpression(1.0));

        assertFalse(op.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationLessThanOrEqualWithNegativeValues() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new CoreOperationNegateExpression(new ConstantExpression(-2.0)),
                new ConstantExpression(-1.0));

        assertTrue(op.computeValue(new EvalContext()).booleanValue());

        op = new CoreOperationLessThanOrEqual(new ConstantExpression(-1.0),
                new CoreOperationNegateExpression(new ConstantExpression(-2.0)));

        assertFalse(op.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationLessThanOrEqualWithMixedTypes() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new ConstantExpression(1.0), new ConstantExpression(2.0L));

        assertTrue(op.computeValue(new EvalContext()).booleanValue());

        op = new CoreOperationLessThanOrEqual(new ConstantExpression("1.0"),
                new ConstantExpression(2.0));

        assertTrue(op.computeValue(new EvalContext()).booleanValue());

        op = new CoreOperationLessThanOrEqual(new ConstantExpression("1.0"),
                new ConstantExpression("2.0"));

        assertTrue(op.computeValue(new EvalContext()).booleanValue());

        op = new CoreOperationLessThanOrEqual(new ConstantExpression(1.0),
                new ConstantExpression("2.0"));

        assertFalse(op.computeValue(new EvalContext()).booleanValue());

        op = new CoreOperationLessThanOrEqual(new ConstantExpression("1.0"),
                new ConstantExpression(1.0L));

        assertTrue(op.computeValue(new EvalContext()).booleanValue());

        op = new CoreOperationLessThanOrEqual(new ConstantExpression("1.0"),
                new ConstantExpression('2'));

        assertFalse(op.computeValue(new EvalContext()).booleanValue());
    }
}