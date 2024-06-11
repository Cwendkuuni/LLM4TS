package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.info.IteratorInfo;
import org.apache.commons.jxpath.ri.info.NodeInfo;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression leftExpr = new LiteralExpression(new NodeInfo("1"));
        Expression rightExpr = new LiteralExpression(new NodeInfo("2"));
        CoreOperationRelationalExpression expr = new GreaterThanExpression(new CoreOperation[]{leftExpr, rightExpr});

        EvalContext context = new EvalContext(new HashSet<>(), new InitialContext(new NodeInfo("document")));
        Object leftValue = leftExpr.computeValue(context);
        Object rightValue = rightExpr.computeValue(context);

        Object result = expr.computeValue(context);

        assertEquals(Boolean.FALSE, result);

        leftExpr = new LiteralExpression(new NodeInfo("3"));
        rightExpr = new LiteralExpression(new NodeInfo("2"));
        expr = new GreaterThanExpression(new CoreOperation[]{leftExpr, rightExpr});

        context = new EvalContext(new HashSet<>(), new InitialContext(new NodeInfo("document")));
        leftValue = leftExpr.computeValue(context);
        rightValue = rightExpr.computeValue(context);

        result = expr.computeValue(context);

        assertTrue(result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new GreaterThanExpression(new CoreOperation[0]);

        int precedence = expr.getPrecedence();

        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new GreaterThanExpression(new CoreOperation[0]);

        assertFalse(expr.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expr = new GreaterThanExpression(new CoreOperation[0]);

        boolean result = expr.evaluateCompare(0);
        assertFalse(result);

        result = expr.evaluateCompare(-1);
        assertTrue(result);

        result = expr.evaluateCompare(1);
        assertFalse(result);
    }

    @Test
    public void testComputeWithInitialContexts() {
        Expression leftExpr = new LiteralExpression(new InitialContext(new NodeInfo("document"), "root"));
        Expression rightExpr = new LiteralExpression(new InitialContext(new NodeInfo("document"), "root"));
        CoreOperationRelationalExpression expr = new GreaterThanExpression(new CoreOperation[]{leftExpr, rightExpr});

        EvalContext context = new EvalContext(new HashSet<>(), new InitialContext(new NodeInfo("document")));
        Object leftValue = leftExpr.computeValue(context);
        Object rightValue = rightExpr.computeValue(context);

        Object result = expr.computeValue(context);

        assertTrue(result);

        context = new EvalContext(new HashSet<>(), new InitialContext(new NodeInfo("document"), "root2"));
        leftValue = leftExpr.computeValue(context);
        rightValue = rightExpr.computeValue(context);

        result = expr.computeValue(context);

        assertFalse(result);
    }

    @Test
    public void testComputeWithIterators() {
        List<NodeInfo> leftValues = new ArrayList<>();
        leftValues.add(new NodeInfo("1"));
        leftValues.add(new NodeInfo("2"));

        Iterator<Object> leftIterator = Collections.singletonList(new IteratorInfo(leftValues.iterator())).iterator();

        List<NodeInfo> rightValues = new ArrayList<>();
        rightValues.add(new NodeInfo("2"));
        rightValues.add(new NodeInfo("3"));

        Iterator<Object> rightIterator = Collections.singletonList(new IteratorInfo(rightValues.iterator())).iterator();

        CoreOperationRelationalExpression expr = new GreaterThanExpression(new CoreOperation[]{new LiteralExpression(leftIterator), new LiteralExpression(rightIterator)});

        Object result = expr.computeValue(new EvalContext(new HashSet<>(), new InitialContext(new NodeInfo("document"))));

        assertFalse(result);

        leftValues.clear();
        leftValues.add(new NodeInfo("3"));

        rightValues.clear();
        rightValues.add(new NodeInfo("2"));

        leftIterator = Collections.singletonList(new IteratorInfo(leftValues.iterator())).iterator();
        rightIterator = Collections.singletonList(new IteratorInfo(rightValues.iterator())).iterator();

        result = expr.computeValue(new EvalContext(new HashSet<>(), new InitialContext(new NodeInfo("document"))));

        assertTrue(result);
    }

    @Test
    public void testComputeWithCollections() {
        HashSet<NodeInfo> leftValues = new HashSet<>();
        leftValues.add(new NodeInfo("1"));
        leftValues.add(new NodeInfo("2"));

        Expression leftExpr = new LiteralExpression(leftValues.iterator());

        HashSet<NodeInfo> rightValues = new HashSet<>();
        rightValues.add(new NodeInfo("2"));
        rightValues.add(new NodeInfo("3"));

        Expression rightExpr = new LiteralExpression(rightValues.iterator());

        CoreOperationRelationalExpression expr = new GreaterThanExpression(new CoreOperation[]{leftExpr, rightExpr});

        EvalContext context = new EvalContext(new HashSet<>(), new InitialContext(new NodeInfo("document")));
        Object leftValue = leftExpr.computeValue(context);
        Object rightValue = rightExpr.computeValue(context);

        Object result = expr.computeValue(context);

        assertFalse(result);

        leftValues.clear();
        leftValues.add(new NodeInfo("3"));

        rightValues.clear();
        rightValues.add(new NodeInfo("2"));

        leftExpr = new LiteralExpression(leftValues.iterator());
        rightExpr = new LiteralExpression(rightValues.iterator());

        result = expr.computeValue(context);

        assertTrue(result);
    }

    @Test
    public void testComputeWithMixedTypes() {
        HashSet<NodeInfo> leftValues = new HashSet<>();
        leftValues.add(new NodeInfo("1"));
        leftValues.add(new NodeInfo("2.5"));

        Expression leftExpr = new LiteralExpression(leftValues.iterator());

        HashSet<NodeInfo> rightValues = new HashSet<>();
        rightValues.add(new NodeInfo("2"));
        rightValues.add(new NodeInfo("3"));

        Expression rightExpr = new LiteralExpression(rightValues.iterator());

        CoreOperationRelationalExpression expr = new GreaterThanExpression(new CoreOperation[]{leftExpr, rightExpr});

        EvalContext context = new EvalContext(new HashSet<>(), new InitialContext(new NodeInfo("document")));
        Object leftValue = leftExpr.computeValue(context);
        Object rightValue = rightExpr.computeValue(context);

        Object result = expr.computeValue(context);

        assertFalse(result);

        leftValues.clear();
        leftValues.add(new NodeInfo("3"));
        leftValues.add(new NodeInfo("2.5"));

        leftExpr = new LiteralExpression(leftValues.iterator());

        rightValues.clear();
        rightValues.add(new NodeInfo("2"));
        rightValues.add(new NodeInfo("3"));

        rightExpr = new LiteralExpression(rightValues.iterator());

        result = expr.computeValue(context);

        assertTrue(result);
    }

    @Test
    public void testComputeWithNull() {
        HashSet<Object> leftValues = new HashSet<>();
        leftValues.add(new InitialContext(new NodeInfo("document"), "root"));
        leftValues.add(null);

        Expression leftExpr = new LiteralExpression(leftValues.iterator());

        HashSet<NodeInfo> rightValues = new HashSet<>();
        rightValues.add(new NodeInfo("2"));
        rightValues.add(new NodeInfo("3"));

        Expression rightExpr = new LiteralExpression(rightValues.iterator());

        CoreOperationRelationalExpression expr = new GreaterThanExpression(new CoreOperation[]{leftExpr, rightExpr});

        EvalContext context = new EvalContext(new HashSet<>(), new InitialContext(new NodeInfo("document")));
        Object leftValue = leftExpr.computeValue(context);
        Object rightValue = rightExpr.computeValue(context);

        assertNull(leftValue);

        Object result = expr.computeValue(context);

        assertNull(result);
    }

}