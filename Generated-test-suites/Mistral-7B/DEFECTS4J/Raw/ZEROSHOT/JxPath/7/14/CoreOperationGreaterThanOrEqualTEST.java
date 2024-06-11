package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

/**
 * Test class for CoreOperationGreaterThanOrEqual.
 */
public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        // Test with double values
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(5.5), new ConstantExpression(4.5));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = operation.computeValue(context);
        assertTrue("Expected true but got: " + result, result instanceof Boolean
                && ((Boolean) result).booleanValue());

        // Test with double values, false case
        operation = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(4.5), new ConstantExpression(5.5));
        context = new EvalContext(infoSet);
        result = operation.computeValue(context);
        assertFalse("Expected false but got: " + result, result instanceof Boolean
                && ((Boolean) result).booleanValue());

        // Test with double values, equal case
        operation = new CoreOperationGreaterThanOrEqual(
                new ConstantExpression(5.5), new ConstantExpression(5.5));
        context = new EvalContext(infoSet);
        result = operation.computeValue(context);
        assertTrue("Expected true but got: " + result, result instanceof Boolean
                && ((Boolean) result).booleanValue());

        // Test with expressions that return double values
        Expression arg1 = new VariableExpression("x");
        Expression arg2 = new ConstantExpression(4.5);
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        InfoSet infoSet1 = new InfoSet();
        context = new EvalContext(infoSet1);
        infoSet1.setItem(0, new Double(6.5));
        result = expression.computeValue(context);
        assertTrue("Expected true but got: " + result, result instanceof Boolean
                && ((Boolean) result).booleanValue());

        infoSet1.setItem(0, new Double(4.5));
        context = new EvalContext(infoSet1);
        result = expression.computeValue(context);
        assertFalse("Expected false but got: " + result, result instanceof Boolean
                && ((Boolean) result).booleanValue());

        VariableExpression y = new VariableExpression("y");
        expression = new CoreOperationGreaterThanOrEqual(arg1, y);
        InfoSet infoSet2 = new InfoSet();
        context = new EvalContext(infoSet2);
        infoSet2.setItem(0, new Double(5.5));
        infoSet2.setItem(1, new Double(5.5));
        result = expression.computeValue(context);
        assertTrue("Expected true but got: " + result, result instanceof Boolean
                && ((Boolean) result).booleanValue());

        infoSet2.setItem(1, new Double(6.5));
        context = new EvalContext(infoSet2);
        result = expression.computeValue(context);
        assertTrue("Expected true but got: " + result, result instanceof Boolean
                && ((Boolean) result).booleanValue());

        infoSet2.setItem(1, new Double(4.5));
        context = new EvalContext(infoSet2);
        result = expression.computeValue(context);
        assertFalse("Expected false but got: " + result, result instanceof Boolean
                && ((Boolean) result).booleanValue());
    }
}