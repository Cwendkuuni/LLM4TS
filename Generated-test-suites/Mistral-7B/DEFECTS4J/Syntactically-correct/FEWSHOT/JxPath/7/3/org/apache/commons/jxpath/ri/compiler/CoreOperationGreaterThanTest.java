package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThanWithNumericValues() {
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Info getItem(int index) {
                return new NodeInfo(new Object() {}, 1, 1, 1, 1, null);
            }
        });

        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(5.5)),
                new CoreConstantExpression(new Double(3.3)));

        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 1;
            }

            @Override
            public Info getItem(int index) {
                return new NodeInfo(new Object() {}, 1, 1, 1, 1, null);
            }
        });

        coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(3.3)),
                new CoreConstantExpression(new Double(5.5)));

        result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testCoreOperationGreaterThanWithNodeInfoValues() {
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public int getSize() {
                return 2;
            }

            @Override
            public Info getItem(int index) {
                if (index == 0) {
                    return new NodeInfo(new Double(5.5), null, null);
                } else {
                    return new NodeInfo(new Double(3.3), null, null);
                }
            }
        });

        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreExpression(new Class[] { NodeInfo.class }, null, null),
                new CoreExpression(new Class[] { NodeInfo.class }, null, null));

        Object[] args = new Object[] { new NodeInfo(new Double(5.5), null, null), new NodeInfo(new Double(3.3), null, null) };
        Object result = coreOperationGreaterThan.computeValue(context, args);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        args = new Object[] { new NodeInfo(new Double(3.3), null, null), new NodeInfo(new Double(5.5), null, null) };
        coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreExpression(new Class[] { NodeInfo.class }, null, null),
                new CoreExpression(new Class[] { NodeInfo.class }, null, null));
        result = coreOperationGreaterThan.computeValue(context, args);
        assertFalse(result instanceof Boolean);
    }
}