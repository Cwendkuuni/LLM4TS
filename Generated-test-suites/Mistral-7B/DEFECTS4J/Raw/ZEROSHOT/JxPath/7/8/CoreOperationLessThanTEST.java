package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Path;
import org.junit.Test;

/**
 * Test class for CoreOperationLessThan.
 */
public class CoreOperationLessThanTest {

    @Test
    public void testComputeValueWithNumbers() {
        CoreOperationLessThan op = new CoreOperationLessThan(
                new ConstantExpression(new Double(5.0)),
                new ConstantExpression(new Double(3.0)));
        EvalContext context = new EvalContext(new InfoSet(), new Path());
        NodeInfo nodeInfo = new NodeInfo(new Object());
        context.setCurrentNode(nodeInfo);

        Boolean result = (Boolean) op.computeValue(context);

        assertTrue(result);
    }

    @Test
    public void testComputeValueWithNumbersFalse() {
        CoreOperationLessThan op = new CoreOperationLessThan(
                new ConstantExpression(new Double(3.0)),
                new ConstantExpression(new Double(5.0)));
        EvalContext context = new EvalContext(new InfoSet(), new Path());
        NodeInfo nodeInfo = new NodeInfo(new Object());
        context.setCurrentNode(nodeInfo);

        Boolean result = (Boolean) op.computeValue(context);

        assertFalse(result);
    }

    @Test
    public void testComputeValueWithMixedTypes() {
        CoreOperationLessThan op = new CoreOperationLessThan(
                new ConstantExpression(new Double(5.0)),
                new ConstantExpression(new Integer(6)));
        EvalContext context = new EvalContext(new InfoSet(), new Path());
        NodeInfo nodeInfo = new NodeInfo(new Object());
        context.setCurrentNode(nodeInfo);

        try {
            op.computeValue(context);
            fail("Expected NumberMismatchException");
        } catch (Exception e) {
            assertTrue(e instanceof NumberMismatchException);
        }
    }

    @Test
    public void testSymbol() {
        CoreOperationLessThan op = new CoreOperationLessThan(
                new ConstantExpression(new Double(5.0)),
                new ConstantExpression(new Double(3.0)));
        String symbol = op.getSymbol();

        assertEquals("<", symbol);
    }
}