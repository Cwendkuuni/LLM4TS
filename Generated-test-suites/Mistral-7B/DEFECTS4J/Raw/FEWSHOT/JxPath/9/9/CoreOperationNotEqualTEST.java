package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expression.Expression;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = new ConstantExpression("abc");
        arg2 = new ConstantExpression("xyz");
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        arg1 = new ConstantExpression(1);
        arg2 = new ConstantExpression(1);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}