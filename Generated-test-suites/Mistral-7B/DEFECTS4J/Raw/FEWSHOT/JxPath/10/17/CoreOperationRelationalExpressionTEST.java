package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new ConstantExpression(5L), new ConstantExpression(4L));
        EvalContext context = new EvalContext(new Object[0]);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithNull() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new ConstantExpression(5L), new ConstantExpression(null));
        EvalContext context = new EvalContext(new Object[0]);
        Object result = expression.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testComputeValueWithIterators() {
        List list1 = new ArrayList();
        list1.add(1.0);
        list1.add(2.0);
        List list2 = new ArrayList();
        list2.add(2.0);
        list2.add(3.0);

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new IteratorExpression(list1.iterator()), new IteratorExpression(list2.iterator()));
        EvalContext context = new EvalContext(new Object[0]);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        list1 = new ArrayList();
        list1.add(2.0);
        list1.add(3.0);
        list2 = new ArrayList();
        list2.add(2.0);
        list2.add(3.0);

        expression = new CoreOperationGreaterThan(new IteratorExpression(list1.iterator()), new IteratorExpression(list2.iterator()));
        context = new EvalContext(new Object[0]);
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithCollections() {
        Collection coll1 = new HashSet();
        coll1.add(1.0);
        coll1.add(2.0);
        Collection coll2 = new HashSet();
        coll2.add(2.0);
        coll2.add(3.0);

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CollectionExpression(coll1.iterator()), new CollectionExpression(coll2.iterator()));
        EvalContext context = new EvalContext(new Object[0]);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        coll1 = new HashSet();
        coll1.add(2.0);
        coll1.add(3.0);
        coll2 = new HashSet();
        coll2.add(2.0);
        coll2.add(3.0);

        expression = new CoreOperationGreaterThan(new CollectionExpression(coll1.iterator()), new CollectionExpression(coll2.iterator()));
        context = new EvalContext(new Object[0]);
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithInitialContexts() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new InitialContextExpression(), new InitialContextExpression());
        EvalContext context = new EvalContext(new Object[0]);
        context.setSingleNodePointer(new Object());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result);
    }

    @Test
    public void testComputeValueWithSelfContexts() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new SelfContextExpression(), new SelfContextExpression());
        EvalContext context = new EvalContext(new Object[0]);
        context.setSingleNodePointer(new Object());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result);
    }

    @Test
    public void testComputeValueWithIncomparableTypes() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new ConstantExpression("foo"), new ConstantExpression(5L));
        EvalContext context = new EvalContext(new Object[0]);
        Object result = expression.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new ConstantExpression(5L), new ConstantExpression(4L));
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new ConstantExpression(5L), new ConstantExpression(4L));
        boolean symmetric = expression.isSymmetric();
        assertFalse(symmetric);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEvaluateCompareUnsupported() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new ConstantExpression(5L), new ConstantExpression(4L));
        expression.evaluateCompare(0);
    }
}