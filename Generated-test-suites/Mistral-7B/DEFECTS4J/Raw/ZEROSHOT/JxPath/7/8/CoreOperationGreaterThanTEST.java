package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.test.TestSupport;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
                new LiteralExpression(5.0), new LiteralExpression(3.0));

        EvalContext context = new TestSupport.SimpleEvalContext();
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThanFalse() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
                new LiteralExpression(3.0), new LiteralExpression(5.0));

        EvalContext context = new TestSupport.SimpleEvalContext();
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationGreaterThanWithStrings() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
                new StringLiteralExpression("apple"), new StringLiteralExpression("banana"));

        EvalContext context = new TestSupport.SimpleEvalContext();
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanWithInvalidArguments() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
                null, new LiteralExpression(5.0));

        EvalContext context = new TestSupport.SimpleEvalContext();
        expression.computeValue(context);
    }
}