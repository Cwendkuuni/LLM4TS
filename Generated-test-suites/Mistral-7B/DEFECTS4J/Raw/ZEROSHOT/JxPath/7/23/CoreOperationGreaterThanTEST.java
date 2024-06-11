package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

/**
 * Test class for CoreOperationGreaterThan class.
 */
public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThanWithTwoDoubleValues() {
        // Test case 1: l > r where l and r are both positive numbers
        Expression arg1 = new LiteralExpression(5.0);
        Expression arg2 = new LiteralExpression(3.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertTrue(result);

        // Test case 2: l <= r where l and r are both positive numbers
        arg1 = new LiteralExpression(3.0);
        arg2 = new LiteralExpression(5.0);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertFalse(result);

        // Test case 3: l > r where l is a positive number and r is a negative number
        arg1 = new LiteralExpression(5.0);
        arg2 = new LiteralExpression(-3.0);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertTrue(result);

        // Test case 4: l <= r where l is a positive number and r is a negative number
        arg1 = new LiteralExpression(3.0);
        arg2 = new LiteralExpression(-5.0);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertFalse(result);

        // Test case 5: l > r where l and r are both zero
        arg1 = new LiteralExpression(0.0);
        arg2 = new LiteralExpression(0.0);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertFalse(result);

        // Test case 6: l <= r where l and r are both zero
        arg1 = new LiteralExpression(0.0);
        arg2 = new LiteralExpression(0.0);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertTrue(result);

        // Test case 7: l > r where l is a positive number and r is a very small negative number
        arg1 = new LiteralExpression(0.000001);
        arg2 = new LiteralExpression(-0.000001);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertTrue(result);

        // Test case 8: l <= r where l is a positive number and r is a very small negative number
        arg1 = new LiteralExpression(0.000001);
        arg2 = new LiteralExpression(-0.000001);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanWithInvalidArguments() {
        // Test case 1: l = null and r is a valid expression
        Expression arg1 = null;
        Expression arg2 = new LiteralExpression(5.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        // Test case 2: l is a valid expression and r = null
        Expression arg1 = new LiteralExpression(5.0);
        Expression arg2 = null;
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        // Test case 3: both l and r are null
        Expression arg1Null = null;
        Expression arg2Null = null;
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1Null, arg2Null);

        // Test case 4: l is an invalid expression
        Expression arg1Invalid = new CoreOperationGreaterThan(null, new LiteralExpression(5.0));
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1Invalid, arg2);

        // Test case 5: r is an invalid expression
        Expression arg1 = new LiteralExpression(5.0);
        Expression arg2Invalid = new CoreOperationGreaterThan(arg1, null);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2Invalid);

        // Test case 6: l and r are both invalid expressions
        Expression arg1Invalid2 = new CoreOperationGreaterThan(null, null);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1Invalid2, arg2);

        coreOperationGreaterThan.computeValue(context);
    }
}