package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        // Test with valid numbers
        Expression arg1 = new CoreNumberExpression(1.0);
        Expression arg2 = new CoreNumberExpression(2.0);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertTrue(coreOperationLessThan.computeValue(new EvalContext()).booleanValue());

        arg1 = new CoreNumberExpression(2.0);
        arg2 = new CoreNumberExpression(1.0);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertTrue(coreOperationLessThan.computeValue(new EvalContext()).booleanValue());

        arg1 = new CoreNumberExpression(1.0);
        arg2 = new CoreNumberExpression(1.0);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertFalse(coreOperationLessThan.computeValue(new EvalContext()).booleanValue());

        // Test with null arguments
        Expression nullArg1 = new CoreNullExpression();
        Expression nullArg2 = new CoreNullExpression();
        CoreOperationLessThan coreOperationLessThanNull = new CoreOperationLessThan(nullArg1, nullArg2);
        assertNull(coreOperationLessThanNull.computeValue(new EvalContext()));
    }
}