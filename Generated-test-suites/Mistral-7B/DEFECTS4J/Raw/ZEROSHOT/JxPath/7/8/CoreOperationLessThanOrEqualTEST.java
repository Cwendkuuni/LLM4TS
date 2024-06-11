package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

/**
 * Test class for CoreOperationLessThanOrEqual.
 */
public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqual() {
        // Test with double values
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = lessThanOrEqual.computeValue(context);
        assertTrue("Expected true but was false", result instanceof Boolean && ((Boolean) result).booleanValue());

        arg1 = new LiteralExpression(2.0);
        arg2 = new LiteralExpression(1.0);
        lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = lessThanOrEqual.computeValue(context);
        assertTrue("Expected false but was true", !((Boolean) result).booleanValue());

        arg1 = new LiteralExpression(1.0);
        arg2 = new LiteralExpression(1.0);
        lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = lessThanOrEqual.computeValue(context);
        assertTrue("Expected true but was false", result instanceof Boolean && ((Boolean) result).booleanValue());

        // Test with negative double values
        arg1 = new LiteralExpression(-1.0);
        arg2 = new LiteralExpression(-2.0);
        lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = lessThanOrEqual.computeValue(context);
        assertTrue("Expected true but was false", result instanceof Boolean && ((Boolean) result).booleanValue());

        arg1 = new LiteralExpression(-2.0);
        arg2 = new LiteralExpression(-1.0);
        lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = lessThanOrEqual.computeValue(context);
        assertFalse("Expected false but was true", result instanceof Boolean && ((Boolean) result).booleanValue());

        arg1 = new LiteralExpression(-1.0);
        arg2 = new LiteralExpression(-1.0);
        lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = lessThanOrEqual.computeValue(context);
        assertTrue("Expected true but was false", result instanceof Boolean && ((Boolean) result).booleanValue());

        // Test with integer values
        arg1 = new LiteralExpression(1);
        arg2 = new LiteralExpression(2);
        lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = lessThanOrEqual.computeValue(context);
        assertFalse("Expected false but was true", result instanceof Boolean && ((Boolean) result).booleanValue());

        arg1 = new LiteralExpression(2);
        arg2 = new LiteralExpression(1);
        lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = lessThanOrEqual.computeValue(context);
        assertTrue("Expected true but was false", result instanceof Boolean && ((Boolean) result).booleanValue());

        arg1 = new LiteralExpression(1);
        arg2 = new LiteralExpression(1);
        lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        result = lessThanOrEqual.computeValue(context);
        assertTrue("Expected true but was false", result instanceof Boolean && ((Boolean) result).booleanValue());
    }
}