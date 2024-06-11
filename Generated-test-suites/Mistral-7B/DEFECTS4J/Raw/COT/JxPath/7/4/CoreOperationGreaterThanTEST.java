package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.PathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.expr.Expression;
import org.junit.Test;

/**
 * Test class for CoreOperationGreaterThan.
 */
public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan_TwoNumbers_BothPositive() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(0.5);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new PathContext(), infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThan_TwoNumbers_FirstNegative_SecondPositive() {
        Expression arg1 = new LiteralExpression(-1.0);
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new PathContext(), infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationGreaterThan_TwoNumbers_BothZero() {
        Expression arg1 = new LiteralExpression(0.0);
        Expression arg2 = new LiteralExpression(0.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new PathContext(), infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThan_OneArgumentIsNotNumber() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(1.5);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new PathContext(), infoSet);
        coreOperationGreaterThan.computeValue(context);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThan_SecondArgumentIsNotNumber() {
        Expression arg1 = new LiteralExpression(1.5);
        Expression arg2 = new LiteralExpression(1);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new PathContext(), infoSet);
        coreOperationGreaterThan.computeValue(context);
    }
}