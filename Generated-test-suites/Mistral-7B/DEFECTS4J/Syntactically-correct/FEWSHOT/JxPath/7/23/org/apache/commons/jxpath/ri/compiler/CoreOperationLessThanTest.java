package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.IntSequence;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThanWithNumericValues() {
        EvalContext context = new EvalContext();
        context.setInfoSet(InfoSetUtil.createInfoSet(new Object[] { new IntSequence(new int[] { 5, 3 }), Integer.valueOf(4) }));

        CoreOperationLessThan lessThan = new CoreOperationLessThan(new CoreConstantExpression(new IntSequence(new int[] { 5 })),
                new CoreConstantExpression(Integer.valueOf(4)));

        assertTrue(lessThan.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationLessThanWithNullAndNumericValues() {
        EvalContext context = new EvalContext();
        context.setInfoSet(InfoSetUtil.createInfoSet(new Object[] { new IntSequence(new int[] { 5, 3 }), Integer.valueOf(4), null }));

        CoreOperationLessThan lessThan = new CoreOperationLessThan(new CoreConstantExpression(new IntSequence(new int[] { 5 })),
                new CoreConstantExpression(Integer.valueOf(4)));

        assertFalse(lessThan.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationLessThanWithNumericValuesAndNaN() {
        EvalContext context = new EvalContext();
        context.setInfoSet(InfoSetUtil.createInfoSet(new Object[] { new IntSequence(new int[] { 5, 3 }), Double.NaN, Double.NaN }));

        CoreOperationLessThan lessThan = new CoreOperationLessThan(new CoreConstantExpression(new IntSequence(new int[] { 5 })),
                new CoreConstantExpression(Double.NaN));

        assertFalse(lessThan.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationLessThanWithNumericValuesAndNegativeValue() {
        EvalContext context = new EvalContext();
        context.setInfoSet(InfoSetUtil.createInfoSet(new Object[] { new IntSequence(new int[] { 5, 3 }), Integer.valueOf(-2), Integer.valueOf(4) }));

        CoreOperationLessThan lessThan = new CoreOperationLessThan(new CoreConstantExpression(new IntSequence(new int[] { 5 })),
                new CoreConstantExpression(Integer.valueOf(4)));

        assertFalse(lessThan.computeValue(context).booleanValue());

        CoreOperationLessThan lessThanNegative = new CoreOperationLessThan(new CoreConstantExpression(new IntSequence(new int[] { 5 })),
                new CoreConstantExpression(Integer.valueOf(-2)));

        assertTrue(lessThanNegative.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationLessThanWithStringValues() {
        EvalContext context = new EvalContext();
        context.setInfoSet(InfoSetUtil.createInfoSet(new Object[] { "5", "3", "4" }));

        CoreOperationLessThan lessThan = new CoreOperationLessThan(new CoreConstantExpression("5"), new CoreConstantExpression("4"));

        assertTrue(lessThan.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationLessThanWithNullAndStringValues() {
        EvalContext context = new EvalContext();
        context.setInfoSet(InfoSetUtil.createInfoSet(new Object[] { "5", "3", null, "4" }));

        CoreOperationLessThan lessThan = new CoreOperationLessThan(new CoreConstantExpression("5"), new CoreConstantExpression("4"));

        assertFalse(lessThan.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationLessThanWithStringValuesAndComparableValues() {
        EvalContext context = new EvalContext();
        context.setInfoSet(InfoSetUtil.createInfoSet(new Object[] { "5", "3", "4.5" }));

        CoreOperationLessThan lessThan = new CoreOperationLessThan(new CoreConstantExpression("5"), new CoreConstantExpression(Double.valueOf(4.5)));

        assertTrue(lessThan.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationLessThanWithStringValuesAndNull() {
        EvalContext context = new EvalContext();
        context.setInfoSet(InfoSetUtil.createInfoSet(new Object[] { "5", null, "4" }));

        CoreOperationLessThan lessThan = new CoreOperationLessThan(new CoreConstantExpression("5"), new CoreConstantExpression(null));

        assertFalse(lessThan.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationLessThanWithStringValuesAndStringValues() {
        EvalContext context = new EvalContext();
        context.setInfoSet(InfoSetUtil.createInfoSet(new Object[] { "5", "3", "4" }));

        CoreOperationLessThan lessThan = new CoreOperationLessThan(new CoreConstantExpression("5"), new CoreConstantExpression("4"));

        assertTrue(lessThan.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationLessThanWithDifferentTypes() {
        EvalContext context = new EvalContext();
        context.setInfoSet(InfoSetUtil.createInfoSet(new Object[] { new IntSequence(new int[] { 5 }), Double.valueOf(3), Integer.valueOf(4) }));

        CoreOperationLessThan lessThan = new CoreOperationLessThan(new CoreConstantExpression(new IntSequence(new int[] { 5 })),
                new CoreConstantExpression(Double.valueOf(3)));

        assertFalse(lessThan.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationLessThanWithMixedTypes() {
        EvalContext context = new EvalContext();
        context.setInfoSet(InfoSetUtil.createInfoSet(new Object[] { "5", Integer.valueOf(3), Double.valueOf(4) }));

        CoreOperationLessThan lessThan = new CoreOperationLessThan(new CoreConstantExpression("5"), new CoreConstantExpression(Integer.valueOf(3)));

        assertTrue(lessThan.computeValue(context).booleanValue());

        CoreOperationLessThan lessThanMixed = new CoreOperationLessThan(new CoreConstantExpression("5"), new CoreConstantExpression(Double.valueOf(4)));

        assertFalse(lessThanMixed.computeValue(context).booleanValue());
    }
}