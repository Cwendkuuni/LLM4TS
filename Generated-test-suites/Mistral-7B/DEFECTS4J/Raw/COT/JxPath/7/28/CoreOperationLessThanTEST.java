package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;

/**
 * Test cases for CoreOperationLessThan class.
 */
public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThanWithTwoNumbers() {
        Expression arg1 = new CoreNumberExpression(1.0);
        Expression arg2 = new CoreNumberExpression(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testCoreOperationLessThanWithTwoNumbersFalse() {
        Expression arg1 = new CoreNumberExpression(2.0);
        Expression arg2 = new CoreNumberExpression(1.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanWithOneArgument() {
        Expression arg1 = new CoreNumberExpression(1.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanWithTwoNullArguments() {
        Expression arg1 = new CoreNullExpression();
        Expression arg2 = new CoreNullExpression();
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
    }

    @Test
    public void testCoreOperationLessThanWithTwoNonNumbers() {
        Expression arg1 = new CoreStringExpression("1");
        Expression arg2 = new CoreStringExpression("2");
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertFalse(result);
    }

    @Test
    public void testCoreOperationLessThanWithTwoNegativeNumbers() {
        Expression arg1 = new CoreNumberExpression(-1.0);
        Expression arg2 = new CoreNumberExpression(-2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testCoreOperationLessThanWithZeroAndANumber() {
        Expression arg1 = new CoreNumberExpression(0.0);
        Expression arg2 = new CoreNumberExpression(1.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testCoreOperationLessThanWithANumberAndZero() {
        Expression arg1 = new CoreNumberExpression(1.0);
        Expression arg2 = new CoreNumberExpression(0.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testCoreOperationLessThanWithTwoBigNumbers() {
        Expression arg1 = new CoreNumberExpression(Double.MAX_VALUE);
        Expression arg2 = new CoreNumberExpression(Double.MAX_VALUE + 1.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);
    }
}