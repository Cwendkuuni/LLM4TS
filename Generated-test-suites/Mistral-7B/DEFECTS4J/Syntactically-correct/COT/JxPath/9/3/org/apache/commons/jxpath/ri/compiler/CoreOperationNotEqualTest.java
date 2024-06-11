package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqualConstruction() {
        Expression arg1 = new CoreOperationConstant(1);
        Expression arg2 = new CoreOperationConstant(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertSame(arg1, coreOperationNotEqual.getArg1());
        assertSame(arg2, coreOperationNotEqual.getArg2());
    }

    @Test
    public void testCoreOperationNotEqualFunctionality() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new CoreOperationConstant(1), new CoreOperationConstant(1));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(coreOperationCompare, new CoreOperationConstant(2));
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqualSymbol() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new CoreOperationConstant(1), new CoreOperationConstant(1));
        assertEquals("!=", coreOperationNotEqual.getSymbol());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqualNullArg() {
        Expression arg1 = new CoreOperationConstant(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqualNullArg2() {
        Expression arg1 = new CoreOperationConstant(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(null, arg1);
    }

    @Test
    public void testCoreOperationNotEqualEqualsEqual() {
        Expression arg1 = new CoreOperationConstant(1);
        Expression arg2 = new CoreOperationConstant(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertFalse((Boolean) result);

        arg1 = new CoreOperationConstant(1);
        arg2 = new CoreOperationConstant(1);
        coreOperationNotEqual = new CoreOperationNotEqual(arg2, arg1);
        result = coreOperationNotEqual.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqualEqualsDifferentType() {
        Expression arg1 = new CoreOperationConstant(1);
        Expression arg2 = new CoreOperationConstant("1");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqualNullArg1() {
        Expression arg1 = new CoreOperationConstant(null);
        Expression arg2 = new CoreOperationConstant(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testCoreOperationNotEqualNullArg2() {
        Expression arg1 = new CoreOperationConstant(1);
        Expression arg2 = new CoreOperationConstant(null);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqualBooleanFalseAndTrue() {
        Expression arg1 = new CoreOperationConstant(Boolean.FALSE);
        Expression arg2 = new CoreOperationConstant(Boolean.TRUE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue((Boolean) result);

        arg1 = new CoreOperationConstant(Boolean.TRUE);
        arg2 = new CoreOperationConstant(Boolean.FALSE);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqualBooleanTrueAndTrue() {
        Expression arg1 = new CoreOperationConstant(Boolean.TRUE);
        Expression arg2 = new CoreOperationConstant(Boolean.TRUE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqualNumberAndBoolean() {
        Expression arg1 = new CoreOperationConstant(1);
        Expression arg2 = new CoreOperationConstant(Boolean.TRUE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqualNumberAndNumber() {
        Expression arg1 = new CoreOperationConstant(1);
        Expression arg2 = new CoreOperationConstant(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqualStringAndString() {
        Expression arg1 = new CoreOperationConstant("1");
        Expression arg2 = new CoreOperationConstant("2");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqualStringAndNumber() {
        Expression arg1 = new CoreOperationConstant("1");
        Expression arg2 = new CoreOperationConstant(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqualArrayAndArray() {
        Expression arg1 = new CoreOperationArrayConstant(new Object[]{1, 2, 3});
        Expression arg2 = new CoreOperationArrayConstant(new Object[]{1, 2, 4});
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqualArrayAndNull() {
        Expression arg1 = new CoreOperationArrayConstant(new Object[]{1, 2, 3});
        Expression arg2 = new CoreOperationConstant(null);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testCoreOperationNotEqualNullAndArray() {
        Expression arg1 = new CoreOperationConstant(null);
        Expression arg2 = new CoreOperationArrayConstant(new Object[]{1, 2, 3});
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue((Boolean) result);
    }
}