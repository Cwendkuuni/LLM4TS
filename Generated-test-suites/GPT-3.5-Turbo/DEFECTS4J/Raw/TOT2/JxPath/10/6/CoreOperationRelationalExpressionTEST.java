package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression(5);
        args[1] = new MockExpression(3);

        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(args);
        EvalContext context = new EvalContext(new InitialContext());
        assertEquals(Boolean.TRUE, expression.computeValue(context));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(args);
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(args);
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testCompute_withEqualDoubles() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression(3.0);
        args[1] = new MockExpression(3.0);

        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(args);
        EvalContext context = new EvalContext(new InitialContext());
        assertEquals(Boolean.TRUE, expression.computeValue(context));
    }

    @Test
    public void testCompute_withNaN() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression(Double.NaN);
        args[1] = new MockExpression(3.0);

        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(args);
        EvalContext context = new EvalContext(new InitialContext());
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    private static class MockExpression extends Expression {
        private Object value;

        MockExpression(Object value) {
            this.value = value;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return value;
        }
    }

    private static class MockCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
        MockCoreOperationRelationalExpression(Expression[] args) {
            super(args);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return compare > 0;
        }
    }
}