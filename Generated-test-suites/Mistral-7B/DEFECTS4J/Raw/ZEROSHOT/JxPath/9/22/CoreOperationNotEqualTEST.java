package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.test.TestEvaluator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for CoreOperationNotEqual class
 */
public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        Expression arg1 = new LiteralExpression(Integer.valueOf(1));
        Expression arg2 = new LiteralExpression(Integer.valueOf(2));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new TestEvaluator();
        Object result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = new LiteralExpression(Integer.valueOf(2));
        arg2 = new LiteralExpression(Integer.valueOf(2));

        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = new LiteralExpression(Integer.valueOf(1));
        arg2 = new LiteralExpression(Integer.valueOf(1));

        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = new LiteralExpression(Boolean.TRUE);
        arg2 = new LiteralExpression(Boolean.FALSE);

        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = new LiteralExpression(Boolean.FALSE);
        arg2 = new LiteralExpression(Boolean.TRUE);

        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new LiteralExpression(Integer.valueOf(1)), new LiteralExpression(Integer.valueOf(2)));
        String symbol = coreOperationNotEqual.getSymbol();

        assertEquals("!=", symbol);
    }
}