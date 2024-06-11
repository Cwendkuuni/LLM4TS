package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValueWithDoubleValues() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(5.5)),
                new CoreConstantExpression(new Double(3.3)));

        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationGreaterThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext(new InfoSet());
        coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(3.3)),
                new CoreConstantExpression(new Double(5.5)));

        result = coreOperationGreaterThan.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertFalse((Boolean) result);

        coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(5.5)),
                new CoreConstantExpression(new Double(5.5)));

        result = coreOperationGreaterThan.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueWithMixedValues() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreConstantExpression(new Double(5.5)),
                new CoreVariableExpression("x"));

        EvalContext context = new EvalContext(new InfoSet());
        context.setVariable("x", new Double(3.3));
        Object result = coreOperationGreaterThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext(new InfoSet());
        coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreVariableExpression("x"),
                new CoreConstantExpression(new Double(5.5)));

        context.setVariable("x", new Double(3.3));
        result = coreOperationGreaterThan.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertFalse((Boolean) result);

        context = new EvalContext(new InfoSet());
        coreOperationGreaterThan = new CoreOperationGreaterThan(
                new CoreVariableExpression("x"),
                new CoreVariableExpression("y"));

        context.setVariable("x", new Double(5.5));
        context.setVariable("y", new Double(3.3));
        result = coreOperationGreaterThan.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context.setVariable("x", new Double(3.3));
        context.setVariable("y", new Double(5.5));
        result = coreOperationGreaterThan.computeValue(context);

        assertFalse(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
}