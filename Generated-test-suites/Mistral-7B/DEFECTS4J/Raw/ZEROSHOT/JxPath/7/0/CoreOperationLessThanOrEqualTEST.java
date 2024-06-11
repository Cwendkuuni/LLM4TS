package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new ConstantExpression(new Double(5.0)),
                new ConstantExpression(new Double(10.0)));

        EvalContext context = new EvalContext();
        context.setCurrentInfoSet(new InfoSet());

        Boolean result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result);

        context.setCurrentInfoSet(new InfoSet(InfoSetUtil.createDoubleNode(context, 15.0)));
        result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);
        assertFalse(result);

        context.setCurrentInfoSet(new InfoSet(InfoSetUtil.createDoubleNode(context, 5.0)));
        result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result);

        context.setCurrentInfoSet(new InfoSet(InfoSetUtil.createDoubleNode(context, 5.0)));
        Expression arg1 = new ConstantExpression(new Double(5.0));
        Expression arg2 = new ConstantExpression(new Double(5.0));
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqualNew = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = (Boolean) coreOperationLessThanOrEqualNew.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new ConstantExpression(new Double(5.0)),
                new ConstantExpression(new Double(10.0)));
        assertEquals("<=", coreOperationLessThanOrEqual.getSymbol());
    }
}