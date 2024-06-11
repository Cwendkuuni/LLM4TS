package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.NodeInfo;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual_BooleanValues() {
        Expression arg1 = new ConstantExpression(Boolean.TRUE);
        Expression arg2 = new ConstantExpression(Boolean.TRUE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_BooleanFalseValues() {
        Expression arg1 = new ConstantExpression(Boolean.FALSE);
        Expression arg2 = new ConstantExpression(Boolean.FALSE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_BooleanTrueFalse() {
        Expression arg1 = new ConstantExpression(Boolean.TRUE);
        Expression arg2 = new ConstantExpression(Boolean.FALSE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_NumberValues() {
        Expression arg1 = new ConstantExpression(Double.valueOf(1.0));
        Expression arg2 = new ConstantExpression(Double.valueOf(1.0));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_NumberNotEqualValues() {
        Expression arg1 = new ConstantExpression(Double.valueOf(1.0));
        Expression arg2 = new ConstantExpression(Double.valueOf(2.0));
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_StringValues() {
        Expression arg1 = new ConstantExpression("foo");
        Expression arg2 = new ConstantExpression("foo");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_StringValuesDifferent() {
        Expression arg1 = new ConstantExpression("foo");
        Expression arg2 = new ConstantExpression("bar");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_NodeInfoValuesSameNode() {
        NodeInfo nodeInfo1 = new NodeInfo(new Object(), null, null, null, null, 1);
        NodeInfo nodeInfo2 = nodeInfo1;
        Expression arg1 = new NodeInfoExpression(nodeInfo1);
        Expression arg2 = new NodeInfoExpression(nodeInfo2);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_NodeInfoValuesDifferentNodes() {
        NodeInfo nodeInfo1 = new NodeInfo(new Object(), null, null, null, null, 1);
        NodeInfo nodeInfo2 = new NodeInfo(new Object(), null, null, null, null, 2);
        Expression arg1 = new NodeInfoExpression(nodeInfo1);
        Expression arg2 = new NodeInfoExpression(nodeInfo2);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqual_NullValues() {
        Expression arg1 = new ConstantExpression(null);
        Expression arg2 = new ConstantExpression(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test(expected = IllegalStateException.class)
    public void testCoreOperationEqual_MismatchedTypes() {
        Expression arg1 = new ConstantExpression(Double.valueOf(1.0));
        Expression arg2 = new ConstantExpression("foo");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
    }
}