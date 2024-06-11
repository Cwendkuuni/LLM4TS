package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertEquals(">", coreOperationGreaterThan.getSymbol());
    }

    @Test
    public void testComputeValueTrue() {
        Expression arg1 = new LiteralExpression(3.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        Object result = coreOperationGreaterThan.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueFalse() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        Object result = coreOperationGreaterThan.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueNegativeNumbers() {
        Expression arg1 = new LiteralExpression(-1.0);
        Expression arg2 = new LiteralExpression(-2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        Object result = coreOperationGreaterThan.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression(-2.0);
        arg2 = new LiteralExpression(-1.0);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        result = coreOperationGreaterThan.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueNulls() {
        Expression arg1 = new LiteralExpression(Double.NaN);
        Expression arg2 = new LiteralExpression(Double.NaN);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        Object result = coreOperationGreaterThan.computeValue(new EvalContext());
        assertNull(result);

        arg1 = new LiteralExpression(1.0);
        arg2 = new LiteralExpression(null);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        result = coreOperationGreaterThan.computeValue(new EvalContext());
        assertFalse(result instanceof Boolean);

        arg1 = new LiteralExpression(null);
        arg2 = new LiteralExpression(1.0);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        result = coreOperationGreaterThan.computeValue(new EvalContext());
        assertFalse(result instanceof Boolean);

        arg1 = new LiteralExpression(null);
        arg2 = new LiteralExpression(null);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        result = coreOperationGreaterThan.computeValue(new EvalContext());
        assertNull(result);
    }

    @Test
    public void testComputeValueMixedTypes() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new StringLiteralExpression("2");
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        Object result = coreOperationGreaterThan.computeValue(new EvalContext());
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testComputeValueSameValues() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        Object result = coreOperationGreaterThan.computeValue(new EvalContext());
        assertFalse(result instanceof Boolean);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanInvalidArguments() {
        Expression arg1 = new CoreOperationGreaterThan(null, new LiteralExpression(1.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanInvalidArguments2() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new CoreOperationGreaterThan(new LiteralExpression(1.0), null);
    }

    @Test
    public void testEquals() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan1 = new CoreOperationGreaterThan(arg1, arg2);
        CoreOperationGreaterThan coreOperationGreaterThan2 = new CoreOperationGreaterThan(arg1, arg2);
        assertTrue(coreOperationGreaterThan1.equals(coreOperationGreaterThan2));
    }

    @Test
    public void testNotEquals() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan1 = new CoreOperationGreaterThan(arg1, arg2);
        CoreOperationGreaterThan coreOperationGreaterThan2 = new CoreOperationGreaterThan(arg2, arg1);
        assertNotSame(coreOperationGreaterThan1, coreOperationGreaterThan2);
    }

    @Test
    public void testHashCodeSameObjects() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan1 = new CoreOperationGreaterThan(arg1, arg2);
        CoreOperationGreaterThan coreOperationGreaterThan2 = new CoreOperationGreaterThan(arg1, arg2);
        assertEquals(coreOperationGreaterThan1.hashCode(), coreOperationGreaterThan2.hashCode());
    }

    @Test
    public void testHashCodeDifferentObjects() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan1 = new CoreOperationGreaterThan(arg1, arg2);
        CoreOperationGreaterThan coreOperationGreaterThan2 = new CoreOperationGreaterThan(arg2, arg1);
        assertNotSame(coreOperationGreaterThan1.hashCode(), coreOperationGreaterThan2.hashCode());
    }
}