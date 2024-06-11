package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.test.TestUtils;
import org.junit.Test;

/**
 * Test class for CoreOperationLessThan.
 */
public class CoreOperationLessThanTest {

    @Test
    public void testLessThan() {
        Expression arg1 = TestUtils.createNumberExpression(1.0);
        Expression arg2 = TestUtils.createNumberExpression(2.0);
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = TestUtils.createContext();

        Boolean result = (Boolean) op.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testLessThanFalse() {
        Expression arg1 = TestUtils.createNumberExpression(2.0);
        Expression arg2 = TestUtils.createNumberExpression(1.0);
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = TestUtils.createContext();

        Boolean result = (Boolean) op.computeValue(context);
        assertFalse(result);
    }

    @Test
    public void testLessThanZeroAndPositive() {
        Expression arg1 = TestUtils.createNumberExpression(-1.0);
        Expression arg2 = TestUtils.createNumberExpression(1.0);
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = TestUtils.createContext();

        Boolean result = (Boolean) op.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testLessThanZeroAndZero() {
        Expression arg1 = TestUtils.createNumberExpression(-1.0);
        Expression arg2 = TestUtils.createNumberExpression(0.0);
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = TestUtils.createContext();

        Boolean result = (Boolean) op.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testLessThanZeroAndNegative() {
        Expression arg1 = TestUtils.createNumberExpression(-1.0);
        Expression arg2 = TestUtils.createNumberExpression(-2.0);
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = TestUtils.createContext();

        Boolean result = (Boolean) op.computeValue(context);
        assertFalse(result);
    }

    @Test
    public void testLessThanNullAndNumber() {
        Expression arg1 = TestUtils.createNullExpression();
        Expression arg2 = TestUtils.createNumberExpression(1.0);
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = TestUtils.createContext();

        Boolean result = (Boolean) op.computeValue(context);
        assertFalse(result);
    }

    @Test
    public void testLessThanNullAndNull() {
        Expression arg1 = TestUtils.createNullExpression();
        Expression arg2 = TestUtils.createNullExpression();
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = TestUtils.createContext();

        Boolean result = (Boolean) op.computeValue(context);
        assertTrue(result);
    }
}