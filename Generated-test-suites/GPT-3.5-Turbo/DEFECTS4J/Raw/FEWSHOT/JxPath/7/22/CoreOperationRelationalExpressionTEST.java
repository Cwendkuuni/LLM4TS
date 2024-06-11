
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(null);
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(null);
        assertFalse(expression.isSymmetric());
    }

    private class CoreOperationRelationalExpressionMock extends CoreOperationRelationalExpression {
        CoreOperationRelationalExpressionMock(Expression[] args) {
            super(args);
        }
    }

}
