package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        Expression arg1 = new LiteralExpression(1.2);
        Expression arg2 = new LiteralExpression(1.1);
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);

        assertTrue(expression.computeValue(new EvalContext()).booleanValue());

        arg1 = new LiteralExpression(1.1);
        arg2 = new LiteralExpression(1.2);
        expression = new CoreOperationGreaterThan(arg1, arg2);

        assertFalse(expression.computeValue(new EvalContext()).booleanValue());

        arg1 = new LiteralExpression(Double.MAX_VALUE);
        arg2 = new LiteralExpression(Double.MAX_VALUE);
        expression = new CoreOperationGreaterThan(arg1, arg2);

        assertFalse(expression.computeValue(new EvalContext()).booleanValue());

        arg1 = new LiteralExpression(Double.MAX_VALUE);
        arg2 = new LiteralExpression(Double.MIN_VALUE);
        expression = new CoreOperationGreaterThan(arg1, arg2);

        assertTrue(expression.computeValue(new EvalContext()).booleanValue());
    }
}