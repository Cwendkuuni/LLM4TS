package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(5.0), new ConstantExpression(4.0));

        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext(new InfoSet());
        expression = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(4.0), new ConstantExpression(5.0));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        context = new EvalContext(new InfoSet());
        expression = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(4.5), new ConstantExpression(4.5));
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        context = new EvalContext(new InfoSet());
        expression = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression("5"), new ConstantExpression("4"));
        assertFalse(expression.computeValue(context) instanceof Boolean);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithComplexExpressions() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new AddExpression(
                        new MultiplyExpression(new ConstantExpression(2.0), new ConstantExpression(3.0)),
                        new ConstantExpression(1.0)),
                new ConstantExpression(5.0));

        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expression = new CoreOperationGreaterThanOrEqual(
                new SubtractExpression(new ConstantExpression(2.0), new ConstantExpression(3.0)),
                new ConstantExpression(5.0));
        assertFalse(expression.computeValue(context) instanceof Boolean);
    }
}