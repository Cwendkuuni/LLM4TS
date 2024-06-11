package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Iterate;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Object left = new Integer(5);
        Object right = new Integer(10);
        CoreOperationRelationalExpression expr = new GreaterThanExpression(new VariableExpression("x"));
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public Iterator iterator() {
                return Iterators.EMPTY_ITERATOR;
            }

            @Override
            public Object getSingleNodePointer() {
                return left;
            }
        }, null);

        assertTrue(expr.computeValue(context).equals(Boolean.FALSE));

        context = new EvalContext(new InfoSet() {
            @Override
            public Iterator iterator() {
                return Iterators.singletonIterator(right);
            }

            @Override
            public Object getSingleNodePointer() {
                return left;
            }
        }, null);

        assertTrue(expr.computeValue(context).equals(Boolean.FALSE));

        context = new EvalContext(new InfoSet() {
            @Override
            public Iterator iterator() {
                return Iterators.singletonIterator(left);
            }

            @Override
            public Object getSingleNodePointer() {
                return right;
            }
        }, null);

        assertTrue(expr.computeValue(context).equals(Boolean.FALSE));

        context = new EvalContext(new InfoSet() {
            @Override
            public Iterator iterator() {
                return Iterators.singletonIterator(right);
            }

            @Override
            public Object getSingleNodePointer() {
                return left;
            }
        }, null);

        assertTrue(expr.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new GreaterThanExpression(new VariableExpression("x"));
        assertEquals(3, expr.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new GreaterThanExpression(new VariableExpression("x"));
        assertFalse(expr.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expr = new GreaterThanExpression(new VariableExpression("x"));
        assertTrue(expr.evaluateCompare(1) == -1);
        assertTrue(expr.evaluateCompare(0) == 0);
        assertTrue(expr.evaluateCompare(-1) == 1);
    }

    @Test
    public void testCompute() {
        Object left = new Integer(5);
        Object right = new Integer(10);

        CoreOperationRelationalExpression expr = new GreaterThanExpression(new VariableExpression("x"));

        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public Iterator iterator() {
                return Iterators.singletonIterator(left);
            }

            @Override
            public Object getSingleNodePointer() {
                return left;
            }
        }, null);

        Object testLeft = expr.compute(left, right);
        Object testRight = expr.compute(right, left);

        assertTrue(testLeft.equals(Boolean.FALSE));
        assertTrue(testRight.equals(Boolean.TRUE));
    }

    @Test
    public void testReduce() {
        Object left = new Integer(5);
        Object right = new Integer(10);

        CoreOperationRelationalExpression expr = new GreaterThanExpression(new VariableExpression("x"));

        assertTrue(expr.reduce(left)
                .equals(left));
        assertTrue(expr.reduce(right)
                .equals(right));
        assertTrue(expr.reduce(new SelfContext())
                .equals(null));
        assertTrue(expr.reduce(Iterators.singletonIterator(left))
                .equals(left));
        assertTrue(expr.reduce(Iterators.singletonIterator(right))
                .equals(right));
    }

    @Test
    public void testContainsMatch() {
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        NodeTest nodeTest = new NodeTest(new VariableExpression("x"));
        NodeIterator nodeIterator = Iterators.arrayIterator(numbers);

        assertTrue(expr.containsMatch(nodeIterator, new Integer(6)));
        assertFalse(expr.containsMatch(nodeIterator, new Integer(4)));
    }

    @Test
    public void testFindMatch() {
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        NodeTest nodeTest = new NodeTest(new VariableExpression("x"));
        NodeIterator leftIterator = Iterators.arrayIterator(numbers);
        NodeIterator rightIterator = Iterators.singletonIterator(new Integer(5));

        assertTrue(expr.findMatch(leftIterator, rightIterator));
        assertFalse(expr.findMatch(leftIterator, Iterators.singletonIterator(new Integer(6))));
    }
}