package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        CoreOperationLessThan opLessThan = new CoreOperationLessThan(new ConstantExpression(1.0), new ConstantExpression(2.0));
        EvalContext context = new EvalContext();
        Object result = opLessThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        opLessThan = new CoreOperationLessThan(new ConstantExpression(1.0), new ConstantExpression(1.0));
        result = opLessThan.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationLessThanWithVariousDataTypes() {
        CoreOperationLessThan opLessThan = new CoreOperationLessThan(new NumberExpression(1), new NumberExpression(2.0));
        EvalContext context = new EvalContext();
        Object result = opLessThan.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        opLessThan = new CoreOperationLessThan(new NumberExpression(1.0), new NumberExpression(2.0));
        result = opLessThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        opLessThan = new CoreOperationLessThan(new NumberExpression(1.0), new NumberExpression(1.0));
        result = opLessThan.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        opLessThan = new CoreOperationLessThan(new StringExpression("a"), new StringExpression("b"));
        result = opLessThan.computeValue(context);
        assertNull(result);

        opLessThan = new CoreOperationLessThan(new NumberExpression(1.0), new StringExpression("1"));
        context.setVariable("x", new NumberExpression(1.0));
        result = opLessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        opLessThan = new CoreOperationLessThan(new StringExpression("a"), new StringExpression("b"));
        context.setVariable("x", new StringExpression("a"));
        context.setVariable("y", new StringExpression("b"));
        result = opLessThan.computeValue(context);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanWithInvalidArgumentTypes() {
        CoreOperationLessThan opLessThan = new CoreOperationLessThan(new ConstantExpression(1), new BinaryOperatorExpression("+"));
    }
}