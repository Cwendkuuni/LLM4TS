package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        // Test with numeric values
        assertFalse(CoreOperationLessThan(new ConstantExpression(1.0), new ConstantExpression(1.0)).computeValue(new EvalContext()).booleanValue());
        assertFalse(CoreOperationLessThan(new ConstantExpression(1.0), new ConstantExpression(2.0)).computeValue(new EvalContext()).booleanValue());
        assertTrue(CoreOperationLessThan(new ConstantExpression(1.0), new ConstantExpression(0.0)).computeValue(new EvalContext()).booleanValue());
        assertTrue(CoreOperationLessThan(new ConstantExpression(0.5), new ConstantExpression(1.0)).computeValue(new EvalContext()).booleanValue());

        // Test with negative numbers
        assertTrue(CoreOperationLessThan(new ConstantExpression(-1.0), new ConstantExpression(-2.0)).computeValue(new EvalContext()).booleanValue());
        assertFalse(CoreOperationLessThan(new ConstantExpression(-1.0), new ConstantExpression(-1.0)).computeValue(new EvalContext()).booleanValue());

        // Test with expressions
        Expression arg1 = new VariableExpression("x");
        Expression arg2 = new ConstantExpression(1.0);
        assertTrue(CoreOperationLessThan(arg1, arg2).computeValue(new EvalContext()).booleanValue());

        arg1 = new VariableExpression("x");
        arg2 = new VariableExpression("y");
        EvalContext context = new EvalContext();
        context.set("x", 2.0);
        context.set("y", 1.0);
        assertTrue(CoreOperationLessThan(arg1, arg2).computeValue(context).booleanValue());

        context.set("x", 1.0);
        context.set("y", 2.0);
        assertFalse(CoreOperationLessThan(arg1, arg2).computeValue(context).booleanValue());
    }

    private CoreOperationLessThan CoreOperationLessThan(Expression arg1, Expression arg2) {
        return new CoreOperationLessThan(arg1, arg2);
    }
}