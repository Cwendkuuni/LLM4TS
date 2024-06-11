package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        // Test with valid numbers
        Expression arg1 = new LiteralExpression(new Double(5.0));
        Expression arg2 = new LiteralExpression(new Double(3.0));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        // Test with invalid numbers
        arg1 = new LiteralExpression(new Integer(5));
        arg2 = new LiteralExpression(new Double(3.0));
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result);

        // Test with the same number
        arg1 = new LiteralExpression(new Double(5.0));
        arg2 = new LiteralExpression(new Double(5.0));
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext(new InfoSet());
        result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result);

        // Test with null arguments
        arg1 = new LiteralExpression(null);
        arg2 = new LiteralExpression(new Double(3.0));
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext(new InfoSet());
        try {
            coreOperationGreaterThan.computeValue(context);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        arg1 = new LiteralExpression(new Double(3.0));
        arg2 = new LiteralExpression(null);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext(new InfoSet());
        try {
            coreOperationGreaterThan.computeValue(context);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with non-numeric arguments
        arg1 = new LiteralExpression("five");
        arg2 = new LiteralExpression(new Double(3.0));
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext(new InfoSet());
        try {
            coreOperationGreaterThan.computeValue(context);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        arg1 = new LiteralExpression(new Double(3.0));
        arg2 = new LiteralExpression("three");
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext(new InfoSet());
        try {
            coreOperationGreaterThan.computeValue(context);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(null, null);
        assertEquals(">", coreOperationGreaterThan.getSymbol());
    }
}