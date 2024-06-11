package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CoreOperationLessThanOrEqual.
 */
public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        // Basic test case for CoreOperationLessThanOrEqual method with valid inputs
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new ConstExpression(1.0), new ConstExpression(2.0));
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) operation.computeValue(context);
        assertTrue(result);

        // Test edge cases
        testEdgeCases(operation, context);
    }

    // Test method for edge cases and exceptions
    private void testEdgeCases(CoreOperationLessThanOrEqual operation, EvalContext context) {
        // Test with equal values
        operation = new CoreOperationLessThanOrEqual(new ConstExpression(1.0), new ConstExpression(1.0));
        Boolean result = (Boolean) operation.computeValue(context);
        assertTrue(result);

        // Test with reversed order of arguments
        operation = new CoreOperationLessThanOrEqual(new ConstExpression(2.0), new ConstExpression(1.0));
        result = (Boolean) operation.computeValue(context);
        assertFalse(result);

        // Test with NaN values
        operation = new CoreOperationLessThanOrEqual(new ConstExpression(Double.NaN), new ConstExpression(Double.NaN));
        result = (Boolean) operation.computeValue(context);
        assertFalse(result);

        // Test with negative numbers
        operation = new CoreOperationLessThanOrEqual(new ConstExpression(-1.0), new ConstExpression(-2.0));
        result = (Boolean) operation.computeValue(context);
        assertTrue(result);

        // Test with large numbers
        operation = new CoreOperationLessThanOrEqual(new ConstExpression(Long.MAX_VALUE), new ConstExpression(Long.MAX_VALUE + 1));
        result = (Boolean) operation.computeValue(context);
        assertFalse(result);
    }
}