package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import java.util.Set;

public class CoreOperationCompareTest {

    @Test
    public void testCoreOperationCompare_equal_twoBooleanValues() {
        Expression left = new Const(true);
        Expression right = new Const(true);
        CoreOperationCompare coreOperationCompare = new Equal(left, right);
        EvalContext context = new EvalContext(new InitialContext());
        assertTrue(coreOperationCompare.compute(context));
    }

    @Test
    public void testCoreOperationCompare_equal_twoStringValues() {
        Expression left = new Const("foo");
        Expression right = new Const("foo");
        CoreOperationCompare coreOperationCompare = new Equal(left, right);
        EvalContext context = new EvalContext(new InitialContext());
        assertTrue(coreOperationCompare.compute(context));
    }

    @Test
    public void testCoreOperationCompare_equal_twoNumberValues() {
        Expression left = new Const(1.0);
        Expression right = new Const(1.0);
        CoreOperationCompare coreOperationCompare = new Equal(left, right);
        EvalContext context = new EvalContext(new InitialContext());
        assertTrue(coreOperationCompare.compute(context));
    }

    @Test
    public void testCoreOperationCompare_equal_twoInitialContexts() {
        Expression left = new InitialContext();
        Expression right = new InitialContext();
        CoreOperationCompare coreOperationCompare = new Equal(left, right);
        EvalContext context = new EvalContext(new InitialContext());
        assertFalse(coreOperationCompare.compute(context));
    }

    @Test
    public void testCoreOperationCompare_equal_twoSelfContexts() {
        Expression left = new SelfContext();
        Expression right = new SelfContext();
        CoreOperationCompare coreOperationCompare = new Equal(left, right);
        EvalContext context = new EvalContext(new SelfContext());
        assertFalse(coreOperationCompare.compute(context));
    }

    @Test
    public void testCoreOperationCompare_equal_twoIteratorsWithMatchingValues() {
        Iterator<Object> leftIter = Collections.singletonList("foo").iterator();
        Iterator<Object> rightIter = Collections.singletonList("foo").iterator();
        Expression left = new IteratorExpression(new ConstantIterator(leftIter));
        Expression right = new IteratorExpression(new ConstantIterator(rightIter));
        CoreOperationCompare coreOperationCompare = new Equal(left, right);
        EvalContext context = new EvalContext(new InitialContext());
        assertTrue(coreOperationCompare.compute(context));
    }

    @Test
    public void testCoreOperationCompare_equal_twoIteratorsWithNonMatchingValues() {
        Iterator<Object> leftIter = Collections.singletonList("foo").iterator();
        Iterator<Object> rightIter = Collections.singletonList("bar").iterator();
        Expression left = new IteratorExpression(new ConstantIterator(leftIter));
        Expression right = new IteratorExpression(new ConstantIterator(rightIter));
        CoreOperationCompare coreOperationCompare = new Equal(left, right);
        EvalContext context = new EvalContext(new InitialContext());
        assertFalse(coreOperationCompare.compute(context));
    }

    @Test
    public void testCoreOperationCompare_equal_twoIteratorsWithMixedValues() {
        Iterator<Object> leftIter = Collections.singletonList("foo").iterator();
        Iterator<Object> rightIter = Collections.singletonList("bar").iterator();
        Expression left = new IteratorExpression(new ConstantIterator(leftIter));
        Expression right = new IteratorExpression(new ConstantIterator(rightIter));
        CoreOperationCompare coreOperationCompare = new NotEqual(left, right);
        EvalContext context = new EvalContext(new InitialContext());
        assertFalse(coreOperationCompare.compute(context));
    }

    @Test
    public void testCoreOperationCompare_notEqual_twoBooleanValues() {
        Expression left = new Const(true);
        Expression right = new Const(false);
        CoreOperationCompare coreOperationCompare = new NotEqual(left, right);
        EvalContext context = new EvalContext(new InitialContext());
        assertFalse(coreOperationCompare.compute(context));
    }

    @Test
    public void testCoreOperationCompare_notEqual_twoStringValues() {
        Expression left = new Const("foo");
        Expression right = new Const("bar");
        CoreOperationCompare coreOperationCompare = new NotEqual(left, right);
        EvalContext context = new EvalContext(new InitialContext());
        assertFalse(coreOperationCompare.compute(context));
    }

    @Test
    public void testCoreOperationCompare_notEqual_twoNumberValues() {
        Expression left = new Const(1.0);
        Expression right = new Const(2.0);
        CoreOperationCompare coreOperationCompare = new NotEqual(left, right);
        EvalContext context = new EvalContext(new InitialContext());
        assertFalse(coreOperationCompare.compute(context));
    }

    @Test
    public void testCoreOperationCompare_notEqual_twoInitialContexts() {
        Expression left = new InitialContext();
        Expression right = new InitialContext();
        CoreOperationCompare coreOperationCompare = new NotEqual(left, right);
        EvalContext context = new EvalContext(new InitialContext());
        assertFalse(coreOperationCompare.compute(context));
    }

    @Test
    public void testCoreOperationCompare_notEqual_twoSelfContexts() {
        Expression left = new SelfContext();
        Expression right = new SelfContext();
        CoreOperationCompare coreOperationCompare = new NotEqual(left, right);
        EvalContext context = new EvalContext(new SelfContext());
        assertFalse(coreOperationCompare.compute(context));
    }

    @Test
    public void testCoreOperationCompare_notEqual_twoIteratorsWithMatchingValues() {
        Iterator<Object> leftIter = Collections.singletonList("foo").iterator();
        Iterator<Object> rightIter = Collections.singletonList("foo").iterator();
        Expression left = new IteratorExpression(new ConstantIterator(leftIter));
        Expression right = new IteratorExpression(new ConstantIterator(rightIter));
        CoreOperationCompare coreOperationCompare = new NotEqual(left, right);
        EvalContext context = new EvalContext(new InitialContext());
        assertFalse(coreOperationCompare.compute(context));
    }

    @Test
    public void testCoreOperationCompare_notEqual_twoIteratorsWithNonMatchingValues() {
        Iterator<Object> leftIter = Collections.singletonList("foo").iterator();
        Iterator<Object> rightIter = Collections.singletonList("bar").iterator();
        Expression left = new IteratorExpression(new ConstantIterator(leftIter));
        Expression right = new IteratorExpression(new ConstantIterator(rightIter));
        CoreOperationCompare coreOperationCompare = new NotEqual(left, right);
        EvalContext context = new EvalContext(new InitialContext());
        assertTrue(coreOperationCompare.compute(context));
    }

    @Test
    public void testCoreOperationCompare_notEqual_twoIteratorsWithMixedValues() {
        Iterator<Object> leftIter = Collections.singletonList("foo").iterator();
        Iterator<Object> rightIter = Collections.singletonList("bar").iterator();
        Expression left = new IteratorExpression(new ConstantIterator(leftIter));
        Expression right = new IteratorExpression(new ConstantIterator(rightIter));
        CoreOperationCompare coreOperationCompare = new Equal(left, right);
        EvalContext context = new EvalContext(new InitialContext());
        assertFalse(coreOperationCompare.compute(context));
    }

    @Test
    public void testCoreOperationCompare_equal_twoNullValues() {
        Expression left = new Const(null);
        Expression right = new Const(null);
        CoreOperationCompare coreOperationCompare = new Equal(left, right);
        EvalContext context = new EvalContext(new InitialContext());
        assertTrue(coreOperationCompare.compute(context));
    }
}