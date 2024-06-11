package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;

/**
 * CoreOperationNotEqualTest class for testing CoreOperationNotEqual methods.
 */
public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        Expression arg1 = new CoreConstantExpression(Boolean.TRUE);
        Expression arg2 = new CoreConstantExpression(Boolean.FALSE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new CoreConstantExpression(Boolean.FALSE);
        arg2 = new CoreConstantExpression(Boolean.TRUE);

        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new CoreConstantExpression(1);
        arg2 = new CoreConstantExpression(1);

        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        try {
            result = coreOperationNotEqual.computeValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        arg1 = new CoreConstantExpression(null);
        arg2 = new CoreConstantExpression(Integer.valueOf(1));

        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        try {
            result = coreOperationNotEqual.computeValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        arg1 = new CoreConstantExpression(Integer.valueOf(1));
        arg2 = new CoreConstantExpression(null);

        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        try {
            result = coreOperationNotEqual.computeValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        arg1 = new CoreConstantExpression(Integer.valueOf(1));
        arg2 = new CoreConstantExpression(new CoreConstantExpression(1));

        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new CoreOperationCompare(new CoreConstantExpression(Integer.valueOf(1)), new CoreConstantExpression(Operator.GT), new CoreConstantExpression(Integer.valueOf(2)));
        arg2 = new CoreConstantExpression(Boolean.TRUE);

        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new CoreOperationCompare(new CoreConstantExpression(Integer.valueOf(1)), new CoreConstantExpression(Operator.GT), new CoreConstantExpression(Integer.valueOf(1)));
        arg2 = new CoreConstantExpression(Boolean.TRUE);

        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new CoreOperationCompare(new CoreConstantExpression(Integer.valueOf(1)), new CoreConstantExpression(Operator.EQ), new CoreConstantExpression(Integer.valueOf(1)));
        arg2 = new CoreConstantExpression(Boolean.TRUE);

        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new CoreOperationCompare(new CoreConstantExpression(Integer.valueOf(1)), new CoreConstantExpression(Operator.NE), new CoreConstantExpression(Integer.valueOf(1)));
        arg2 = new CoreConstantExpression(Boolean.TRUE);

        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }
}