package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.apache.commons.jxpath.ri.expression.Literal;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        Expression arg1 = new Literal(1);
        Expression arg2 = new Literal(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        arg1 = new Literal(1);
        arg2 = new Literal(2);
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((boolean) result);

        arg1 = new Literal("foo");
        arg2 = new Literal("foo");
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        arg1 = new Literal("foo");
        arg2 = new Literal("bar");
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((boolean) result);
    }
}