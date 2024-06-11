package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.test.TestSupport;

public class CoreOperationLessThanTest {

    @Test
    public void testLessThan() {
        EvalContext context = TestSupport.createEvaluationContext();

        Expression arg1 = new ConstantExpression(1.0, context);
        Expression arg2 = new ConstantExpression(2.0, context);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);

        assertTrue(lessThan.computeValue(context).booleanValue());

        arg1 = new ConstantExpression(2.0, context);
        arg2 = new ConstantExpression(1.0, context);
        lessThan = new CoreOperationLessThan(arg1, arg2);

        assertFalse(lessThan.computeValue(context).booleanValue());
    }

    @Test
    public void testLessThanWithNaN() {
        EvalContext context = TestSupport.createEvaluationContext();

        Expression arg1 = new ConstantExpression(Double.NaN, context);
        Expression arg2 = new ConstantExpression(1.0, context);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);

        assertFalse(lessThan.computeValue(context).booleanValue());

        arg1 = new ConstantExpression(1.0, context);
        arg2 = new ConstantExpression(Double.NaN, context);
        lessThan = new CoreOperationLessThan(arg1, arg2);

        assertFalse(lessThan.computeValue(context).booleanValue());

        arg1 = new ConstantExpression(Double.NaN, context);
        arg2 = new ConstantExpression(Double.NaN, context);
        lessThan = new CoreOperationLessThan(arg1, arg2);

        assertFalse(lessThan.computeValue(context).booleanValue());
    }

    @Test
    public void testLessThanWithZero() {
        EvalContext context = TestSupport.createEvaluationContext();

        Expression arg1 = new ConstantExpression(0.0, context);
        Expression arg2 = new ConstantExpression(1.0, context);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(arg1, arg2);

        assertFalse(lessThan.computeValue(context).booleanValue());

        arg1 = new ConstantExpression(1.0, context);
        arg2 = new ConstantExpression(0.0, context);
        lessThan = new CoreOperationLessThan(arg1, arg2);

        assertTrue(lessThan.computeValue(context).booleanValue());
    }
}