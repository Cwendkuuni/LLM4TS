package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expr.Expression;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        CoreOperationEqual operation = new CoreOperationEqual(createConstantExpression(1), createConstantExpression(1));
        EvalContext context = new EvalContext(new Object[]{1}, null);
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        operation = new CoreOperationEqual(createConstantExpression(1), createConstantExpression(2));
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        operation = new CoreOperationEqual(createConstantExpression("foo"), createConstantExpression("foo"));
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        operation = new CoreOperationEqual(createConstantExpression("foo"), createConstantExpression("bar"));
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    private Expression createConstantExpression(Object value) {
        return new CoreConstantExpression(value);
    }
}