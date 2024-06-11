package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        CoreOperation[] args = new CoreOperation[2]; // 2 arguments are expected
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertEquals(2, expression.getArity()); // arity should be 2
    }

    /**
     * Test getPrecedence method.
     */
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[2]);
        assertEquals(3, expression.getPrecedence());
    }

    /**
     * Test isSymmetric method.
     */
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[2]);
        assertFalse(expression.isSymmetric());
    }

    /**
     * Test functionality of ">" method.
     */
    @Test
    public void testGreaterThan() {
        CoreOperation left = new CoreOperationLiteral(1);
        CoreOperation right = new CoreOperationLiteral(2);
        CoreOperationRelationalExpression expression = new CoreOperationGT(new CoreOperation[2], left, right);
        assertTrue(expression.evaluate(new Context()));
    }

    /**
     * Test functionality of "<=" method with equal values.
     */
    @Test
    public void testLessThanOrEqualWithEqualValues() {
        CoreOperation left = new CoreOperationLiteral(2);
        CoreOperation right = new CoreOperationLiteral(2);
        CoreOperationRelationalExpression expression = new CoreOperationLE(new CoreOperation[2], left, right);
        assertTrue(expression.evaluate(new Context()));
    }

    /**
     * Test functionality of "<=" method with different values.
     */
    @Test
    public void testLessThanOrEqualWithDifferentValues() {
        CoreOperation left = new CoreOperationLiteral(1);
        CoreOperation right = new CoreOperationLiteral(2);
        CoreOperationRelationalExpression expression = new CoreOperationLE(new CoreOperation[2], left, right);
        assertFalse(expression.evaluate(new Context()));
    }

    /**
     * Test functionality of ">" method with different values.
     */
    @Test
    public void testGreaterThanWithDifferentValues() {
        CoreOperation left = new CoreOperationLiteral(1);
        CoreOperation right = new CoreOperationLiteral(2);
        CoreOperationRelationalExpression expression = new CoreOperationGT(new CoreOperation[2], left, right);
        assertFalse(expression.evaluate(new Context()));
    }

    /**
     * Test functionality of "<=" method with different values in reverse order.
     */
    @Test
    public void testLessThanOrEqualInReverseOrder() {
        CoreOperation left = new CoreOperationLiteral(2);
        CoreOperation right = new CoreOperationLiteral(1);
        CoreOperationRelationalExpression expression = new CoreOperationLE(new CoreOperation[2], left, right);
        assertFalse(expression.evaluate(new Context()));
    }

    /**
     * Test functionality of ">" method with equal values.
     */
    @Test
    public void testGreaterThanWithEqualValues() {
        CoreOperation left = new CoreOperationLiteral(2);
        CoreOperation right = new CoreOperationLiteral(2);
        CoreOperationRelationalExpression expression = new CoreOperationGT(new CoreOperation[2], left, right);
        assertFalse(expression.evaluate(new Context()));
    }

    /**
     * Test edge case for ">" method with null left operand.
     */
    @Test(expected = NullPointerException.class)
    public void testGreaterThanWithNullLeftOperand() {
        CoreOperationRelationalExpression expression = new CoreOperationGT(new CoreOperation[2], null, new CoreOperationLiteral(2));
    }

    /**
     * Test edge case for ">" method with null right operand.
     */
    @Test(expected = NullPointerException.class)
    public void testGreaterThanWithNullRightOperand() {
        CoreOperationRelationalExpression expression = new CoreOperationGT(new CoreOperation[2], new CoreOperationLiteral(2), null);
    }

    /**
     * Test edge case for "<=" method with null left operand.
     */
    @Test
    public void testLessThanOrEqualWithNullLeftOperand() {
        CoreOperation left = null;
        CoreOperation right = new CoreOperationLiteral(2);
        CoreOperationRelationalExpression expression = new CoreOperationLE(new CoreOperation[2], left, right);
        assertNull(expression.evaluate(new Context()));
    }

    /**
     * Test edge case for "<=" method with null right operand.
     */
    @Test
    public void testLessThanOrEqualWithNullRightOperand() {
        CoreOperation left = new CoreOperationLiteral(2);
        CoreOperation right = null;
        CoreOperationRelationalExpression expression = new CoreOperationLE(new CoreOperation[2], left, right);
        assertNull(expression.evaluate(new Context()));
    }
}