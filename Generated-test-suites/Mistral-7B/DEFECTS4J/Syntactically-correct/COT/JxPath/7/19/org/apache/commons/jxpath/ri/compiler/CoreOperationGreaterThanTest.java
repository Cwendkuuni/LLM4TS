package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;

/**
 * Test class for CoreOperationGreaterThan.
 */
public class CoreOperationGreaterThanTest {

    @SuppressWarnings("unchecked")
    private Expression createGreaterThanExpression(Expression arg1, Expression arg2) {
        return new CoreOperationGreaterThan(arg1, arg2);
    }

    /**
     * Test method for {@link CoreOperationGreaterThan#CoreOperationGreaterThan(Expression, Expression)}.
     */
    @Test
    public void testCoreOperationGreaterThan() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationGreaterThan expression = createGreaterThanExpression(arg1, arg2);
        assertTrue(expression.computeValue(new EvalContext()) instanceof Boolean &&
                ((Boolean) expression.computeValue(new EvalContext())).booleanValue());
    }

    /**
     * Test method for {@link CoreOperationGreaterThan#computeValue(EvalContext)}.
     */
    @Test
    public void testComputeValue() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(2.0);
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(arg1, arg2);
        assertTrue(expression.computeValue(new EvalContext()) instanceof Boolean &&
                ((Boolean) expression.computeValue(new EvalContext())).booleanValue());

        arg1 = new LiteralExpression(2.0);
        arg2 = new LiteralExpression(1.0);
        expression = new CoreOperationGreaterThan(arg1, arg2);
        assertFalse(expression.computeValue(new EvalContext()) instanceof Boolean ||
                ((Boolean) expression.computeValue(new EvalContext())).booleanValue());

        arg1 = new LiteralExpression(0.0);
        arg2 = new LiteralExpression(0.0);
        expression = new CoreOperationGreaterThan(arg1, arg2);
        assertFalse(expression.computeValue(new EvalContext()) instanceof Boolean ||
                ((Boolean) expression.computeValue(new EvalContext())).booleanValue());
    }

    /**
     * Test method for {@link CoreOperationGreaterThan#getSymbol()}.
     */
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new LiteralExpression(1.0),
                new LiteralExpression(2.0));
        assertEquals(">", expression.getSymbol());
    }

    /**
     * Test method for {@link CoreOperationGreaterThan#computeValue(EvalContext)} with NaN values.
     */
    @Test(expected = ArithmeticException.class)
    public void testComputeValueWithNaN() {
        Expression arg1 = new LiteralExpression(Double.NaN);
        Expression arg2 = new LiteralExpression(Double.NaN);
        new CoreOperationGreaterThan(arg1, arg2).computeValue(new EvalContext());
    }

    /**
     * Test method for {@link CoreOperationGreaterThan#computeValue(EvalContext)} with different types.
     */
    @Test(expected = ClassCastException.class)
    public void testComputeValueWithDifferentTypes() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression("2");
        new CoreOperationGreaterThan(arg1, arg2).computeValue(new EvalContext());
    }
}