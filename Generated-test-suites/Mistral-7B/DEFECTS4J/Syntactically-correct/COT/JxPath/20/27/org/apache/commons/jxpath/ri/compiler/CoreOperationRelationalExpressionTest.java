package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.core.Expression;
import org.apache.commons.jxpath.ri.core.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.core.CoreOperation;
import org.apache.commons.jxpath.ri.core.Constant;
import org.apache.commons.jxpath.ri.eval.EvalContext;
import org.apache.commons.jxpath.ri.iter.ArrayIterator;
import org.apache.commons.jxpath.ri.iter.IteratorSupport;
import org.junit.Test;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test method for {@link CoreOperationRelationalExpression#CoreOperationRelationalExpression(Expression[])}.
     */
    @Test
    public void testCoreOperationRelationalExpression() {
        Expression left = new Constant(1);
        Expression right = new Constant(2);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[] { left, right });
        assertNotNull(expression);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#computeValue(EvalContext)}.
     */
    @Test
    public void testComputeValue() {
        Expression left = new Constant(1);
        Expression right = new Constant(1);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[] { left, right });
        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        left = new Constant(2);
        result = expression.computeValue(context);
        assertFalse((boolean) result);

        right = new Constant(1);
        result = expression.computeValue(context);
        assertFalse((boolean) result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#getPrecedence()}.
     */
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[0]);
        int precedence = expression.getPrecedence();
        assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#isSymmetric()}.
     */
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[0]);
        assertFalse(expression.isSymmetric());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#evaluateCompare(int)}.
     */
    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[0]);
        assertTrue(expression.evaluateCompare(1));
        assertTrue(expression.evaluateCompare(-1));
        assertFalse(expression.evaluateCompare(0));
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#compute(Object, Object)}.
     */
    @Test
    public void testCompute() {
        Expression left = new Constant(1);
        Expression right = new Constant(1);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[] { left, right });

        Object result = expression.compute(left.compute(new EvalContext()), right.compute(new EvalContext()));
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);

        left = new Constant(2);
        result = expression.compute(left.compute(new EvalContext()), right.compute(new EvalContext()));
        assertFalse((boolean) result);

        right = new Constant(1);
        result = expression.compute(left.compute(new EvalContext()), right.compute(new EvalContext()));
        assertFalse((boolean) result);

        left = new SelfContext();
        right = new Constant(1);
        result = expression.compute(left.compute(new EvalContext()), right.compute(new EvalContext()));
        assertFalse((boolean) result);

        left = new Constant(1);
        right = new SelfContext();
        result = expression.compute(left.compute(new EvalContext()), right.compute(new EvalContext()));
        assertFalse((boolean) result);

        left = new InitialContext();
        result = expression.compute(left.compute(new EvalContext()), right.compute(new EvalContext()));
        assertNull(result);

        left = new IteratorSupport(Arrays.asList(new Object[] { 1, 2, 3 }));
        right = new IteratorSupport(Arrays.asList(new Object[] { 1, 2, 3 }));
        result = expression.compute(left.iterator(), right.iterator());
        assertTrue((boolean) result);

        left = new IteratorSupport(Arrays.asList(new Object[] { 1, 2, 3 }));
        right = new IteratorSupport(Arrays.asList(new Object[] { 4, 5, 6 }));
        result = expression.compute(left.iterator(), right.iterator());
        assertFalse((boolean) result);

        left = new IteratorSupport(Collections.singletonList(new Object[] { 1 }));
        right = new IteratorSupport(Collections.singletonList(new Object[] { 1 }));
        result = expression.compute(left.iterator(), right.iterator());
        assertTrue((boolean) result);

        left = new IteratorSupport(Collections.singletonList(new Object[] { 1 }));
        right = new IteratorSupport(Collections.singletonList(new Object[] { 2 }));
        result = expression.compute(left.iterator(), right.iterator());
        assertFalse((boolean) result);

        left = new IteratorSupport(new ArrayList(Arrays.asList(new Object[] { 1, 2, 3 })).iterator());
        right = new IteratorSupport(new ArrayList(Arrays.asList(new Object[] { 1, 2, 3 })).iterator());
        result = expression.compute(left, right);
        assertTrue((boolean) result);

        left = new IteratorSupport(new ArrayList(Arrays.asList(new Object[] { 1, 2, 3 })).iterator());
        right = new IteratorSupport(new ArrayList(Arrays.asList(new Object[] { 4, 5, 6 })).iterator());
        result = expression.compute(left, right);
        assertFalse((boolean) result);

        left = new IteratorSupport(Collections.singletonList(new Object[] { 1 }).iterator());
        right = new IteratorSupport(new ArrayList(Arrays.asList(new Object[] { 1, 2, 3 })).iterator());
        result = expression.compute(left, right);
        assertFalse((boolean) result);

        left = new IteratorSupport(new ArrayList(Arrays.asList(new Object[] { 1, 2, 3 })).iterator());
        right = new IteratorSupport(Collections.singletonList(new Object[] { 1 }).iterator());
        result = expression.compute(left, right);
        assertFalse((boolean) result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#reduce(Object)}.
     */
    @Test
    public void testReduce() {
        Object input = new SelfContext();
        Object result = new CoreOperationRelationalExpression(new Expression[0]).reduce(input);
        assertSame(input, result);

        input = new Constant(1);
        result = new CoreOperationRelationalExpression(new Expression[0]).reduce(input);
        assertSame(input, result);

        input = new InitialContext();
        result = new CoreOperationRelationalExpression(new Expression[0]).reduce(input);
        assertNull(result);

        input = new IteratorSupport(Arrays.asList(new Object[] { 1, 2, 3 }).iterator());
        result = new CoreOperationRelationalExpression(new Expression[0]).reduce(input);
        assertSame(input, result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#containsMatch(Iterator, Object)}.
     */
    @Test
    public void testContainsMatch() {
        Iterator leftIterator = new ArrayIterator(new Object[] { 1, 2, 3 });
        Object right = new Constant(1);
        assertTrue(new CoreOperationRelationalExpression(new Expression[0]).containsMatch(leftIterator, right));

        leftIterator = new ArrayIterator(new Object[] { 1, 2, 3 });
        right = new Constant(2);
        assertFalse(new CoreOperationRelationalExpression(new Expression[0]).containsMatch(leftIterator, right));

        leftIterator = new ArrayIterator(new Object[] { 1, 2, 3 });
        right = new Constant(3);
        assertTrue(new CoreOperationRelationalExpression(new Expression[0]).containsMatch(leftIterator, right));
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#findMatch(Iterator, Iterator)}.
     */
    @Test
    public void testFindMatch() {
        Iterator leftIterator = new ArrayIterator(new Object[] { 1, 2, 3 });
        Iterator rightIterator = new ArrayIterator(new Object[] { 1, 2, 3 });
        assertTrue(new CoreOperationRelationalExpression(new Expression[0]).findMatch(leftIterator, rightIterator));

        leftIterator = new ArrayIterator(new Object[] { 1, 2, 3 });
        rightIterator = new ArrayIterator(new Object[] { 4, 5, 6 });
        assertFalse(new CoreOperationRelationalExpression(new Expression[0]).findMatch(leftIterator, rightIterator));

        leftIterator = new ArrayIterator(new Object[] { 1, 2, 3 });
        rightIterator = new ArrayIterator(new Object[] { 1 });
        assertTrue(new CoreOperationRelationalExpression(new Expression[0]).findMatch(leftIterator, rightIterator));

        leftIterator = new ArrayIterator(new Object[] { 1, 2, 3 });
        rightIterator = new ArrayIterator(new Object[] { 2 });
        assertFalse(new CoreOperationRelationalExpression(new Expression[0]).findMatch(leftIterator, rightIterator));
    }
}