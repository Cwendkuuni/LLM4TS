package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    
    private static final Expression[] ARGS = {};
    
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(ARGS) {
            @Override
            protected String getOperatorSymbol() {
                return "";
            }
        };
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(ARGS) {
            @Override
            protected String getOperatorSymbol() {
                return "";
            }
        };
        assertEquals(false, expression.isSymmetric());
    }
}