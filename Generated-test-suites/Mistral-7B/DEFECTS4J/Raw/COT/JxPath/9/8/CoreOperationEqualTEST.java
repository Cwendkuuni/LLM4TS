package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CoreOperationEqual.
 */
public class CoreOperationEqualTest {

    /**
     * Test constructor.
     */
    @Test
    public void testCoreOperationEqualConstructor() {
        Expression arg1 = new CoreExpressionLiteral("5");
        Expression arg2 = new CoreExpressionLiteral("5");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertNotNull(coreOperationEqual);
        assertEquals("=", coreOperationEqual.getSymbol());
    }

    /**
     * Test computeValue method with equal values.
     */
    @Test
    public void testComputeValueEqual() {
        EvalContext context = new EvalContext();
        Expression arg1 = new CoreExpressionLiteral("5");
        Expression arg2 = new CoreExpressionLiteral("5");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test computeValue method with unequal values.
     */
    @Test
    public void testComputeValueUnequal() {
        EvalContext context = new EvalContext();
        Expression arg1 = new CoreExpressionLiteral("5");
        Expression arg2 = new CoreExpressionLiteral("6");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    /**
     * Test computeValue method with null values.
     */
    @Test(expected = NullPointerException.class)
    public void testComputeValueNull() {
        EvalContext context = new EvalContext();
        Expression arg1 = new CoreExpressionLiteral("5");
        Expression arg2 = null;
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        coreOperationEqual.computeValue(context);
    }

    /**
     * Test computeValue method with non-Boolean results.
     */
    @Test
    public void testComputeValueNonBoolean() {
        EvalContext context = new EvalContext();
        Expression arg1 = new CoreExpressionLiteral("5");
        Expression arg2 = new CoreExpressionLiteral("7");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    /**
     * Test computeValue method with different types of arguments.
     */
    @Test
    public void testComputeValueDifferentTypes() {
        EvalContext context = new EvalContext();
        Expression arg1 = new CoreExpressionLiteral("5");
        Expression arg2 = new CoreExpressionLiteral(5);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        Expression arg1 = new CoreExpressionLiteral("5");
        Expression arg2 = new CoreExpressionLiteral("5");
        CoreOperationEqual coreOperationEqual1 = new CoreOperationEqual(arg1, arg2);
        CoreOperationEqual coreOperationEqual2 = new CoreOperationEqual(arg1, arg2);
        assertTrue(coreOperationEqual1.equals(coreOperationEqual2));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        Expression arg1 = new CoreExpressionLiteral("5");
        Expression arg2 = new CoreExpressionLiteral("5");
        CoreOperationEqual coreOperationEqual1 = new CoreOperationEqual(arg1, arg2);
        CoreOperationEqual coreOperationEqual2 = new CoreOperationEqual(arg1, arg2);
        int hashCode1 = coreOperationEqual1.hashCode();
        int hashCode2 = coreOperationEqual2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        Expression arg1 = new CoreExpressionLiteral("5");
        Expression arg2 = new CoreExpressionLiteral("5");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        String result = coreOperationEqual.toString();
        String expected = "CoreOperationEqual(CoreExpressionLiteral(5), CoreExpressionLiteral(5))";
        assertEquals(expected, result);
    }
}