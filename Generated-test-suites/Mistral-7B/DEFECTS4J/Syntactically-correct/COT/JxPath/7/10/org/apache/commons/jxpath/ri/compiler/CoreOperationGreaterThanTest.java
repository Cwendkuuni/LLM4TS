package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.info.InfoSetFactory;
import org.apache.commons.jxpath.ri.nodes.AbstractNode;
import org.apache.commons.jxpath.ri.nodes.DoubleNode;
import org.apache.commons.jxpath.ri.nodes.Node;
import org.apache.commons.jxpath.ri.nodes.NodeFactory;
import org.apache.commons.jxpath.ri.nodes.Variable;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.junit.Test;

/**
 * Test class for CoreOperationGreaterThan.
 */
public class CoreOperationGreaterThanTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertNotNull(coreOperationGreaterThan);
    }

    /**
     * Test computeValue method with valid double values.
     */
    @Test
    public void testComputeValueValidDoubleValues() {
        Expression arg1 = new DoubleNode(1.5);
        Expression arg2 = new DoubleNode(0.5);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSetFactory().newInfoSet(), new NodeFactory());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new DoubleNode(2.5);
        arg2 = new DoubleNode(1.5);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new DoubleNode(-1.5);
        arg2 = new DoubleNode(-2.5);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test computeValue method with invalid double values.
     */
    @Test
    public void testComputeValueInvalidDoubleValues() {
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Variable("arg2");
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        // Test null arguments
        EvalContext context = new EvalContext(new InfoSetFactory().newInfoSet(), new NodeFactory());
        arg1 = null;
        Object result = coreOperationGreaterThan.computeValue(context);
        assertNull(result);

        arg2 = null;
        result = coreOperationGreaterThan.computeValue(context);
        assertNull(result);

        // Test NaN values
        arg1 = new DoubleNode(Double.NaN);
        arg2 = new DoubleNode(Double.NaN);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        result = coreOperationGreaterThan.computeValue(context);
        assertNull(result);

        arg1 = new DoubleNode(Double.NaN);
        arg2 = new DoubleNode(Double.POSITIVE_INFINITY);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);

        arg1 = new DoubleNode(Double.POSITIVE_INFINITY);
        arg2 = new DoubleNode(Double.NaN);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    /**
     * Test getSymbol method.
     */
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new Variable("arg1"), new Variable("arg2"));
        assertEquals(">", coreOperationGreaterThan.getSymbol());
    }
}