package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new LiteralExpression(2.0), new LiteralExpression(3.0));
        Object result = expr.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expr = new CoreOperationGreaterThanOrEqual(new LiteralExpression(2.0), new LiteralExpression(3.0));
        result = expr.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expr = new CoreOperationGreaterThan(new LiteralExpression(2.0), new LiteralExpression(2.0));
        result = expr.computeValue(new EvalContext());
        assertFalse(result instanceof Boolean);

        expr = new CoreOperationGreaterThanOrEqual(new LiteralExpression(2.0), new LiteralExpression(2.0));
        result = expr.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expr = new CoreOperationGreaterThan(new LiteralExpression("foo"), new LiteralExpression("bar"));
        result = expr.computeValue(new EvalContext());
        assertFalse(result instanceof Boolean);

        expr = new CoreOperationGreaterThanOrEqual(new LiteralExpression("foo"), new LiteralExpression("foo"));
        result = expr.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expr = new CoreOperationGreaterThan(new LiteralExpression(Integer.MIN_VALUE), new LiteralExpression(0));
        result = expr.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expr = new CoreOperationGreaterThanOrEqual(new LiteralExpression(Integer.MIN_VALUE), new LiteralExpression(0));
        result = expr.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expr = new CoreOperationGreaterThan(new LiteralExpression(null), new LiteralExpression(null));
        result = expr.computeValue(new EvalContext());
        assertFalse(result instanceof Boolean);

        expr = new CoreOperationGreaterThanOrEqual(new LiteralExpression(null), new LiteralExpression(null));
        result = expr.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expr = new CoreOperationGreaterThan(new LiteralExpression(null), new LiteralExpression(""));
        result = expr.computeValue(new EvalContext());
        assertFalse(result instanceof Boolean);

        expr = new CoreOperationGreaterThanOrEqual(new LiteralExpression(null), new LiteralExpression(""));
        result = expr.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // Test with initial contexts
        EvalContext context = new EvalContext();
        InitialContext leftContext = new InitialContext(context);
        InitialContext rightContext = new InitialContext(context);
        context.setSingleNodePointer(leftContext);
        context.setSingleNodePointer(rightContext);

        expr = new CoreOperationGreaterThan(leftContext, rightContext);
        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        context.setSingleNodePointer(rightContext);
        context.setSingleNodePointer(leftContext);
        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        expr = new CoreOperationGreaterThanOrEqual(leftContext, rightContext);
        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        context.setSingleNodePointer(rightContext);
        context.setSingleNodePointer(leftContext);
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // Test with self contexts
        SelfContext leftSelfContext = new SelfContext();
        SelfContext rightSelfContext = new SelfContext();
        context.setSingleNodePointer(leftSelfContext);
        context.setSingleNodePointer(rightSelfContext);

        expr = new CoreOperationGreaterThan(leftSelfContext, rightSelfContext);
        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        context.setSingleNodePointer(rightSelfContext);
        context.setSingleNodePointer(leftSelfContext);
        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        expr = new CoreOperationGreaterThanOrEqual(leftSelfContext, rightSelfContext);
        result = expr.computeValue(context);
        assertFalse(result instanceof Boolean);

        context.setSingleNodePointer(rightSelfContext);
        context.setSingleNodePointer(leftSelfContext);
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // Test with collections
        Collection leftCollection = new ArrayList();
        leftCollection.add(new LiteralExpression(2.0));
        leftCollection.add(new LiteralExpression(3.0));
        leftCollection.add(new LiteralExpression(4.0));

        Collection rightCollection = new ArrayList();
        rightCollection.add(new LiteralExpression(1.0));
        rightCollection.add(new LiteralExpression(2.0));
        rightCollection.add(new LiteralExpression(3.0));

        expr = new CoreOperationGreaterThan(new IteratorExpression(leftCollection.iterator()), new IteratorExpression(rightCollection.iterator()));
        result = expr.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        expr = new CoreOperationGreaterThanOrEqual(new IteratorExpression(leftCollection.iterator()), new IteratorExpression(rightCollection.iterator()));
        result = expr.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // Test with intersection
        Collection leftIntersectionCollection = new ArrayList();
        leftIntersectionCollection.add(new LiteralExpression(2.0));
        leftIntersectionCollection.add(new LiteralExpression(3.0));

        Collection rightIntersectionCollection = new ArrayList();
        rightIntersectionCollection.add(new LiteralExpression(2.0));
        rightIntersectionCollection.add(new LiteralExpression(3.0));
        rightIntersectionCollection.add(new LiteralExpression(4.0));

        expr = new CoreOperationGreaterThan(new IteratorExpression(leftIntersectionCollection.iterator()), new IteratorExpression(rightIntersectionCollection.iterator()));
        result = expr.computeValue(new EvalContext());
        assertFalse(result instanceof Boolean);

        expr = new CoreOperationGreaterThanOrEqual(new IteratorExpression(leftIntersectionCollection.iterator()), new IteratorExpression(rightIntersectionCollection.iterator()));
        result = expr.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // Test with disjoint sets
        Collection leftDisjointCollection = new ArrayList();
        leftDisjointCollection.add(new LiteralExpression(2.0));
        leftDisjointCollection.add(new LiteralExpression(5.0));

        Collection rightDisjointCollection = new ArrayList();
        rightDisjointCollection.add(new LiteralExpression(1.0));
        rightDisjointCollection.add(new LiteralExpression(3.0));

        expr = new CoreOperationGreaterThan(new IteratorExpression(leftDisjointCollection.iterator()), new IteratorExpression(rightDisjointCollection.iterator()));
        result = expr.computeValue(new EvalContext());
        assertFalse(result instanceof Boolean);

        expr = new CoreOperationGreaterThanOrEqual(new IteratorExpression(leftDisjointCollection.iterator()), new IteratorExpression(rightDisjointCollection.iterator()));
        result = expr.computeValue(new EvalContext());
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new LiteralExpression(1), new LiteralExpression(2));
        Iterator leftIterator = new ArrayList() {{ add(new LiteralExpression(1)); add(new LiteralExpression(2)); add(new LiteralExpression(3)); }}.iterator();
        Iterator rightIterator = new ArrayList() {{ add(new LiteralExpression(1)); add(new LiteralExpression(2)); add(new LiteralExpression(3)); add(new LiteralExpression(4)); }}.iterator();

        assertTrue(expr.findMatch(leftIterator, rightIterator));

        leftIterator = new ArrayList() {{ add(new LiteralExpression(1)); add(new LiteralExpression(2)); add(new LiteralExpression(3)); }}.iterator();
        rightIterator = new ArrayList() {{ add(new LiteralExpression(1)); add(new LiteralExpression(2)); add(new LiteralExpression(3)); add(new LiteralExpression(5)); }}.iterator();

        assertFalse(expr.findMatch(leftIterator, rightIterator));

        leftIterator = new ArrayList() {{ add(new LiteralExpression(1)); add(new LiteralExpression(2)); add(new LiteralExpression(3)); add(new LiteralExpression(4)); }}.iterator();
        rightIterator = new ArrayList() {{ add(new LiteralExpression(1)); add(new LiteralExpression(2)); add(new LiteralExpression(3)); }}.iterator();

        assertTrue(expr.findMatch(leftIterator, rightIterator));

        leftIterator = new ArrayList() {{ add(new LiteralExpression(1)); add(new LiteralExpression(2)); add(new LiteralExpression(3)); }}.iterator();
        rightIterator = new ArrayList() {{ add(new LiteralExpression(1)); add(new LiteralExpression(2)); add(new LiteralExpression(4)); }}.iterator();

        assertFalse(expr.findMatch(leftIterator, rightIterator));
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new LiteralExpression(1), new LiteralExpression(2));
        Iterator iter = new ArrayList() {{ add(new LiteralExpression(1)); add(new LiteralExpression(2)); add(new LiteralExpression(3)); }}.iterator();

        assertTrue(expr.containsMatch(iter, new LiteralExpression(2)));

        iter = new ArrayList() {{ add(new LiteralExpression(1)); add(new LiteralExpression(2)); add(new LiteralExpression(3)); }}.iterator();

        assertFalse(expr.containsMatch(iter, new LiteralExpression(4)));

        iter = new ArrayList() {{ add(new LiteralExpression(1)); add(new LiteralExpression(2)); add(new LiteralExpression(3)); }}.iterator();

        assertTrue(expr.containsMatch(iter, new LiteralExpression(1)));

        iter = new ArrayList() {{ add(new LiteralExpression(1)); add(new LiteralExpression(2)); add(new LiteralExpression(3)); add(new LiteralExpression(4)); }}.iterator();

        assertFalse(expr.containsMatch(iter, new LiteralExpression(5)));
    }
}