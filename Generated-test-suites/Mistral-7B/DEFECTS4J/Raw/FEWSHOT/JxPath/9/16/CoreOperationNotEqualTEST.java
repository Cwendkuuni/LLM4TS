package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.core.Expression;
import org.apache.commons.jxpath.ri.core.Value;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        Expression arg1 = new LiteralExpression(new Value("10"));
        Expression arg2 = new LiteralExpression(new Value("20"));

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        assertNotNull(coreOperationNotEqual);
        assertEquals("!=", coreOperationNotEqual.getSymbol());
        assertNull(coreOperationNotEqual.getLeftArgument());
        assertNull(coreOperationNotEqual.getRightArgument());
    }

    @Test
    public void testComputeValueFalse() {
        Expression arg1 = new LiteralExpression(new Value("10"));
        Expression arg2 = new LiteralExpression(new Value("20"));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueTrue() {
        Expression arg1 = new LiteralExpression(new Value("10"));
        Expression arg2 = new LiteralExpression(new Value("10"));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithNulls() {
        Expression arg1 = new LiteralExpression(null);
        Expression arg2 = new LiteralExpression(new Value("20"));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);

        assertNull(result);

        arg1 = new LiteralExpression(new Value("10"));
        arg2 = new LiteralExpression(null);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression(null);
        arg2 = new LiteralExpression(null);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        result = coreOperationNotEqual.computeValue(context);

        assertNull(result);
    }

    @Test
    public void testComputeValueWithSameObjects() {
        Expression arg1 = new LiteralExpression(new Value("foo"));
        Expression arg2 = new LiteralExpression(new Value("foo"));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);

        assertNull(result);
    }

    @Test
    public void testComputeValueWithSameValues() {
        Expression arg1 = new LiteralExpression(new Value("10"));
        Expression arg2 = new LiteralExpression(new Value("10"));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);

        assertNull(result);
    }

    @Test
    public void testComputeValueWithDifferentTypes() {
        Expression arg1 = new LiteralExpression(new Value("10"));
        Expression arg2 = new LiteralExpression(new Value(10));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWithDifferentValues() {
        Expression arg1 = new LiteralExpression(new Value("10"));
        Expression arg2 = new LiteralExpression(new Value("20"));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    private void assertEquals(Object expected, Object actual) {
        if (expected == null) {
            assertEqualsNull(actual);
        } else if (expected instanceof Boolean) {
            assertEquals((Boolean) expected, (Boolean) actual);
        } else if (expected instanceof String) {
            assertEquals((String) expected, (String) actual);
            // Add other comparisons for other types as needed
        } else {
            fail("Unexpected type: " + expected.getClass().getName());
        }
    }

    private void assertEqualsNull(Object actual) {
        if (actual != null) {
            fail("Expected null, but got: " + actual.toString());
        }
    }

    private void assertEquals(String expected, String actual) {
        if (!expected.equals(actual)) {
            fail("Expected: " + expected + ", but got: " + actual);
        }
    }
}