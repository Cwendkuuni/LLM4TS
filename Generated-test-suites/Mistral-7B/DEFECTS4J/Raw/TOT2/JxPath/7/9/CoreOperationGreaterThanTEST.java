package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        Expression arg1 = new CoreNumberExpression(1.0);
        Expression arg2 = new CoreNumberExpression(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThan.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue() {
        Expression arg1 = new CoreNumberExpression(1.0);
        Expression arg2 = new CoreNumberExpression(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThan.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        arg1 = new CoreNumberExpression(-1.0);
        arg2 = new CoreNumberExpression(1.0);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext();
        result = coreOperationGreaterThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        arg1 = new CoreNumberExpression(0.0);
        arg2 = new CoreNumberExpression(0.0);
        coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext();
        result = coreOperationGreaterThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = ClassCastException.class)
    public void testComputeValueWithInvalidArguments() {
        Expression arg1 = new CoreStringExpression("1");
        Expression arg2 = new CoreNumberExpression(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationGreaterThan.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueWithNullArguments() {
        Expression arg1 = null;
        Expression arg2 = new CoreNumberExpression(2.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        coreOperationGreaterThan.computeValue(context);
    }
}