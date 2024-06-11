package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = { new Expression() {}, new Expression() {} };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        assertEquals(Boolean.FALSE, expression.computeValue(null));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = { new Expression() {}, new Expression() {} };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = { new Expression() {}, new Expression() {} };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        assertEquals(false, expression.isSymmetric());
    }

    @Test
    public void testCompute() {
        Expression[] args = { new Expression() {}, new Expression() {} };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        assertEquals(true, expression.compute(4, 5));
        assertEquals(false, expression.compute(5, 4));
        assertEquals(false, expression.compute(5, 5));
    }

    @Test
    public void testReduce() {
        Expression[] args = { new Expression() {}, new Expression() {} };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        assertEquals(5, expression.reduce(5));
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = { new Expression() {}, new Expression() {} };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        assertEquals(false, expression.containsMatch(null, null));
    }

    @Test
    public void testFindMatch() {
        Expression[] args = { new Expression() {}, new Expression() {} };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        assertEquals(false, expression.findMatch(null, null));
    }

}