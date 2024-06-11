package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testConstructor() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[0]);
        assertInstanceOf(CoreOperationRelationalExpression.class, expression);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[0]);
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsSymmetricFailure() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[0]);
        boolean symmetric = expression.isSymmetric();
        fail("isSymmetric should throw UnsupportedOperationException");
    }

    @Test
    public void testDefaultIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[0]);
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testNegativeRelationalExpression() {
        CoreOperation op1 = Mockito.mock(CoreOperation.class);
        Expression arg1 = Mockito.mock(Expression.class);
        CoreOperationRelationalExpression expression = new CoreOperationNotRelationalExpression(op1, arg1);
        CoreOperationRelationalExpression negatedExpression = new CoreOperationNotRelationalExpression(expression);

        CoreOperation op2 = Mockito.mock(CoreOperation.class);
        Expression arg2 = Mockito.mock(Expression.class);
        CoreOperationRelationalExpression originalExpression = new CoreOperationRelationalExpression(op1, arg1);

        assertEquals(expression, negatedExpression.getNegatedExpression());
        assertEquals(originalExpression, negatedExpression.getExpression());

        try {
            int result = negatedExpression.compare(1, 2);
            fail("compare should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArgument() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null);
        fail("Constructor should have thrown IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArguments() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[0], null);
        fail("Constructor should have thrown IllegalArgumentException");
    }

    @Test
    public void testEqualOperators() {
        CoreOperation op1 = Mockito.mock(CoreOperation.class);
        Expression arg1 = Mockito.mock(Expression.class);
        CoreOperation op2 = Mockito.mock(CoreOperation.class);
        Expression arg2 = Mockito.mock(Expression.class);

        CoreOperationRelationalExpression expression1 = new CoreOperationRelationalExpression(op1, arg1);
        CoreOperationRelationalExpression expression2 = new CoreOperationRelationalExpression(op2, arg2);

        assertEquals(expression1, expression2);
        assertEquals(expression1.hashCode(), expression2.hashCode());
    }

    @Test
    public void testNotEqualOperators() {
        CoreOperation op1 = Mockito.mock(CoreOperation.class);
        Expression arg1 = Mockito.mock(Expression.class);
        CoreOperation op2 = Mockito.mock(CoreOperation.class);
        Expression arg2 = Mockito.mock(Expression.class);

        CoreOperationRelationalExpression expression1 = new CoreOperationRelationalExpression(op1, arg1);
        CoreOperationRelationalExpression expression2 = new CoreOperationRelationalExpression(op2, arg2);

        assertNotEquals(expression1, expression2);
        assertNotEquals(expression1.hashCode(), expression2.hashCode());
    }

}