package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationGT;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationGTE;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationLT;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationLTE;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test method for {@link CoreOperationRelationalExpression#CoreOperationRelationalExpression(Expression[])} constructor.
     */
    @Test
    public void testCoreOperationRelationalExpressionConstructor() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertNotNull(expression);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#computeValue(EvalContext)} method.
     */
    @Test
    public void testComputeValue() {
        CoreOperationGT gtExpression = new CoreOperationGT(new ConstantExpression(10));
        CoreOperationGTE gteExpression = new CoreOperationGTE(new ConstantExpression(10));
        CoreOperationLT ltExpression = new CoreOperationLT(new ConstantExpression(10));
        CoreOperationLTE lteExpression = new CoreOperationLTE(new ConstantExpression(10));

        EvalContext context = new EvalContext();
        Object leftValue = new Integer(5);
        Object rightValue = new Integer(10);
        Object resultGt = gtExpression.computeValue(context);
        Object resultGte = gteExpression.computeValue(context);
        Object resultLt = ltExpression.computeValue(context);
        Object resultLte = lteExpression.computeValue(context);

        assertTrue(resultGt instanceof Boolean && ((Boolean) resultGt).booleanValue() == false);
        assertTrue(resultGte instanceof Boolean && ((Boolean) resultGte).booleanValue() == false);
        assertTrue(resultLt instanceof Boolean && ((Boolean) resultLt).booleanValue() == true);
        assertTrue(resultLte instanceof Boolean && ((Boolean) resultLte).booleanValue() == true);

        leftValue = new Integer(10);
        rightValue = new Integer(10);
        resultGt = gtExpression.computeValue(context);
        resultGte = gteExpression.computeValue(context);
        resultLt = ltExpression.computeValue(context);
        resultLte = lteExpression.computeValue(context);

        assertTrue(resultGt instanceof Boolean && ((Boolean) resultGt).booleanValue() == false);
        assertTrue(resultGte instanceof Boolean && ((Boolean) resultGte).booleanValue() == true);
        assertTrue(resultLt instanceof Boolean && ((Boolean) resultLt).booleanValue() == false);
        assertTrue(resultLte instanceof Boolean && ((Boolean) resultLte).booleanValue() == true);

        leftValue = new Double(5.5);
        rightValue = new Double(10.5);
        resultGt = gtExpression.computeValue(context);
        resultGte = gteExpression.computeValue(context);
        resultLt = ltExpression.computeValue(context);
        resultLte = lteExpression.computeValue(context);

        assertTrue(resultGt instanceof Boolean && ((Boolean) resultGt).booleanValue() == false);
        assertTrue(resultGte instanceof Boolean && ((Boolean) resultGte).booleanValue() == false);
        assertTrue(resultLt instanceof Boolean && ((Boolean) resultLt).booleanValue() == true);
        assertTrue(resultLte instanceof Boolean && ((Boolean) resultLte).booleanValue() == true);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#getPrecedence()} method.
     */
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        int precedence = expression.getPrecedence();
        assertEquals(CoreOperationRelationalExpression.RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#isSymmetric()} method.
     */
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        assertFalse(expression.isSymmetric());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#evaluateCompare(int)} method.
     */
    @Test(expected = AssertionError.class)
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                fail("Should not be called.");
                return false;
            }
        };
        expression.computeValue(new EvalContext());
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#compute(Object, Object)} method with {@code InitialContext}.
     */
    @Test
    public void testComputeWithInitialContext() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        InitialContext leftInitialContext = new InitialContext();
        InitialContext rightInitialContext = new InitialContext();
        Object left = leftInitialContext.getCurrentNode();
        Object right = rightInitialContext.getCurrentNode();
        Object result = expression.computeValue(new EvalContext(leftInitialContext, rightInitialContext));
        assertNotNull(result);
        assertTrue(result instanceof Boolean);

        leftInitialContext.reset();
        rightInitialContext.reset();
        left = leftInitialContext.getCurrentNode();
        right = rightInitialContext.getCurrentNode();
        result = expression.computeValue(new EvalContext(leftInitialContext, rightInitialContext));
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#compute(Object, Object)} method with {@code SelfContext}.
     */
    @Test
    public void testComputeWithSelfContext() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        SelfContext leftSelfContext = new SelfContext();
        SelfContext rightSelfContext = new SelfContext();
        Object left = leftSelfContext;
        Object right = rightSelfContext;
        Object result = expression.computeValue(new EvalContext(leftSelfContext, rightSelfContext));
        assertNotNull(result);
        assertTrue(result instanceof Boolean);

        leftSelfContext.setNode(new Integer(1));
        rightSelfContext.setNode(new Integer(2));
        result = expression.computeValue(new EvalContext(leftSelfContext, rightSelfContext));
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#compute(Object, Object)} method with {@code Iterator}.
     */
    @Test
    public void testComputeWithIterator() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        Collection collection = new HashSet();
        collection.add(new Integer(1));
        collection.add(new Integer(2));
        collection.add(new Integer(3));
        Iterator iterator = collection.iterator();
        Object left = iterator;
        Object right = iterator;
        Object result = expression.computeValue(new EvalContext(left, right));
        assertNotNull(result);
        assertTrue(result instanceof Boolean);

        iterator = collection.iterator();
        left = iterator.next();
        right = iterator.next();
        result = expression.computeValue(new EvalContext(left, right));
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#compute(Object, Object)} method with {@code Double}.
     */
    @Test
    public void testComputeWithDouble() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        double leftDouble = 5.5;
        double rightDouble = 10.5;
        Object result = expression.computeValue(new EvalContext(InfoSetUtil.createDoubleNode(leftDouble), InfoSetUtil.createDoubleNode(rightDouble)));
        assertNotNull(result);
        assertTrue(result instanceof Boolean);

        leftDouble = 10.5;
        rightDouble = 5.5;
        result = expression.computeValue(new EvalContext(InfoSetUtil.createDoubleNode(leftDouble), InfoSetUtil.createDoubleNode(rightDouble)));
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#reduce(Object)} method with {@code InitialContext}.
     */
    @Test
    public void testReduceWithInitialContext() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        Object initialContext = new InitialContext();
        Object result = expression.reduce(initialContext);
        assertNotNull(result);
        assertFalse(result instanceof InitialContext);

        InitialContext initialContext2 = new InitialContext();
        result = expression.reduce(initialContext2);
        assertNotNull(result);
        assertFalse(result instanceof InitialContext);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#reduce(Object)} method with {@code Collection}.
     */
    @Test
    public void testReduceWithCollection() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        Collection collection = new HashSet();
        collection.add(new Integer(1));
        collection.add(new Integer(2));
        collection.add(new Integer(3));
        Object result = expression.reduce(collection.iterator());
        assertNotNull(result);
        assertFalse(result instanceof Collection);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#findMatch(Iterator, Iterator)} method.
     */
    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);
        Collection leftCollection = new HashSet();
        leftCollection.add(new Integer(1));
        leftCollection.add(new Integer(2));
        leftCollection.add(new Integer(3));
        Iterator leftIterator = leftCollection.iterator();
        Collection rightCollection = new HashSet();
        rightCollection.add(new Integer(1));
        rightCollection.add(new Integer(2));
        rightCollection.add(new Integer(3));
        Iterator rightIterator = rightCollection.iterator();

        boolean result = expression.findMatch(leftIterator, rightIterator);
        assertTrue(result);

        leftCollection.add(new Integer(4));
        result = expression.findMatch(leftIterator, rightIterator);
        assertFalse(result);

        rightCollection.add(new Integer(4));
        result = expression.findMatch(leftIterator, rightIterator);
        assertFalse(result);

        leftCollection.remove(new Integer(1));
        result = expression.findMatch(leftIterator, rightIterator);
        assertFalse(result);
    }

}