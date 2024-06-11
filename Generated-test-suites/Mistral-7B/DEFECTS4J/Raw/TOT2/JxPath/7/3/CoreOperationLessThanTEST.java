package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.TestUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        Expression arg1 = new LiteralExpression(1.5);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = TestUtil.newContext();
        InfoSet infoSet = TestUtil.createSimpleInfoSet();
        context.setInfoSet(infoSet);
        Object result = coreOperationLessThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression(1.5);
        arg2 = new LiteralExpression(1.5);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression(1.5);
        arg2 = new LiteralExpression(1.0);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new LiteralExpression(1.5), new LiteralExpression(2.0));
        assertEquals(Boolean.FALSE, coreOperationLessThan.computeValue(TestUtil.newContext()));

        coreOperationLessThan = new CoreOperationLessThan(new LiteralExpression(1.5), new LiteralExpression(1.5));
        assertEquals(Boolean.FALSE, coreOperationLessThan.computeValue(TestUtil.newContext()));

        coreOperationLessThan = new CoreOperationLessThan(new LiteralExpression(1.5), new LiteralExpression(1.0));
        assertEquals(Boolean.TRUE, coreOperationLessThan.computeValue(TestUtil.newContext()));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new Expression[2]);
        assertEquals("<", coreOperationLessThan.getSymbol());
    }
}