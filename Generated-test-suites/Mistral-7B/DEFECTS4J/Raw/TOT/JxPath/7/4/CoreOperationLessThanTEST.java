package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for CoreOperationLessThan.
 */
public class CoreOperationLessThanTest {

    @Test
    public void testLessThan() {
        // Test with valid double numbers
        CoreOperationLessThan expression = new CoreOperationLessThan(
                new ConstantExpression(new Double(5.5)),
                new ConstantExpression(new Double(3.3)));
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);
        Object result = expression.computeValue(context);
        assertEquals("Expected result to be true", Boolean.TRUE, result);

        // Test with valid integer numbers
        expression = new CoreOperationLessThan(
                new ConstantExpression(new Integer(5)),
                new ConstantExpression(new Integer(3)));
        result = expression.computeValue(context);
        assertEquals("Expected result to be true", Boolean.TRUE, result);

        // Test with negative numbers
        expression = new CoreOperationLessThan(
                new ConstantExpression(new Double(-5.5)),
                new ConstantExpression(new Double(-7.7)));
        result = expression.computeValue(context);
        assertEquals("Expected result to be true", Boolean.TRUE, result);

        // Test with zero
        expression = new CoreOperationLessThan(
                new ConstantExpression(new Double(0.0)),
                new ConstantExpression(new Double(5.5)));
        result = expression.computeValue(context);
        assertEquals("Expected result to be false", Boolean.FALSE, result);

        // Test with NaN
        expression = new CoreOperationLessThan(
                new ConstantExpression(new Double(Double.NaN)),
                new ConstantExpression(new Double(Double.NaN)));
        result = expression.computeValue(context);
        assertEquals("Expected result to be undefined", null, result);

        // Test with null argument
        expression = new CoreOperationLessThan(
                new ConstantExpression((Object) null),
                new ConstantExpression(new Double(3.3)));
        result = expression.computeValue(context);
        assertEquals("Expected a runtime error", null, result);
    }

    @Test(expected = ClassCastException.class)
    public void testMismatchedTypes() {
        // Test with mismatched types
        CoreOperationLessThan expression = new CoreOperationLessThan(
                new ConstantExpression(new Integer(5)),
                new ConstantExpression(new String("3.3")));
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);
        expression.computeValue(context);
    }
}