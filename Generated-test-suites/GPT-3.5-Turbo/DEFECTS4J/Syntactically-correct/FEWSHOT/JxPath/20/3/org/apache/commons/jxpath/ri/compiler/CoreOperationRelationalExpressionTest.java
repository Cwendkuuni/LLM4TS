package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;

    @Before
    public void setUp() {
        Expression leftOperand = new ConstantExpression(10);
        Expression rightOperand = new ConstantExpression(5);
        expression = new CoreOperationRelationalExpressionImpl(leftOperand, rightOperand);
    }

    @Test
    public void testComputeValue() {
        assertTrue((Boolean) expression.computeValue(null));
    }

    @Test
    public void testGetPrecedence() {
        assertEquals(Expression.RELATIONAL_EXPR_PRECEDENCE, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        assertTrue(expression.evaluateCompare(0));
        assertFalse(expression.evaluateCompare(-1));
        assertFalse(expression.evaluateCompare(1));
    }

    @Test
    public void testCompute() {
        assertTrue(expression.compute(10, 5));
        assertFalse(expression.compute(10, 10));
        assertFalse(expression.compute(5, 10));
    }

    @Test
    public void testReduce() {
        Object result1 = expression.reduce(new SelfContext(null));
        assertTrue(result1 instanceof SelfContext);

        Object result2 = expression.reduce(new ConstantExpression(10));
        assertTrue(result2 instanceof ConstantExpression);

        Object result3 = expression.reduce(new HashSet<>());
        assertTrue(result3 instanceof Iterator);

        Object result4 = expression.reduce(10);
        assertEquals(10, result4);
    }

    @Test
    public void testContainsMatch() {
        Iterator<Integer> it = new Iterator<Integer>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < 5;
            }

            @Override
            public Integer next() {
                return index++;
            }
        };
        assertTrue(expression.containsMatch(it, 3));
        assertFalse(expression.containsMatch(it, 7));
    }

    @Test
    public void testFindMatch() {
        Iterator<Integer> lit = new Iterator<Integer>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < 5;
            }

            @Override
            public Integer next() {
                return index++;
            }
        };
        Iterator<Integer> rit = new Iterator<Integer>() {
            private int index = 3;

            @Override
            public boolean hasNext() {
                return index < 8;
            }

            @Override
            public Integer next() {
                return index++;
            }
        };
        assertTrue(expression.findMatch(lit, rit));
    }

    private class CoreOperationRelationalExpressionImpl extends CoreOperationRelationalExpression {

        public CoreOperationRelationalExpressionImpl(Expression leftOperand, Expression rightOperand) {
            super(new Expression[] { leftOperand, rightOperand });
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return compare == 0;
        }
    }
}