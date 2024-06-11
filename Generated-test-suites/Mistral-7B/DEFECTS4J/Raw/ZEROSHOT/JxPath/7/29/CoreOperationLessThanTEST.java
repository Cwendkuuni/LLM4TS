package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Path;
import org.junit.Test;

/**
 * Testing class for CoreOperationLessThan.
 */
public class CoreOperationLessThanTest {

    @Test
    public void testLessThan() {
        // Testing with valid inputs
        CoreOperationLessThan operation = new CoreOperationLessThan(
                new LiteralExpression(new Double(5.0)),
                new LiteralExpression(new Double(10.0)));
        EvalContext context = new EvalContext();
        context.setCurrentInfoSet(new InfoSet());
        context.setCurrentNode(new Path(""));

        Object result = operation.computeValue(context);
        assertTrue("Expected true but was false", result instanceof Boolean);
        assertTrue("Expected true but was false", ((Boolean) result).booleanValue());

        // Testing with reversed operands
        operation = new CoreOperationLessThan(
                new LiteralExpression(new Double(10.0)),
                new LiteralExpression(new Double(5.0)));
        result = operation.computeValue(context);
        assertFalse("Expected false but was true", result instanceof Boolean);
        assertFalse("Expected false but was true", ((Boolean) result).booleanValue());

        // Testing with invalid inputs
        operation = new CoreOperationLessThan(
                new LiteralExpression(new Integer(5)),
                new LiteralExpression(new String("Hello World")));
        context = new EvalContext();
        context.setCurrentInfoSet(new InfoSet());
        context.setCurrentNode(new Path(""));

        try {
            result = operation.computeValue(context);
            fail("Expected an exception but none was thrown");
        } catch (ClassCastException e) {
            // Expected
        }
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(
                new LiteralExpression(new Double(5.0)),
                new LiteralExpression(new Double(10.0)));
        assertEquals("<", operation.getSymbol());
    }

    @Test
    public void testComputeValueWithNullArg1() {
        CoreOperationLessThan operation = new CoreOperationLessThan(
                new LiteralExpression(null),
                new LiteralExpression(new Double(10.0)));
        EvalContext context = new EvalContext();
        context.setCurrentInfoSet(new InfoSet());
        context.setCurrentNode(new Path(""));

        try {
            operation.computeValue(context);
            fail("Expected an exception but none was thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testComputeValueWithNullArg2() {
        CoreOperationLessThan operation = new CoreOperationLessThan(
                new LiteralExpression(new Double(5.0)),
                new LiteralExpression(null));
        EvalContext context = new EvalContext();
        context.setCurrentInfoSet(new InfoSet());
        context.setCurrentNode(new Path(""));

        try {
            operation.computeValue(context);
            fail("Expected an exception but none was thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}