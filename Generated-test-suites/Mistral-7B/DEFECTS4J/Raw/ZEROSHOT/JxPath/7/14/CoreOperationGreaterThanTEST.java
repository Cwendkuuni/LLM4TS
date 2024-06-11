package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreExpression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for CoreOperationGreaterThan class.
 */
public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        // Test with valid inputs
        CoreExpression arg1 = new CoreNumber(1.5);
        CoreExpression arg2 = new CoreNumber(1.0);
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new Path(""), new InfoSet());

        Object result = expression.computeValue(context);
        assertTrue("Expected true but got false", result instanceof Boolean && ((Boolean) result).booleanValue());

        arg1 = new CoreNumber(1);
        arg2 = new CoreNumber(2);
        expression = new CoreOperationGreaterThan(arg1, arg2);
        result = expression.computeValue(context);
        assertFalse("Expected false but got true", result instanceof Boolean && ((Boolean) result).booleanValue());

        arg1 = new CoreNumber(5.5);
        arg2 = new CoreNumber(5.0);
        expression = new CoreOperationGreaterThan(arg1, arg2);
        result = expression.computeValue(context);
        assertTrue("Expected true but got false", result instanceof Boolean && ((Boolean) result).booleanValue());

        arg1 = new CoreNumber(Double.MAX_VALUE);
        arg2 = new CoreNumber(Double.MAX_VALUE);
        expression = new CoreOperationGreaterThan(arg1, arg2);
        result = expression.computeValue(context);
        assertFalse("Expected false but got true", result instanceof Boolean && !((Boolean) result).booleanValue());

        arg1 = new CoreNumber(Double.MIN_VALUE);
        arg2 = new CoreNumber(Double.MIN_VALUE);
        expression = new CoreOperationGreaterThan(arg1, arg2);
        result = expression.computeValue(context);
        assertFalse("Expected false but got true", result instanceof Boolean && !((Boolean) result).booleanValue());

        // Test with invalid inputs
        arg1 = null;
        arg2 = new CoreNumber(1.0);
        expression = new CoreOperationGreaterThan(arg1, arg2);
        try {
            expression.computeValue(context);
            fail("Expected IllegalArgumentException but no exception was thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        arg1 = new CoreNumber(1.0);
        arg2 = null;
        expression = new CoreOperationGreaterThan(arg1, arg2);
        try {
            expression.computeValue(context);
            fail("Expected IllegalArgumentException but no exception was thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}