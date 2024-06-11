package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {
    private CoreOperationGreaterThanOrEqual operation;
    private Expression arg1;
    private Expression arg2;
    private EvalContext context;
    private InfoSet infoSet;

    @Before
    public void setUp() {
        arg1 = new CoreConstantExpression(new Double(10.5));
        arg2 = new CoreConstantExpression(new Double(5.5));
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext(null, null, null, null);
        infoSet = InfoSetUtil.createEmptyInfoSet();
    }

    @Test
    public void testComputeValueTrue() {
        context.setCurrentInfoSet(infoSet);
        context.setVariable("x", new Double(12.5));
        double result = operation.computeValue(context).doubleValue();
        assertTrue(result == Boolean.TRUE.booleanValue());
    }

    @Test
    public void testComputeValueFalse() {
        context.setCurrentInfoSet(infoSet);
        context.setVariable("x", new Double(5.5));
        double result = operation.computeValue(context).doubleValue();
        assertFalse(result == Boolean.TRUE.booleanValue());
    }

    @Test
    public void testComputeValueSameValue() {
        context.setCurrentInfoSet(infoSet);
        context.setVariable("x", new Double(10.5));
        double result = operation.computeValue(context).doubleValue();
        assertTrue(result == Boolean.TRUE.booleanValue());
    }

    @Test
    public void testComputeValueNull() {
        context.setCurrentInfoSet(infoSet);
        arg1 = new CoreConstantExpression(null);
        double result = operation.computeValue(context).doubleValue();
        assertFalse(result == Boolean.TRUE.booleanValue());
    }

    @Test
    public void testComputeValueNaN() {
        context.setCurrentInfoSet(infoSet);
        arg1 = new CoreConstantExpression(Double.NaN);
        double result = operation.computeValue(context).doubleValue();
        assertFalse(result == Boolean.TRUE.booleanValue());
    }

    @Test
    public void testComputeValueArg1NaN() {
        context.setCurrentInfoSet(infoSet);
        arg2 = new CoreConstantExpression(Double.NaN);
        double result = operation.computeValue(context).doubleValue();
        assertFalse(result == Boolean.TRUE.booleanValue());
    }

    @Test
    public void testComputeValueArg2NaN() {
        context.setCurrentInfoSet(infoSet);
        arg1 = new CoreConstantExpression(Double.NaN);
        double result = operation.computeValue(context).doubleValue();
        assertFalse(result == Boolean.TRUE.booleanValue());
    }
}