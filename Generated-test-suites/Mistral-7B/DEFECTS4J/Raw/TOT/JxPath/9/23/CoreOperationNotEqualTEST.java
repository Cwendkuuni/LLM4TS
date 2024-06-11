import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqualTest;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CoreOperationNotEqual.
 */
public class CoreOperationNotEqualTest {

    /**
     * Test method for CoreOperationNotEqual.computeValue(EvalContext).
     */
    @Test
    public void testComputeValue() {
        Expression arg1 = new CoreConstExpression(1);
        Expression arg2 = new CoreConstExpression(2);
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new CoreConstExpression(2);
        arg2 = new CoreConstExpression(1);
        operation = new CoreOperationNotEqual(arg1, arg2);
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new CoreConstExpression(1);
        arg2 = new CoreConstExpression(null);
        operation = new CoreOperationNotEqual(arg1, arg2);
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new CoreConstExpression(null);
        arg2 = new CoreConstExpression(1);
        operation = new CoreOperationNotEqual(arg1, arg2);
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new CoreConstExpression(null);
        arg2 = new CoreConstExpression(null);
        operation = new CoreOperationNotEqual(arg1, arg2);
        result = operation.computeValue(context);
        assertNull(result);

        arg1 = new CoreConstExpression(new Object());
        arg2 = new CoreConstExpression(new Object());
        operation = new CoreOperationNotEqual(arg1, arg2);
        result = operation.computeValue(context);
        assertNull(result);
    }

    /**
     * Test method for CoreOperationNotEqual.getSymbol().
     */
    @Test
    public void testGetSymbol() {
        Expression operation = new CoreOperationNotEqual(new CoreConstExpression(1), new CoreConstExpression(2));
        String symbol = operation.getSymbol();
        assertEquals("!=", symbol);
    }
}