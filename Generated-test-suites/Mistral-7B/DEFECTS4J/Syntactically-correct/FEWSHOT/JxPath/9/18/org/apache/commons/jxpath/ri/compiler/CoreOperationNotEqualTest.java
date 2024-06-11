package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(2);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        arg1 = new LiteralExpression(2);
        arg2 = new LiteralExpression(1);
        notEqual = new CoreOperationNotEqual(arg1, arg2);

        result = notEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((boolean) result);
    }
}