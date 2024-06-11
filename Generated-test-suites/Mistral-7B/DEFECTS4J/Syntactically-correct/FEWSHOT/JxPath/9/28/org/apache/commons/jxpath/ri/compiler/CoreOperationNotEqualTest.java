package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.JXPathContext;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_nullArguments() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(null, null);
        EvalContext context = new EvalContext(new JXPathContext(null, null), null);
        assertNull(operation.computeValue(context));
    }

    @Test
    public void testComputeValue_equalArguments() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(1));
        EvalContext context = new EvalContext(new JXPathContext(null, null), null);
        assertFalse(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValue_notEqualArguments() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(2));
        EvalContext context = new EvalContext(new JXPathContext(null, null), null);
        assertTrue(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValue_differentTypes() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new NumberExpression(1.0), new StringExpression("1"));
        EvalContext context = new EvalContext(new JXPathContext(null, null), null);
        assertTrue(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValue_booleanArguments() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new BooleanExpression(true), new BooleanExpression(false));
        EvalContext context = new EvalContext(new JXPathContext(null, null), null);
        assertTrue(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValue_nodeArguments() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new NodeTestExpression("*"), new NodeTestExpression("*"));
        EvalContext context = new EvalContext(new JXPathContext(new NodeInfo("root", null), null), null);
        NodeInfo node1 = new NodeInfo("node1", null);
        NodeInfo node2 = new NodeInfo("node2", null);
        context.setCurrentNode(node1);
        assertFalse(operation.computeValue(context).booleanValue());
        context.setCurrentNode(node2);
        assertTrue(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValue_mixedTypes() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new NumberExpression(1), new StringExpression("1"));
        EvalContext context = new EvalContext(new JXPathContext(null, null), null);
        assertFalse(operation.computeValue(context).booleanValue());
    }

    @Test
    public void testSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(1));
        String symbol = operation.getSymbol();
        assertNotNull(symbol);
        assertEquals("!=", symbol);
    }
}