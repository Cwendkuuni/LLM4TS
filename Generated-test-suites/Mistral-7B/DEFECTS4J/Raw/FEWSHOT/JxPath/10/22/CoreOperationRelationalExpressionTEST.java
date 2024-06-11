package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.iter.EmptyIterator;
import org.apache.commons.jxpath.ri.iter.SingletonIterator;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expr = new GreaterThanExpression(new VariableExpression("x"), new NumberExpression(5.0));
        assertTrue(expr.computeValue(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet())) == Boolean.TRUE);
        CoreOperationRelationalExpression expr2 = new GreaterThanOrEqualExpression(new VariableExpression("x"), new NumberExpression(5.0));
        assertTrue(expr2.computeValue(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet())) == Boolean.TRUE);
        CoreOperationRelationalExpression expr3 = new GreaterThanExpression(new NumberExpression(5.0), new VariableExpression("x"));
        assertTrue(expr3.computeValue(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet())) == Boolean.FALSE);
        CoreOperationRelationalExpression expr4 = new GreaterThanOrEqualExpression(new NumberExpression(5.0), new VariableExpression("x"));
        assertTrue(expr4.computeValue(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet())) == Boolean.TRUE);
        CoreOperationRelationalExpression expr5 = new LessThanExpression(new VariableExpression("x"), new NumberExpression(5.0));
        assertTrue(expr5.computeValue(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet())) == Boolean.FALSE);
        CoreOperationRelationalExpression expr6 = new LessThanOrEqualExpression(new VariableExpression("x"), new NumberExpression(5.0));
        assertTrue(expr6.computeValue(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet())) == Boolean.TRUE);
        CoreOperationRelationalExpression expr7 = new LessThanExpression(new NumberExpression(5.0), new VariableExpression("x"));
        assertTrue(expr7.computeValue(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet())) == Boolean.TRUE);
        CoreOperationRelationalExpression expr8 = new LessThanOrEqualExpression(new NumberExpression(5.0), new VariableExpression("x"));
        assertTrue(expr8.computeValue(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet())) == Boolean.FALSE);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new GreaterThanExpression(new VariableExpression("x"), new VariableExpression("y"));
        assertFalse(expr.isSymmetric());
        CoreOperationRelationalExpression expr2 = new GreaterThanOrEqualExpression(new VariableExpression("x"), new VariableExpression("y"));
        assertFalse(expr2.isSymmetric());
        CoreOperationRelationalExpression expr3 = new LessThanExpression(new VariableExpression("x"), new VariableExpression("y"));
        assertFalse(expr3.isSymmetric());
        CoreOperationRelationalExpression expr4 = new LessThanOrEqualExpression(new VariableExpression("x"), new VariableExpression("y"));
        assertFalse(expr4.isSymmetric());
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new GreaterThanExpression(new VariableExpression("x"), new VariableExpression("y"));
        assertEquals(3, expr.getPrecedence());
    }

    @Test
    public void testFindMatch() {
        Iterator lit = Arrays.asList(1, 2, 3, 4, 5).iterator();
        Iterator rit = Arrays.asList(2, 3, 4, 5, 6).iterator();
        CoreOperationRelationalExpression expr = new GreaterThanExpression(lit, rit);
        assertFalse(expr.computeValue(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet())));

        lit = Arrays.asList(5, 6).iterator();
        rit = Arrays.asList(2, 3, 4, 5, 6).iterator();
        expr = new ContainsExpression(lit, rit);
        assertTrue(expr.computeValue(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet())));

        Iterator left = new SingletonIterator(2);
        Object value = new NumberExpression(5.0);
        CoreOperationRelationalExpression expr2 = new ContainsExpression(left, value);
        assertFalse(expr2.computeValue(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet())));

        left = Iterators.emptyIterator();
        value = new NumberExpression(5.0);
        CoreOperationRelationalExpression expr3 = new ContainsExpression(left, value);
        assertFalse(expr3.computeValue(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet())));

        left = Arrays.asList(1, 2, 3, 4, 5).iterator();
        value = new NumberExpression(5.0);
        CoreOperationRelationalExpression expr4 = new ContainsExpression(left, value);
        assertTrue(expr4.computeValue(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet())));

        HashSet set = new HashSet();
        set.add(2);
        set.add(5);
        left = set.iterator();
        value = new NumberExpression(5.0);
        CoreOperationRelationalExpression expr5 = new ContainsExpression(left, value);
        assertTrue(expr5.computeValue(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet())));

        set.clear();
        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);
        set.add(5);
        left = set.iterator();
        value = new NumberExpression(6.0);
        CoreOperationRelationalExpression expr6 = new ContainsExpression(left, value);
        assertFalse(expr6.computeValue(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet())));

        left = new SingletonIterator(5);
        value = new NumberExpression(5.0);
        CoreOperationRelationalExpression expr7 = new ContainsExpression(left, value);
        assertTrue(expr7.computeValue(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet())));

        left = Iterators.emptyIterator();
        value = new SingletonIterator(5);
        CoreOperationRelationalExpression expr8 = new ContainsExpression(left, value);
        assertFalse(expr8.computeValue(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet())));

        left = Arrays.asList(1, 2, 3, 4, 5).iterator();
        value = new SingletonIterator(5);
        CoreOperationRelationalExpression expr9 = new ContainsExpression(left, value);
        assertTrue(expr9.computeValue(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet())));
    }

    @Test
    public void testContainsMatch() {
        Iterator it = Arrays.asList(1, 2, 3, 4, 5).iterator();
        Object value = new NumberExpression(5.0);
        CoreOperationRelationalExpression expr = new ContainsExpression(it, value);
        assertFalse(expr.computeValue(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet())));

        it = Arrays.asList(1, 2, 3, 5, 4).iterator();
        expr = new ContainsExpression(it, value);
        assertTrue(expr.computeValue(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet())));

        it = new EmptyIterator();
        expr = new ContainsExpression(it, value);
        assertFalse(expr.computeValue(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet())));

        it = Arrays.asList(1, 2, 3, 4, 5).iterator();
        value = new NumberExpression(6.0);
        CoreOperationRelationalExpression expr2 = new ContainsExpression(it, value);
        assertFalse(expr2.computeValue(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet())));

        it = new SingletonIterator(5);
        value = new NumberExpression(5.0);
        CoreOperationRelationalExpression expr3 = new ContainsExpression(it, value);
        assertTrue(expr3.computeValue(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet())));

        it = Iterators.emptyIterator();
        value = new NumberExpression(5.0);
        CoreOperationRelationalExpression expr4 = new ContainsExpression(it, value);
        assertFalse(expr4.computeValue(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet())));

        it = Arrays.asList(1, 2, 3, 4, 5).iterator();
        value = new SingletonIterator(5);
        CoreOperationRelationalExpression expr5 = new ContainsExpression(it, value);
        assertTrue(expr5.computeValue(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet())));
    }

    @Test
    public void testReduce() {
        Object o = new SelfContext();
        Object reduced = CoreOperationRelationalExpression.this.reduce(o);
        assertTrue(reduced instanceof Object);

        o = new SelfContext();
        reduced = CoreOperationRelationalExpression.this.reduce(new EvalContext(Collections.emptyMap(), new InfoSetUtil.EmptyInfoSet()));
        assertTrue(reduced instanceof Object);

        o = Arrays.asList(1, 2, 3, 4, 5);
        reduced = CoreOperationRelationalExpression.this.reduce(o);
        assertTrue(reduced instanceof Iterator);

        o = Collections.singletonList(1);
        reduced = CoreOperationRelationalExpression.this.reduce(o);
        assertTrue(reduced instanceof Iterator);

        o = new NumberExpression(5.0);
        reduced = CoreOperationRelationalExpression.this.reduce(o);
        assertTrue(reduced instanceof Number);

        o = Iterators.emptyIterator();
        reduced = CoreOperationRelationalExpression.this.reduce(o);
        assertTrue(reduced instanceof Iterator);
    }
}