package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[]{new CoreOperationRelationalExpressionMock(), new CoreOperationRelationalExpressionMock()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(args);
        assertTrue((Boolean) expression.computeValue(EvalContextMock()));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[]{new CoreOperationRelationalExpressionMock(), new CoreOperationRelationalExpressionMock()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(args);
        assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[]{new CoreOperationRelationalExpressionMock(), new CoreOperationRelationalExpressionMock()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(args);
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[]{new CoreOperationRelationalExpressionMock(), new CoreOperationRelationalExpressionMock()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(args);
        assertTrue(expression.evaluateCompare(0));
        assertFalse(expression.evaluateCompare(-1));
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[]{new CoreOperationRelationalExpressionMock(), new CoreOperationRelationalExpressionMock()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(args);
        assertTrue(expression.compute("left", "right"));
        assertFalse(expression.compute("left", "left"));
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[]{new CoreOperationRelationalExpressionMock(), new CoreOperationRelationalExpressionMock()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(args);
        Object o = expression.reduce("operand");
        assertEquals("operand", o);
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[]{new CoreOperationRelationalExpressionMock(), new CoreOperationRelationalExpressionMock()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(args);
        assertFalse(expression.containsMatch("left", "right"));
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[]{new CoreOperationRelationalExpressionMock(), new CoreOperationRelationalExpressionMock()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(args);
        assertFalse(expression.findMatch("left", "right"));
    }

    private EvalContext EvalContextMock() {
        return new EvalContext();
    }

    private class CoreOperationRelationalExpressionMock extends CoreOperationRelationalExpression {

        public CoreOperationRelationalExpressionMock(Expression[] args) {
            super(args);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return compare == 0;
        }
    }
}