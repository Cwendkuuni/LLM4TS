package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpressionConstructor() {
        Expression[] args = new Expression[2];
        args[0] = new LiteralExpression(1);
        args[1] = new LiteralExpression(2);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertEquals(args, expression.getArgs());
        assertEquals(3, expression.getPrecedence());
        assertFalse(expression.isSymmetric());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationRelationalExpressionInvalidConstructor() {
        new CoreOperationRelationalExpression(null);
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

}