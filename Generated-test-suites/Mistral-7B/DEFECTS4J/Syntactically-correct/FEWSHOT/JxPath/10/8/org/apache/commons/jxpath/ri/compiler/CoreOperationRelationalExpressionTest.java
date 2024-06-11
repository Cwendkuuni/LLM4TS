package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.IteratorWrapper;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.iter.EmptyIterator;
import org.apache.commons.jxpath.ri.iter.SingletonIterator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression left = new ConstantExpression(new SelfContext(null, null, null, 0));
        Expression right = new ConstantExpression(new Integer(5));
        CoreOperationRelationalExpression op = newGTExpression(left, right);

        assertTrue(op.computeValue(new EvalContext()) instanceof Boolean);
        assertEquals(Boolean.TRUE, op.computeValue(new EvalContext()));

        left = new ConstantExpression(new SelfContext(null, null, null, 0));
        right = new ConstantExpression(new Integer(5));
        op = newGTEExpression(left, right);

        assertTrue(op.computeValue(new EvalContext()) instanceof Boolean);
        assertEquals(Boolean.TRUE, op.computeValue(new EvalContext()));

        left = new ConstantExpression(new SelfContext(null, null, null, 0));
        right = new ConstantExpression(new Integer(2));
        op = newGTExpression(left, right);

        assertFalse(op.computeValue(new EvalContext()) instanceof Boolean);
        assertNull(op.computeValue(new EvalContext()));

        left = new ConstantExpression(new SelfContext(null, null, null, 0));
        right = new ConstantExpression(new Integer(5));
        op = newGTEExpression(left, right);

        assertTrue(op.computeValue(new EvalContext()) instanceof Boolean);
        assertEquals(Boolean.TRUE, op.computeValue(new EvalContext()));

        left = new ConstantExpression(new SelfContext(null, null, null, 0));
        right = new ConstantExpression(new Integer(2));
        op = newGTEExpression(left, right);

        assertFalse(op.computeValue(new EvalContext()) instanceof Boolean);
        assertNull(op.computeValue(new EvalContext()));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression op1 = newGTExpression(new Expression[0]);
        CoreOperationRelationalExpression op2 = newGTEExpression(new Expression[0]);

        assertEquals(3, op1.getPrecedence());
        assertEquals(3, op2.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression op = newGTExpression(new Expression[0]);

        assertFalse(op.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression op = newGTExpression(new Expression[0]);

        assertTrue(op.evaluateCompare(1));
        assertTrue(op.evaluateCompare(-2));
        assertFalse(op.evaluateCompare(0));
        assertFalse(op.evaluateCompare(-1));
    }

    @Test
    public void testReduce() {
        Expression o = new SingletonIterator(new Integer(5));
        Object reduced = CoreOperationRelationalExpression.class.cast(new GTExpression(new Expression[0])).reduce(o);

        assertNotNull(reduced);
        assertFalse(reduced instanceof Iterator);
    }

    @Test
    public void testContainsMatch() {
        Iterator left = new IteratorWrapper(Arrays.asList(1, 2, 3).iterator());
        Object right = new Integer(3);

        assertTrue(CoreOperationRelationalExpression.class.cast(new GTEExpression(new Expression[0])).containsMatch(left, right));

        left = new IteratorWrapper(Arrays.asList(1, 2, 3).iterator());
        right = new Integer(4);

        assertFalse(CoreOperationRelationalExpression.class.cast(new GTEExpression(new Expression[0])).containsMatch(left, right));

        left = new IteratorWrapper(new EmptyIterator().iterator());
        right = new Integer(5);

        assertFalse(CoreOperationRelationalExpression.class.cast(new GTEExpression(new Expression[0])).containsMatch(left, right));
    }

    @Test
    public void testFindMatch() {
        Iterator left = new IteratorWrapper(Arrays.asList(1, 2, 3, 4, 5).iterator());
        Iterator right = new IteratorWrapper(Arrays.asList(2, 3, 4).iterator());

        assertTrue(CoreOperationRelationalExpression.class.cast(new GTEExpression(new Expression[0])).findMatch(left, right).hasNext());

        left = new IteratorWrapper(Arrays.asList(1, 2, 3, 4, 5).iterator());
        right = new IteratorWrapper(Collections.singletonList(6).iterator());

        assertFalse(CoreOperationRelationalExpression.class.cast(new GTEExpression(new Expression[0])).findMatch(left, right).hasNext());
    }

}