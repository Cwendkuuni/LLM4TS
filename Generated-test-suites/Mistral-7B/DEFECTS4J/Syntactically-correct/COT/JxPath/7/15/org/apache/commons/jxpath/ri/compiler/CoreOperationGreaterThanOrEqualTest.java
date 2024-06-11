package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test cases for the CoreOperationGreaterThanOrEqual class.
 */
public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        // Test basic functionality
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new ConstExpression(5.0), new ConstExpression(5.0));
        EqualEvaluator evaluator = new EqualEvaluator();
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(infoSet);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // Test edge cases and exception handling
        testEdgeCases(operation, evaluator, context, 5.0, 4.9999999999, 5.0, 4.9999999998, 5.0, 5.1);
        testEdgeCases(operation, evaluator, context, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 0.0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        testEdgeCases(operation, evaluator, context, -0.0, 0.0, 0.0, -0.0, 0.0, 0.0);
    }

    private void testEdgeCases(CoreOperationGreaterThanOrEqual operation, EqualEvaluator evaluator, EvalContext context, double l, double r, double l2, double r2, double l3, double r3) {
        // Test edge cases with double values
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        Object result1 = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result1);
        infoSet.setValue(0, new DoubleValue(l));
        infoSet.setValue(1, new DoubleValue(r));
        context.setInfoSet(infoSet);
        result1 = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result1);

        infoSet.setValue(0, new DoubleValue(l2));
        infoSet.setValue(1, new DoubleValue(r2));
        context.setInfoSet(infoSet);
        Object result2 = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result2);

        infoSet.setValue(0, new DoubleValue(l3));
        infoSet.setValue(1, new DoubleValue(r3));
        context.setInfoSet(infoSet);
        Object result3 = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result3);
    }
}

class EqualEvaluator extends AbstractEvaluator {
    @Override
    public Object evaluate(Expression expression, EvalContext context) {
        return expression.computeValue(context);
    }
}