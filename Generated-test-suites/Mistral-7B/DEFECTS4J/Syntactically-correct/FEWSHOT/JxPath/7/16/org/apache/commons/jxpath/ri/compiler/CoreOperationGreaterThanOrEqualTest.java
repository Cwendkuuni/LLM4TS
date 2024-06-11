package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.SimpleNode;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(5.0), new ConstantExpression(4.0));
        EvalContext context = new EvalContext(new InfoSet());
        NodeInfo nodeInfo = new SimpleNode("root", null, null);
        context.setCurrentNode(nodeInfo);

        Boolean result = (Boolean) op.computeValue(context);
        assertTrue(result.booleanValue());

        op = new CoreOperationGreaterThanOrEqual(new ConstantExpression(4.0), new ConstantExpression(5.0));
        context = new EvalContext(new InfoSet());
        context.setCurrentNode(nodeInfo);

        result = (Boolean) op.computeValue(context);
        assertFalse(result.booleanValue());

        op = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5.0), new ConstantExpression(5.0));
        context = new EvalContext(new InfoSet());
        context.setCurrentNode(nodeInfo);

        result = (Boolean) op.computeValue(context);
        assertTrue(result.booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithStrings() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(
                new StringLiteralExpression("apple"), new StringLiteralExpression("banana"));
        EvalContext context = new EvalContext(new InfoSet());
        NodeInfo nodeInfo = new SimpleNode("root", null, null);
        context.setCurrentNode(nodeInfo);

        Boolean result = (Boolean) op.computeValue(context);
        assertFalse(result.booleanValue());

        op = new CoreOperationGreaterThanOrEqual(new StringLiteralExpression("banana"), new StringLiteralExpression("apple"));
        context = new EvalContext(new InfoSet());
        context.setCurrentNode(nodeInfo);

        result = (Boolean) op.computeValue(context);
        assertFalse(result.booleanValue());

        op = new CoreOperationGreaterThanOrEqual(new StringLiteralExpression("banana"), new StringLiteralExpression("banana"));
        context = new EvalContext(new InfoSet());
        context.setCurrentNode(nodeInfo);

        result = (Boolean) op.computeValue(context);
        assertTrue(result.booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNodes() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(
                new NumberValueExpression(new Double(5.0)), new NumberValueExpression(new Double(4.0)));
        EvalContext context = new EvalContext(new InfoSet());
        NodeInfo nodeInfo = new SimpleNode("root", null, null);
        context.setCurrentNode(nodeInfo);

        NodeInfo node1 = new SimpleNode("child1", null, null);
        nodeInfo.addChild(node1);
        node1.setValue(new Double(5.0));

        NodeInfo node2 = new SimpleNode("child2", null, null);
        nodeInfo.addChild(node2);
        node2.setValue(new Double(4.0));

        Boolean result = (Boolean) op.computeValue(context);
        assertTrue(result.booleanValue());

        op = new CoreOperationGreaterThanOrEqual(new NumberValueExpression(new Double(4.0)), new NumberValueExpression(new Double(5.0)));
        context = new EvalContext(new InfoSet());
        context.setCurrentNode(nodeInfo);

        result = (Boolean) op.computeValue(context);
        assertFalse(result.booleanValue());

        op = new CoreOperationGreaterThanOrEqual(new NumberValueExpression(new Double(5.0)), new NumberValueExpression(new Double(5.0)));
        context = new EvalContext(new InfoSet());
        context.setCurrentNode(nodeInfo);

        result = (Boolean) op.computeValue(context);
        assertTrue(result.booleanValue());
    }
}