package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValueTrue() {
        Expression arg1 = new CoreNumber(1);
        Expression arg2 = new CoreNumber(2);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueFalse() {
        Expression arg1 = new CoreNumber(1);
        Expression arg2 = new CoreNumber(1);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueNull() {
        Expression arg1 = new CoreNumber(1);
        Expression arg2 = null;
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(new CoreNumber(1), new CoreNumber(2));
        String symbol = notEqual.getSymbol();
        assertEquals("!=" , symbol);
    }

    @Test
    public void testSameExpression() {
        Expression arg1 = new CoreNumber(1);
        Expression arg2 = arg1;
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);
        assertNull(result);
    }
}