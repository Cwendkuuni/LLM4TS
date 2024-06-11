package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.AbstractIterableTestBase;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.IterableResult;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.NodeSet;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.Value;
import org.apache.commons.jxpath.ri.Vars;

import org.junit.Test;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest extends AbstractIterableTestBase {

    /**
     * Test that &gt; returns true if left is greater than right.
     */
    @Test
    public void testGreaterThan() {
        Expression left = new Const(1.5);
        Expression right = new Const(1.0);
        CoreOperationRelationalExpression op = new GreaterThan(left, right);

        EvalContext context = new TestEvalContext();
        Value result = op.compute(context);
        assertTrue(result.isBoolean());
        assertTrue(result.getBooleanValue());

        context.setVariable("x", new IntNode(2));
        context.setVariable("y", new IntNode(1));
        left = new Variable("x");
        right = new Variable("y");
        op = new GreaterThan(left, right);

        result = op.compute(context);
        assertTrue(result.isBoolean());
        assertTrue(result.getBooleanValue());
    }

    /**
     * Test that &gt;= returns true if left is greater than or equal to right.
     */
    @Test
    public void testGreaterThanOrEqual() {
        Expression left = new Const(1.5);
        Expression right = new Const(1.0);
        CoreOperationRelationalExpression op = new GreaterThanOrEqual(left, right);

        EvalContext context = new TestEvalContext();
        Value result = op.compute(context);
        assertTrue(result.isBoolean());
        assertTrue(result.getBooleanValue());

        left = new Const(1.5);
        right = new Const(2.0);
        op = new GreaterThanOrEqual(left, right);

        context = new TestEvalContext();
        result = op.compute(context);
        assertFalse(result.isBoolean());
    }

    /**
     * Test that &lt; returns true if left is less than right.
     */
    @Test
    public void testLessThan() {
        Expression left = new Const(1.0);
        Expression right = new Const(1.5);
        CoreOperationRelationalExpression op = new LessThan(left, right);

        EvalContext context = new TestEvalContext();
        Value result = op.compute(context);
        assertTrue(result.isBoolean());
        assertFalse(result.getBooleanValue());

        left = new Const(1);
        right = new Const(2);
        op = new LessThan(left, right);

        context = new TestEvalContext();
        result = op.compute(context);
        assertTrue(result.isBoolean());
        assertFalse(result.getBooleanValue());
    }

    /**
     * Test that &lt;= returns true if left is less than or equal to right.
     */
    @Test
    public void testLessThanOrEqual() {
        Expression left = new Const(1.0);
        Expression right = new Const(1.5);
        CoreOperationRelationalExpression op = new LessThanOrEqual(left, right);

        EvalContext context = new TestEvalContext();
        Value result = op.compute(context);
        assertTrue(result.isBoolean());
        assertFalse(result.getBooleanValue());

        left = new Const(1.0);
        right = new Const(1.0);
        op = new LessThanOrEqual(left, right);

        context = new TestEvalContext();
        result = op.compute(context);
        assertTrue(result.isBoolean());
        assertTrue(result.getBooleanValue());
    }

    /**
     * Test that &gt; returns false if left is less than or equal to right.
     */
    @Test
    public void testGreaterThanFailure() {
        Expression left = new Const(1.0);
        Expression right = new Const(2.0);
        CoreOperationRelationalExpression op = new GreaterThan(left, right);

        EvalContext context = new TestEvalContext();
        Value result = op.compute(context);
        assertTrue(result.isBoolean());
        assertFalse(result.getBooleanValue());

        left = new Const(1.5);
        right = new Const(1.0);
        op = new GreaterThan(left, right);

        context = new TestEvalContext();
        result = op.compute(context);
        assertTrue(result.isBoolean());
        assertFalse(result.getBooleanValue());
    }

    /**
     * Test that &gt;= returns false if left is less than right.
     */
    @Test
    public void testGreaterThanOrEqualFailure() {
        Expression left = new Const(1.0);
        Expression right = new Const(1.5);
        CoreOperationRelationalExpression op = new GreaterThanOrEqual(left, right);

        EvalContext context = new TestEvalContext();
        Value result = op.compute(context);
        assertTrue(result.isBoolean());
        assertFalse(result.getBooleanValue());

        left = new Const(1.5);
        right = new Const(2.0);
        op = new GreaterThanOrEqual(left, right);

        context = new TestEvalContext();
        result = op.compute(context);
        assertTrue(result.isBoolean());
        assertFalse(result.getBooleanValue());
    }

    /**
     * Test that &lt; returns true if left is less than right.
     */
    @Test
    public void testLessThanSuccess() {
        Expression left = new Const(1.0);
        Expression right = new Const(2.0);
        CoreOperationRelationalExpression op = new LessThan(left, right);

        EvalContext context = new TestEvalContext();
        Value result = op.compute(context);
        assertTrue(result.isBoolean());
        assertTrue(result.getBooleanValue());

        left = new Const(1.5);
        right = new Const(1.0);
        op = new LessThan(left, right);

        context = new TestEvalContext();
        result = op.compute(context);
        assertTrue(result.isBoolean());
        assertTrue(result.getBooleanValue());
    }

    /**
     * Test that &lt;= returns true if left is less than or equal to right.
     */
    @Test
    public void testLessThanOrEqualSuccess() {
        Expression left = new Const(1.0);
        Expression right = new Const(1.5);
        CoreOperationRelationalExpression op = new LessThanOrEqual(left, right);

        EvalContext context = new TestEvalContext();
        Value result = op.compute(context);
        assertTrue(result.isBoolean());
        assertTrue(result.getBooleanValue());

        left = new Const(1.5);
        right = new Const(1.5);
        op = new LessThanOrEqual(left, right);

        context = new TestEvalContext();
        result = op.compute(context);
        assertTrue(result.isBoolean());
        assertTrue(result.getBooleanValue());
    }

    /**
     * Test that &gt;= returns true if left is greater than or equal to right.
     */
    @Test
    public void testGreaterThanOrEqualSuccess() {
        Expression left = new Const(1.5);
        Expression right = new Const(1.5);
        CoreOperationRelationalExpression op = new GreaterThanOrEqual(left, right);

        EvalContext context = new TestEvalContext();
        Value result = op.compute(context);
        assertTrue(result.isBoolean());
        assertTrue(result.getBooleanValue());

        left = new Const(1.5);
        right = new Const(1.0);
        op = new GreaterThanOrEqual(left, right);

        context = new TestEvalContext();
        result = op.compute(context);
        assertTrue(result.isBoolean());
        assertTrue(result.getBooleanValue());
    }

    /**
     * Test that computation of &gt;, &gt;=, &lt;, &lt;= on NaN values returns false.
     */
    @Test
    public void testNaNComparison() {
        Expression left = new Const(Double.NaN);
        Expression right = new Const(Double.NaN);
        CoreOperationRelationalExpression op = new GreaterThan(left, right);

        EvalContext context = new TestEvalContext();
        Value result = op.compute(context);
        assertTrue(result.isBoolean());
        assertFalse(result.getBooleanValue());

        op = new GreaterThanOrEqual(left, right);
        result = op.compute(context);
        assertTrue(result.isBoolean());
        assertFalse(result.getBooleanValue());

        op = new LessThan(left, right);
        result = op.compute(context);
        assertTrue(result.isBoolean());
        assertFalse(result.getBooleanValue());

        op = new LessThanOrEqual(left, right);
        result = op.compute(context);
        assertTrue(result.isBoolean());
        assertFalse(result.getBooleanValue());
    }

    /**
     * Test that computation of &gt;, &gt;=, &lt;, &lt;= on String values fails with NPE
     * if the strings are not comparable.
     */
    @Test(expected = NullPointerException.class)
    public void testStringComparisonFailure() {
        Expression left = new Const("foo");
        Expression right = new Const("bar");
        CoreOperationRelationalExpression op = new GreaterThan(left, right);

        EvalContext context = new TestEvalContext();
        Value result = op.compute(context);

        op = new GreaterThanOrEqual(left, right);
        result = op.compute(context);

        op = new LessThan(left, right);
        result = op.compute(context);

        op = new LessThanOrEqual(left, right);
        result = op.compute(context);
    }

    /**
     * Test that computation of &gt;, &gt;=, &lt;, &lt;= on Iterators returns true if any
     * element of the left Iterator matches the right Iterator.
     */
    @Test
    public void testIteratorComparison() {
        Iterator left = Iterators.iter(new Object[] { 1, 2, 3 });
        Iterator right = Iterators.iter(new Object[] { 1, 2 });
        CoreOperationRelationalExpression op = new GreaterThanOrEqual(left, right);

        EvalContext context = new TestEvalContext();
        Value result = op.compute(context);
        assertTrue(result.isBoolean());
        assertTrue(result.getBooleanValue());

        left = Iterators.iter(new Object[] { 1, 2, 3 });
        right = Iterators.iter(new Object[] { 3, 2, 1 });
        op = new LessThan(left, right);

        context = new TestEvalContext();
        result = op.compute(context);
        assertTrue(result.isBoolean());
        assertTrue(result.getBooleanValue());

        left = Iterators.iter(new Object[] { 1, 2, 3 });
        right = Iterators.iter(new Object[] { 3, 2, 1, 1 });
        op = new LessThanOrEqual(left, right);

        context = new TestEvalContext();
        result = op.compute(context);
        assertTrue(result.isBoolean());
        assertTrue(result.getBooleanValue());

        left = Iterators.iter(new Object[] { "foo", "bar", "baz" });
        right = Iterators.iter(new Object[] { "bar" });
        op = new GreaterThanOrEqual(left, right);

        context = new TestEvalContext();
        result = op.compute(context);
        assertTrue(result.isBoolean());
        assertTrue(result.getBooleanValue());
    }

    /**
     * Test that computation of &gt;, &gt;=, &lt;, &lt;= on Iterators fails if left
     * Iterator does not implement Iterator.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorComparisonFailure() {
        NodeSet left = new NodeSet();
        Iterator right = Iterators.iter(new Object[] { 1, 2, 3 });
        CoreOperationRelationalExpression op = new GreaterThanOrEqual(left.iterator(), right);

        EvalContext context = new TestEvalContext();
        Value result = op.compute(context);
    }
}