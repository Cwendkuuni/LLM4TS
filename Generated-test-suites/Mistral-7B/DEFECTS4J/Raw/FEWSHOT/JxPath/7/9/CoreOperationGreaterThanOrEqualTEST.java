package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.nodes.NumberNode;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValueWithDoubleValues() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new NumberNode(3.5), new NumberNode(2.5));

        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext();
        operation = new CoreOperationGreaterThanOrEqual(new NumberNode(2.5), new NumberNode(3.5));

        result = operation.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertNull(result);

        context = new EvalContext();
        operation = new CoreOperationGreaterThanOrEqual(new NumberNode(3.5), new NumberNode(3.5));

        result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext();
        operation = new CoreOperationGreaterThanOrEqual(new NumberNode(3.5), new NumberNode(3.5));

        result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueWithNullValues() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new NumberNode(3.5), null);

        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertNull(result);

        context = new EvalContext();
        operation = new CoreOperationGreaterThanOrEqual(null, new NumberNode(3.5));

        result = operation.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertNull(result);

        context = new EvalContext();
        operation = new CoreOperationGreaterThanOrEqual(null, null);

        result = operation.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testComputeValueWithDifferentDataTypes() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new NumberNode(3.5), new StringNode("3.5"));

        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertNull(result);

        context = new EvalContext();
        operation = new CoreOperationGreaterThanOrEqual(new StringNode("3.5"), new NumberNode(3.5));

        result = operation.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertNull(result);

        context = new EvalContext();
        operation = new CoreOperationGreaterThanOrEqual(new StringNode("3.5"), new StringNode("3.5"));

        result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new NumberNode(3.5), new NumberNode(2.5));

        String symbol = operation.getSymbol();

        assertEquals(">=", symbol);
    }
}