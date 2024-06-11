package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new CoreGreaterThanExpression(new ConstantExpression(5));
        assertTrue(expr.getPrecedence() == 3);

        expr = new CoreGreaterThanOrEqualExpression(new ConstantExpression(5));
        assertTrue(expr.getPrecedence() == 3);

        expr = new CoreLessThanExpression(new ConstantExpression(5));
        assertTrue(expr.getPrecedence() == 3);

        expr = new CoreLessThanOrEqualExpression(new ConstantExpression(5));
        assertTrue(expr.getPrecedence() == 3);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new CoreGreaterThanExpression(new ConstantExpression(5));
        assertFalse(expr.isSymmetric());

        expr = new CoreGreaterThanOrEqualExpression(new ConstantExpression(5));
        assertFalse(expr.isSymmetric());

        expr = new CoreLessThanExpression(new ConstantExpression(5));
        assertFalse(expr.isSymmetric());

        expr = new CoreLessThanOrEqualExpression(new ConstantExpression(5));
        assertFalse(expr.isSymmetric());
    }
}