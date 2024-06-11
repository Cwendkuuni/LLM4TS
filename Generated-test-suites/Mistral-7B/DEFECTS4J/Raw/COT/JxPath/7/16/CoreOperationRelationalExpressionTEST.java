package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.ComparisonOperator;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Value;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    @SuppressWarnings({ "unchecked" })
    private Expression createExpression(ComparisonOperator op, Expression... args) {
        return new CoreOperationRelationalExpression(args) {
            @Override
            public ComparisonOperator getOperator() {
                return op;
            }
        };
    }

    // Test cases for constructor

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArguments() {
        new CoreOperationRelationalExpression((Expression[]) null);
    }

    @org.junit.Test
    public void testConstructorWithEmptyArguments() {
        new CoreOperationRelationalExpression(new Expression[0]);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidNumberOfArguments() {
        new CoreOperationRelationalExpression(new Expression[] { new Expression() });
    }

    // Test cases for getPrecedence()

    @org.junit.Test
    public void testGetPrecedence() {
        assertEquals(3, new CoreOperationRelationalExpression(new Expression[0]).getPrecedence());
    }

    // Test cases for isSymmetric()

    @org.junit.Test
    public void testIsSymmetric() {
        assertFalse(new CoreOperationRelationalExpression(new Expression[0]).isSymmetric());
    }

    // Test cases for comparisons with null values

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testComparisonWithNullLeftOperand() {
        createExpression(ComparisonOperator.GT, null, new Value(5));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testComparisonWithNullRightOperand() {
        createExpression(ComparisonOperator.EQ, new Value(5), null);
    }

    // Test cases for comparisons with non-numeric values

    @org.junit.Test(expected = ClassCastException.class)
    public void testComparisonWithNonNumericLeftOperand() {
        createExpression(ComparisonOperator.GT, new Value("5"), new Value(4));
    }

    @org.junit.Test(expected = ClassCastException.class)
    public void testComparisonWithNonNumericRightOperand() {
        createExpression(ComparisonOperator.EQ, new Value(5), new Value("4"));
    }

    // Test cases for comparisons with equal operands

    @org.junit.Test
    public void testComparisonWithEqualOperands() {
        Expression expression = createExpression(ComparisonOperator.EQ, new Value(5), new Value(5));
        Value result = (Value) expression.evaluate();
        assertTrue(result.isTrue());
    }

    // Test cases for comparisons with different operands

    @org.junit.Test
    public void testComparisonWithDifferentOperands() {
        Expression expression = createExpression(ComparisonOperator.GT, new Value(5), new Value(4));
        Value result = (Value) expression.evaluate();
        assertTrue(result.isTrue());
    }

    @org.junit.Test
    public void testComparisonWithDifferentOperandsReverse() {
        Expression expression = createExpression(ComparisonOperator.LT, new Value(4), new Value(5));
        Value result = (Value) expression.evaluate();
        assertTrue(result.isTrue());
    }

    // Test cases for comparisons with equal values

    @org.junit.Test
    public void testComparisonWithEqualValues() {
        Expression expression = createExpression(ComparisonOperator.EQ, new Value(5), new Value(5));
        Value result = (Value) expression.evaluate();
        assertTrue(result.isTrue());
    }

    // Test cases for comparisons with different values

    @org.junit.Test
    public void testComparisonWithDifferentValues() {
        Expression expression = createExpression(ComparisonOperator.GT, new Value(5), new Value(4));
        Value result = (Value) expression.evaluate();
        assertTrue(result.isTrue());
    }

    @org.junit.Test
    public void testComparisonWithDifferentValuesReverse() {
        Expression expression = createExpression(ComparisonOperator.LT, new Value(4), new Value(5));
        Value result = (Value) expression.evaluate();
        assertTrue(result.isTrue());
    }

    // Test cases for comparisons with zero

    @org.junit.Test
    public void testComparisonWithZero() {
        Expression expression = createExpression(ComparisonOperator.GT, new Value(5), new Value(0));
        Value result = (Value) expression.evaluate();
        assertTrue(result.isTrue());
    }

    @org.junit.Test
    public void testComparisonWithZeroEqual() {
        Expression expression = createExpression(ComparisonOperator.EQ, new Value(0), new Value(0));
        Value result = (Value) expression.evaluate();
        assertTrue(result.isTrue());
    }

    @org.junit.Test
    public void testComparisonWithZeroNotEqual() {
        Expression expression = createExpression(ComparisonOperator.NEQ, new Value(0), new Value(0));
        Value result = (Value) expression.evaluate();
        assertFalse(result.isTrue());
    }

    @org.junit.Test
    public void testComparisonWithZeroGreater() {
        Expression expression = createExpression(ComparisonOperator.GT, new Value(0), new Value(5));
        Value result = (Value) expression.evaluate();
        assertFalse(result.isTrue());
    }

    @org.junit.Test
    public void testComparisonWithZeroLesser() {
        Expression expression = createExpression(ComparisonOperator.LT, new Value(5), new Value(0));
        Value result = (Value) expression.evaluate();
        assertFalse(result.isTrue());
    }

    // Test cases for comparisons with minimum and maximum values

    @org.junit.Test
    public void testComparisonWithMinValue() {
        Expression expression = createExpression(ComparisonOperator.GT, new Value.MinValue(), new Value(5));
        Value result = (Value) expression.evaluate();
        assertFalse(result.isTrue());
    }

    @org.junit.Test
    public void testComparisonWithMaxValue() {
        Expression expression = createExpression(ComparisonOperator.LT, new Value.MaxValue(), new Value(5));
        Value result = (Value) expression.evaluate();
        assertFalse(result.isTrue());
    }

    @org.junit.Test
    public void testComparisonWithMinValueEqual() {
        Expression expression = createExpression(ComparisonOperator.EQ, new Value.MinValue(), new Value.MinValue());
        Value result = (Value) expression.evaluate();
        assertTrue(result.isTrue());
    }

    @org.junit.Test
    public void testComparisonWithMaxValueEqual() {
        Expression expression = createExpression(ComparisonOperator.EQ, new Value.MaxValue(), new Value.MaxValue());
        Value result = (Value) expression.evaluate();
        assertTrue(result.isTrue());
    }

    @org.junit.Test
    public void testComparisonWithMinValueGreater() {
        Expression expression = createExpression(ComparisonOperator.GT, new Value.MinValue(), new Value(5));
        Value result = (Value) expression.evaluate();
        assertFalse(result.isTrue());
    }

    @org.junit.Test
    public void testComparisonWithMinValueLesser() {
        Expression expression = createExpression(ComparisonOperator.LT, new Value.MinValue(), new Value.MinValue());
        Value result = (Value) expression.evaluate();
        assertFalse(result.isTrue());
    }

    @org.junit.Test
    public void testComparisonWithMaxValueGreater() {
        Expression expression = createExpression(ComparisonOperator.GT, new Value.MaxValue(), new Value.MaxValue());
        Value result = (Value) expression.evaluate();
        assertFalse(result.isTrue());
    }

    @org.junit.Test
    public void testComparisonWithMaxValueLesser() {
        Expression expression = createExpression(ComparisonOperator.LT, new Value.MaxValue(), new Value(5));
        Value result = (Value) expression.evaluate();
        assertFalse(result.isTrue());
    }

    // Test cases for comparisons with negative numbers

    @org.junit.Test
    public void testComparisonWithNegativeNumbers() {
        Expression expression = createExpression(ComparisonOperator.LT, new Value(-5), new Value(-4));
        Value result = (Value) expression.evaluate();
        assertTrue(result.isTrue());
    }

    @org.junit.Test
    public void testComparisonWithNegativeNumbersReverse() {
        Expression expression = createExpression(ComparisonOperator.GT, new Value(-4), new Value(-5));
        Value result = (Value) expression.evaluate();
        assertTrue(result.isTrue());
    }

    @org.junit.Test
    public void testComparisonWithNegativeNumbersAndPositiveNumbers() {
        Expression expression = createExpression(ComparisonOperator.GT, new Value(-5), new Value(4));
        Value result = (Value) expression.evaluate();
        assertFalse(result.isTrue());
    }

    @org.junit.Test
    public void testComparisonWithNegativeNumbersAndNegativeNumbers() {
        Expression expression = createExpression(ComparisonOperator.LT, new Value(-5), new Value(-4));
        Value result = (Value) expression.evaluate();
        assertTrue(result.isTrue());
    }

    // Test cases for exceptions

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testComparisonWithMoreThanTwoOperands() {
        createExpression(ComparisonOperator.GT, new Expression[3]);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testComparisonWithInvalidOperator() {
        createExpression(new ComparisonOperator() {}, new Expression[0], new Expression[0]);
    }

}