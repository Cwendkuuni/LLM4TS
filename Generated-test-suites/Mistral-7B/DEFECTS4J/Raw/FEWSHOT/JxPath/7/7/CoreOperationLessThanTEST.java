package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        Expression arg1 = new LiteralExpression(new Double(1.0));
        Expression arg2 = new LiteralExpression(new Double(2.0));
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression(new Double(1.0));
        arg2 = new LiteralExpression(new Double(1.0));
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression(new Double(1.0));
        arg2 = new LiteralExpression(new Double(0.5));
        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOperationLessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }
}