
package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        assertFalse(expression.isSymmetric());
    }

    private static class CoreOperationRelationalExpressionImpl extends CoreOperationRelationalExpression {
        public CoreOperationRelationalExpressionImpl(Expression[] args) {
            super(args);
        }

        protected boolean computeContextDependent() {
            return false;
        }

        public Object computeValue(EvalContext context) {
            return null;
        }
    }
}
