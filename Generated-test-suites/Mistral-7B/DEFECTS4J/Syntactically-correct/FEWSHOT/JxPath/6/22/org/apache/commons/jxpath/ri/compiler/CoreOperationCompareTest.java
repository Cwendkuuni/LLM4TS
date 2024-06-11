package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

/**
 * Test cases for CoreOperationCompare class
 */
public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext(null, new InitialContext(null));

        CoreOperationCompare compare = new CoreOperationCompare(
            new LiteralExpression(InfoSetUtil.createString("foo")),
            new LiteralExpression(InfoSetUtil.createString("foo"))
        );

        assertTrue(compare.evaluate(context).getValue());

        compare = new CoreOperationCompare(
            new LiteralExpression(InfoSetUtil.createString("foo")),
            new LiteralExpression(InfoSetUtil.createString("bar"))
        );

        assertFalse(compare.evaluate(context).getValue());

        compare = new CoreOperationCompare(
            new LiteralExpression(InfoSetUtil.createString("foo")),
            null
        );

        assertFalse(compare.evaluate(context).getValue());
    }

    @Test
    public void testEqualCollection() {
        EvalContext context = new EvalContext(null, new InitialContext(null));

        CoreOperationCompare compare = new CoreOperationCompare(
            new LiteralExpression(InfoSetUtil.createString("foo")),
            new ListExpression(new LiteralExpression(InfoSetUtil.createString("foo")))
        );

        assertTrue(compare.evaluate(context).getValue());

        List list = new ArrayList();
        list.add(InfoSetUtil.createString("foo"));
        compare = new CoreOperationCompare(
            new LiteralExpression(InfoSetUtil.createString("foo")),
            new ListExpression(new LiteralExpression(InfoSetUtil.createString("bar")))
        );

        assertFalse(compare.evaluate(context).getValue());

        compare = new CoreOperationCompare(
            new LiteralExpression(InfoSetUtil.createString("foo")),
            new ListExpression(list)
        );

        assertTrue(compare.evaluate(context).getValue());
    }

    @Test
    public void testEqualIterator() {
        EvalContext context = new EvalContext(null, new InitialContext(null));

        String[] values = {"foo", "bar", "baz"};
        Iterator iterator = new HashSet(List.of(values)).iterator();

        CoreOperationCompare compare = new CoreOperationCompare(
            new LiteralExpression(InfoSetUtil.createString("foo")),
            iterator
        );

        assertTrue(compare.evaluate(context).getValue());

        compare = new CoreOperationCompare(
            new LiteralExpression(InfoSetUtil.createString("bar")),
            iterator
        );

        assertFalse(compare.evaluate(context).getValue());

        compare = new CoreOperationCompare(
            new LiteralExpression(InfoSetUtil.createString("foo")),
            new IteratorExpression(iterator)
        );

        assertTrue(compare.evaluate(context).getValue());
    }

    @Test
    public void testEqualPointer() {
        EvalContext context = new EvalContext(null, new InitialContext(null));

        Pointer leftPointer = new Pointer(null, new SelfContext());
        Pointer rightPointer = new Pointer(null, new SelfContext());

        CoreOperationCompare compare = new CoreOperationCompare(
            leftPointer,
            rightPointer
        );

        assertTrue(compare.evaluate(context).getValue());

        leftPointer = new Pointer(null, new SelfContext());
        rightPointer = new Pointer(null, new InitialContext(null));

        assertFalse(compare.evaluate(context).getValue());
    }

    @Test
    public void testEqualBoolean() {
        EvalContext context = new EvalContext(null, new InitialContext(null));

        CoreOperationCompare compare = new CoreOperationCompare(
            new LiteralExpression(InfoSetUtil.createBoolean(true)),
            new LiteralExpression(InfoSetUtil.createBoolean(true))
        );

        assertTrue(compare.evaluate(context).getValue());

        compare = new CoreOperationCompare(
            new LiteralExpression(InfoSetUtil.createBoolean(true)),
            new LiteralExpression(InfoSetUtil.createBoolean(false))
        );

        assertFalse(compare.evaluate(context).getValue());
    }

    @Test
    public void testEqualNumber() {
        EvalContext context = new EvalContext(null, new InitialContext(null));

        CoreOperationCompare compare = new CoreOperationCompare(
            new LiteralExpression(InfoSetUtil.createNumber(1.0)),
            new LiteralExpression(InfoSetUtil.createNumber(1.0))
        );

        assertTrue(compare.evaluate(context).getValue());

        compare = new CoreOperationCompare(
            new LiteralExpression(InfoSetUtil.createNumber(1.0)),
            new LiteralExpression(InfoSetUtil.createNumber(2.0))
        );

        assertFalse(compare.evaluate(context).getValue());
    }

    @Test
    public void testEqualString() {
        EvalContext context = new EvalContext(null, new InitialContext(null));

        CoreOperationCompare compare = new CoreOperationCompare(
            new LiteralExpression(InfoSetUtil.createString("foo")),
            new LiteralExpression(InfoSetUtil.createString("foo"))
        );

        assertTrue(compare.evaluate(context).getValue());

        compare = new CoreOperationCompare(
            new LiteralExpression(InfoSetUtil.createString("foo")),
            new LiteralExpression(InfoSetUtil.createString("bar"))
        );

        assertFalse(compare.evaluate(context).getValue());
    }

    @Test
    public void testContains() {
        EvalContext context = new EvalContext(null, new InitialContext(null));

        String[] values = {"foo", "bar", "baz"};

        CoreOperationCompare compare = new CoreOperationCompare(
            new LiteralExpression(InfoSetUtil.createString("foo")),
            new ListExpression(new LiteralExpression(InfoSetUtil.createString("foo")))
        );

        assertTrue(compare.evaluate(context).getValue());

        compare = new CoreOperationCompare(
            new LiteralExpression(InfoSetUtil.createString("bar")),
            new ListExpression(new LiteralExpression(InfoSetUtil.createString("foo")))
        );

        assertFalse(compare.evaluate(context).getValue());

        compare = new CoreOperationCompare(
            new LiteralExpression(InfoSetUtil.createString("foo")),
            new ListExpression(new LiteralExpression(InfoSetUtil.createString("foo")), new LiteralExpression(InfoSetUtil.createString("bar")), new LiteralExpression(InfoSetUtil.createString("baz")))
        );

        assertTrue(compare.evaluate(context).getValue());
    }

    @Test
    public void testFindMatch() {
        EvalContext context = new EvalContext(null, new InitialContext(null));

        String[] leftValues = {"foo", "bar", "baz"};
        String[] rightValues = {"foo", "bar", "baz"};

        CoreOperationCompare compare = new CoreOperationCompare(
            new ListExpression(new LiteralExpression(InfoSetUtil.createString("foo"))),
            new ListExpression(new LiteralExpression(InfoSetUtil.createString("foo")))
        );

        assertTrue(compare.evaluate(context).getValue());

        compare = new CoreOperationCompare(
            new ListExpression(new LiteralExpression(InfoSetUtil.createString("bar"))),
            new ListExpression(new LiteralExpression(InfoSetUtil.createString("foo")))
        );

        assertFalse(compare.evaluate(context).getValue());

        compare = new CoreOperationCompare(
            new ListExpression(new LiteralExpression(InfoSetUtil.createString("foo"))),
            new ListExpression(new LiteralExpression(InfoSetUtil.createString("foo")), new LiteralExpression(InfoSetUtil.createString("bar")), new LiteralExpression(InfoSetUtil.createString("baz")))
        );

        assertTrue(compare.evaluate(context).getValue());
    }
}