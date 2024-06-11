package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvaluationContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Test for CoreOperationCompare class.
 */
public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new LiteralExpression(1), new LiteralExpression(1));
        EvaluationContext context = new EvaluationContext();
        assertTrue(comparison.compute(context));
    }

    @Test
    public void testNotEqual() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new LiteralExpression(1), new LiteralExpression(2));
        EvaluationContext context = new EvaluationContext();
        assertFalse(comparison.compute(context));
    }

    @Test
    public void testEqualWithInitialContext() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new InitialContext(), new InitialContext());
        EvaluationContext context = new EvaluationContext();
        assertTrue(comparison.compute(context));
    }

    @Test
    public void testNotEqualWithInitialContext() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new InitialContext(), new InitialContext());
        EvaluationContext context = new EvaluationContext();
        assertFalse(comparison.compute(context));
    }

    @Test
    public void testEqualWithSelfContext() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new SelfContext(), new SelfContext());
        EvaluationContext context = new EvaluationContext();
        context.setSingleNodePointer(new Pointer("foo"));
        assertTrue(comparison.compute(context));
    }

    @Test
    public void testNotEqualWithSelfContext() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new SelfContext(), new SelfContext());
        EvaluationContext context = new EvaluationContext();
        context.setSingleNodePointer(new Pointer("foo"));
        assertFalse(comparison.compute(context));
    }

    @Test
    public void testEqualWithIterators() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new IteratorExpression(Arrays.asList(1, 2, 3).iterator()),
                new IteratorExpression(Arrays.asList(1, 2, 3).iterator()));
        EvaluationContext context = new EvaluationContext();
        assertTrue(comparison.compute(context));
    }

    @Test
    public void testNotEqualWithIterators() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new IteratorExpression(Arrays.asList(1, 2, 3).iterator()),
                new IteratorExpression(Arrays.asList(1, 2, 4).iterator()));
        EvaluationContext context = new EvaluationContext();
        assertFalse(comparison.compute(context));
    }

    @Test
    public void testEqualWithCollections() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new CollectionExpression(Arrays.asList(1, 2, 3)),
                new CollectionExpression(Arrays.asList(1, 2, 3)));
        EvaluationContext context = new EvaluationContext();
        assertTrue(comparison.compute(context));
    }

    @Test
    public void testNotEqualWithCollections() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new CollectionExpression(Arrays.asList(1, 2, 3)),
                new CollectionExpression(Arrays.asList(1, 2, 4)));
        EvaluationContext context = new EvaluationContext();
        assertFalse(comparison.compute(context));
    }

    @Test
    public void testEqualWithStrings() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new ConstantExpression("foo"), new ConstantExpression("foo"));
        EvaluationContext context = new EvaluationContext();
        assertTrue(comparison.compute(context));
    }

    @Test
    public void testNotEqualWithStrings() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new ConstantExpression("foo"), new ConstantExpression("bar"));
        EvaluationContext context = new EvaluationContext();
        assertFalse(comparison.compute(context));
    }

    @Test
    public void testEqualWithNulls() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new ConstantExpression(null), new ConstantExpression(null));
        EvaluationContext context = new EvaluationContext();
        assertTrue(comparison.compute(context));
    }

    @Test
    public void testNotEqualWithNullAndNonnull() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new ConstantExpression("foo"), new ConstantExpression(null));
        EvaluationContext context = new EvaluationContext();
        assertFalse(comparison.compute(context));
    }

    @Test
    public void testEqualWithBoolean() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new ConstantExpression(true), new ConstantExpression(true));
        EvaluationContext context = new EvaluationContext();
        assertTrue(comparison.compute(context));
    }

    @Test
    public void testNotEqualWithBoolean() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new ConstantExpression(true), new ConstantExpression(false));
        EvaluationContext context = new EvaluationContext();
        assertFalse(comparison.compute(context));
    }

    @Test
    public void testEqualWithDoubles() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new ConstantExpression(1.0), new ConstantExpression(1.0));
        EvaluationContext context = new EvaluationContext();
        assertTrue(comparison.compute(context));
    }

    @Test
    public void testNotEqualWithDoubles() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new ConstantExpression(1.0), new ConstantExpression(2.0));
        EvaluationContext context = new EvaluationContext();
        assertFalse(comparison.compute(context));
    }

    @Test
    public void testEqualWithPointers() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new VariableExpression("$v1"), new VariableExpression("$v2"));
        EvaluationContext context = new EvaluationContext();
        context.setVariable("$v1", new Pointer("foo:1"));
        context.setVariable("$v2", new Pointer("foo:1"));
        assertTrue(comparison.compute(context));
    }

    @Test
    public void testNotEqualWithPointers() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new VariableExpression("$v1"), new VariableExpression("$v2"));
        EvaluationContext context = new EvaluationContext();
        context.setVariable("$v1", new Pointer("foo:1"));
        context.setVariable("$v2", new Pointer("bar:1"));
        assertFalse(comparison.compute(context));
    }

    @Test
    public void testEqualWithEmptyCollections() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new CollectionExpression(Collections.emptyList()),
                new CollectionExpression(Collections.emptyList()));
        EvaluationContext context = new EvaluationContext();
        assertTrue(comparison.compute(context));
    }

    @Test
    public void testNotEqualWithEmptyCollections() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new CollectionExpression(Collections.emptyList()),
                new CollectionExpression(Collections.singletonList(1)));
        EvaluationContext context = new EvaluationContext();
        assertFalse(comparison.compute(context));
    }

    @Test
    public void testEqualWithIteratorsAndCollections() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new IteratorExpression(Arrays.asList(1, 2, 3).iterator()),
                new CollectionExpression(Arrays.asList(1, 2, 3)));
        EvaluationContext context = new EvaluationContext();
        assertTrue(comparison.compute(context));
    }

    @Test
    public void testNotEqualWithIteratorsAndCollections() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new IteratorExpression(Arrays.asList(1, 2, 3).iterator()),
                new CollectionExpression(Arrays.asList(1, 2, 4)));
        EvaluationContext context = new EvaluationContext();
        assertFalse(comparison.compute(context));
    }

    @Test
    public void testEqualWithMixedIteratorsAndCollections() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new IteratorExpression(Arrays.asList(1, 2, 3).iterator()),
                new CollectionExpression(Arrays.asList(2, 3, 4)));
        EvaluationContext context = new EvaluationContext();
        assertFalse(comparison.compute(context));
    }

    @Test
    public void testNotEqualWithMixedIteratorsAndCollections() {
        CoreOperationCompare comparison = new CoreOperationCompare(
                new IteratorExpression(Arrays.asList(1, 2, 3).iterator()),
                new CollectionExpression(Arrays.asList(1, 2, 4)));
        EvaluationContext context = new EvaluationContext();
        assertFalse(comparison.compute(context));
    }

}