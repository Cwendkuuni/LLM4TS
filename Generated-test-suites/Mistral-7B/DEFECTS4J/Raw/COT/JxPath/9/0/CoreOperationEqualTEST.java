package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
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
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(1));
        assertNotNull(coreOperationEqual);
    }

    /**
     * Test method computeValue with equal values.
     */
    @Test
    public void testComputeValueWithEqualValues() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(1));
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test method computeValue with not equal values.
     */
    @Test
    public void testComputeValueWithNotEqualValues() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(2));
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test method computeValue with null values.
     */
    @Test(expected = NullPointerException.class)
    public void testComputeValueWithNullValues() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(1), null);
        EvalContext context = new EvalContext();
        coreOperationEqual.computeValue(context);
    }

    /**
     * Test method computeValue with non-Boolean results.
     */
    @Test(expected = ClassCastException.class)
    public void testComputeValueWithNonBooleanResults() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression("1"));
        EvalContext context = new EvalContext();
        coreOperationEqual.computeValue(context);
    }

    /**
     * Test method getSymbol.
     */
    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new ConstantExpression(1), new ConstantExpression(1));
        String symbol = coreOperationEqual.getSymbol();
        assertEquals("=", symbol);
    }
}