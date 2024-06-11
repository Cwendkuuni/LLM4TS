package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        // Test with doubles
        Expression arg1 = new LiteralExpression(2.3);
        Expression arg2 = new LiteralExpression(1.5);
        CoreOperationGreaterThanOrEqual expr = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertTrue(expr.computeValue(new EvalContext()).booleanValue());

        arg1 = new LiteralExpression(1.5);
        arg2 = new LiteralExpression(2.3);
        expr = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertTrue(expr.computeValue(new EvalContext()).booleanValue());

        arg1 = new LiteralExpression(2.3);
        arg2 = new LiteralExpression(-1.5);
        expr = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertTrue(expr.computeValue(new EvalContext()).booleanValue());

        arg1 = new LiteralExpression(-1.5);
        arg2 = new LiteralExpression(2.3);
        expr = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertFalse(expr.computeValue(new EvalContext()).booleanValue());

        arg1 = new LiteralExpression(null);
        arg2 = new LiteralExpression(1.5);
        expr = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertNull(expr.computeValue(new EvalContext()));

        arg1 = new LiteralExpression(1.5);
        arg2 = new LiteralExpression(null);
        expr = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertNull(expr.computeValue(new EvalContext()));

        arg1 = new LiteralExpression(null);
        arg2 = new LiteralExpression(null);
        expr = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertNull(expr.computeValue(new EvalContext()));
    }
}