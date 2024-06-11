package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.PathParser;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        Expression expr1 = new CoreConstantExpression(1);
        Expression expr2 = new CoreConstantExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(expr1, expr2);
        assertEquals("(1 = 1)", coreOperationEqual.toString());

        EvalContext context = new EvalContext();
        Object value = coreOperationEqual.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertEquals(Boolean.TRUE, value);
    }

    @Test
    public void testCoreOperationEqualWithNull() {
        Expression expr1 = new CoreConstantExpression(null);
        Expression expr2 = new CoreConstantExpression(null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(expr1, expr2);
        assertEquals("(null = null)", coreOperationEqual.toString());

        EvalContext context = new EvalContext();
        Object value = coreOperationEqual.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertEquals(Boolean.TRUE, value);
    }

    @Test
    public void testCoreOperationEqualWithDifferentTypes() {
        Expression expr1 = new CoreConstantExpression(1);
        Expression expr2 = new CoreStringExpression("1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(expr1, expr2);
        assertEquals("(1 = '1')", coreOperationEqual.toString());

        EvalContext context = new EvalContext();
        Object value = coreOperationEqual.computeValue(context);
        assertFalse(value instanceof Boolean);
        assertNull(value);
    }

    @Test
    public void testCoreOperationEqualWithBooleanValues() {
        Expression expr1 = new CoreConstantExpression(Boolean.TRUE);
        Expression expr2 = new CoreConstantExpression(Boolean.TRUE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(expr1, expr2);
        assertEquals("(true = true)", coreOperationEqual.toString());

        EvalContext context = new EvalContext();
        Object value = coreOperationEqual.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertEquals(Boolean.TRUE, value);
    }

    @Test
    public void testCoreOperationEqualWithBooleanValuesDifferent() {
        Expression expr1 = new CoreConstantExpression(Boolean.TRUE);
        Expression expr2 = new CoreConstantExpression(Boolean.FALSE);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(expr1, expr2);
        assertEquals("(true = false)", coreOperationEqual.toString());

        EvalContext context = new EvalContext();
        Object value = coreOperationEqual.computeValue(context);
        assertFalse(value instanceof Boolean);
        assertNull(value);
    }

    @Test
    public void testCoreOperationEqualWithNumberValues() {
        Expression expr1 = new CoreConstantExpression(1.0);
        Expression expr2 = new CoreConstantExpression(1.0);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(expr1, expr2);
        assertEquals("(1.0 = 1.0)", coreOperationEqual.toString());

        EvalContext context = new EvalContext();
        Object value = coreOperationEqual.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertEquals(Boolean.TRUE, value);
    }

    @Test
    public void testCoreOperationEqualWithNumberValuesDifferent() {
        Expression expr1 = new CoreConstantExpression(1.0);
        Expression expr2 = new CoreConstantExpression(2.0);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(expr1, expr2);
        assertEquals("(1.0 = 2.0)", coreOperationEqual.toString());

        EvalContext context = new EvalContext();
        Object value = coreOperationEqual.computeValue(context);
        assertFalse(value instanceof Boolean);
        assertNull(value);
    }

    @Test
    public void testCoreOperationEqualWithStringValuesEqual() {
        Expression expr1 = new CoreStringExpression("foo");
        Expression expr2 = new CoreStringExpression("foo");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(expr1, expr2);
        assertEquals("('foo' = 'foo')", coreOperationEqual.toString());

        EvalContext context = new EvalContext();
        Object value = coreOperationEqual.computeValue(context);
        assertTrue(value instanceof Boolean);
        assertEquals(Boolean.TRUE, value);
    }

    @Test
    public void testCoreOperationEqualWithStringValuesDifferent() {
        Expression expr1 = new CoreStringExpression("foo");
        Expression expr2 = new CoreStringExpression("bar");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(expr1, expr2);
        assertEquals("('foo' = 'bar')", coreOperationEqual.toString());

        EvalContext context = new EvalContext();
        Object value = coreOperationEqual.computeValue(context);
        assertFalse(value instanceof Boolean);
        assertNull(value);
    }

    @Test
    public void testCoreOperationEqualWithMixedValues() {
        Expression expr1 = new CoreConstantExpression(1);
        Expression expr2 = new CoreStringExpression("1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(expr1, expr2);
        assertEquals("(1 = '1')", coreOperationEqual.toString());

        EvalContext context = new EvalContext();
        Object value = coreOperationEqual.computeValue(context);
        assertFalse(value instanceof Boolean);
        assertNull(value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqualWithInvalidArgument() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(null, new CoreConstantExpression(1));
    }
}