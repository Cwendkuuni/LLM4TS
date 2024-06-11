package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            protected boolean computeValue() {
                return false;
            }
        };
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            protected boolean computeValue() {
                return false;
            }
        };
        assertFalse(expression.isSymmetric());
    }
}