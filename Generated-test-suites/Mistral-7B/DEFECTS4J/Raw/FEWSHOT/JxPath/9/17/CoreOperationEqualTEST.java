package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression("1"), new LiteralExpression("1"));
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        coreOperationEqual = new CoreOperationEqual(new LiteralExpression("1"), new LiteralExpression("2"));
        result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        coreOperationEqual = new CoreOperationEqual(new LiteralExpression("1.0"), new LiteralExpression("1.0"));
        result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        coreOperationEqual = new CoreOperationEqual(new LiteralExpression("1.0"), new LiteralExpression("2.0"));
        result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        Expression arg1 = new VariableExpression("x");
        Expression arg2 = new LiteralExpression("1");
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        context.setValue("x", "1");
        result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        context.setValue("x", "2");
        result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
}