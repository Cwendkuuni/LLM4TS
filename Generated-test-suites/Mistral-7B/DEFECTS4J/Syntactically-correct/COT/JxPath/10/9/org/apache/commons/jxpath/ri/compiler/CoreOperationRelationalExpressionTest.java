package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testConstructor() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationIdentifier("child");
        args[1] = new CoreOperationNumber(1);
        CoreOperationRelationalExpression expression = new CoreOperationGt(args);
        assertNotNull(expression);
    }

    @Test
    public void testComputeValue() {
        Expression left = new CoreOperationNumber(2);
        Expression right = new CoreOperationNumber(3);
        CoreOperationRelationalExpression expression = new CoreOperationGt(new Expression[]{left, right});
        EvalContext context = new EvalContext(new SelfContext());
        Object result = expression.computeValue(context);
        assertFalse("expected false", result instanceof Boolean);
        assertSame("expected different objects", left, expression.getArgs()[0]);
        assertSame("expected different objects", right, expression.getArgs()[1]);

        left = new CoreOperationNumber(3);
        right = new CoreOperationNumber(2);
        expression = new CoreOperationGt(new Expression[]{left, right});
        context = new EvalContext(new SelfContext());
        result = expression.computeValue(context);
        assertTrue("expected true", result instanceof Boolean);
        assertEquals("expected true", Boolean.TRUE, result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(new Expression[0]);
        int precedence = expression.getPrecedence();
        assertEquals("expected precedence 3", 3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(new Expression[0]);
        assertFalse("expected false", expression.isSymmetric());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEvaluateCompareWithUnsupportedCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(new Expression[0]);
        expression.evaluateCompare(-1);
    }

    @Test
    public void testEvaluateCompareForEquals() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(new Expression[0]);
        int compare = 0;
        assertFalse("expected false", expression.evaluateCompare(compare));
    }

    @Test
    public void testEvaluateCompareForLessThan() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(new Expression[0]);
        int compare = -1;
        assertFalse("expected false", expression.evaluateCompare(compare));
    }

    @Test
    public void testEvaluateCompareForGreaterThan() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(new Expression[0]);
        int compare = 1;
        assertFalse("expected false", expression.evaluateCompare(compare));

        expression = new CoreOperationGt(new Expression[0]);
        compare = 1;
        assertTrue("expected true", expression.evaluateCompare(compare));

        expression = new CoreOperationGe(new Expression[0]);
        compare = 1;
        assertTrue("expected true", expression.evaluateCompare(compare));
    }

    @Test
    public void testReduceForSelfContext() {
        Expression expression = new CoreOperationNumber(1);
        Object result = expression.reduce(new SelfContext());
        assertSame("expected SelfContext", new SelfContext(), result);
    }

    @Test
    public void testReduceForInitialContext() {
        Expression expression = new CoreOperationNumber(1);
        Object result = expression.reduce(new InitialContext());
        assertSame("expected InitialContext", new InitialContext(), result);
    }

    @Test
    public void testReduceForCollection() {
        Expression expression = new CoreOperationNumber(1);
        Collection collection = new ArrayList();
        collection.add(new Integer(2));
        Object result = expression.reduce(collection.iterator());
        assertSame("expected Integer 2", new Integer(2), result);
    }

    @Test
    public void testContainsMatch() {
        Iterator iterator = new ArrayList(new Integer[]{1, 2, 3}).iterator();
        Object value = new Integer(2);
        assertTrue("expected true", CoreOperationRelationalExpressionTest.class.getSuperclass().getDeclaredMethod("containsMatch", Iterator.class, Object.class).invoke(null, iterator, value));
    }

    @Test
    public void testFindMatch() {
        Iterator leftIterator = new ArrayList(new Integer[]{1, 2}).iterator();
        Iterator rightIterator = new ArrayList(new Integer[]{1, 2, 3}).iterator();
        assertTrue("expected true", CoreOperationRelationalExpressionTest.class.getSuperclass().getDeclaredMethod("findMatch", Iterator.class, Iterator.class).invoke(null, leftIterator, rightIterator));
    }

    @Test
    public void testComputeForNaN() {
        Expression left = new CoreOperationNumber(Double.NaN);
        Expression right = new CoreOperationNumber(Double.NaN);
        CoreOperationRelationalExpression expression = new CoreOperationGt(new Expression[]{left, right});
        EvalContext context = new EvalContext(new SelfContext());
        Object result = expression.computeValue(context);
        assertFalse("expected false", result instanceof Boolean);
    }
}