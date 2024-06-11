package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.iter.IteratorTestHelper;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new LiteralExpression(Integer.valueOf(5)), new LiteralExpression(Integer.valueOf(3)));
        EvalContext context = new EvalContext(new Object[]{});

        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));

        context = new EvalContext(new Object[]{Integer.valueOf(3), Integer.valueOf(5)});
        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new LiteralExpression(Integer.valueOf(5)), new LiteralExpression(Integer.valueOf(3)));
        int precedence = expression.getPrecedence();
        assertTrue(precedence == 3);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new LiteralExpression(Integer.valueOf(5)), new LiteralExpression(Integer.valueOf(3)));
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Object left = InfoSetUtil.createIterator(new Integer[]{1, 2, 3});
        Object right = InfoSetUtil.createIterator(new Integer[]{3, 2, 1});

        assertTrue(expression.compute(left, right));

        left = InfoSetUtil.createIterator(new Integer[]{1, 2, 3});
        right = InfoSetUtil.createIterator(new Integer[]{3, 2, 1, 1});

        assertFalse(expression.compute(left, right));
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression() {
            @Override
            protected Object reduce(Object o) {
                return InfoSetUtil.createIterator(new Integer[]{1, 2, 3});
            }
        };

        Object o = expression.reduce(null);
        assertTrue(o instanceof Iterator);

        o = expression.reduce(new Integer[]{1, 2, 3});
        assertTrue(o instanceof Iterator);
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new LiteralExpression(Integer.valueOf(5)), new LiteralExpression(Integer.valueOf(3)));

        IteratorTestHelper left = new IteratorTestHelper(new Integer[]{1, 2, 3, 4, 5});
        IteratorTestHelper right = new IteratorTestHelper(new Integer[]{3, 2, 1});

        assertTrue(expression.containsMatch(left.iterator(), right.iterator()));

        left = new IteratorTestHelper(new Integer[]{1, 2, 3, 4, 5});
        right = new IteratorTestHelper(new Integer[]{3, 2, 1, 4});

        assertTrue(expression.containsMatch(left.iterator(), right.iterator()));

        left = new IteratorTestHelper(new Integer[]{1, 2, 3, 4, 5});
        right = new IteratorTestHelper(new Integer[]{3, 2, 1});

        assertFalse(expression.containsMatch(left.iterator(), right.iterator()));

        left = new IteratorTestHelper(new Integer[]{1, 2, 3, 4, 5});
        right = new IteratorTestHelper(new Integer[]{3, 2, 1, 5});

        assertTrue(expression.containsMatch(left.iterator(), right.iterator()));
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new LiteralExpression(Integer.valueOf(5)), new LiteralExpression(Integer.valueOf(3)));

        IteratorTestHelper left = new IteratorTestHelper(new Integer[]{1, 2, 3, 4, 5});
        IteratorTestHelper right = new IteratorTestHelper(new Integer[]{3, 2, 1});

        HashSet set = new HashSet();
        while (left.hasNext()) {
            set.add(left.next());
        }

        IteratorTestHelper it = new IteratorTestHelper(right.iterator());
        assertTrue(expression.findMatch(left.iterator(), it));

        left = new IteratorTestHelper(new Integer[]{1, 2, 3, 4, 5});
        right = new IteratorTestHelper(new Integer[]{3, 2, 1, 1});

        it = new IteratorTestHelper(right.iterator());
        assertFalse(expression.findMatch(left.iterator(), it));

    }
}