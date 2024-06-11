package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpressionConstructor() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertSame(CoreOperationRelationalExpression.class, expression.getClass());
        assertEquals(2, expression.getArguments().length);
        assertEquals(3, expression.getPrecedence());
        assertFalse(expression.isSymmetric());
    }

    // Test methods for each public method in CoreOperationRelationalExpression

    // Test method for getPrecedence()
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        assertEquals(3, expression.getPrecedence());
    }

    // Test method for isSymmetric()
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        assertFalse(expression.isSymmetric());
    }

    // Test edge cases and exception handling

    // Test null arguments
    @Test(expected = IllegalArgumentException.class)
    public void testNullArguments() {
        new CoreOperationRelationalExpression(null);
    }

    // Test empty arguments array
    @Test
    public void testEmptyArguments() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        assertEquals(3, expression.getPrecedence());
        assertFalse(expression.isSymmetric());
    }

    // Test arguments of different types
    @Test(expected = IllegalArgumentException.class)
    public void testArgumentsOfDifferentTypes() {
        Expression arg1 = new CoreNumber(1);
        Expression arg2 = new CoreVariable("x");
        new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
    }

    // Test arguments of the same type but different values
    @Test
    public void testArgumentsOfSameTypeButDifferentValues() {
        Expression arg1 = new CoreNumber(1);
        Expression arg2 = new CoreNumber(2);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
        assertEquals(3, expression.getPrecedence());
        assertFalse(expression.isSymmetric());
    }

    // Test equality comparison with same operands
    @Test
    public void testEqualityComparisonWithSameOperands() {
        Expression arg1 = new CoreNumber(1);
        Expression arg2 = new CoreNumber(1);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
        // Test "==" (identical_to) operation
        CoreOperation op = expression.getOperation();
        assertEquals("==", op.toString());
        // Test comparison result for true case
        assertSame(Boolean.TRUE, expression.evaluate(new CoreContext()));
        // Test comparison result for false case (different data types)
        Expression arg3 = new CoreString("1");
        expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg3});
        assertNull(expression.evaluate(new CoreContext()));
    }

    // Test inequality comparison with same operands
    @Test
    public void testInequalityComparisonWithSameOperands() {
        Expression arg1 = new CoreNumber(1);
        Expression arg2 = new CoreNumber(1);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
        // Test "<>" (not_equal_to) operation
        CoreOperation op = expression.getOperation();
        assertEquals("!=", op.toString());
        // Test comparison result for false case (identical values)
        assertSame(Boolean.FALSE, expression.evaluate(new CoreContext()));
        // Test comparison result for true case (different data types)
        Expression arg3 = new CoreString("1");
        expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg3});
        assertSame(Boolean.TRUE, expression.evaluate(new CoreContext()));
    }

    // Test greater than comparison with different operands
    @Test
    public void testGreaterThanComparisonWithDifferentOperands() {
        Expression arg1 = new CoreNumber(1);
        Expression arg2 = new CoreNumber(2);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
        // Test ">" operation
        CoreOperation op = expression.getOperation();
        assertEquals(">", op.toString());
        // Test comparison result for false case
        assertSame(Boolean.FALSE, expression.evaluate(new CoreContext()));
        // Test comparison result for true case
        arg1 = new CoreNumber(3);
        assertSame(Boolean.TRUE, expression.evaluate(new CoreContext()));
    }

    // Test greater than or equal to comparison with different operands
    @Test
    public void testGreaterThanOrEqualComparisonWithDifferentOperands() {
        Expression arg1 = new CoreNumber(1);
        Expression arg2 = new CoreNumber(2);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
        // Test ">=" operation
        CoreOperation op = expression.getOperation();
        assertEquals(">=", op.toString());
        // Test comparison result for false case
        assertSame(Boolean.FALSE, expression.evaluate(new CoreContext()));
        // Test comparison result for true case (arg1>=arg2)
        arg1 = new CoreNumber(2);
        assertSame(Boolean.TRUE, expression.evaluate(new CoreContext()));
        // Test comparison result for true case (arg1=arg2)
        arg1 = new CoreNumber(2);
        assertSame(Boolean.TRUE, expression.evaluate(new CoreContext()));
    }

    // Test less than comparison with different operands
    @Test
    public void testLessThanComparisonWithDifferentOperands() {
        Expression arg1 = new CoreNumber(1);
        Expression arg2 = new CoreNumber(2);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
        // Test "<" operation
        CoreOperation op = expression.getOperation();
        assertEquals("<", op.toString());
        // Test comparison result for false case
        assertSame(Boolean.FALSE, expression.evaluate(new CoreContext()));
        // Test comparison result for true case (arg1<arg2)
        arg1 = new CoreNumber(0);
        assertSame(Boolean.TRUE, expression.evaluate(new CoreContext()));
    }

    // Test less than or equal to comparison with different operands
    @Test
    public void testLessThanOrEqualComparisonWithDifferentOperands() {
        Expression arg1 = new CoreNumber(1);
        Expression arg2 = new CoreNumber(2);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});
        // Test "<=" operation
        CoreOperation op = expression.getOperation();
        assertEquals("<=", op.toString());
        // Test comparison result for false case
        assertSame(Boolean.FALSE, expression.evaluate(new CoreContext()));
        // Test comparison result for true case (arg1<=arg2)
        arg1 = new CoreNumber(2);
        assertSame(Boolean.TRUE, expression.evaluate(new CoreContext()));
        // Test comparison result for true case (arg1=arg2)
        arg1 = new CoreNumber(2);
        assertSame(Boolean.TRUE, expression.evaluate(new CoreContext()));
    }

}