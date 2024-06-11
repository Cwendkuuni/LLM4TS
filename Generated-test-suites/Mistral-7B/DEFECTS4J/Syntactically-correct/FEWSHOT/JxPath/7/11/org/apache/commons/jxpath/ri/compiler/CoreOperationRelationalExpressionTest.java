package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpressionDefaultConstructor() {
        assertNull(new CoreOperationRelationalExpression(new Expression[0]));
    }

    @Test
    public void testCoreOperationRelationalExpressionConstructor() {
        Expression[] args = {new Expression(), new Expression()};
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(args);
        assertNotNull(expr);
        assertEquals(3, expr.getPrecedence());
        assertFalse(expr.isSymmetric());
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[0]);
        CoreOperationRelationalExpression exprWithArgs = new CoreOperationRelationalExpression(new Expression[0]);
        assertEquals(3, expr.getPrecedence());
        assertEquals(3, exprWithArgs.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[0]);
        assertFalse(expr.isSymmetric());
    }
}