package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new LiteralExpression(5.0), new LiteralExpression(3.0));
        InfoSet infoSet = InfoSetUtil.createSimpleInfoSet(1, 1);
        EvalContext context = new EvalContext(infoSet);

        assertTrue(coreOperationGreaterThan.computeValue(context).equals(Boolean.TRUE));

        infoSet = InfoSetUtil.createSimpleInfoSet(1, 2);
        context = new EvalContext(infoSet);

        assertFalse(coreOperationGreaterThan.computeValue(context).equals(Boolean.TRUE));

        infoSet = InfoSetUtil.createSimpleInfoSet(1, 3);
        context = new EvalContext(infoSet);

        assertTrue(coreOperationGreaterThan.computeValue(context).equals(Boolean.TRUE));

        infoSet = InfoSetUtil.createSimpleInfoSet(1, 5);
        context = new EvalContext(infoSet);

        assertTrue(coreOperationGreaterThan.computeValue(context).equals(Boolean.TRUE));

        infoSet = InfoSetUtil.createSimpleInfoSet(1, 5.1);
        context = new EvalContext(infoSet);

        assertTrue(coreOperationGreaterThan.computeValue(context).equals(Boolean.TRUE));

        infoSet = InfoSetUtil.createSimpleInfoSet(1, 5.0);
        context = new EvalContext(infoSet);

        assertTrue(coreOperationGreaterThan.computeValue(context).equals(Boolean.TRUE));

        infoSet = InfoSetUtil.createSimpleInfoSet(1, 4.9);
        context = new EvalContext(infoSet);

        assertFalse(coreOperationGreaterThan.computeValue(context).equals(Boolean.TRUE));

        infoSet = InfoSetUtil.createSimpleInfoSet(1, 4.95);
        context = new EvalContext(infoSet);

        assertFalse(coreOperationGreaterThan.computeValue(context).equals(Boolean.TRUE));

        infoSet = InfoSetUtil.createSimpleInfoSet(1, null);
        context = new EvalContext(infoSet);

        assertFalse(coreOperationGreaterThan.computeValue(context) instanceof Boolean);
    }
}