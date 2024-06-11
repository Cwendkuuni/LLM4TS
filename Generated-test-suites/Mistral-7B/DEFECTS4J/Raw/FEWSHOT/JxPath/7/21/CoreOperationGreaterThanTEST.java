package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new ConstantExpression(1.0), new ConstantExpression(0.5));
        EvalContext context = new EvalContext(new InfoSet());
        assertTrue(operation.computeValue(context).booleanValue());

        operation = new CoreOperationGreaterThan(new ConstantExpression(1.0), new ConstantExpression(1.5));
        assertFalse(operation.computeValue(context).booleanValue());

        operation = new CoreOperationGreaterThan(new ConstantExpression(10.0), new ConstantExpression(5.0));
        assertTrue(operation.computeValue(context).booleanValue());

        operation = new CoreOperationGreaterThan(new ConstantExpression("10"), new ConstantExpression("5"));
        assertTrue(operation.computeValue(context).booleanValue());

        operation = new CoreOperationGreaterThan(new ConstantExpression("10.5"), new ConstantExpression("5.5"));
        assertTrue(operation.computeValue(context).booleanValue());

        operation = new CoreOperationGreaterThan(new ConstantExpression(new Integer(10)), new ConstantExpression(new Integer(5)));
        assertTrue(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanWithNull() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new ConstantExpression(1.0), null);
        EvalContext context = new EvalContext(new InfoSet());
        assertNull(operation.computeValue(context));

        operation = new CoreOperationGreaterThan(null, new ConstantExpression(1.0));
        assertNull(operation.computeValue(context));

        operation = new CoreOperationGreaterThan(null, null);
        assertNull(operation.computeValue(context));
    }

    @Test
    public void testCoreOperationGreaterThanWithNonNumeric() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new ConstantExpression("10"), new ConstantExpression("5"));
        EvalContext context = new EvalContext(new InfoSet());
        assertTrue(operation.computeValue(context).booleanValue());

        operation = new CoreOperationGreaterThan(new ConstantExpression("10.5"), new ConstantExpression("5.5"));
        assertTrue(operation.computeValue(context).booleanValue());

        operation = new CoreOperationGreaterThan(new ConstantExpression("10"), new ConstantExpression("5.5"));
        assertFalse(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanWithNaN() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new ConstantExpression(Double.NaN), new ConstantExpression(Double.NaN));
        EvalContext context = new EvalContext(new InfoSet());
        assertFalse(operation.computeValue(context).booleanValue());

        operation = new CoreOperationGreaterThan(new ConstantExpression(Double.NaN), new ConstantExpression(1.0));
        assertFalse(operation.computeValue(context).booleanValue());

        operation = new CoreOperationGreaterThan(new ConstantExpression(1.0), new ConstantExpression(Double.NaN));
        assertTrue(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanWithInfinity() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new ConstantExpression(Double.POSITIVE_INFINITY), new ConstantExpression(Double.POSITIVE_INFINITY));
        EvalContext context = new EvalContext(new InfoSet());
        assertTrue(operation.computeValue(context).booleanValue());

        operation = new CoreOperationGreaterThan(new ConstantExpression(Double.POSITIVE_INFINITY), new ConstantExpression(1.0));
        assertTrue(operation.computeValue(context).booleanValue());

        operation = new CoreOperationGreaterThan(new ConstantExpression(1.0), new CoreOperationGreaterThan(new ConstantExpression(Double.POSITIVE_INFINITY), new ConstantExpression(1.0)));
        assertFalse(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanWithNegativeInfinity() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new ConstantExpression(Double.NEGATIVE_INFINITY), new ConstantExpression(Double.NEGATIVE_INFINITY));
        EvalContext context = new EvalContext(new InfoSet());
        assertFalse(operation.computeValue(context).booleanValue());

        operation = new CoreOperationGreaterThan(new ConstantExpression(Double.NEGATIVE_INFINITY), new ConstantExpression(1.0));
        assertTrue(operation.computeValue(context).booleanValue());

        operation = new CoreOperationGreaterThan(new ConstantExpression(1.0), new CoreOperationGreaterThan(new ConstantExpression(1.0), new ConstantExpression(Double.NEGATIVE_INFINITY)));
        assertFalse(operation.computeValue(context).booleanValue());
    }
}