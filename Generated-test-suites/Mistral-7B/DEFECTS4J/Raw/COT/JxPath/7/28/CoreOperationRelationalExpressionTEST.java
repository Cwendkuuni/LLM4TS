package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * CoreOperationRelationalExpressionTest class file.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test constructor.
     */
    @Test
    public void testCoreOperationRelationalExpressionConstructor() {
        Expression[] args = new Expression[1];
        CoreOperationRelationalExpression test = new CoreOperationRelationalExpression(args);
        assertNotNull(test);
    }

    /**
     * Test getPrecedence method.
     */
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression test = new CoreOperationRelationalExpression(new Expression[0]);
        int result = test.getPrecedence();
        assertTrue(result == 3);
    }

    /**
     * Test isSymmetric method.
     */
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression test = new CoreOperationRelationalExpression(new Expression[0]);
        assertFalse(test.isSymmetric());
    }

    /**
     * Test empty arguments for relational operations.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRelationalOperationEmptyArguments() {
        new CoreOperationRelationalExpression(new Expression[0]);
    }

    /**
     * Test null argument for relational operations.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRelationalOperationNullArgument() {
        Expression[] args = new Expression[1];
        args[0] = null;
        new CoreOperationRelationalExpression(args);
    }

    /**
     * Test equals method with different types of arguments.
     */
    @Test
    public void testEqualsDifferentTypes() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreStringExpression("one");
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[2], arg1, arg2);
        assertFalse(expression.equals(new CoreOperationGreaterThan(new Expression[2], arg2, arg1)));
    }

    /**
     * Test equals method with same expression.
     */
    @Test
    public void testEqualsSameExpression() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreConstantExpression(1);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[2], arg1, arg2);
        CoreOperationRelationalExpression expression2 = new CoreOperationGreaterThan(new Expression[2], arg2, arg1);
        assertTrue(expression.equals(expression2));
    }

    /**
     * Test equals method with same expression, but different class.
     */
    @Test
    public void testEqualsDifferentClass() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreConstantExpression(1);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[2], arg1, arg2);
        CoreOperation expression2 = new CoreOperationAdd(new Expression[3], expression, expression);
        assertFalse(expression.equals(expression2));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        Expression arg1 = new CoreConstantExpression(1);
        Expression arg2 = new CoreConstantExpression(1);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[2], arg1, arg2);
        int hashCode1 = expression.hashCode();
        Expression arg3 = new CoreConstantExpression(1);
        CoreOperationRelationalExpression expression2 = new CoreOperationGreaterThan(new Expression[2], arg3, arg2);
        int hashCode2 = expression2.hashCode();
        // Different arguments
        assertFalse(hashCode1 == hashCode2);
        // Same arguments, but different order
        CoreOperationRelationalExpression expression3 = new CoreOperationGreaterThan(new Expression[2], arg2, arg1);
        int hashCode3 = expression3.hashCode();
        // Same arguments, same order
        CoreOperationRelationalExpression expression4 = new CoreOperationGreaterThan(new Expression[2], arg1, arg2);
        int hashCode4 = expression4.hashCode();
        // Same expression
        CoreOperationRelationalExpression expression5 = expression;
        int hashCode5 = expression5.hashCode();
        // Different expression, but same hashCode
        CoreOperationRelationalExpression expression6 = new CoreOperationGreaterThan(new Expression[2], arg1, arg1);
        int hashCode6 = expression6.hashCode();
        assertTrue(hashCode1 != hashCode2 && hashCode1 != hashCode3 && hashCode1 != hashCode4 && hashCode1 != hashCode5 && hashCode1 != hashCode6);
        assertTrue(hashCode2 != hashCode3 && hashCode2 != hashCode4 && hashCode2 != hashCode5 && hashCode2 != hashCode6);
        assertTrue(hashCode3 != hashCode4 && hashCode3 != hashCode5 && hashCode3 != hashCode6);
        assertTrue(hashCode4 != hashCode5 && hashCode4 != hashCode6);
        assertTrue(hashCode5 == hashCode5);
        assertTrue(hashCode6 == hashCode6);
    }
}