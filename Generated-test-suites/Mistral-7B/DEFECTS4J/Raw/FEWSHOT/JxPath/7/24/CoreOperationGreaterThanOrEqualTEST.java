package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {
    @Test
    public void testComputeValueWithDoubleValuesGreaterThanOrEqual() {
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);

        Expression arg1 = new LiteralExpression(5.0, null);
        Expression arg2 = new LiteralExpression(5.0, null);
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueWithDoubleValuesLessThan() {
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);

        Expression arg1 = new LiteralExpression(3.0, null);
        Expression arg2 = new LiteralExpression(5.0, null);
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueWithDoubleValuesEqual() {
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);

        Expression arg1 = new LiteralExpression(5.0, null);
        Expression arg2 = new LiteralExpression(5.0, null);
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueWithNodeInfoValues() {
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);

        NodeInfo arg1 = new NodeInfo();
        arg1.setValue(new Double(5.0));
        Expression arg1Expression = new LiteralExpression(arg1, null);

        NodeInfo arg2 = new NodeInfo();
        arg2.setValue(new Double(5.0));
        Expression arg2Expression = new LiteralExpression(arg2, null);

        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1Expression, arg2Expression);

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueWithNodeInfoValuesDifferent() {
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);

        NodeInfo arg1 = new NodeInfo();
        arg1.setValue(new Double(3.0));
        Expression arg1Expression = new LiteralExpression(arg1, null);

        NodeInfo arg2 = new NodeInfo();
        arg2.setValue(new Double(5.0));
        Expression arg2Expression = new LiteralExpression(arg2, null);

        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1Expression, arg2Expression);

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
}