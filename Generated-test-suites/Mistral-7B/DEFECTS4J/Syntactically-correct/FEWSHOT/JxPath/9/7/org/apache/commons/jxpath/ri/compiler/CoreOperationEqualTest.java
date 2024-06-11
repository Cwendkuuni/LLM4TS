package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.Parser;
import org.apache.commons.jxpath.ri.Result;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        Parser parser = new Parser();
        String expression = "1 = 1";
        Expression coreOperationEqual = parser.parseExpression(expression);
        assertNotNull(coreOperationEqual);
        assertInstanceOf(CoreOperationEqual.class, coreOperationEqual);
        Expression arg1 = coreOperationEqual.getArgs()[0];
        Expression arg2 = coreOperationEqual.getArgs()[1];
        assertEquals(arg1.getClass(), arg2.getClass());
    }

    @Test
    public void testComputeValueTrue() {
        Parser parser = new Parser();
        String expression = "1 = 1";
        Expression coreOperationEqual = parser.parseExpression(expression);
        EvalContext context = new EvalContext();
        Result result = coreOperationEqual.computeValue(context);
        assertTrue(result.getValue() instanceof Boolean);
        assertEquals(Boolean.TRUE, result.getValue());
    }

    @Test
    public void testComputeValueFalse() {
        Parser parser = new Parser();
        String expression = "1 = 2";
        Expression coreOperationEqual = parser.parseExpression(expression);
        EvalContext context = new EvalContext();
        Result result = coreOperationEqual.computeValue(context);
        assertTrue(result.getValue() instanceof Boolean);
        assertEquals(Boolean.FALSE, result.getValue());
    }

    @Test
    public void testComputeValueNull() {
        Parser parser = new Parser();
        String expression = "null = null";
        Expression coreOperationEqual = parser.parseExpression(expression);
        EvalContext context = new EvalContext();
        Result result = coreOperationEqual.computeValue(context);
        assertTrue(result.getValue() instanceof Boolean);
        assertEquals(Boolean.TRUE, result.getValue());
    }

    @Test
    public void testComputeValueNullOther() {
        Parser parser = new Parser();
        String expression = "null = 1";
        Expression coreOperationEqual = parser.parseExpression(expression);
        EvalContext context = new EvalContext();
        Result result = coreOperationEqual.computeValue(context);
        assertTrue(result.getValue() instanceof Boolean);
        assertEquals(Boolean.FALSE, result.getValue());
    }

    @Test
    public void testComputeValueDifferentTypes() {
        Parser parser = new Parser();
        String expression = "1 = \"1\"";
        Expression coreOperationEqual = parser.parseExpression(expression);
        EvalContext context = new EvalContext();
        Result result = coreOperationEqual.computeValue(context);
        assertTrue(result.getValue() instanceof Boolean);
        assertEquals(Boolean.FALSE, result.getValue());
    }

    @Test
    public void testComputeValueSameType() {
        Parser parser = new Parser();
        String expression = "1 = 1.0";
        Expression coreOperationEqual = parser.parseExpression(expression);
        EvalContext context = new EvalContext();
        Result result = coreOperationEqual.computeValue(context);
        assertTrue(result.getValue() instanceof Boolean);
        assertEquals(Boolean.TRUE, result.getValue());
    }

    @Test
    public void testGetSymbol() {
        Parser parser = new Parser();
        String expression = "1 = 1";
        Expression coreOperationEqual = parser.parseExpression(expression);
        assertEquals("=", coreOperationEqual.getSymbol());
    }
}