package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.junit.Test;

/**
 * Test class for CoreOperationLessThanOrEqual.
 */
public class CoreOperationLessThanOrEqualTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertNotNull(coreOperationLessThanOrEqual);
        assertEquals(2, coreOperationLessThanOrEqual.getArgs().length);
        assertEquals(arg1, coreOperationLessThanOrEqual.getArgs()[0]);
        assertEquals(arg2, coreOperationLessThanOrEqual.getArgs()[1]);
    }

    /**
     * Test computeValue method.
     */
    @Test
    public void testComputeValue() {
        double l = 1.0;
        double r = 1.0;
        Expression arg1 = new LiteralExpression(l);
        Expression arg2 = new LiteralExpression(r);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        l = 2.0;
        r = 1.0;
        arg1 = new LiteralExpression(l);
        arg2 = new LiteralExpression(r);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        context = new EvalContext();
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        l = 1.0;
        r = 2.0;
        arg1 = new LiteralExpression(l);
        arg2 = new LiteralExpression(r);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        context = new EvalContext();
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        l = 1.0;
        r = 1.0;
        arg1 = new LiteralExpression(l);
        arg2 = new LiteralExpression(r);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        context = new EvalContext();
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        l = Double.NaN;
        r = 1.0;
        arg1 = new LiteralExpression(l);
        arg2 = new LiteralExpression(r);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        context = new EvalContext();
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertNull(result);

        l = 1.0;
        r = Double.NaN;
        arg1 = new LiteralExpression(l);
        arg2 = new LiteralExpression(r);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        context = new EvalContext();
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertNull(result);
    }

    /**
     * Test getSymbol method.
     */
    @Test
    public void testGetSymbol() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        String symbol = coreOperationLessThanOrEqual.getSymbol();
        assertEquals("<=", symbol);
    }
}