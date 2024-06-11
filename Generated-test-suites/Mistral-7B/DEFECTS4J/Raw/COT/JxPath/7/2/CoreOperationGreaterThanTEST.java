package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.expression.Expression;

public class CoreOperationGreaterThanTest {

    @org.junit.Test
    public void testCoreOperationGreaterThan() {
        // Test basic functionality
        Expression arg1 = new CoreConstantExpression(new Double(5.0));
        Expression arg2 = new CoreConstantExpression(new Double(3.0));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertTrue(result);

        // Test edge cases and exception handling

        // Test with double equal values
        arg1 = new CoreConstantExpression(new Double(5.0));
        arg2 = new CoreConstantExpression(new Double(5.0));
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertFalse(result);

        // Test with null arguments
        arg1 = new CoreConstantExpression(new Double(5.0));
        arg2 = null;
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        try {
            coreOperationGreaterThan.computeValue(context);
            fail("Should have thrown an NPE");
        } catch (NullPointerException e) {
            // Expected
        }

        arg1 = null;
        arg2 = new CoreConstantExpression(new Double(5.0));
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        try {
            coreOperationGreaterThan.computeValue(context);
            fail("Should have thrown an NPE");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test with non-numeric types
        arg1 = new CoreConstantExpression(new String("5"));
        arg2 = new CoreConstantExpression(new String("3"));
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        try {
            coreOperationGreaterThan.computeValue(context);
            fail("Should have thrown a TypeMismatchException");
        } catch (ClassCastException e) {
            // Expected
        }

        arg1 = new CoreConstantExpression(new Integer(5));
        arg2 = new CoreConstantExpression(new Integer(3));
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertTrue(coreOperationGreaterThan.getType() == Boolean.TYPE);

        result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertTrue(result);

        arg1 = new CoreConstantExpression(new Double("5.0"));
        arg2 = new CoreConstantExpression(new Double("3.0"));
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertTrue(coreOperationGreaterThan.getType() == Boolean.TYPE);

        result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertTrue(result);
    }
}