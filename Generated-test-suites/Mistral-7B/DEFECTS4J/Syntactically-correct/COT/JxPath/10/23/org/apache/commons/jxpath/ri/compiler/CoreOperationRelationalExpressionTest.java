package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionTest;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.CoreOperationConstant;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNegate;
import org.apache.commons.jxpath.ri.compiler.CoreOperationUnaryMinus;
import org.apache.commons.jxpath.ri.compiler.CoreNode;
import org.apache.commons.jxpath.ri.compiler.StaticContext;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test method for {@link CoreOperationRelationalExpression#CoreOperationRelationalExpression(Expression[])}
     * with two constant arguments.
     */
    @org.junit.Test
    public void testConstConst() {
        CoreOperation left = new CoreOperationConstant(new Integer(1));
        CoreOperation right = new CoreOperationConstant(new Integer(1));
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGreaterThan(new CoreOperation[] { left, right });
        Object result = relationalExpression.computeValue(new StaticContext(new EvalContext()));
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#CoreOperationRelationalExpression(Expression[])}
     * with one constant argument and one variable argument.
     */
    @org.junit.Test
    public void testConstVar() {
        CoreOperation left = new CoreOperationConstant(new Integer(1));
        CoreOperation right = new CoreOperationVariable();
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGreaterThan(new CoreOperation[] { left, right });
        EvalContext context = new EvalContext(new InitialContext(new Object[0]));
        context.setVariable("x", new Integer(1));
        Object result = relationalExpression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#CoreOperationRelationalExpression(Expression[])}
     * with two variable arguments.
     */
    @org.junit.Test
    public void testVarVar() {
        CoreOperation left = new CoreOperationVariable();
        CoreOperation right = new CoreOperationVariable();
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGreaterThan(new CoreOperation[] { left, right });
        EvalContext context = new EvalContext(new InitialContext(new Object[0]));
        context.setVariable("x", new Integer(1));
        context.setVariable("y", new Integer(2));
        Object result = relationalExpression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#CoreOperationRelationalExpression(Expression[])}
     * with one constant argument and one null argument.
     */
    @org.junit.Test
    public void testConstNull() {
        CoreOperation left = new CoreOperationConstant(new Integer(1));
        CoreOperation right = null;
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGreaterThan(new CoreOperation[] { left, right });
        EvalContext context = new EvalContext(new InitialContext(new Object[0]));
        try {
            relationalExpression.computeValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#CoreOperationRelationalExpression(Expression[])}
     * with two null arguments.
     */
    @org.junit.Test
    public void testNullNull() {
        CoreOperation left = null;
        CoreOperation right = null;
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGreaterThan(new CoreOperation[] { left, right });
        EvalContext context = new EvalContext(new InitialContext(new Object[0]));
        try {
            relationalExpression.computeValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#computeValue(EvalContext)} with a collection
     * of integers as left argument and an iterator of integers as right argument.
     */
    @org.junit.Test
    public void testCollectionIterator() {
        List<Integer> collection = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        Iterator<Integer> iterator = collection.iterator();
        CoreOperation left = new CoreOperationVariable();
        CoreOperation right = new CoreOperationVariable();
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGreaterThan(new CoreOperation[] { left, right });
        EvalContext context = new EvalContext(new InitialContext(new Object[0]));
        context.setVariable("collection", collection);
        context.setVariable("iterator", iterator);
        Object result = relationalExpression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#computeValue(EvalContext)} with an iterator
     * of integers as left argument and a collection of integers as right argument.
     */
    @org.junit.Test
    public void testIteratorCollection() {
        List<Integer> collection = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        Iterator<Integer> iterator = collection.iterator();
        CoreOperation left = new CoreOperationVariable();
        CoreOperation right = new CoreOperationVariable();
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGreaterThan(new CoreOperation[] { left, right });
        EvalContext context = new EvalContext(new InitialContext(new Object[0]));
        context.setVariable("iterator", iterator);
        context.setVariable("collection", collection);
        Object result = relationalExpression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#computeValue(EvalContext)} with a collection
     * of integers as both arguments.
     */
    @org.junit.Test
    public void testCollectionCollection() {
        List<Integer> collectionLeft = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> collectionRight = new ArrayList<>(Arrays.asList(2, 3, 4, 5, 6));
        CoreOperation left = new CoreOperationVariable();
        CoreOperation right = new CoreOperationVariable();
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGreaterThan(new CoreOperation[] { left, right });
        EvalContext context = new EvalContext(new InitialContext(new Object[0]));
        context.setVariable("collectionLeft", collectionLeft);
        context.setVariable("collectionRight", collectionRight);
        Object result = relationalExpression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#computeValue(EvalContext)} with two self contexts
     * as arguments.
     */
    @org.junit.Test
    public void testSelfContextSelfContext() {
        CoreOperation left = new CoreOperationVariable();
        CoreOperation right = new CoreOperationVariable();
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGreaterThan(new CoreOperation[] { left, right });
        EvalContext context = new EvalContext(new InitialContext(new Object[0]));
        context.setVariable("leftSelfContext", new SelfContext());
        context.setVariable("rightSelfContext", new SelfContext());
        Object result = relationalExpression.computeValue(context);
        assertNull(result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#getPrecedence()}
     */
    @org.junit.Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGreaterThan(new CoreOperation[0]);
        int precedence = relationalExpression.getPrecedence();
        assertEquals(3, precedence);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#isSymmetric()}
     */
    @org.junit.Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGreaterThan(new CoreOperation[0]);
        assertFalse(relationalExpression.isSymmetric());
    }
}