package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        Expression arg1 = new CoreNumberExpression(new Double(5.0));
        Expression arg2 = new CoreNumberExpression(new Double(3.0));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new Object(), new InfoSetUtil.EmptyInfoSet());
        assertTrue(coreOperationGreaterThan.computeValue(context).equals(Boolean.TRUE));

        arg1 = new CoreNumberExpression(new Double(3.0));
        arg2 = new CoreNumberExpression(new Double(5.0));
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertFalse(coreOperationGreaterThan.computeValue(context).equals(Boolean.TRUE));

        arg1 = new CoreNumberExpression(new Double(5.0));
        arg2 = new CoreNumberExpression(new Double(5.0));
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertFalse(coreOperationGreaterThan.computeValue(context).equals(Boolean.TRUE));
    }
}