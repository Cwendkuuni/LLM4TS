package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext();
        Expression left = new ConstantExpression(new Integer(1));
        Expression right = new ConstantExpression(new Integer(1));
        CoreOperationCompare operation = new EqualExpression(left, right);
        assertTrue(operation.compute(context));

        left = new ConstantExpression(new Integer(2));
        right = new ConstantExpression(new Integer(1));
        operation = new EqualExpression(left, right);
        assertFalse(operation.compute(context));

        left = new ConstantExpression(new Integer(1));
        right = new StringExpression("1");
        operation = new EqualExpression(left, right);
        assertTrue(operation.compute(context));

        left = new StringExpression("foo");
        right = new StringExpression("foo");
        operation = new EqualExpression(left, right);
        assertTrue(operation.compute(context));

        left = new StringExpression("foo");
        right = new StringExpression("bar");
        operation = new EqualExpression(left, right);
        assertFalse(operation.compute(context));

        left = new ConstantExpression(new Integer(1));
        right = null;
        operation = new EqualExpression(left, right);
        assertFalse(operation.compute(context));

        left = null;
        right = new ConstantExpression(new Integer(1));
        operation = new EqualExpression(left, right);
        assertFalse(operation.compute(context));

        left = new ConstantExpression(new Integer(1));
        right = new ConstantExpression(new Integer(1.0));
        operation = new EqualExpression(left, right);
        assertFalse(operation.compute(context));

        left = new ConstantExpression(Double.NaN);
        right = new ConstantExpression(Double.NaN);
        operation = new EqualExpression(left, right);
        assertFalse(operation.compute(context));

        left = new PointerExpression(new InitialContext(), "/root");
        right = new PointerExpression(new InitialContext(), "/root");
        operation = new EqualExpression(left, right);
        assertTrue(operation.compute(context));

        left = new PointerExpression(new InitialContext(), "/root/child1");
        right = new PointerExpression(new InitialContext(), "/root/child2");
        operation = new EqualExpression(left, right);
        assertFalse(operation.compute(context));

        left = new PointerExpression(new InitialContext(), "/root/child1");
        right = new PointerExpression(new InitialContext(), "/root/child1");
        operation = new EqualExpression(left, right);
        assertTrue(operation.compute(context));

        left = new PointerExpression(new SelfContext(new InitialContext()), null);
        right = new PointerExpression(new SelfContext(new InitialContext()), null);
        operation = new EqualExpression(left, right);
        assertTrue(operation.compute(context));

        left = new PointerExpression(new SelfContext(new InitialContext()), null);
        right = new PointerExpression(new InitialContext(), "/root");
        operation = new EqualExpression(left, right);
        assertFalse(operation.compute(context));
    }

    @Test
    public void testContains() {
        EvalContext context = new EvalContext();

        Iterator iterator = Collections.singletonList("foo").iterator();
        Expression left = new IteratorExpression(iterator);
        Expression right = new ConstantExpression("foo");
        CoreOperationCompare operation = new ContainsExpression(left, right);
        assertTrue(operation.compute(context));

        iterator = Collections.singletonList("foo").iterator();
        left = new IteratorExpression(iterator);
        right = new ConstantExpression("bar");
        operation = new ContainsExpression(left, right);
        assertFalse(operation.compute(context));

        iterator = Collections.singletonList("foo", "bar").iterator();
        left = new IteratorExpression(iterator);
        right = new ConstantExpression("foo");
        operation = new ContainsExpression(left, right);
        assertTrue(operation.compute(context));

        iterator = Collections.singletonList("foo", "bar").iterator();
        left = new IteratorExpression(iterator);
        right = new ConstantExpression("bar");
        operation = new ContainsExpression(left, right);
        assertTrue(operation.compute(context));

        iterator = Collections.singletonList("foo", "bar").iterator();
        left = new IteratorExpression(iterator);
        right = new ConstantExpression("baz");
        operation = new ContainsExpression(left, right);
        assertFalse(operation.compute(context));

        iterator = Collections.emptyList().iterator();
        left = new IteratorExpression(iterator);
        right = new ConstantExpression("foo");
        operation = new ContainsExpression(left, right);
        assertFalse(operation.compute(context));
    }

    @Test
    public void testFindMatch() {
        EvalContext context = new EvalContext();

        Iterator leftIterator = Collections.singletonList("foo", "bar").iterator();
        Iterator rightIterator = Collections.singletonList("bar", "baz").iterator();
        CoreOperationCompare operation = new ContainsExpression(new IteratorExpression(leftIterator), new IteratorExpression(rightIterator));
        assertTrue(operation.compute(context));

        leftIterator = Collections.singletonList("foo", "bar").iterator();
        rightIterator = Collections.singletonList("qux", "quuz").iterator();
        operation = new ContainsExpression(new IteratorExpression(leftIterator), new IteratorExpression(rightIterator));
        assertFalse(operation.compute(context));
    }
}