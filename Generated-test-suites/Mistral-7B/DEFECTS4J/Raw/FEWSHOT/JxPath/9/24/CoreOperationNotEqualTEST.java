package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.core.CoreConstant;
import org.apache.commons.jxpath.ri.core.CoreOperationCompare;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqualNull() {
        Expression arg1 = null;
        Expression arg2 = new CoreConstant(Boolean.TRUE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertNull(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqualBooleanTrueFalse() {
        Expression arg1 = new CoreConstant(Boolean.TRUE);
        Expression arg2 = new CoreConstant(Boolean.FALSE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualBooleanFalseTrue() {
        Expression arg1 = new CoreConstant(Boolean.FALSE);
        Expression arg2 = new CoreConstant(Boolean.TRUE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualBooleanEqualsBoolean() {
        Expression arg1 = new CoreConstant(Boolean.TRUE);
        Expression arg2 = new CoreConstant(Boolean.TRUE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualStringEqualsString() {
        Expression arg1 = new CoreConstant("foo");
        Expression arg2 = new CoreConstant("foo");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualStringNotEqualsString() {
        Expression arg1 = new CoreConstant("foo");
        Expression arg2 = new CoreConstant("bar");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualStringIsNull() {
        Expression arg1 = new CoreConstant("foo");
        Expression arg2 = null;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualNullString() {
        Expression arg1 = null;
        Expression arg2 = new CoreConstant("foo");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualNullNull() {
        Expression arg1 = null;
        Expression arg2 = null;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertNull(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqualNumberEqualsNumber() {
        Expression arg1 = new CoreConstant(1.0);
        Expression arg2 = new CoreConstant(1.0);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualNumberNotEqualsNumber() {
        Expression arg1 = new CoreConstant(1.0);
        Expression arg2 = new CoreConstant(2.0);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualNumberIsNull() {
        Expression arg1 = new CoreConstant(1.0);
        Expression arg2 = null;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualNullNumber() {
        Expression arg1 = null;
        Expression arg2 = new CoreConstant(1.0);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualDateEqualsDate() {
        Expression arg1 = new CoreConstant(new java.util.Date());
        Expression arg2 = new CoreConstant(new java.util.Date());
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualDateNotEqualsDate() {
        Expression arg1 = new CoreConstant(new java.util.Date());
        Expression arg2 = new CoreConstant(new java.util.Date(System.currentTimeMillis() + 1000));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualDateNull() {
        Expression arg1 = new CoreConstant(new java.util.Date());
        Expression arg2 = null;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualNullDate() {
        Expression arg1 = null;
        Expression arg2 = new CoreConstant(new java.util.Date());
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualMixedTypes() {
        Expression arg1 = new CoreConstant(1.0);
        Expression arg2 = new CoreConstant("foo");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertNotEquals(arg1.getClass(), arg2.getClass());
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }

    @Test
    public void testCoreOperationNotEqualSameInstance() {
        Expression arg1 = new CoreConstant(1.0);
        Expression arg2 = arg1;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()).booleanValue());
    }
}