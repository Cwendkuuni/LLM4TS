package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;

public class CoreOperationLessThanTest {

    @org.junit.Test
    public void testCoreOperationLessThan() {
        // Test with double values
        Expression arg1 = new CoreNumberExpression(1.5);
        Expression arg2 = new CoreNumberExpression(2.5);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);

        arg1 = new CoreNumberExpression(2.5);
        arg2 = new CoreNumberExpression(1.5);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) coreOperationLessThan.computeValue(context);
        assertFalse(result);

        arg1 = new CoreNumberExpression(1.5);
        arg2 = new CoreNumberExpression(1.5);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) coreOperationLessThan.computeValue(context);
        assertFalse(result);

        // Test with int values
        arg1 = new CoreNumberExpression(1);
        arg2 = new CoreNumberExpression(2);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) coreOperationLessThan.computeValue(context);
        assertFalse(result);

        arg1 = new CoreNumberExpression(1);
        arg2 = new CoreNumberExpression(0);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);

        // Test with negative int values
        arg1 = new CoreNumberExpression(-1);
        arg2 = new CoreNumberExpression(-2);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);

        arg1 = new CoreNumberExpression(-1);
        arg2 = new CoreNumberExpression(-1);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) coreOperationLessThan.computeValue(context);
        assertFalse(result);

        // Test with null arguments
        arg1 = new CoreNumberExpression(null);
        arg2 = new CoreNumberExpression(1.5);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) coreOperationLessThan.computeValue(context);
        assertNull(result);

        arg1 = new CoreNumberExpression(1.5);
        arg2 = new CoreNumberExpression(null);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = (Boolean) coreOperationLessThan.computeValue(context);
        assertNull(result);
    }

    // Add other test methods if necessary

    @org.junit.Before
    public void setUp() {
        // Initialize resources if needed
    }

    @org.junit.After
    public void tearDown() {
        // Release resources if needed
    }
}