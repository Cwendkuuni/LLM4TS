package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreExpression;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CoreOperationLessThanOrEqual.
 */
public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqual() {
        CoreExpression arg1 = new CoreNumber(1.0);
        CoreExpression arg2 = new CoreNumber(2.0);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new Path(), infoSet);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanOrEqualWithEqualValues() {
        CoreExpression arg1 = new CoreNumber(1.0);
        CoreExpression arg2 = new CoreNumber(1.0);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new Path(), infoSet);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanOrEqualWithDifferentSigns() {
        CoreExpression arg1 = new CoreNumber(-1.0);
        CoreExpression arg2 = new CoreNumber(1.0);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new Path(), infoSet);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLessThanOrEqualWithIncompatibleTypes() {
        CoreExpression arg1 = new CoreNumber(1.0);
        CoreExpression arg2 = new CoreString("2");
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new Path(), infoSet);
        expression.computeValue(context);
    }

    @Test
    public void testLessThanOrEqualWithNaN() {
        CoreExpression arg1 = new CoreNumber(Double.NaN);
        CoreExpression arg2 = new CoreNumber(1.0);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new Path(), infoSet);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testLessThanOrEqualWithNegativeZero() {
        CoreExpression arg1 = new CoreNumber(-0.0);
        CoreExpression arg2 = new CoreNumber(0.0);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new Path(), infoSet);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanOrEqualWithZero() {
        CoreExpression arg1 = new CoreNumber(0.0);
        CoreExpression arg2 = new CoreNumber(0.0);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new Path(), infoSet);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanOrEqualWithInfinity() {
        CoreExpression arg1 = new CoreNumber(Double.POSITIVE_INFINITY);
        CoreExpression arg2 = new CoreNumber(Double.POSITIVE_INFINITY);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new Path(), infoSet);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testLessThanOrEqualWithNegativeInfinity() {
        CoreExpression arg1 = new CoreNumber(Double.NEGATIVE_INFINITY);
        CoreExpression arg2 = new CoreNumber(Double.NEGATIVE_INFINITY);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new Path(), infoSet);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanOrEqualWithMaxValue() {
        CoreExpression arg1 = new CoreNumber(Double.MAX_VALUE);
        CoreExpression arg2 = new CoreNumber(Double.MAX_VALUE);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new Path(), infoSet);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThanOrEqualWithMinValue() {
        CoreExpression arg1 = new CoreNumber(Double.MIN_VALUE);
        CoreExpression arg2 = new CoreNumber(Double.MIN_VALUE);
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new Path(), infoSet);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}