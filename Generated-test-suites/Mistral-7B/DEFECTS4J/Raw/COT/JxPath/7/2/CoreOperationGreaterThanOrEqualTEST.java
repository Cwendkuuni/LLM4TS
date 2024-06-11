package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.apache.commons.jxpath.ri.expression.Literal;
import org.apache.commons.jxpath.ri.expression.Variable;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.junit.Test;

/**
 * Test class for CoreOperationGreaterThanOrEqual.
 */
public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Literal(5.0);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new Literal(6.0);
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanOrEqualNullArg() {
        Expression arg1 = null;
        Expression arg2 = new Literal(5.0);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanOrEqualNullArg2() {
        Expression arg1 = new Variable("arg1");
        Expression arg2 = null;
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanOrEqualMismatchingTypes() {
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Literal(5);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanOrEqualMismatchingTypes2() {
        Expression arg1 = new Literal(5.0);
        Expression arg2 = new Variable("arg2");
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualNaN() {
        Expression arg1 = new Variable("arg1");
        Expression arg2 = new Literal(Double.NaN);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result.equals(Boolean.TRUE));
        assertFalse(result.equals(Boolean.FALSE));
        assertNull(result);

        arg1 = new Literal(Double.NaN);
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result.equals(Boolean.TRUE));
        assertFalse(result.equals(Boolean.FALSE));
        assertNull(result);

        arg1 = new Literal(Double.POSITIVE_INFINITY);
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result.equals(Boolean.TRUE));
        assertFalse(result.equals(Boolean.FALSE));
        assertNull(result);

        arg2 = new Literal(Double.POSITIVE_INFINITY);
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result.equals(Boolean.TRUE));
        assertFalse(result.equals(Boolean.FALSE));
        assertNull(result);

        arg1 = new Literal(Double.NEGATIVE_INFINITY);
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result.equals(Boolean.TRUE));
        assertFalse(result.equals(Boolean.FALSE));
        assertNull(result);

        arg2 = new Literal(Double.NEGATIVE_INFINITY);
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result.equals(Boolean.TRUE));
        assertFalse(result.equals(Boolean.FALSE));
        assertNull(result);
    }
}