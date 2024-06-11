package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.nodes.NumberNode;

import java.math.BigDecimal;

/**
 * Test the CoreOperationGreaterThan class.
 */
public class CoreOperationGreaterThanTest {

    @org.junit.Test
    public void testConstructor() {
        Expression arg1 = new NumberNode(new BigDecimal("5.5"));
        Expression arg2 = new NumberNode(new BigDecimal("4.4"));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertNotNull(coreOperationGreaterThan);
        assertEquals(CoreOperationRelationalExpression.class, coreOperationGreaterThan.getClass());
        assertNotNull(coreOperationGreaterThan.args);
        assertEquals(2, coreOperationGreaterThan.args.length);
    }

    @org.junit.Test
    public void testComputeValue_positive() {
        Expression arg1 = new NumberNode(new BigDecimal("5.5"));
        Expression arg2 = new NumberNode(new BigDecimal("4.4"));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @org.junit.Test
    public void testComputeValue_negative() {
        Expression arg1 = new NumberNode(new BigDecimal("4.4"));
        Expression arg2 = new NumberNode(new BigDecimal("5.5"));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @org.junit.Test
    public void testComputeValue_equal() {
        Expression arg1 = new NumberNode(new BigDecimal("5.5"));
        Expression arg2 = new NumberNode(new BigDecimal("5.5"));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testComputeValue_arg1Null() {
        Expression arg1 = null;
        Expression arg2 = new NumberNode(new BigDecimal("4.4"));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationGreaterThan.computeValue(context);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testComputeValue_arg2Null() {
        Expression arg1 = new NumberNode(new BigDecimal("5.5"));
        Expression arg2 = null;
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationGreaterThan.computeValue(context);
    }

    @org.junit.Test
    public void testGetSymbol() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(null, null);
        assertEquals(">", coreOperationGreaterThan.getSymbol());
    }
}