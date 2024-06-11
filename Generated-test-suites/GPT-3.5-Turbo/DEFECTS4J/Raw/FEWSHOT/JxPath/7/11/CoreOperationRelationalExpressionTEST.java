package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected int getPrecedence() {
                return 3;
            }
        };

        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean isSymmetric() {
                return false;
            }
        };

        assertFalse(expression.isSymmetric());
    }
}