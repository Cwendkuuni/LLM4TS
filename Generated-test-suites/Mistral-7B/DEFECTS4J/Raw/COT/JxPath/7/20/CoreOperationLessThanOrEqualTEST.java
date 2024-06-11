package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.test.ExpressionTestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationLessThanOrEqualTest extends ExpressionTestCase {

    @Test
    public void testCoreOperationLessThanOrEqual_Double_Double() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
                new ConstantExpression(new Double(2.5)),
                new ConstantExpression(new Double(3.0)));
        EvalContext context = new EvalContext();
        context.setInfoSet(createInfoSet(new Double[]{2.5, 3.0}));
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());

        context = new EvalContext();
        context.setInfoSet(createInfoSet(new Double[]{1.5, 3.0}));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());

        context = new EvalContext();
        context.setInfoSet(createInfoSet(new Double[]{1.5, 1.5}));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());

        context = new EvalContext();
        context.setInfoSet(createInfoSet(new Double[]{Double.NaN, Double.NaN}));
        result = expression.computeValue(context);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqual_Null_Null() {
        new CoreOperationLessThanOrEqual(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqual_Null_Double() {
        new CoreOperationLessThanOrEqual(null, new ConstantExpression(new Double(2.5)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqual_Double_Null() {
        new CoreOperationLessThanOrEqual(new ConstantExpression(new Double(2.5)), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqual_String_Double() {
        new CoreOperationLessThanOrEqual(new ConstantExpression(new String("2.5")), new ConstantExpression(new Double(3.0)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanOrEqual_Double_String() {
        new CoreOperationLessThanOrEqual(new ConstantExpression(new Double(2.5)), new ConstantExpression(new String("3.0")));
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationLessThanOrEqual_InfoSet_InfoSet() {
        new CoreOperationLessThanOrEqual(new ConstantExpression(new InfoSet()), new ConstantExpression(new InfoSet()));
    }

    private static InfoSet createInfoSet(Object... values) {
        InfoSet infoSet = new InfoSet();
        infoSet.setItemArray(values);
        return infoSet;
    }
}