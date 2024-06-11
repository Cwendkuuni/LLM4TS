package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Object l = "abc";
        Object r = "abc";
        EvalContext context = new EvalContext(new InitialContext(null));
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression("abc"),
                new ConstantExpression("abc")
        );
        assertTrue(op.equal(context, null, null));
        assertTrue(op.equal(context, new ConstantExpression(l), new ConstantExpression(r)));
    }

    @Test
    public void testEqual_null() {
        EvalContext context = new EvalContext(new InitialContext(null));
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(null),
                new ConstantExpression(null)
        );
        assertTrue(op.equal(context, null, null));
        assertTrue(op.equal(context, new ConstantExpression(null), new ConstantExpression(null)));
    }

    @Test
    public void testEqual_boolean() {
        EvalContext context = new EvalContext(new InitialContext(null));
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(true),
                new ConstantExpression(true)
        );
        assertTrue(op.equal(context, null, null));
        assertTrue(op.equal(context, new ConstantExpression(true), new ConstantExpression(true)));
    }

    @Test
    public void testEqual_number() {
        EvalContext context = new EvalContext(new InitialContext(null));
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(1.0),
                new ConstantExpression(1.0)
        );
        assertTrue(op.equal(context, null, null));
        assertTrue(op.equal(context, new ConstantExpression(1.0), new ConstantExpression(1.0)));
    }

    @Test
    public void testEqual_string() {
        EvalContext context = new EvalContext(new InitialContext(null));
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression("abc"),
                new ConstantExpression("abc")
        );
        assertTrue(op.equal(context, null, null));
        assertTrue(op.equal(context, new ConstantExpression("abc"), new ConstantExpression("abc")));
    }

    @Test
    public void testEqual_pointer() {
        EvalContext context = new EvalContext(new InitialContext(null));
        Pointer l = new Pointer("abc");
        Pointer r = new Pointer("abc");
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(l),
                new ConstantExpression(r)
        );
        assertTrue(op.equal(context, null, null));
        assertTrue(op.equal(context, new ConstantExpression(l), new ConstantExpression(r)));
    }

    @Test
    public void testEqual_iterators() {
        EvalContext context = new EvalContext(new InitialContext(null));
        List<Object> left = Arrays.asList("a", "b", "c");
        List<Object> right = Arrays.asList("a", "b", "c");
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(left.iterator()),
                new ConstantExpression(right.iterator())
        );
        assertTrue(op.equal(context, null, null));
        assertTrue(op.equal(context, new ConstantExpression(left.iterator()), new ConstantExpression(right.iterator())));
    }

    @Test
    public void testEquals_iterators_matching_element() {
        EvalContext context = new EvalContext(new InitialContext(null));
        List<Object> left = Arrays.asList("a", "b", "c");
        List<Object> right = Arrays.asList("a", "b", "c");
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(left.iterator()),
                new ConstantExpression(right.iterator())
        );
        assertTrue(op.equal(context, null, null));
        assertTrue(op.equal(context, new ConstantExpression(left.iterator()), new ConstantExpression(right.iterator())));
    }

    @Test
    public void testEquals_iterators_no_match() {
        EvalContext context = new EvalContext(new InitialContext(null));
        List<Object> left = Arrays.asList("a", "b");
        List<Object> right = Arrays.asList("a", "c");
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(left.iterator()),
                new ConstantExpression(right.iterator())
        );
        assertFalse(op.equal(context, null, null));
        assertFalse(op.equal(context, new ConstantExpression(left.iterator()), new ConstantExpression(right.iterator())));
    }

    @Test
    public void testFindMatch() {
        EvalContext context = new EvalContext(new InitialContext(null));
        List<Object> left = Arrays.asList("a", "b", "c");
        List<Object> right = Arrays.asList("a", "b", "d");
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(left.iterator()),
                new ConstantExpression(right.iterator())
        );
        assertFalse(op.equal(context, null, null));
        assertFalse(op.findMatch(left.iterator(), right.iterator()));
    }

    @Test
    public void testFindMatch_matching_elements() {
        EvalContext context = new EvalContext(new InitialContext(null));
        List<Object> left = Arrays.asList("a", "b", "c");
        List<Object> right = Arrays.asList("a", "b", "c");
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(left.iterator()),
                new ConstantExpression(right.iterator())
        );
        assertTrue(op.equal(context, null, null));
        assertTrue(op.findMatch(left.iterator(), right.iterator()));
    }

    @Test
    public void testContain() {
        EvalContext context = new EvalContext(new InitialContext(null));
        List<Object> left = Arrays.asList("a", "b", "c");
        Object r = "b";
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(left.iterator()),
                new ConstantExpression(r)
        );
        assertTrue(op.contains(left.iterator(), r));
    }

    @Test
    public void testContains_no_match() {
        EvalContext context = new EvalContext(new InitialContext(null));
        List<Object> left = Arrays.asList("a", "b", "c");
        Object r = "d";
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(left.iterator()),
                new ConstantExpression(r)
        );
        assertFalse(op.contains(left.iterator(), r));
    }

    @Test
    public void testContains_empty_collection() {
        EvalContext context = new EvalContext(new InitialContext(null));
        CoreOperationCompare op = new CoreOperationCompare(
                new ConstantExpression(Collections.emptyIterator()),
                new ConstantExpression("text")
        );
        assertFalse(op.contains(Collections.emptyIterator(), "text"));
    }
}