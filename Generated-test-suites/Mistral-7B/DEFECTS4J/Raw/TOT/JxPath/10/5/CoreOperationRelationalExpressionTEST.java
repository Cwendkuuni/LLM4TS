package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.IteratedNodeList;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.nodes.DocumentInfoSet;
import org.apache.commons.jxpath.ri.nodes.NodeInfo;
import org.apache.commons.jxpath.ri.nodes.TextInfo;
import org.junit.Test;

/**
 * Tests for {@link CoreOperationRelationalExpression}.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new ConstantExpression(5), new ConstantExpression(3));
        EvalContext context = new EvalContext(new DocumentInfoSet(), new Iterators.EmptyIterator());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanOrEqual() {
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(new ConstantExpression(5), new ConstantExpression(5));
        EvalContext context = new EvalContext(new DocumentInfoSet(), new Iterators.EmptyIterator());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThan() {
        CoreOperationRelationalExpression expression = new LessThanExpression(new ConstantExpression(3), new ConstantExpression(5));
        EvalContext context = new EvalContext(new DocumentInfoSet(), new Iterators.EmptyIterator());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testLessThanOrEqual() {
        CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(new ConstantExpression(5), new ConstantExpression(5));
        EvalContext context = new EvalContext(new DocumentInfoSet(), new Iterators.EmptyIterator());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanWithNull() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new ConstantExpression(5), new ConstantExpression(null));
        EvalContext context = new EvalContext(new DocumentInfoSet(), new Iterators.EmptyIterator());
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testGreaterThanOrEqualWithNull() {
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(new ConstantExpression(5), new ConstantExpression(null));
        EvalContext context = new EvalContext(new DocumentInfoSet(), new Iterators.EmptyIterator());
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testLessThanWithNull() {
        CoreOperationRelationalExpression expression = new LessThanExpression(new ConstantExpression(null), new ConstantExpression(5));
        EvalContext context = new EvalContext(new DocumentInfoSet(), new Iterators.EmptyIterator());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testLessThanOrEqualWithNull() {
        CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(new ConstantExpression(null), new ConstantExpression(5));
        EvalContext context = new EvalContext(new DocumentInfoSet(), new Iterators.EmptyIterator());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanWithNonComparableTypes() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new StringLiteralExpression("hello"), new NumberLiteralExpression(5));
        EvalContext context = new EvalContext(new DocumentInfoSet(), new Iterators.EmptyIterator());
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testGreaterThanOrEqualWithNonComparableTypes() {
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(new StringLiteralExpression("hello"), new NumberLiteralExpression(5));
        EvalContext context = new EvalContext(new DocumentInfoSet(), new Iterators.EmptyIterator());
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testLessThanWithNonComparableTypes() {
        CoreOperationRelationalExpression expression = new LessThanExpression(new NumberLiteralExpression(5), new StringLiteralExpression("hello"));
        EvalContext context = new EvalContext(new DocumentInfoSet(), new Iterators.EmptyIterator());
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testLessThanOrEqualWithNonComparableTypes() {
        CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(new NumberLiteralExpression(5), new StringLiteralExpression("hello"));
        EvalContext context = new EvalContext(new DocumentInfoSet(), new Iterators.EmptyIterator());
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testIteratorsGreaterThan() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new ConstantExpression(new TextInfo("5")), new ConstantExpression(new TextInfo("4")));
        EvalContext context = new EvalContext(new DocumentInfoSet(), new Iterators.EmptyIterator());
        IteratedNodeList left = context.makeIterator(new NodeInfo[]{new TextInfo("4"), new TextInfo("5")});
        IteratedNodeList right = context.makeIterator(new NodeInfo[]{new TextInfo("4")});
        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));
        assertTrue(expression.compute(left.iterator(), right.iterator()));
    }

    @Test
    public void testIteratorsGreaterThanOrEqual() {
        CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(new ConstantExpression(new TextInfo("5")), new ConstantExpression(new TextInfo("5")));
        EvalContext context = new EvalContext(new DocumentInfoSet(), new Iterators.EmptyIterator());
        IteratedNodeList left = context.makeIterator(new NodeInfo[]{new TextInfo("5")});
        IteratedNodeList right = context.makeIterator(new NodeInfo[]{new TextInfo("5")});
        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));
        assertTrue(expression.compute(left.iterator(), right.iterator()));
    }

    @Test
    public void testIteratorsLessThan() {
        CoreOperationRelationalExpression expression = new LessThanExpression(new ConstantExpression(new TextInfo("4")), new ConstantExpression(new TextInfo("5")));
        EvalContext context = new EvalContext(new DocumentInfoSet(), new Iterators.EmptyIterator());
        IteratedNodeList left = context.makeIterator(new NodeInfo[]{new TextInfo("4"), new TextInfo("5")});
        IteratedNodeList right = context.makeIterator(new NodeInfo[]{new TextInfo("5")});
        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
        assertFalse(expression.compute(left.iterator(), right.iterator()));
    }

    @Test
    public void testIteratorsLessThanOrEqual() {
        CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(new ConstantExpression(new TextInfo("5")), new ConstantExpression(new TextInfo("5")));
        EvalContext context = new EvalContext(new DocumentInfoSet(), new Iterators.EmptyIterator());
        IteratedNodeList left = context.makeIterator(new NodeInfo[]{new TextInfo("5")});
        IteratedNodeList right = context.makeIterator(new NodeInfo[]{new TextInfo("5")});
        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));
        assertTrue(expression.compute(left.iterator(), right.iterator()));
    }

    @Test
    public void testIteratorsDifferentTypes() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new ConstantExpression(new TextInfo("5")), new ConstantExpression(new NumberLiteralExpression(5)));
        EvalContext context = new EvalContext(new DocumentInfoSet(), new Iterators.EmptyIterator());
        IteratedNodeList left = context.makeIterator(new NodeInfo[]{new TextInfo("5")});
        IteratedNodeList right = context.makeIterator(new NodeInfo[]{new NumberInfo(5)});
        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
        assertFalse(expression.compute(left.iterator(), right.iterator()));
    }

    @Test
    public void testIteratorsNull() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new ConstantExpression(new TextInfo("5")), new ConstantExpression(null));
        EvalContext context = new EvalContext(new DocumentInfoSet(), new Iterators.EmptyIterator());
        IteratedNodeList left = context.makeIterator(new NodeInfo[]{new TextInfo("5")});
        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
        assertFalse(expression.compute(left.iterator(), null));
    }
}