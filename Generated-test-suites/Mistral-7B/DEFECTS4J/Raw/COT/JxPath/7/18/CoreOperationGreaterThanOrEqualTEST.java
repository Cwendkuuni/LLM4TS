package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreExpression;

/**
 * Test class for CoreOperationGreaterThanOrEqual.
 */
public class CoreOperationGreaterThanOrEqualTest {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationGreaterThanOrEqual_basic() {
        CoreExpression arg1 = new CoreNumberExpression(new Double(5.0));
        CoreExpression arg2 = new CoreNumberExpression(new Double(5.0));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);
        assertTrue("5 >= 5 should be true", result instanceof Boolean && ((Boolean) result).booleanValue());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationGreaterThanOrEqual_false() {
        CoreExpression arg1 = new CoreNumberExpression(new Double(5.0));
        CoreExpression arg2 = new CoreNumberExpression(new Double(6.0));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);
        assertFalse("5 >= 6 should be false", result instanceof Boolean && !((Boolean) result).booleanValue());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanOrEqual_args_null() {
        CoreExpression arg1 = null;
        CoreExpression arg2 = new CoreNumberExpression(new Double(6.0));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanOrEqual_args_not_number() {
        CoreExpression arg1 = new CoreStringExpression("5");
        CoreExpression arg2 = new CoreNumberExpression(new Double(6.0));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationGreaterThanOrEqual_edge_cases() {
        CoreExpression arg1 = new CoreNumberExpression(Double.MIN_VALUE);
        CoreExpression arg2 = new CoreNumberExpression(Double.MIN_VALUE);
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);
        assertTrue("MinDouble >= MinDouble should be true", result instanceof Boolean && ((Boolean) result).booleanValue());

        arg1 = new CoreNumberExpression(Double.MIN_VALUE);
        arg2 = new CoreNumberExpression(Double.MAX_VALUE);
        expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext(new InfoSet());
        result = expression.computeValue(context);
        assertFalse("MinDouble >= MaxDouble should be false", result instanceof Boolean && !((Boolean) result).booleanValue());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqual_args_non_comparable() {
        CoreExpression arg1 = new CoreStringExpression("5");
        CoreExpression arg2 = new CoreNumberExpression(new Double(6.0));
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqual_args_non_number() {
        CoreExpression arg1 = new CoreStringExpression("5");
        CoreExpression arg2 = new CoreStringExpression("6");
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }
}