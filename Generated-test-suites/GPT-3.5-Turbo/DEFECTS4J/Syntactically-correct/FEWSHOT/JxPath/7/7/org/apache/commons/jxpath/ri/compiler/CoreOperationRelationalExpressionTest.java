
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    // Test cases for getPrecedence method
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected Object computeValue(EvaluationContext context) {
                return null;
            }
        };

        assertEquals(3, expression.getPrecedence());
    }

    // Test cases for isSymmetric method
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected Object computeValue(EvaluationContext context) {
                return null;
            }
        };

        assertFalse(expression.isSymmetric());
    }
}
