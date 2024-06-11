package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.info.InfoSet;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreNumberExpression(new Double(5.0)),
                new CoreNumberExpression(new Double(3.0)));

        EvalContext context = new EvalContext(InfoSet.EMPTY_NODE_INFOSET);

        Object result = coreOperationGreaterThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext(InfoSet.EMPTY_NODE_INFOSET);
        CoreOperationGreaterThan coreOperationGreaterThan2 = new CoreOperationGreaterThan(
                new CoreNumberExpression(new Double(3.0)),
                new CoreNumberExpression(new Double(5.0)));

        result = coreOperationGreaterThan2.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertFalse((Boolean) result);

        context = new EvalContext(InfoSet.EMPTY_NODE_INFOSET);
        CoreOperationGreaterThan coreOperationGreaterThan3 = new CoreOperationGreaterThan(
                new CoreNumberExpression(new Double(5.0)),
                new CoreNumberExpression(new Double(5.0)));

        result = coreOperationGreaterThan3.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext(InfoSet.EMPTY_NODE_INFOSET);
        CoreOperationGreaterThan coreOperationGreaterThan4 = new CoreOperationGreaterThan(
                new CoreNumberExpression(new Double(0.0)),
                new CoreNumberExpression(new Double(0.0)));

        result = coreOperationGreaterThan4.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
}