package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.junit.Test;
import org.junit.Assert.*;

/**
 * Test class for CoreOperationEqual
 */
public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqualBasic() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression(1), new LiteralExpression(1));
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }

    @Test
    public void testCoreOperationEqualFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression(1), new LiteralExpression(2));
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqualNullArg() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(null, new LiteralExpression(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqualNullArg2() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression(1), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqualDifferentTypes() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression("1"), new LiteralExpression(1));
    }

    @Test
    public void testCoreOperationEqualSameObjects() {
        Object obj1 = new Object();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression(obj1), new LiteralExpression(obj1));
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }

    @Test
    public void testCoreOperationEqualDifferentObjectsWithEqualValue() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression(obj1), new LiteralExpression(obj2));
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((boolean) result);
    }

    @Test
    public void testCoreOperationEqualWithNaN() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression(Double.NaN), new LiteralExpression(Double.NaN));
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }

    @Test
    public void testCoreOperationEqualWithInfinite() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression(Double.POSITIVE_INFINITY), new LiteralExpression(Double.POSITIVE_INFINITY));
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }

    @Test
    public void testCoreOperationEqualWithNegativeInfinite() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression(Double.NEGATIVE_INFINITY), new LiteralExpression(Double.NEGATIVE_INFINITY));
        Object result = coreOperationEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }
}