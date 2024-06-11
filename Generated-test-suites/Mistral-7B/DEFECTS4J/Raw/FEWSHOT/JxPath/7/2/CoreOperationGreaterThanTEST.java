package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        // Test with double values
        CoreOperationGreaterThan testee = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(5.0)),
                new CoreConstantExpression(new Double(3.0)));
        EvalContext context = new EvalContext(new InfoSet(), new NodeInfo[0]);
        assertTrue(testee.computeValue(context).equals(Boolean.TRUE));

        testee = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(3.0)),
                new CoreConstantExpression(new Double(5.0)));
        assertFalse(testee.computeValue(context).equals(Boolean.TRUE));

        // Test with NodeInfo values
        testee = new CoreOperationGreaterThan(
                new CoreConstantExpression(new NodeInfo(1.0, null)),
                new CoreConstantExpression(new NodeInfo(3.0, null)));
        context = new EvalContext(new InfoSet(), new NodeInfo[0]);
        InfoSet infoSet = new InfoSet();
        infoSet.setNodeInfoAt(0, new NodeInfo(5.0, null));
        context.setInfoSet(infoSet);
        assertTrue(testee.computeValue(context).equals(Boolean.TRUE));

        testee = new CoreOperationGreaterThan(
                new CoreConstantExpression(new NodeInfo(3.0, null)),
                new CoreConstantExpression(new NodeInfo(5.0, null)));
        assertFalse(testee.computeValue(context).equals(Boolean.TRUE));
    }
}