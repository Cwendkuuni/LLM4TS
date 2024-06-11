package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg2 = new ConstantExpression(2);
        result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testCoreOperationEqualWithDifferentTypes() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new StringConstantExpression("1");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        arg1 = new StringConstantExpression("1");
        arg2 = new ConstantExpression(1);
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new StringConstantExpression("1");
        arg2 = new StringConstantExpression("1");
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new StringConstantExpression("1");
        arg2 = new StringConstantExpression("2");
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        result = coreOperationEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }
}