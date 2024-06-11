package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationEqualTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testCoreOperationEqualConstructor() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression(1), new LiteralExpression(1));
        assertNotNull(coreOperationEqual);
    }

    /**
     * Test computeValue method with equal values.
     */
    @Test
    public void testComputeValueWithEqualValues() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression(1), new LiteralExpression(1));
        EvalContext context = new EvalContext(new Object[]{});
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test computeValue method with not equal values.
     */
    @Test
    public void testComputeValueWithNotEqualValues() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression(1), new LiteralExpression(2));
        EvalContext context = new EvalContext(new Object[]{});
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test computeValue method with null values.
     */
    @Test
    public void testComputeValueWithNullValues() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression(null), new LiteralExpression(null));
        EvalContext context = new EvalContext(new Object[]{});
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test computeValue method with null and non-null values.
     */
    @Test
    public void testComputeValueWithNullAndNonNullableValues() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression(1), new LiteralExpression(null));
        EvalContext context = new EvalContext(new Object[]{});
        Object result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    /**
     * Test computeValue method with non-numeric values.
     */
    @Test(expected = IllegalStateException.class)
    public void testComputeValueWithNonNumericValues() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression("1"), new LiteralExpression("2"));
        EvalContext context = new EvalContext(new Object[]{});
        coreOperationEqual.computeValue(context);
    }
}