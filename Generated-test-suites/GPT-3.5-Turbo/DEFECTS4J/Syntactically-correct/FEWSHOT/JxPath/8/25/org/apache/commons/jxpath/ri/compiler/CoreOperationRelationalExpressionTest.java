package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionStub(new ExpressionStub(), new ExpressionStub());
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new SelfContext());

        Object result = expression.computeValue(context);

        assertEquals(Boolean.FALSE, result);
    }

    private static class CoreOperationRelationalExpressionStub extends CoreOperationRelationalExpression {
        public CoreOperationRelationalExpressionStub(Expression left, Expression right) {
            super(new Expression[] { left, right });
        }

        protected boolean evaluateCompare(int compare) {
            return false;
        }
    }

    private static class ExpressionStub extends Expression {
        public Expression[] getComponents() {
            return null;
        }

        public void setContext(ExpressionContext context) {
        }

        public void setLocationPathLocation(int locationPathLocation) {
        }

        public void optimize() {
        }

        public void computeContextDependent() {
        }

        public int computePrecedence() {
            return 0;
        }

        public boolean isContextDependent() {
            return false;
        }

        public boolean isAbsolute() {
            return false;
        }

        public String toString() {
            return null;
        }

        public int hashCode() {
            return 0;
        }
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionStub(new ExpressionStub(), new ExpressionStub());

        int result = expression.getPrecedence();

        assertEquals(3, result);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionStub(new ExpressionStub(), new ExpressionStub());

        boolean result = expression.isSymmetric();

        assertEquals(false, result);
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionStub(new ExpressionStub(), new ExpressionStub());

        Object result = expression.reduce(null);

        assertEquals(null, result);
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionStub(new ExpressionStub(), new ExpressionStub());

        boolean result = expression.containsMatch(null, null);

        assertEquals(false, result);
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionStub(new ExpressionStub(), new ExpressionStub());

        boolean result = expression.findMatch(null, null);

        assertEquals(false, result);
    }
}