package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpression() {
        assertNotNull(CoreOperationRelationalExpression.class);
    }

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new LiteralExpression(5.0), new LiteralExpression(3.0));
        EvalContext context = new EvalContext();
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expr = new CoreOperationGreaterThanOrEqual(new LiteralExpression(5.0), new LiteralExpression(3.0));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expr = new CoreOperationLessThan(new LiteralExpression(3.0), new LiteralExpression(5.0));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        expr = new CoreOperationLessThanOrEqual(new LiteralExpression(3.0), new LiteralExpression(5.0));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new LiteralExpression(5.0), new LiteralExpression(3.0));
        int precedence = expr.getPrecedence();
        assertEquals(CoreOperationRelationalExpression.RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new LiteralExpression(5.0), new LiteralExpression(3.0));
        assertFalse(expr.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new LiteralExpression(5.0), new LiteralExpression(3.0));
        assertTrue(expr.evaluateCompare(-1));
        assertTrue(expr.evaluateCompare(0));
        assertFalse(expr.evaluateCompare(1));
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new LiteralExpression(5.0), new LiteralExpression(3.0));
        Object left = new Double(3.5);
        Object right = new Double(5.5);
        Object result = expr.compute(left, right);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new Double(3.5);
        right = new Double(3.5);
        result = expr.compute(left, right);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new Double(Double.NaN);
        right = new Double(5.5);
        result = expr.compute(left, right);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new Double(Double.NaN);
        right = new Double(Double.NaN);
        result = expr.compute(left, right);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new SelfContext();
        right = new SelfContext();
        result = expr.compute(left, right);
        assertNull(result);

        left = new InitialContext();
        right = new InitialContext();
        result = expr.compute(left, right);
        assertNull(result);

        left = new Iterator[] { new InitialContext() };
        right = new Iterator[] { new InitialContext() };
        result = expr.compute(left, right);
        assertNull(result);

        // Test iteration comparison
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);

        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(3);
        list2.add(4);

        Iterator<Object> it1 = list1.iterator();
        Iterator<Object> it2 = list2.iterator();

        expr = new CoreOperationGreaterThan(it1, it2);
        result = expr.compute(it1, it2);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        Iterator<Object> it3 = list1.iterator();
        Iterator<Object> it4 = list2.iterator();

        expr = new CoreOperationGreaterThanOrEqual(it3, it4);
        result = expr.compute(it3, it4);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        it3 = list1.iterator();
        it4 = list2.iterator();

        expr = new CoreOperationLessThan(it3, it4);
        result = expr.compute(it3, it4);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        it3 = list1.iterator();
        it4 = list2.iterator();

        expr = new CoreOperationLessThanOrEqual(it3, it4);
        result = expr.compute(it3, it4);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // Test intersection comparison
        Set<Object> set1 = new HashSet<>();
        set1.add(new Integer(1));
        set1.add(new Integer(2));
        set1.add(new Integer(3));

        Set<Object> set2 = new HashSet<>();
        set2.add(new Integer(1));
        set2.add(new Integer(3));
        set2.add(new Integer(4));

        Iterator<Object> iter1 = set1.iterator();
        Iterator<Object> iter2 = set2.iterator();

        expr = new CoreOperationGreaterThan(iter1, iter2);
        result = expr.compute(iter1, iter2);
        assertFalse(result instanceof Boolean);

        iter1 = set1.iterator();
        iter2 = set2.iterator();

        expr = new CoreOperationGreaterThanOrEqual(iter1, iter2);
        result = expr.compute(iter1, iter2);
        assertFalse(result instanceof Boolean);

        iter1 = set1.iterator();
        iter2 = set2.iterator();

        expr = new CoreOperationLessThan(iter1, iter2);
        result = expr.compute(iter1, iter2);
        assertFalse(result instanceof Boolean);

        iter1 = set1.iterator();
        iter2 = set2.iterator();

        expr = new CoreOperationLessThanOrEqual(iter1, iter2);
        result = expr.compute(iter1, iter2);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testReduce() {
        Object o = new SelfContext();
        Object reduced = CoreOperationRelationalExpression.class.cast(new CoreOperationRelationalExpression(new CoreOperation[] { new LiteralExpression(5.0), new CoreOperationGreaterThan(o, new LiteralExpression(3.0)) })).reduce(o);
        assertNotNull(reduced);
        assertTrue(reduced instanceof InitialContext);

        o = new Collection<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return Collections.emptyIterator();
            }
        };
        reduced = CoreOperationRelationalExpression.class.cast(new CoreOperationRelationalExpression(new CoreOperation[] { new LiteralExpression(5.0), new CoreOperationGreaterThan(o, new LiteralExpression(3.0)) })).reduce(o);
        assertNotNull(reduced);
        assertNull(reduced);

        o = new Double(3.5);
        reduced = CoreOperationRelationalExpression.class.cast(new CoreOperationRelationalExpression(new CoreOperation[] { new LiteralExpression(5.0), new CoreOperationGreaterThan(o, new LiteralExpression(3.0)) })).reduce(o);
        assertNotNull(reduced);
        assertNull(reduced);
    }

    @Test
    public void testContainsMatch() {
        Iterator<Object> it = new ArrayList<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return null;
            }
        }.iterator();
        Object value = new Object();
        boolean matchFound = CoreOperationRelationalExpression.class.cast(new CoreOperationRelationalExpression(new CoreOperation[] { new CoreOperationGreaterThan(it, new LiteralExpression(value)), new LiteralExpression(5.0) })).containsMatch(it, value);
        assertFalse(matchFound);
    }

    @Test
    public void testFindMatch() {
        Iterator<Object> it1 = new ArrayList<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return null;
            }
        }.iterator();
        Iterator<Object> it2 = new ArrayList<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return null;
            }
        }.iterator();
        boolean matchFound = CoreOperationRelationalExpression.class.cast(new CoreOperationRelationalExpression(new CoreOperation[] { new CoreOperationGreaterThan(it1, it2), new LiteralExpression(5.0) })).findMatch(it1, it2);
        assertFalse(matchFound);
    }
}