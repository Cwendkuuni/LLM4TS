package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(createExpression(1), createExpression(2));
        assertFalse(notEqual.getSymbol().equals("="));
        assertFalse(notEqual.getSymbol().equals("!="));

        Expression arg1 = createExpression(1);
        Expression arg2 = createExpression(1);
        CoreOperationNotEqual notEqual1 = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object value1 = notEqual1.computeValue(context);
        assertTrue(value1 instanceof Boolean);
        assertEquals(Boolean.TRUE, value1);

        arg1 = createExpression(1);
        arg2 = createExpression("1");
        CoreOperationNotEqual notEqual2 = new CoreOperationNotEqual(arg1, arg2);
        value1 = notEqual2.computeValue(context);
        assertTrue(value1 instanceof Boolean);
        assertEquals(Boolean.FALSE, value1);

        arg1 = createExpression("1");
        arg2 = createExpression(1);
        CoreOperationNotEqual notEqual3 = new CoreOperationNotEqual(arg1, arg2);
        value1 = notEqual3.computeValue(context);
        assertTrue(value1 instanceof Boolean);
        assertEquals(Boolean.FALSE, value1);

        arg1 = createExpression("1");
        arg2 = createExpression("1");
        CoreOperationNotEqual notEqual4 = new CoreOperationNotEqual(arg1, arg2);
        value1 = notEqual4.computeValue(context);
        assertTrue(value1 instanceof Boolean);
        assertEquals(Boolean.TRUE, value1);

        arg1 = createExpression(null);
        arg2 = createExpression(null);
        CoreOperationNotEqual notEqual5 = new CoreOperationNotEqual(arg1, arg2);
        value1 = notEqual5.computeValue(context);
        assertTrue(value1 instanceof Boolean);
        assertEquals(Boolean.UNKNOWN, value1);

        arg1 = createExpression(null);
        arg2 = createExpression(1);
        CoreOperationNotEqual notEqual6 = new CoreOperationNotEqual(arg1, arg2);
        value1 = notEqual6.computeValue(context);
        assertNull(value1);

        arg1 = createExpression(1);
        arg2 = createExpression(null);
        CoreOperationNotEqual notEqual7 = new CoreOperationNotEqual(arg1, arg2);
        value1 = notEqual7.computeValue(context);
        assertNull(value1);
    }

    private Expression createExpression(int value) {
        return new CoreConstantExpression(Integer.valueOf(value));
    }

    private Expression createExpression(String value) {
        return new CoreConstantStringExpression(value);
    }
}