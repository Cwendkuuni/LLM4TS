package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.test.AbstractJXPathTest;

public class CoreOperationLessThanOrEqualTest extends AbstractJXPathTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testCoreOperationLessThanOrEqual() {
        Object leftValue = 5.0;
        Object rightValue = 6.0;

        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(
                new ConstantExpression(leftValue), new ConstantExpression(rightValue));

        EvalContext context = new EvalContext();

        Boolean result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);

        assertFalse(result.booleanValue());

        rightValue = 5.0;

        result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);

        assertTrue(result.booleanValue());

        leftValue = 3.0;
        rightValue = 4.0;

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new ConstantExpression(leftValue),
                new ConstantExpression(rightValue));

        result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);

        assertTrue(result.booleanValue());

        leftValue = 4.0;
        rightValue = 4.0;

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new ConstantExpression(leftValue),
                new ConstantExpression(rightValue));

        result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);

        assertFalse(result.booleanValue());
    }

    @Test
    public void testCoreOperationLessThanOrEqualWithExpressions() {
        double leftValue = 5.0;
        double rightValue = 6.0;

        Expression leftExpression = new VariableExpression("x");
        Expression rightExpression = new ConstantExpression(rightValue);

        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(leftExpression,
                rightExpression);

        EvalContext context = new EvalContext();
        context.setVariable("x", leftValue);

        Boolean result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);

        assertFalse(result.booleanValue());

        rightValue = 5.0;

        result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);

        assertTrue(result.booleanValue());

        leftValue = 3.0;
        rightValue = 4.0;

        leftExpression = new VariableExpression("x");
        rightExpression = new VariableExpression("y");
        context.setVariable("x", leftValue);
        context.setVariable("y", rightValue);

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(leftExpression, rightExpression);

        result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);

        assertTrue(result.booleanValue());

        leftValue = 4.0;
        rightValue = 4.0;

        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(leftExpression, rightExpression);

        result = (Boolean) coreOperationLessThanOrEqual.computeValue(context);

        assertFalse(result.booleanValue());
    }
}