package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationRelationalExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.TestHelper;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link CoreOperationRelationalExpression} class.
 */
public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpressionFactory factory;

    @Before
    public void setup() {
        factory = TestHelper.getCoreOperationRelationalExpressionFactory();
    }

    @Test
    public void testGreaterThan() {
        Object left = new Integer(3);
        Object right = new Integer(4);
        CoreOperationRelationalExpression expr = factory.createGreaterThanExpression(Arrays.asList(left), Arrays.asList(right));
        assertEquals(Boolean.TRUE, expr.computeValue(new EvalContext(new SelfContext(new Object()))));
    }

    @Test
    public void testGreaterThanOrEqual() {
        Object left = new Integer(3);
        Object right = new Integer(3);
        CoreOperationRelationalExpression expr = factory.createGreaterThanOrEqualExpression(Arrays.asList(left), Arrays.asList(right));
        assertEquals(Boolean.TRUE, expr.computeValue(new EvalContext(new SelfContext(new Object()))));

        left = new Integer(2);
        right = new Integer(3);
        expr = factory.createGreaterThanOrEqualExpression(Arrays.asList(left), Arrays.asList(right));
        assertFalse(expr.computeValue(new EvalContext(new SelfContext(new Object()))));
    }

    @Test
    public void testLessThan() {
        Object left = new Integer(2);
        Object right = new Integer(3);
        CoreOperationRelationalExpression expr = factory.createLessThanExpression(Arrays.asList(left), Arrays.asList(right));
        assertFalse(expr.computeValue(new EvalContext(new SelfContext(new Object()))));

        left = new Integer(3);
        right = new Integer(2);
        expr = factory.createLessThanExpression(Arrays.asList(left), Arrays.asList(right));
        assertEquals(Boolean.TRUE, expr.computeValue(new EvalContext(new SelfContext(new Object()))));
    }

    @Test
    public void testLessThanOrEqual() {
        Object left = new Integer(2);
        Object right = new Integer(2);
        CoreOperationRelationalExpression expr = factory.createLessThanOrEqualExpression(Arrays.asList(left), Arrays.asList(right));
        assertEquals(Boolean.TRUE, expr.computeValue(new EvalContext(new SelfContext(new Object()))));

        left = new Integer(3);
        right = new Integer(2);
        expr = factory.createLessThanOrEqualExpression(Arrays.asList(left), Arrays.asList(right));
        assertFalse(expr.computeValue(new EvalContext(new SelfContext(new Object()))));
    }

    @Test
    public void testComparisonWithInitialContexts() {
        Object left = new InitialContext();
        Object right = new Integer(3);
        CoreOperationRelationalExpression expr = factory.createGreaterThanExpression(Arrays.asList(left), Arrays.asList(right));
        assertFalse(expr.computeValue(new EvalContext(new SelfContext(new Object()))));

        left = new Integer(4);
        expr = factory.createGreaterThanExpression(Arrays.asList(left), Arrays.asList(right));
        assertTrue(expr.computeValue(new EvalContext(new SelfContext(new Object()))));

        left = new InitialContext();
        expr = factory.createLessThanExpression(Arrays.asList(left), Arrays.asList(right));
        assertTrue(expr.computeValue(new EvalContext(new SelfContext(new Object()))));

        left = new Integer(2);
        expr = factory.createLessThanExpression(Arrays.asList(left), Arrays.asList(right));
        assertFalse(expr.computeValue(new EvalContext(new SelfContext(new Object()))));
    }

    @Test
    public void testComparisonWithCollection() {
        Collection leftCollection = new ArrayList();
        leftCollection.add(new Integer(2));
        leftCollection.add(new Integer(3));
        Object right = new Integer(3);
        CoreOperationRelationalExpression expr = factory.createLessThanExpression(Arrays.asList(leftCollection.iterator()), Arrays.asList(right));
        assertFalse(expr.computeValue(new EvalContext(new SelfContext(new Object()))));

        leftCollection.add(new Integer(2));
        expr = factory.createLessThanExpression(Arrays.asList(leftCollection.iterator()), Arrays.asList(right));
        assertTrue(expr.computeValue(new EvalContext(new SelfContext(new Object()))));

        leftCollection.clear();
        leftCollection.add(new Integer(3));
        leftCollection.add(new Integer(2));
        expr = factory.createGreaterThanExpression(Arrays.asList(leftCollection.iterator()), Arrays.asList(right));
        assertFalse(expr.computeValue(new EvalContext(new SelfContext(new Object()))));

        leftCollection.clear();
        leftCollection.add(new Integer(3));
        leftCollection.add(new Integer(3));
        expr = factory.createGreaterThanOrEqualExpression(Arrays.asList(leftCollection.iterator()), Arrays.asList(right));
        assertTrue(expr.computeValue(new EvalContext(new SelfContext(new Object()))));

        leftCollection.clear();
        leftCollection.add(new Integer(3));
        leftCollection.add(new Integer(4));
        expr = factory.createLessThanExpression(Arrays.asList(leftCollection.iterator()), Arrays.asList(right));
        assertFalse(expr.computeValue(new EvalContext(new SelfContext(new Object()))));

        leftCollection.clear();
        leftCollection.add(new Integer(3));
        leftCollection.add(new Integer(3));
        expr = factory.createLessThanOrEqualExpression(Arrays.asList(leftCollection.iterator()), Arrays.asList(right));
        assertTrue(expr.computeValue(new EvalContext(new SelfContext(new Object()))));
    }

    @Test
    public void testComparisonWithDoubleValues() {
        Object left = new Double(1.2);
        Object right = new Double(1.2);
        CoreOperationRelationalExpression expr = factory.createGreaterThanExpression(Arrays.asList(left), Arrays.asList(right));
        assertFalse(expr.computeValue(new EvalContext(new SelfContext(new Object()))));

        left = new Double(1.3);
        expr = factory.createGreaterThanExpression(Arrays.asList(left), Arrays.asList(right));
        assertFalse(expr.computeValue(new EvalContext(new SelfContext(new Object()))));

        left = new Double(1.3);
        right = new Double(1.2);
        expr = factory.createLessThanExpression(Arrays.asList(left), Arrays.asList(right));
        assertTrue(expr.computeValue(new EvalContext(new SelfContext(new Object()))));

        left = new Double(1.2);
        right = new Double(1.3);
        expr = factory.createLessThanExpression(Arrays.asList(left), Arrays.asList(right));
        assertFalse(expr.computeValue(new EvalContext(new SelfContext(new Object()))));

        left = new Double(1.2);
        right = new Double(1.2);
        expr = factory.createGreaterThanOrEqualExpression(Arrays.asList(left), Arrays.asList(right));
        assertTrue(expr.computeValue(new EvalContext(new SelfContext(new Object()))));

        left = new Double(1.3);
        right = new Double(1.2);
        expr = factory.createGreaterThanOrEqualExpression(Arrays.asList(left), Arrays.asList(right));
        assertTrue(expr.computeValue(new EvalContext(new SelfContext(new Object()))));

        left = new Double(1.2);
        right = new Double(1.3);
        expr = factory.createLessThanOrEqualExpression(Arrays.asList(left), Arrays.asList(right));
        assertFalse(expr.computeValue(new EvalContext(new SelfContext(new Object()))));

        left = new Double(1.2);
        right = new Double(1.2);
        expr = factory.createLessThanOrEqualExpression(Arrays.asList(left), Arrays.asList(right));
        assertTrue(expr.computeValue(new EvalContext(new SelfContext(new Object()))));

        left = new Double(Double.NaN);
        right = new Double(Double.NaN);
        expr = factory.createGreaterThanExpression(Arrays.asList(left), Arrays.asList(right));
        assertFalse(expr.computeValue(new EvalContext(new SelfContext(new Object()))));
    }

    @Test(expected = IllegalStateException.class)
    public void testComparisonWithInvalidArguments() {
        Object left = new SelfContext(new Object());
        Object right = new Integer(3);
        CoreOperationRelationalExpression expr = factory.createGreaterThanExpression(Arrays.asList(left), Arrays.asList(right));
        expr.computeValue(new EvalContext(new SelfContext(new Object())));
    }

    @Test
    public void testComparisonWithIterators() {
        Iterator leftIterator = Arrays.asList(new Integer(2), new Integer(3)).iterator();
        Object right = new Integer(3);
        CoreOperationRelationalExpression expr = factory.createLessThanExpression(Arrays.asList(leftIterator), Arrays.asList(right));
        assertFalse(expr.computeValue(new EvalContext(new SelfContext(new Object()))));

        leftIterator = Arrays.asList(new Integer(2), new Integer(3), new Integer(2)).iterator();
        expr = factory.createLessThanExpression(Arrays.asList(leftIterator), Arrays.asList(right));
        assertTrue(expr.computeValue(new EvalContext(new SelfContext(new Object()))));

        leftIterator = Arrays.asList(new Integer(3), new Integer(2)).iterator();
        expr = factory.createGreaterThanExpression(Arrays.asList(leftIterator), Arrays.asList(right));
        assertFalse(expr.computeValue(new EvalContext(new SelfContext(new Object()))));

        leftIterator = Arrays.asList(new Integer(3), new Integer(3)).iterator();
        expr = factory.createGreaterThanOrEqualExpression(Arrays.asList(leftIterator), Arrays.asList(right));
        assertTrue(expr.computeValue(new EvalContext(new SelfContext(new Object()))));

        leftIterator = Arrays.asList(new Integer(3), new Integer(3)).iterator();
        expr = factory.createLessThanOrEqualExpression(Arrays.asList(leftIterator), Arrays.asList(right));
        assertTrue(expr.computeValue(new EvalContext(new SelfContext(new Object()))));

        leftIterator = Arrays.asList(new Integer(3), new Integer(2)).iterator();
        expr = factory.createLessThanOrEqualExpression(Arrays.asList(leftIterator), Arrays.asList(right));
        assertFalse(expr.computeValue(new EvalContext(new SelfContext(new Object()))));
    }
}