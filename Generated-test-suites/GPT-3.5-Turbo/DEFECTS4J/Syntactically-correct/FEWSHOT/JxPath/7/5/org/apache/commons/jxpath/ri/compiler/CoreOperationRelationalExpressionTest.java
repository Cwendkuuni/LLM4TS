package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        Expression[] args = {};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTestImpl(args);
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = {};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTestImpl(args);
        assertFalse(expression.isSymmetric());
    }

    // Implementing a concrete subclass for testing
    private class CoreOperationRelationalExpressionTestImpl extends CoreOperationRelationalExpression {

        public CoreOperationRelationalExpressionTestImpl(Expression[] args) {
            super(args);
        }

        @Override
        public Object computeValue(EvalContext context) {
            return null;
        }

    }
}