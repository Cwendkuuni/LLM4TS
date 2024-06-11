package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected Object computeValue() {
                return null;
            }
        };
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected Object computeValue() {
                return null;
            }
        };
        assertEquals(false, expression.isSymmetric());
    }
}