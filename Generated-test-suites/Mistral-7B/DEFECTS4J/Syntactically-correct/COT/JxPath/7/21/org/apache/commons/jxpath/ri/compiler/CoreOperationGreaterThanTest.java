package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;

/**
 * CoreOperationGreaterThanTest.java - JUnit test file for CoreOperationGreaterThan class
 */
public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan_DoublePositiveValues() {
        Expression arg1 = new ConstantExpression(new Double(5.0));
        Expression arg2 = new ConstantExpression(new Double(3.0));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testCoreOperationGreaterThan_DoubleNegativeValues() {
        Expression arg1 = new ConstantExpression(new Double(-3.0));
        Expression arg2 = new ConstantExpression(new Double(-5.0));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertFalse(result);
    }

    @Test
    public void testCoreOperationGreaterThan_DoublePositiveValueAndZero() {
        Expression arg1 = new ConstantExpression(new Double(5.0));
        Expression arg2 = new ConstantExpression(new Double(0.0));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertTrue(result);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThan_MismatchedTypes() {
        Expression arg1 = new ConstantExpression(new Integer(5));
        Expression arg2 = new ConstantExpression(new Double(3.0));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationGreaterThan.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThan_NullArgument() {
        Expression arg1 = new ConstantExpression(null);
        Expression arg2 = new ConstantExpression(new Double(3.0));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationGreaterThan.computeValue(context);
    }
}