package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.expression.Expression;

public class CoreOperationGreaterThanTest {

    @SuppressWarnings("unchecked")
    private Expression createCoreOperationGreaterThan(Expression arg1, Expression arg2) {
        return new CoreOperationGreaterThan(arg1, arg2);
    }

    // Test case for public constructor
    @org.junit.Test
    public void testCoreOperationGreaterThanConstructor() {
        Expression arg1 = null;
        Expression arg2 = null;
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertNotNull(coreOperationGreaterThan);
        assertArrayEquals(new Expression[] { arg1, arg2 }, coreOperationGreaterThan.args);
    }

    // Test case for computeValue method with positive numbers
    @org.junit.Test
    public void testCoreOperationGreaterThanPositiveNumbers() {
        Expression arg1 = new CoreConstantExpression(3.0);
        Expression arg2 = new CoreConstantExpression(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = createCoreOperationGreaterThan(arg1, arg2);
        assertTrue(coreOperationGreaterThan.computeValue(new EvalContext()).equals(Boolean.TRUE));
    }

    // Test case for computeValue method with negative numbers
    @org.junit.Test
    public void testCoreOperationGreaterThanNegativeNumbers() {
        Expression arg1 = new CoreConstantExpression(-1.0);
        Expression arg2 = new CoreConstantExpression(-2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = createCoreOperationGreaterThan(arg1, arg2);
        assertTrue(coreOperationGreaterThan.computeValue(new EvalContext()).equals(Boolean.FALSE));
    }

    // Test case for computeValue method with same number
    @org.junit.Test
    public void testCoreOperationGreaterThanSameNumber() {
        Expression arg1 = new CoreConstantExpression(2.0);
        Expression arg2 = new CoreConstantExpression(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = createCoreOperationGreaterThan(arg1, arg2);
        assertFalse(coreOperationGreaterThan.computeValue(new EvalContext()).equals(Boolean.TRUE));
        assertFalse(coreOperationGreaterThan.computeValue(new EvalContext()).equals(Boolean.FALSE));
    }

    // Test case for computeValue method with null arguments
    @org.junit.Test(expected = NullPointerException.class)
    public void testCoreOperationGreaterThanNullArguments() {
        Expression arg1 = null;
        Expression arg2 = null;
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        coreOperationGreaterThan.computeValue(new EvalContext());
    }

    // Test case for computeValue method with non-numeric arguments
    @org.junit.Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanNonNumericArguments() {
        Expression arg1 = new CoreStringConstantExpression("3");
        Expression arg2 = new CoreStringConstantExpression("2");
        CoreOperationGreaterThan coreOperationGreaterThan = createCoreOperationGreaterThan(arg1, arg2);
        coreOperationGreaterThan.computeValue(new EvalContext());
    }

    // Test case for computeValue method with one argument null
    @org.junit.Test(expected = NullPointerException.class)
    public void testCoreOperationGreaterThanNullArg() {
        Expression arg1 = new CoreConstantExpression(2.0);
        Expression arg2 = null;
        CoreOperationGreaterThan coreOperationGreaterThan = createCoreOperationGreaterThan(arg1, arg2);
        coreOperationGreaterThan.computeValue(new EvalContext());
    }

    // Test case for computeValue method with one argument not a number
    @org.junit.Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanArgNotNumber() {
        Expression arg1 = new CoreStringConstantExpression("3");
        Expression arg2 = new CoreConstantExpression(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = createCoreOperationGreaterThan(arg1, arg2);
        coreOperationGreaterThan.computeValue(new EvalContext());
    }
}