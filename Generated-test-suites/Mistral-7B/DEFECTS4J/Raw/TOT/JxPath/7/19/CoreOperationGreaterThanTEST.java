package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.Test;
import org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testGreaterThanPositive() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(0.5);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new NodeInfo(new InfoSet()));
        Boolean result = (Boolean) operation.computeValue(context);
        assertTrue(result.booleanValue());
    }

    @Test
    public void testGreaterThanZeroAndZero() {
        Expression arg1 = new LiteralExpression(0.0);
        Expression arg2 = new LiteralExpression(0.0);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new NodeInfo(new InfoSet()));
        Boolean result = (Boolean) operation.computeValue(context);
        assertFalse(result.booleanValue());
    }

    @Test
    public void testGreaterThanNegativeAndPositive() {
        Expression arg1 = new LiteralExpression(-1.0);
        Expression arg2 = new LiteralExpression(1.5);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new NodeInfo(new InfoSet()));
        Boolean result = (Boolean) operation.computeValue(context);
        assertFalse(result.booleanValue());
    }

    @Test(expected = ClassCastException.class)
    public void testGreaterThanStringAndDouble() {
        Expression arg1 = new LiteralExpression("5");
        Expression arg2 = new LiteralExpression(1.5);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new NodeInfo(new InfoSet()));
        operation.computeValue(context);
    }

    @Test(expected = ClassCastException.class)
    public void testGreaterThanDoubleAndString() {
        Expression arg1 = new LiteralExpression(1.5);
        Expression arg2 = new LiteralExpression("5");
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new NodeInfo(new InfoSet()));
        operation.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGreaterThanNullArguments() {
        Expression arg1 = null;
        Expression arg2 = new LiteralExpression(1.5);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new NodeInfo(new InfoSet()));
        operation.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGreaterThanNullArgument1() {
        Expression arg1 = new LiteralExpression(1.5);
        Expression arg2 = null;
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new NodeInfo(new InfoSet()));
        operation.computeValue(context);
    }
}