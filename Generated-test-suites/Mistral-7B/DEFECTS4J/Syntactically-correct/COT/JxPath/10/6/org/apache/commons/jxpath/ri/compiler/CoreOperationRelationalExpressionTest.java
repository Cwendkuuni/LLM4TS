package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.InfoSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

/**
 * Tests for CoreOperationRelationalExpression class
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testConstructor() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertNotNull(expression);
    }

    @Test
    public void testComputeValue() {
        Expression left = new LiteralExpression(new Integer(5));
        Expression right = new LiteralExpression(new Integer(10));
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreOperationUnaryMinus(left), right);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new LiteralExpression(new Integer(10));
        right = new LiteralExpression(new Integer(5));
        expression = new CoreOperationGreaterThan(left, right);
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new LiteralExpression(new Integer(1)), new LiteralExpression(new Integer(1)));
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new LiteralExpression(new Integer(1)), new LiteralExpression(new Integer(1)));
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        Object left = new LiteralExpression(new Integer(5));
        Object right = new LiteralExpression(new Integer(10));
        assertTrue(expression.evaluateCompare(left.getClass().getName().compareTo(right.getClass().getName())));

        left = new LiteralExpression(new Integer(10));
        right = new LiteralExpression(new Integer(5));
        assertFalse(expression.evaluateCompare(left.getClass().getName().compareTo(right.getClass().getName())));
    }

    @Test
    public void testCompute() {
        Expression left = new LiteralExpression(new Integer(5));
        Expression right = new LiteralExpression(new Integer(10));
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(left, right);
        Object result = expression.compute(left.computeValue(new EvalContext(new InfoSet())), right.computeValue(new EvalContext(new InfoSet())));
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new LiteralExpression(new Integer(10));
        right = new LiteralExpression(new Integer(5));
        expression = new CoreOperationGreaterThan(left, right);
        result = expression.compute(left.computeValue(new EvalContext(new InfoSet())), right.computeValue(new EvalContext(new InfoSet())));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testReduce() {
        Object o = new SelfContext();
        Object result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThan(new LiteralExpression(new Integer(1)), o)).reduce(o);
        assertTrue(result instanceof InitialContext);

        Collection<Object> collection = new ArrayList<>();
        collection.add(new Integer(1));
        o = collection.iterator();
        result = ((CoreOperationRelationalExpression) new CoreOperationGreaterThan(new LiteralExpression(new Integer(2)), o)).reduce(o);
        assertTrue(result instanceof Iterator);
    }

    @Test
    public void testContainsMatch() {
        Collection<Object> collection1 = new ArrayList<>();
        collection1.add(new Integer(1));
        collection1.add(new Integer(2));
        collection1.add(new Integer(3));

        Collection<Object> collection2 = new ArrayList<>();
        collection2.add(new Integer(2));
        collection2.add(new Integer(3));

        Iterator iterator1 = collection1.iterator();
        Iterator iterator2 = collection2.iterator();
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(iterator1, new LiteralExpression(new Integer(2)));
        assertTrue(expression.containsMatch(iterator1, new LiteralExpression(new Integer(2))));
        assertFalse(expression.containsMatch(iterator1, new LiteralExpression(new Integer(1))));
        assertFalse(expression.containsMatch(iterator1, new LiteralExpression(new Integer(4))));

        expression = new CoreOperationGreaterThan(iterator2, new LiteralExpression(new Integer(1)));
        assertTrue(expression.containsMatch(iterator2, new LiteralExpression(new Integer(2))));
        assertFalse(expression.containsMatch(iterator2, new LiteralExpression(new Integer(3))));
        assertFalse(expression.containsMatch(iterator2, new LiteralExpression(new Integer(4))));

        expression = new CoreOperationGreaterThan(iterator1, iterator2);
        assertFalse(expression.containsMatch(iterator1, iterator2));
    }

    @Test
    public void testFindMatch() {
        Collection<Object> collection1 = new ArrayList<>();
        collection1.add(new Integer(1));
        collection1.add(new Integer(2));
        collection1.add(new Integer(3));

        Collection<Object> collection2 = new ArrayList<>();
        collection2.add(new Integer(3));
        collection2.add(new Integer(2));

        Iterator iterator1 = collection1.iterator();
        Iterator iterator2 = collection2.iterator();
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(iterator1, iterator2);
        HashSet<Object> set = new HashSet<>();
        while (iterator1.hasNext()) {
            set.add(iterator1.next());
        }
        assertTrue(expression.findMatch(iterator1, iterator2));
        assertTrue(containsAll(set, collection2));
    }

    private boolean containsAll(Collection<Object> set, Collection<Object> collection) {
        for (Object object : collection) {
            if (!set.contains(object)) {
                return false;
            }
        }
        return true;
    }

}