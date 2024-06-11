package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.CompiledExpression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testComputeValue() {
        Expression[] args = {new CoreOperation(), new CoreOperation()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, expression.computeValue(context));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = {new CoreOperation(), new CoreOperation()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = {new CoreOperation(), new CoreOperation()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        assertEquals(false, expression.isSymmetric());
    }

    @Test
    public void testCompute() {
        Expression[] args = {new CoreOperation(), new CoreOperation()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        Object result = expression.compute("left", "right");
        // assert result
    }

    @Test
    public void testReduce() {
        Expression[] args = {new CoreOperation(), new CoreOperation()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        Object result = expression.reduce("input");
        // assert result
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = {new CoreOperation(), new CoreOperation()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        EvalContext context = new EvalContext();
        context.setSingleNodePointer("pointer");
        boolean result = expression.containsMatch(context.iterate(), "value");
        // assert result
    }

    @Test
    public void testFindMatch() {
        Expression[] args = {new CoreOperation(), new CoreOperation()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        boolean result = expression.findMatch(new CoreOperation().iterate(), new CoreOperation().iterate());
        // assert result
    }
}