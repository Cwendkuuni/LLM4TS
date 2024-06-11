package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        Expression arg1 = new ConstantExpression(1.0);
        Expression arg2 = new ConstantExpression(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanWithNullArgs() {
        Expression arg1 = null;
        Expression arg2 = new ConstantExpression(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testCoreOperationLessThanWithNullArg2() {
        Expression arg1 = new ConstantExpression(1.0);
        Expression arg2 = null;
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testCoreOperationLessThanWithInvalidDataTypes() {
        Expression arg1 = new StringExpression("5");
        Expression arg2 = new ConstantExpression(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testCoreOperationLessThanWithEqualValues() {
        Expression arg1 = new ConstantExpression(1.0);
        Expression arg2 = new ConstantExpression(1.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testCoreOperationLessThanWithInvalidDataTypesAndEqualValues() {
        Expression arg1 = new StringExpression("5");
        Expression arg2 = new StringExpression("5");
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testCoreOperationLessThanWithInvalidDataTypesAndDifferentValues() {
        Expression arg1 = new StringExpression("5");
        Expression arg2 = new ConstantExpression(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testCoreOperationLessThanWithValidDataTypesAndDifferentValues() {
        Expression arg1 = new ConstantExpression(1.0);
        Expression arg2 = new ConstantExpression(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanWithZeroAndNegativeNumber() {
        Expression arg1 = new ConstantExpression(0.0);
        Expression arg2 = new ConstantExpression(-1.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationLessThanWithZeroAndZero() {
        Expression arg1 = new ConstantExpression(0.0);
        Expression arg2 = new ConstantExpression(0.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertFalse(result instanceof Boolean);
    }
}