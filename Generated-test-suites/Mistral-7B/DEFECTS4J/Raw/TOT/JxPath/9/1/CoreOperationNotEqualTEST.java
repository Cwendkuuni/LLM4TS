import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual.CoreExpression;
import org.apache.commons.jxpath.ri.evaluator.EvalContext;
import org.apache.commons.jxpath.ri.parser.JXPathParser;
import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test for CoreOperationNotEqual class.
 */
public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_notEqual() {
        CoreExpression arg1 = new CoreLiteralExpression("2");
        CoreExpression arg2 = new CoreLiteralExpression("3");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object value = coreOperationNotEqual.computeValue(context);

        assertTrue(value instanceof Boolean);
        assertEquals(Boolean.FALSE, value);
    }

    @Test
    public void testComputeValue_equal() {
        CoreExpression arg1 = new CoreLiteralExpression("2");
        CoreExpression arg2 = new CoreLiteralExpression("2");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object value = coreOperationNotEqual.computeValue(context);

        assertTrue(value instanceof Boolean);
        assertEquals(Boolean.TRUE, value);
    }

    @Test
    public void testComputeValue_null() {
        CoreExpression arg1 = new CoreLiteralExpression("2");
        CoreExpression arg2 = new CoreNullExpression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object value = coreOperationNotEqual.computeValue(context);

        assertTrue(value instanceof Boolean);
        assertEquals(Boolean.TRUE, value);
    }

    @Test
    public void testComputeValue_sameObjects() {
        CoreExpression arg1 = new CoreLiteralExpression("2");
        CoreExpression arg2 = arg1;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object value = coreOperationNotEqual.computeValue(context);

        assertTrue(value instanceof Boolean);
        assertFalse(value);
    }

    @Test
    public void testComputeValue_sameObjects_true() {
        CoreExpression arg1 = new CoreLiteralExpression(true);
        CoreExpression arg2 = arg1;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object value = coreOperationNotEqual.computeValue(context);

        assertTrue(value instanceof Boolean);
        assertTrue(value);
    }

    @Test
    public void testGetSymbol() {
        CoreExpression arg1 = new CoreLiteralExpression("2");
        CoreExpression arg2 = new CoreLiteralExpression("3");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        assertEquals("!=", coreOperationNotEqual.getSymbol());
    }

    @Ignore("Not implemented in CoreOperationNotEqual")
    public void testComputeValue_array() {
        // Not implemented in CoreOperationNotEqual
    }

    @Ignore("Not implemented in CoreOperationNotEqual")
    public void testComputeValue_map() {
        // Not implemented in CoreOperationNotEqual
    }

    @Test
    public void testComputeValue_nullArg() {
        CoreExpression arg1 = null;
        CoreExpression arg2 = new CoreLiteralExpression("3");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object value = coreOperationNotEqual.computeValue(context);

        assertNull(value);
    }

    @Test
    public void testComputeValue_throwsException() {
        CoreExpression arg1 = new CoreLiteralExpression("2");
        CoreExpression arg2 = new CoreOperationNegateExpression(new CoreNullExpression());
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        try {
            EvalContext context = new EvalContext();
            coreOperationNotEqual.computeValue(context);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Expected
        }
    }
}