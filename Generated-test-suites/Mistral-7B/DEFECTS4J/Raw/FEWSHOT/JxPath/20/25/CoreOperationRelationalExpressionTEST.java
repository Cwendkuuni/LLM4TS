package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationRelationalExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.SelfContext;
import org.apache.commons.jxpath.ri.Iterate;
import org.apache.commons.jxpath.ri.IterateFactory;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.NodeSet;
import org.apache.commons.jxpath.ri.Sequence;
import org.apache.commons.jxpath.ri.Value;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    private static ExpressionFactory expressionFactory = new CoreOperationRelationalExpressionFactory();
    private static IterateFactory iterateFactory = new IterateFactory();

    @Test
    public void testGT() {
        Expression left = expressionFactory.createNumber(1.0);
        Expression right = expressionFactory.createNumber(2.0);
        CoreOperationRelationalExpression gt = (CoreOperationRelationalExpression) expressionFactory.createRelationalExpression(">", left, right);

        assertTrue(gt.computeValue(new EvalContext()));
    }

    @Test
    public void testGTE() {
        Expression left = expressionFactory.createNumber(1.0);
        Expression right = expressionFactory.createNumber(2.0);
        CoreOperationRelationalExpression gte = (CoreOperationRelationalExpression) expressionFactory.createRelationalExpression(">=", left, right);

        assertTrue(gte.computeValue(new EvalContext()));
    }

    @Test
    public void testLT() {
        Expression left = expressionFactory.createNumber(1.0);
        Expression right = expressionFactory.createNumber(2.0);
        CoreOperationRelationalExpression lt = (CoreOperationRelationalExpression) expressionFactory.createRelationalExpression("<", left, right);

        assertFalse(lt.computeValue(new EvalContext()));
    }

    @Test
    public void testLTE() {
        Expression left = expressionFactory.createNumber(1.0);
        Expression right = expressionFactory.createNumber(2.0);
        CoreOperationRelationalExpression lte = (CoreOperationRelationalExpression) expressionFactory.createRelationalExpression("<=", left, right);

        assertFalse(lte.computeValue(new EvalContext()));
    }

    @Test
    public void testEquals() {
        Expression left = expressionFactory.createNumber(1.0);
        Expression right = expressionFactory.createNumber(1.0);
        CoreOperationRelationalExpression equals = (CoreOperationRelationalExpression) expressionFactory.createRelationalExpression("=", left, right);

        assertTrue(equals.computeValue(new EvalContext()));
    }

    @Test
    public void testNotEquals() {
        Expression left = expressionFactory.createNumber(1.0);
        Expression right = expressionFactory.createNumber(2.0);
        CoreOperationRelationalExpression notEquals = (CoreOperationRelationalExpression) expressionFactory.createRelationalExpression("!=", left, right);

        assertFalse(notEquals.computeValue(new EvalContext()));
    }

    @Test
    public void testNullLeft() {
        Expression left = null;
        Expression right = expressionFactory.createNumber(2.0);
        CoreOperationRelationalExpression gt = (CoreOperationRelationalExpression) expressionFactory.createRelationalExpression(">", left, right);

        assertNull(gt.computeValue(new EvalContext()));
    }

    @Test
    public void testNullRight() {
        Expression left = expressionFactory.createNumber(1.0);
        Expression right = null;
        CoreOperationRelationalExpression gt = (CoreOperationRelationalExpression) expressionFactory.createRelationalExpression(">", left, right);

        assertNull(gt.computeValue(new EvalContext()));
    }

    @Test
    public void testSelfContextGT() {
        Expression left = new SelfContext();
        Expression right = expressionFactory.createNumber(2.0);
        CoreOperationRelationalExpression gt = (CoreOperationRelationalExpression) expressionFactory.createRelationalExpression(">", left, right);

        assertFalse(gt.computeValue(new EvalContext()));
    }

    @Test
    public void testSelfContextGTE() {
        Expression left = new SelfContext();
        Expression right = expressionFactory.createNumber(2.0);
        CoreOperationRelationalExpression gte = (CoreOperationRelationalExpression) expressionFactory.createRelationalExpression(">=", left, right);

        assertFalse(gte.computeValue(new EvalContext()));
    }

    @Test
    public void testSelfContextLT() {
        Expression left = new SelfContext();
        Expression right = expressionFactory.createNumber(2.0);
        CoreOperationRelationalExpression lt = (CoreOperationRelationalExpression) expressionFactory.createRelationalExpression("<", left, right);

        assertFalse(lt.computeValue(new EvalContext()));
    }

    @Test
    public void testSelfContextLTE() {
        Expression left = new SelfContext();
        Expression right = expressionFactory.createNumber(2.0);
        CoreOperationRelationalExpression lte = (CoreOperationRelationalExpression) expressionFactory.createRelationalExpression("<=", left, right);

        assertFalse(lte.computeValue(new EvalContext()));
    }

    @Test
    public void testSelfContextEquals() {
        Expression left = new SelfContext();
        Expression right = new SelfContext();
        CoreOperationRelationalExpression equals = (CoreOperationRelationalExpression) expressionFactory.createRelationalExpression("=", left, right);

        assertTrue(equals.computeValue(new EvalContext()));
    }

    @Test
    public void testSelfContextNotEquals() {
        Expression left = new SelfContext();
        Expression right = new SelfContext();
        CoreOperationRelationalExpression notEquals = (CoreOperationRelationalExpression) expressionFactory.createRelationalExpression("!=", left, right);

        assertFalse(notEquals.computeValue(new EvalContext()));
    }

    @Test
    public void testIteratorsMatch() {
        Iterator left = Arrays.asList(1, 2, 3).iterator();
        Iterator right = Arrays.asList(2, 3, 4).iterator();
        CoreOperationRelationalExpression equals = (CoreOperationRelationalExpression) expressionFactory.createRelationalExpression("=", iterateFactory.createIterate(left), iterateFactory.createIterate(right));

        assertTrue(equals.computeValue(new EvalContext()));
    }

    @Test
    public void testIteratorsNoMatch() {
        Iterator left = Arrays.asList(1, 2, 3).iterator();
        Iterator right = Arrays.asList(3, 4, 5).iterator();
        CoreOperationRelationalExpression notEquals = (CoreOperationRelationalExpression) expressionFactory.createRelationalExpression("!=", iterateFactory.createIterate(left), iterateFactory.createIterate(right));

        assertFalse(notEquals.computeValue(new EvalContext()));
    }

    @Test
    public void testIteratorsIntersection() {
        Iterator left = Arrays.asList(1, 2, 3).iterator();
        Iterator right = Arrays.asList(2, 3).iterator();
        CoreOperationRelationalExpression equals = (CoreOperationRelationalExpression) expressionFactory.createRelationalExpression("=", iterateFactory.createIterate(left), iterateFactory.createIterate(right));

        assertFalse(equals.computeValue(new EvalContext()));

        left = Arrays.asList(1, 2, 3).iterator();
        CoreOperationRelationalExpression findMatch = (CoreOperationRelationalExpression) expressionFactory.createRelationalExpression("intersect", iterateFactory.createIterate(left), iterateFactory.createIterate(right));

        assertTrue(findMatch.computeValue(new EvalContext()));
    }

    @Test
    public void testIteratorsNoIntersection() {
        Iterator left = Arrays.asList(1, 2, 3).iterator();
        Iterator right = Arrays.asList(4, 5, 6).iterator();
        CoreOperationRelationalExpression findMatch = (CoreOperationRelationalExpression) expressionFactory.createRelationalExpression("intersect", iterateFactory.createIterate(left), iterateFactory.createIterate(right));

        assertFalse(findMatch.computeValue(new EvalContext()));
    }

    @Test
    public void testValueComparison() {
        Value left = new NodeSet(Collections.singletonList(new SelfContext()));
        Value right = new NodeSet(Collections.singletonList(new SelfContext()));
        CoreOperationRelationalExpression equals = (CoreOperationRelationalExpression) expressionFactory.createRelationalExpression("=", left, right);

        assertTrue(equals.computeValue(new EvalContext()));
    }

    @Test
    public void testValueComparisonDifferentValues() {
        Value left = new NodeSet(Collections.singletonList(expressionFactory.createNumber(1.0)));
        Value right = new NodeSet(Collections.singletonList(expressionFactory.createNumber(2.0)));
        CoreOperationRelationalExpression notEquals = (CoreOperationRelationalExpression) expressionFactory.createRelationalExpression("!=", left, right);

        assertFalse(notEquals.computeValue(new EvalContext()));
    }
}