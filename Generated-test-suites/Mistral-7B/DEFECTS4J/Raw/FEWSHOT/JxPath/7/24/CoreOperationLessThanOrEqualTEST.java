package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.test.TestHelper;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValueWithDoubleValues() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new LiteralExpression(3.5), new LiteralExpression(4.5));
        EvalContext context = TestHelper.createTestContext();
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        operation = new CoreOperationLessThanOrEqual(
                new LiteralExpression(3.5), new LiteralExpression(3.5));
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        operation = new CoreOperationLessThanOrEqual(
                new LiteralExpression(3.5), new LiteralExpression(3.0));
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        operation = new CoreOperationLessThanOrEqual(
                new LiteralExpression(3.5), new LiteralExpression(2.5));
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithMixedValues() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new LiteralExpression(3.5), new VariableExpression("x"));
        EvalContext context = TestHelper.createTestContext();
        context.setVariable("x", new LiteralExpression(4.5));
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context.setVariable("x", new LiteralExpression(3.5));
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithNullValue() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new LiteralExpression(3.5), new LiteralExpression(null));
        EvalContext context = TestHelper.createTestContext();
        Object result = operation.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new LiteralExpression(3.5), new LiteralExpression(4.5));
        String result = operation.getSymbol();
        assertEquals("<=", result);
    }
}