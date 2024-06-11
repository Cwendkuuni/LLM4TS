package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.TestHelpers;
import org.junit.Test;

/**
 * Test class for CoreOperationGreaterThan.
 */
public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        // Test with double values
        double left = 5.0;
        double right = 3.0;
        Expression arg1 = TestHelpers.newLiteralExpression(left);
        Expression arg2 = TestHelpers.newLiteralExpression(right);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertTrue(result);

        // Test with negative double values
        left = -5.0;
        right = -3.0;
        arg1 = TestHelpers.newLiteralExpression(left);
        arg2 = TestHelpers.newLiteralExpression(right);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext();
        result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertTrue(result);

        // Test with double value less than right value
        left = 3.0;
        right = 5.0;
        arg1 = TestHelpers.newLiteralExpression(left);
        arg2 = TestHelpers.newLiteralExpression(right);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext();
        result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertFalse(result);

        // Test with double value equal to right value
        left = 5.0;
        right = 5.0;
        arg1 = TestHelpers.newLiteralExpression(left);
        arg2 = TestHelpers.newLiteralExpression(right);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext();
        result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertFalse(result);

        // Test with non-numeric values
        left = "5";
        right = "3";
        arg1 = TestHelpers.newLiteralExpression(left);
        arg2 = TestHelpers.newLiteralExpression(right);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext();
        try {
            coreOperationGreaterThan.computeValue(context);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }

        left = 5;
        right = "3";
        arg1 = TestHelpers.newLiteralExpression(left);
        arg2 = TestHelpers.newLiteralExpression(right);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext();
        try {
            coreOperationGreaterThan.computeValue(context);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Expected
        }

        left = 5.0;
        right = null;
        arg1 = TestHelpers.newLiteralExpression(left);
        arg2 = TestHelpers.newLiteralExpression(right);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext();
        try {
            coreOperationGreaterThan.computeValue(context);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}