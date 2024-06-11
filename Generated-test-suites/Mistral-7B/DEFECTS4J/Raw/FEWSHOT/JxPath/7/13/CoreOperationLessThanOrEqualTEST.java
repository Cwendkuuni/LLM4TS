package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.impl.SimpleNode;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        // Test with double values
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
                new LiteralExpression(new Double(1.5)),
                new LiteralExpression(new Double(2.5)));
        EvalContext context = new EvalContext(new InfoSet(new Object[][]{}), new Path());
        NodeInfo nodeInfo = new SimpleNode("root", null, null);
        context.setCurrentNode(nodeInfo);
        Object result = op.computeValue(context);
        assertTrue((Boolean) result);

        op = new CoreOperationLessThanOrEqual(
                new LiteralExpression(new Double(2.5)),
                new LiteralExpression(new Double(1.5)));
        context = new EvalContext(new InfoSet(new Object[][]{}), new Path());
        result = op.computeValue(context);
        assertFalse((Boolean) result);

        op = new CoreOperationLessThanOrEqual(
                new LiteralExpression(new Double(1.5)),
                new LiteralExpression(new Double(1.5)));
        context = new EvalContext(new InfoSet(new Object[][]{}), new Path());
        result = op.computeValue(context);
        assertTrue((Boolean) result);

        op = new CoreOperationLessThanOrEqual(
                new LiteralExpression(new Double(1.5)),
                new LiteralExpression(new Double(1.5)));
        context = new EvalContext(new InfoSet(new Object[][]{}), new Path());
        context.setCurrentNode(new SimpleNode("node", null, null));
        result = op.computeValue(context);
        assertFalse((Boolean) result);

        // Test with int values
        op = new CoreOperationLessThanOrEqual(
                new LiteralExpression(new Integer(3)),
                new LiteralExpression(new Integer(5)));
        context = new EvalContext(new InfoSet(new Object[][]{}), new Path());
        result = op.computeValue(context);
        assertFalse((Boolean) result);

        op = new CoreOperationLessThanOrEqual(
                new LiteralExpression(new Integer(5)),
                new LiteralExpression(new Integer(3)));
        context = new EvalContext(new InfoSet(new Object[][]{}), new Path());
        result = op.computeValue(context);
        assertTrue((Boolean) result);

        op = new CoreOperationLessThanOrEqual(
                new LiteralExpression(new Integer(3)),
                new LiteralExpression(new Integer(3)));
        context = new EvalContext(new InfoSet(new Object[][]{}), new Path());
        result = op.computeValue(context);
        assertTrue((Boolean) result);

        op = new CoreOperationLessThanOrEqual(
                new LiteralExpression(new Integer(3)),
                new LiteralExpression(new Integer(3)));
        context = new EvalContext(new InfoSet(new Object[][]{}), new Path());
        context.setCurrentNode(new SimpleNode("node", null, null));
        result = op.computeValue(context);
        assertFalse((Boolean) result);
    }
}