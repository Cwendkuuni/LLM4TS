package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(null) {};
        assertEquals(3, expr.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(null) {};
        assertFalse(expr.isSymmetric());
    }
}