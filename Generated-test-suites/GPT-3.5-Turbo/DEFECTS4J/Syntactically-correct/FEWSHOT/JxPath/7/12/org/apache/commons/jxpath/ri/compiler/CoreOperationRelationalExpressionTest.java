package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        Expression[] args = {new Expression(), new Expression()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {};

        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = {new Expression(), new Expression()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {};

        assertEquals(false, expression.isSymmetric());
    }
}