package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetFactory;
import org.apache.commons.jxpath.ri.JXPathContext;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.PathContext;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for CoreOperationGreaterThan class.
 */
public class CoreOperationGreaterThanTest {

    @Test
    public void testGreaterThan() {
        // Test with valid double values
        Expression arg1 = new ConstExpression(new Double(5.5));
        Expression arg2 = new ConstExpression(new Double(3.3));
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        context.setContext(new JXPathContext(createInfoSet()));
        Object result = expression.computeValue(context);
        assertTrue("Expected true for 5.5 > 3.3", result instanceof Boolean && ((Boolean) result).booleanValue());

        // Test with valid integer values
        arg1 = new ConstExpression(new Integer(7));
        arg2 = new ConstExpression(new Integer(3));
        expression = new CoreOperationGreaterThan(arg1, arg2);
        result = expression.computeValue(context);
        assertTrue("Expected true for 7 > 3", result instanceof Boolean && ((Boolean) result).booleanValue());

        // Test with invalid double values
        arg1 = new ConstExpression(Double.NaN);
        arg2 = new ConstExpression(Double.NaN);
        expression = new CoreOperationGreaterThan(arg1, arg2);
        result = expression.computeValue(context);
        assertFalse("Expected false for NaN > NaN", result instanceof Boolean && !((Boolean) result).booleanValue());

        // Test with null values
        arg1 = new ConstExpression(null);
        arg2 = new ConstExpression(Double.NaN);
        expression = new CoreOperationGreaterThan(arg1, arg2);
        result = expression.computeValue(context);
        assertFalse("Expected false for null > NaN", result instanceof Boolean && !((Boolean) result).booleanValue());

        // Test with error scenario: arguments are not numbers
        arg1 = new ConstExpression("abc");
        arg2 = new ConstExpression(Double.NaN);
        expression = new CoreOperationGreaterThan(arg1, arg2);
        try {
            expression.computeValue(context);
            // This point should not be reached, as the test should fail during computation
            fail("Expected ArithmeticException but got no exception");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    private InfoSet createInfoSet() {
        InfoSetFactory factory = new InfoSetFactory();
        NodeInfo node = factory.createNodeInfo(null, null, null, "test", null, null, null, null, null);
        return factory.createInfoSet(node);
    }
}