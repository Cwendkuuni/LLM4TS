package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue_whenArg1IsGreaterThanArg2_thenReturnTrue() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(new Double(5.0)), new CoreConstantExpression(new Double(3.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_whenArg1IsLessThanOrEqualArg2_thenReturnFalse() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(new Double(3.0)), new CoreConstantExpression(new Double(5.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test
    public void testComputeValue_whenArg1IsNull_thenArg2IsNull_thenReturnNull() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(null), new CoreConstantExpression(null));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testComputeValue_whenArg1IsNotNull_thenArg2IsNull_thenReturnFalse() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(new Double(5.0)), new CoreConstantExpression(null));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test
    public void testComputeValue_whenArg1IsNaN_thenArg2IsNotNaN_thenReturnFalse() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(Double.NaN), new CoreConstantExpression(new Double(5.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test
    public void testComputeValue_whenArg1IsNotNaN_thenArg2IsNaN_thenReturnFalse() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(new Double(5.0)), new CoreConstantExpression(Double.NaN));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test
    public void testComputeValue_whenArg1IsNaN_thenArg2IsNaN_thenReturnNull() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(Double.NaN), new CoreConstantExpression(Double.NaN));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testComputeValue_whenArg1IsPositiveInfinite_thenArg2IsFinite_thenReturnFalse() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(Double.POSITIVE_INFINITY), new CoreConstantExpression(new Double(5.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test
    public void testComputeValue_whenArg1IsFinite_thenArg1IsPositiveInfinite_thenReturnFalse() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(new Double(5.0)), new CoreConstantExpression(Double.POSITIVE_INFINITY));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test
    public void testComputeValue_whenArg1IsFinite_thenArg2IsNaN_thenReturnFalse() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(new Double(5.0)), new CoreConstantExpression(Double.NaN));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test
    public void testComputeValue_whenArg1IsNaN_thenArg2IsFinite_thenReturnFalse() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(Double.NaN), new CoreConstantExpression(new Double(5.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test
    public void testComputeValue_whenArg1IsNegativeInfinite_thenArg2IsFinite_thenReturnTrue() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(Double.NEGATIVE_INFINITY), new CoreConstantExpression(new Double(3.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_whenArg1IsFinite_thenArg1IsNegativeInfinite_thenReturnFalse() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(new Double(3.0)), new CoreConstantExpression(Double.NEGATIVE_INFINITY));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test
    public void testComputeValue_whenArg1IsFinite_thenArg2IsNegativeInfinite_thenReturnFalse() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(new Double(3.0)), new CoreConstantExpression(Double.NEGATIVE_INFINITY));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test
    public void testComputeValue_whenArg1IsNaN_thenArg2IsNaN_thenReturnNull() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(Double.NaN), new CoreConstantExpression(Double.NaN));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testComputeValue_whenArg1IsPositiveFinite_thenArg2IsPositiveFinite_thenReturnTrue_whenArg1GreaterThanArg2() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(new Double(6.0)), new CoreConstantExpression(new Double(5.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_whenArg1IsPositiveFinite_thenArg2IsPositiveFinite_thenReturnFalse_whenArg1IsLessThanOrEqualArg2() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(new Double(5.0)), new CoreConstantExpression(new Double(6.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test
    public void testComputeValue_whenArg1IsNegativeFinite_thenArg2IsNegativeFinite_thenReturnTrue_whenArg1GreaterThanArg2() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(new Double(-6.0)), new CoreConstantExpression(new Double(-5.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_whenArg1IsNegativeFinite_thenArg2IsNegativeFinite_thenReturnFalse_whenArg1IsLessThanOrEqualArg2() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(new Double(-5.0)), new CoreConstantExpression(new Double(-6.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test
    public void testComputeValue_whenArg1IsFinite_thenArg2IsNaN_thenReturnFalse() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(new Double(5.0)), new CoreConstantExpression(Double.NaN));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test
    public void testComputeValue_whenArg1IsNaN_thenArg2IsFinite_thenReturnFalse() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(Double.NaN), new CoreConstantExpression(new Double(5.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test
    public void testComputeValue_whenArg1IsPositiveInfinite_thenArg2IsFinite_thenReturnFalse() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(Double.POSITIVE_INFINITY), new CoreConstantExpression(new Double(5.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test
    public void testComputeValue_whenArg1IsFinite_thenArg2IsPositiveInfinite_thenReturnFalse() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(new Double(5.0)), new CoreConstantExpression(Double.POSITIVE_INFINITY));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test
    public void testComputeValue_whenArg1IsNaN_thenArg2IsNaN_thenReturnNull() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(Double.NaN), new CoreConstantExpression(Double.NaN));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testComputeValue_whenArg1IsPositiveFinite_thenArg2IsNegativeFinite_thenReturnTrue() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(new Double(6.0)), new CoreConstantExpression(new Double(-5.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_whenArg1IsPositiveFinite_thenArg2IsNegativeFinite_thenReturnFalse() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(new Double(5.0)), new CoreConstantExpression(new Double(-6.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test
    public void testComputeValue_whenArg1IsNegativeFinite_thenArg2IsPositiveFinite_thenReturnFalse() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(new Double(-5.0)), new CoreConstantExpression(new Double(6.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse(result == Boolean.FALSE);
    }

    @Test
    public void testComputeValue_whenArg1IsNegativeFinite_thenArg2IsPositiveFinite_thenReturnTrue() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new CoreConstantExpression(new Double(-5.0)), new CoreConstantExpression(new Double(5.0)));
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}