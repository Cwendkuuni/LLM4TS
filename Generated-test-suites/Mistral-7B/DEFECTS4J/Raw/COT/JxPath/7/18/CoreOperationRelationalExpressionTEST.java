package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testConstructor() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertEquals("CoreOperationRelationalExpression", expression.getClass().getSimpleName());
        assertEquals(2, expression.getArguments().length);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        assertFalse(expression.isSymmetric());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArguments() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null);
    }

    @Test
    public void testNullArguments() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[] {null, null});
        assertNull(expression.getLeftExpression());
        assertNull(expression.getRightExpression());
    }

    @Test
    public void testEquals() {
        Expression left1 = new Const(1);
        Expression left2 = new Const(1);
        Expression right1 = new Const(2);
        Expression right2 = new Const(3);

        CoreOperationRelationalExpression expression1 = new CoreOperationGreater(new CoreOperationAdd(left1, new Const(1)), right1);
        CoreOperationRelationalExpression expression2 = new CoreOperationGreater(new CoreOperationAdd(left2, new Const(1)), right2);

        assertFalse(expression1.equals(expression2));

        CoreOperationRelationalExpression expression3 = new CoreOperationGreater(expression1, expression1);
        assertTrue(expression3.equals(expression3));
        assertTrue(expression3.equals(expression1));
    }

    @Test
    public void testHashCode() {
        Expression left1 = new Const(1);
        Expression left2 = new Const(1);
        Expression right1 = new Const(2);
        Expression right2 = new Const(3);

        CoreOperationRelationalExpression expression1 = new CoreOperationGreater(new CoreOperationAdd(left1, new Const(1)), right1);
        CoreOperationRelationalExpression expression2 = new CoreOperationGreater(new CoreOperationAdd(left2, new Const(1)), right2);

        int hash1 = expression1.hashCode();
        int hash2 = expression2.hashCode();
        assertFalse(hash1 == hash2);

        int hash3 = expression1.hashCode();
        assertTrue(hash3 == hash3);
    }
}