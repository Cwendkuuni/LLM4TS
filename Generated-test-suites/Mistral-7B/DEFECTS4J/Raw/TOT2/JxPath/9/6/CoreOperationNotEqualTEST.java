package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.AbstractTest;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() throws Exception {
        // Test Case from Expert 1
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(2);

        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = notEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        arg1 = new LiteralExpression(2);
        arg2 = new LiteralExpression(1);
        notEqual = new CoreOperationNotEqual(arg1, arg2);
        result = notEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationNotEqualEdgeCases() throws Exception {
        // Test Case from Expert 2
        Expression arg1 = new LiteralExpression(null);
        Expression arg2 = new LiteralExpression(1);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
        assertThrows(RuntimeException.class, () -> notEqual.computeValue(new EvalContext()));

        arg1 = new LiteralExpression(1);
        arg2 = new LiteralExpression(null);
        notEqual = new CoreOperationNotEqual(arg1, arg2);
        assertThrows(RuntimeException.class, () -> notEqual.computeValue(new EvalContext()));

        arg1 = new LiteralExpression(1);
        arg2 = new LiteralExpression(1);
        notEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = notEqual.computeValue(new EvalContext());
        assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression("test");
        arg2 = new LiteralExpression(1);
        notEqual = new CoreOperationNotEqual(arg1, arg2);
        assertThrows(RuntimeException.class, () -> notEqual.computeValue(new EvalContext()));

        arg1 = new LiteralExpression(1);
        arg2 = new LiteralExpression("test");
        notEqual = new CoreOperationNotEqual(arg1, arg2);
        assertThrows(RuntimeException.class, () -> notEqual.computeValue(new EvalContext()));
    }
}