package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.Expression;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testCoreOperationLessThanOrEqual() {
        Expression arg1 = new CoreConstantExpression(new Double(5.0));
        Expression arg2 = new CoreConstantExpression(new Double(10.0));
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(new Object(), null);

        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        arg2 = new CoreConstantExpression(new Double(5.0));
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        arg1 = new CoreConstantExpression(new Double(5.0));
        arg2 = new CoreVariableExpression("x");
        context.registerVariable("x", new Double(7.0));
        result = operation.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        arg1 = new CoreVariableExpression("y");
        arg2 = new CoreConstantExpression(new Double(5.0));
        context.registerVariable("y", new Double(7.0));
        result = operation.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(null, null);
        assertEquals("<=", operation.getSymbol());
    }
}