package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new ConstantExpression(new Double(5.0)),
                new ConstantExpression(new Double(3.0)));

        EvalContext context = new EvalContext(new Object(), new InfoSetUtil.EmptyInfoSet());
        assertEquals(Boolean.TRUE, coreOperationGreaterThan.computeValue(context));

        context = new EvalContext(new Object(), new InfoSetUtil.EmptyInfoSet());
        coreOperationGreaterThan = new CoreOperationGreaterThan(
                new ConstantExpression(new Double(3.0)),
                new ConstantExpression(new Double(5.0)));
        assertEquals(Boolean.FALSE, coreOperationGreaterThan.computeValue(context));

        context = new EvalContext(new Object(), new InfoSetUtil.EmptyInfoSet());
        coreOperationGreaterThan = new CoreOperationGreaterThan(
                new ConstantExpression(new Double.NaN()),
                new ConstantExpression(new Double.NaN()));
        assertNull(coreOperationGreaterThan.computeValue(context));
    }

    @Test
    public void testCoreOperationGreaterThanWithExpressions() {
        Expression arg1 = new VariableExpression("x");
        Expression arg2 = new VariableExpression("y");

        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext(new Object(), new InfoSetUtil.EmptyInfoSet());
        context.setVariable("x", new Double(5.0));
        context.setVariable("y", new Double(3.0));

        assertEquals(Boolean.TRUE, coreOperationGreaterThan.computeValue(context));

        context.setVariable("x", new Double(3.0));
        context.setVariable("y", new Double(5.0));

        assertEquals(Boolean.FALSE, coreOperationGreaterThan.computeValue(context));
    }
}