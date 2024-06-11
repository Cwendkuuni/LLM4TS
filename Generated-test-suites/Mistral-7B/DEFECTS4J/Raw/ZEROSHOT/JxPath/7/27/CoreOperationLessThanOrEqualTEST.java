package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.test.TestHelper;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;

public class CoreOperationLessThanOrEqualTest {

    @org.junit.Test
    public void testCoreOperationLessThanOrEqual() {
        // Test with valid double values
        Expression arg1 = ExpressionFactory.newLiteralExpression(1.5);
        Expression arg2 = ExpressionFactory.newLiteralExpression(2.5);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        arg1 = ExpressionFactory.newLiteralExpression(1.5);
        arg2 = ExpressionFactory.newLiteralExpression(1.5);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);

        context = new EvalContext(new InfoSet());
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        arg1 = ExpressionFactory.newLiteralExpression(2.5);
        arg2 = ExpressionFactory.newLiteralExpression(1.5);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);

        context = new EvalContext(new InfoSet());
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // Test with null arguments
        arg1 = ExpressionFactory.newNullExpression();
        arg2 = ExpressionFactory.newLiteralExpression(1.5);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);

        context = new EvalContext(new InfoSet());
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(TestHelper.UNDEFINED, result);

        arg1 = ExpressionFactory.newLiteralExpression(1.5);
        arg2 = ExpressionFactory.newNullExpression();
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);

        context = new EvalContext(new InfoSet());
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // Test with invalid double values
        arg1 = ExpressionFactory.newLiteralExpression("abc");
        arg2 = ExpressionFactory.newLiteralExpression(1.5);
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);

        context = new EvalContext(new InfoSet());
        try {
            coreOperationLessThanOrEqual.computeValue(context);
            fail("Expected an IllegalArgumentException when trying to compare a non-numeric value with a number.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        arg1 = ExpressionFactory.newLiteralExpression(1.5);
        arg2 = ExpressionFactory.newLiteralExpression("abc");
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);

        context = new EvalContext(new InfoSet());
        try {
            coreOperationLessThanOrEqual.computeValue(context);
            fail("Expected an IllegalArgumentException when trying to compare a number with a non-numeric value.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}