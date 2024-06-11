package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        // Test with double values
        EvalContext context = new EvalContext(InfoSetUtil.createInfoSet());
        Expression arg1 = new CoreNumberExpression(new Double(5.0), context);
        Expression arg2 = new CoreNumberExpression(new Double(10.0), context);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertTrue(coreOperationLessThan.computeValue(context).equals(Boolean.TRUE));

        arg1 = new CoreNumberExpression(new Double(10.0), context);
        arg2 = new CoreNumberExpression(new Double(5.0), context);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertFalse(coreOperationLessThan.computeValue(context).equals(Boolean.TRUE));

        arg1 = new CoreNumberExpression(new Double(5.0), context);
        arg2 = new CoreNumberExpression(new Double(5.0), context);
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertFalse(coreOperationLessThan.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testCoreOperationLessThanWithNull() {
        EvalContext context = new EvalContext(InfoSetUtil.createInfoSet());
        Expression arg1 = new CoreNullExpression();
        Expression arg2 = new CoreNumberExpression(new Double(5.0), context);
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertNull(coreOperationLessThan.computeValue(context));

        arg1 = new CoreNumberExpression(new Double(5.0), context);
        coreOperationLessThan = new CoreOperationLessThan(arg1, new CoreNullExpression());
        assertFalse(coreOperationLessThan.computeValue(context).equals(Boolean.TRUE));
    }
}