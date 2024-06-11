package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;

/**
 * Test cases for CoreOperationLessThan class.
 */
public class CoreOperationLessThanTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Expression arg1 = ExpressionFactory.newLiteralExpression(1.0);
        Expression arg2 = ExpressionFactory.newLiteralExpression(2.0);
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);
        assertNotNull(op);
        assertEquals(CoreOperationLessThan.class, op.getClass());
        assertEquals(2, op.getArgCount());
    }

    /**
     * Test computeValue method with valid input.
     */
    @Test
    public void testComputeValueValidInput() {
        Expression arg1 = ExpressionFactory.newLiteralExpression(1.0);
        Expression arg2 = ExpressionFactory.newLiteralExpression(2.0);
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);
        EvaluationContext context = new EvaluationContext(new Path("/"), new InfoSet());
        boolean result = (Boolean) op.computeValue(context);
        assertTrue(result);
    }

    /**
     * Test computeValue method with invalid input.
     */
    @Test(expected = RuntimeException.class)
    public void testComputeValueInvalidInput() {
        Expression arg1 = ExpressionFactory.newLiteralExpression("invalid");
        Expression arg2 = ExpressionFactory.newLiteralExpression(2.0);
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);
        EvaluationContext context = new EvaluationContext(new Path("/"), new InfoSet());
        op.computeValue(context);
    }

    /**
     * Test computeValue method with same number.
     */
    @Test
    public void testComputeValueSameNumber() {
        Expression arg1 = ExpressionFactory.newLiteralExpression(1.0);
        Expression arg2 = ExpressionFactory.newLiteralExpression(1.0);
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);
        EvaluationContext context = new EvaluationContext(new Path("/"), new InfoSet());
        boolean result = (Boolean) op.computeValue(context);
        assertFalse(result);
    }

    /**
     * Test computeValue method with equal numbers.
     */
    @Test
    public void testComputeValueEqualNumbers() {
        Expression arg1 = ExpressionFactory.newLiteralExpression(1.0);
        Expression arg2 = ExpressionFactory.newLiteralExpression(1.0);
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);
        EvaluationContext context = new EvaluationContext(new Path("/"), new InfoSet());
        boolean result = (Boolean) op.computeValue(context);
        assertFalse(result);
    }

    /**
     * Test computeValue method with non-numeric input.
     */
    @Test(expected = RuntimeException.class)
    public void testComputeValueNonNumericInput() {
        Expression arg1 = ExpressionFactory.newLiteralExpression("invalid");
        Expression arg2 = ExpressionFactory.newLiteralExpression(2.0);
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);
        EvaluationContext context = new EvaluationContext(new Path("/"), new InfoSet());
        op.computeValue(context);
    }

    /**
     * Test computeValue method with negative numbers.
     */
    @Test
    public void testComputeValueNegativeNumbers() {
        Expression arg1 = ExpressionFactory.newLiteralExpression(-1.0);
        Expression arg2 = ExpressionFactory.newLiteralExpression(-2.0);
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);
        EvaluationContext context = new EvaluationContext(new Path("/"), new InfoSet());
        boolean result = (Boolean) op.computeValue(context);
        assertTrue(result);
    }

    /**
     * Test computeValue method with very large numbers.
     */
    @Test
    public void testComputeValueVeryLargeNumbers() {
        Expression arg1 = ExpressionFactory.newLiteralExpression(Long.MAX_VALUE);
        Expression arg2 = ExpressionFactory.newLiteralExpression(Long.MAX_VALUE + 1);
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);
        EvaluationContext context = new EvaluationContext(new Path("/"), new InfoSet());
        boolean result = (Boolean) op.computeValue(context);
        assertTrue(result);
    }

    /**
     * Test getSymbol method.
     */
    @Test
    public void testGetSymbol() {
        Expression arg1 = ExpressionFactory.newLiteralExpression(1.0);
        Expression arg2 = ExpressionFactory.newLiteralExpression(2.0);
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);
        String symbol = op.getSymbol();
        assertEquals("<", symbol);
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        Expression arg1 = ExpressionFactory.newLiteralExpression(1.0);
        Expression arg2 = ExpressionFactory.newLiteralExpression(2.0);
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);
        String expected = "CoreOperationLessThan(CoreOperationRelationalExpression(Expression[1.0], Expression[2.0]))";
        String result = op.toString();
        assertEquals(expected, result);
    }
}