package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(2));
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));

        coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1), new ConstantExpression(1));
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqualWithNull() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(1), null);
        assertNull(coreOperationNotEqual.computeValue(new EvalContext()));

        coreOperationNotEqual = new CoreOperationNotEqual(null, new ConstantExpression(1));
        assertNull(coreOperationNotEqual.computeValue(new EvalContext()));

        coreOperationNotEqual = new CoreOperationNotEqual(null, null);
        assertNull(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationNotEqualWithComplexExpressions() {
        Expression arg1 = new VariableExpression("x");
        Expression arg2 = new ConstantExpression(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));

        arg1 = new ArithmeticExpression(new VariableExpression("x"), new ValueExpression(2), Operator.DIVIDE);
        arg2 = new ConstantExpression(1);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));

        arg1 = new VariableExpression("x");
        arg2 = new ArithmeticExpression(new ConstantExpression(1), new ValueExpression(2), Operator.MULTIPLY);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));
    }
}