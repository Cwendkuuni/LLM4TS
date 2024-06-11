package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        Expression arg1 = new CoreNumberExpression(new Double(5.5));
        Expression arg2 = new CoreNumberExpression(new Double(6.5));
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(InfoSetUtil.EMPTY_INFOSET);
        assertEquals(Boolean.FALSE, coreOperationLessThanOrEqual.computeValue(context));

        arg2 = new CoreNumberExpression(new Double(5.5));
        assertEquals(Boolean.TRUE, coreOperationLessThanOrEqual.computeValue(context));

        arg1 = new CoreNumberExpression(new Double(3.5));
        arg2 = new CoreNumberExpression(new Double(3.5));
        assertEquals(Boolean.TRUE, coreOperationLessThanOrEqual.computeValue(context));
    }
}