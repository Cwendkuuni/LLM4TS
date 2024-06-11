package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testGetPrecedence() {
        // Test case with positive integer precedence
        Expression[] args = new Expression[2];
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        args[0] = arg1;
        args[1] = arg2;
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {};

        int result = expression.getPrecedence();

        assertEquals(3, result);
    }

    @Test
    public void testIsSymmetric() {
        // Test case with true value
        Expression[] args = new Expression[2];
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        args[0] = arg1;
        args[1] = arg2;
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {};

        boolean result = expression.isSymmetric();

        assertEquals(false, result);
    }
}