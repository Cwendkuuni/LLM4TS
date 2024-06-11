package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new ConstantExpression(1.0), new ConstantExpression(2.0));

        EvalContext context = new EvalContext();
        Object result = coreOperationLessThanOrEqual.computeValue(context);

        assertEquals(Boolean.FALSE, result);

        context = new EvalContext();
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new ConstantExpression(1.0), new ConstantExpression(1.0));

        result = coreOperationLessThanOrEqual.computeValue(context);

        assertEquals(Boolean.TRUE, result);

        context = new EvalContext();
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new ConstantExpression(2.0), new ConstantExpression(1.0));

        result = coreOperationLessThanOrEqual.computeValue(context);

        assertEquals(Boolean.TRUE, result);

        context = new EvalContext();
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new ConstantExpression(-1.0), new ConstantExpression(-2.0));

        result = coreOperationLessThanOrEqual.computeValue(context);

        assertEquals(Boolean.TRUE, result);

        context = new EvalContext();
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new ConstantExpression(-1.0), new ConstantExpression(-1.0));

        result = coreOperationLessThanOrEqual.computeValue(context);

        assertEquals(Boolean.TRUE, result);
    }
}