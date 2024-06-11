package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.test.AbstractTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CoreOperationEqual.
 */
public class CoreOperationEqualTest extends AbstractTest {

    /**
     * Test constructor.
     */
    @Test
    public void testCoreOperationEqualConstructor() {
        Expression arg1 = getTestExpression("1");
        Expression arg2 = getTestExpression("1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertNotNull(coreOperationEqual);
        assertEquals(CoreOperationEqual.class, coreOperationEqual.getClass());
        assertEquals(2, coreOperationEqual.getArity());
        assertEquals(arg1, coreOperationEqual.getArg(0));
        assertEquals(arg2, coreOperationEqual.getArg(1));
    }

    /**
     * Test computeValue method with equals expressions.
     */
    @Test
    public void testComputeValueWithEqualsExpressions() {
        Expression arg1 = getTestExpression("1");
        Expression arg2 = getTestExpression("1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(getTestContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test computeValue method with not equals expressions.
     */
    @Test
    public void testComputeValueWithNotEqualsExpressions() {
        Expression arg1 = getTestExpression("1");
        Expression arg2 = getTestExpression("2");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(getTestContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test computeValue method with null args.
     */
    @Test
    public void testComputeValueWithNullArgs() {
        Expression arg1 = null;
        Expression arg2 = getTestExpression("1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(getTestContext());
        assertNull(result);
    }

    /**
     * Test computeValue method with null context.
     */
    @Test(expected = RuntimeException.class)
    public void testComputeValueWithNullContext() {
        Expression arg1 = getTestExpression("1");
        Expression arg2 = getTestExpression("1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(null);
    }

    /**
     * Test getSymbol method.
     */
    @Test
    public void testGetSymbol() {
        Expression arg1 = getTestExpression("1");
        Expression arg2 = getTestExpression("1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals("=", coreOperationEqual.getSymbol());
    }
}