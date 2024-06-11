package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.test.TestSupport;

import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        EvalContext context = TestSupport.newEvaluationContext();

        // Testing with double values
        Expression arg1 = new ConstantExpression(1.5);
        Expression arg2 = new ConstantExpression(1.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertTrue(coreOperationGreaterThanOrEqual.computeValue(context).equals(Boolean.TRUE));

        arg1 = new ConstantExpression(1.0);
        arg2 = new ConstantExpression(1.5);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertFalse(coreOperationGreaterThanOrEqual.computeValue(context).equals(Boolean.TRUE));

        arg1 = new ConstantExpression(1.5);
        arg2 = new ConstantExpression(1.5);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertTrue(coreOperationGreaterThanOrEqual.computeValue(context).equals(Boolean.TRUE));

        arg1 = new ConstantExpression(1.5);
        arg2 = new ConstantExpression(1.4);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertTrue(coreOperationGreaterThanOrEqual.computeValue(context).equals(Boolean.TRUE));

        arg1 = new ConstantExpression(-1.5);
        arg2 = new ConstantExpression(-1.5);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertTrue(coreOperationGreaterThanOrEqual.computeValue(context).equals(Boolean.TRUE));

        arg1 = new ConstantExpression(-1.5);
        arg2 = new ConstantExpression(-1.6);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertFalse(coreOperationGreaterThanOrEqual.computeValue(context).equals(Boolean.TRUE));

        arg1 = new ConstantExpression(Double.NaN);
        arg2 = new ConstantExpression(Double.NaN);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertFalse(coreOperationGreaterThanOrEqual.computeValue(context).equals(Boolean.TRUE));

        // Testing with String values
        arg1 = new StringConstantExpression("5");
        arg2 = new StringConstantExpression("4");
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertFalse(coreOperationGreaterThanOrEqual.computeValue(context).equals(Boolean.TRUE));

        arg1 = new StringConstantExpression("5");
        arg2 = new StringConstantExpression("5");
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertTrue(coreOperationGreaterThanOrEqual.computeValue(context).equals(Boolean.TRUE));

        arg1 = new StringConstantExpression("5");
        arg2 = new StringConstantExpression("5.0");
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertFalse(coreOperationGreaterThanOrEqual.computeValue(context).equals(Boolean.TRUE));

        arg1 = new StringConstantExpression("5.5");
        arg2 = new StringConstantExpression("5.0");
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertTrue(coreOperationGreaterThanOrEqual.computeValue(context).equals(Boolean.TRUE));

        // Testing with null values
        arg1 = new ConstantExpression(null);
        arg2 = new ConstantExpression(null);
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertTrue(coreOperationGreaterThanOrEqual.computeValue(context).equals(Boolean.TRUE));
    }
}