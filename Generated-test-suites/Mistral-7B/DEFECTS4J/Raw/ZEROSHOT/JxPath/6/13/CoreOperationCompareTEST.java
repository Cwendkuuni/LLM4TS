package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare.CoreOperationCompareTest;
import org.junit.Test;

/**
 * Tests for CoreOperationCompare class
 */
public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new LiteralExpression(1), new LiteralExpression(1));
        EvalContext context = new EvalContext(new InitialContext());
        assertTrue(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(1), new LiteralExpression(2));
        assertFalse(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(null), new LiteralExpression(null));
        assertTrue(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression("abc"), new LiteralExpression("abc"));
        assertTrue(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression("abc"), new LiteralExpression("def"));
        assertFalse(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new Integer(1)), new LiteralExpression(new Integer(1)));
        assertTrue(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new Integer(1)), new LiteralExpression(new Integer(2)));
        assertFalse(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new Double(1.0)), new LiteralExpression(new Double(1.0)));
        assertTrue(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new Double(1.0)), new LiteralExpression(new Double(2.0)));
        assertFalse(compare.compute(context));
    }

    @Test
    public void testEqualWithIterators() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new LiteralExpression(new ArrayList(Collections.singletonList(1))),
            new LiteralExpression(new ArrayList(Collections.singletonList(1))));
        EvalContext context = new EvalContext(new InitialContext());
        assertTrue(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new ArrayList(Collections.singletonList(1))),
            new LiteralExpression(new ArrayList(Collections.singletonList(2))));
        assertFalse(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new ArrayList(Collections.singletonList(null))),
            new LiteralExpression(new ArrayList(Collections.singletonList(null))));
        assertTrue(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new ArrayList(Collections.singletonList("abc"))),
            new LiteralExpression(new ArrayList(Collections.singletonList("abc"))));
        assertTrue(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new ArrayList(Collections.singletonList("abc"))),
            new LiteralExpression(new ArrayList(Collections.singletonList("def"))));
        assertFalse(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new ArrayList(Collections.singletonList(new Integer(1)))),
            new LiteralExpression(new ArrayList(Collections.singletonList(new Integer(1)))));
        assertTrue(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new ArrayList(Collections.singletonList(new Integer(1)))),
            new LiteralExpression(new ArrayList(Collections.singletonList(new Integer(2)))));
        assertFalse(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new ArrayList(Collections.singletonList(new Double(1.0)))),
            new LiteralExpression(new ArrayList(Collections.singletonList(new Double(1.0)))));
        assertTrue(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new ArrayList(Collections.singletonList(new Double(1.0)))),
            new LiteralExpression(new ArrayList(Collections.singletonList(new Double(2.0)))));
        assertFalse(compare.compute(context));
    }

    @Test
    public void testEqualWithPointers() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new LiteralExpression(new Pointer("a[0]")), new LiteralExpression(new Pointer("a[0]")));
        EvalContext context = new EvalContext(new InitialContext());
        assertTrue(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new Pointer("a[0]")), new LiteralExpression(new Pointer("a[1]")));
        assertFalse(compare.computel(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new Pointer(null)), new LiteralExpression(new Pointer(null)));
        assertTrue(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new Pointer("abc")), new LiteralExpression(new Pointer("abc")));
        assertTrue(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new Pointer("abc")), new LiteralExpression(new Pointer("def")));
        assertFalse(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new Pointer(new Integer(1))), new LiteralExpression(new Pointer(new Integer(1))));
        assertTrue(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new Pointer(new Integer(1))), new LiteralExpression(new Pointer(new Integer(2))));
        assertFalse(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new Pointer(new Double(1.0))), new LiteralExpression(new Pointer(new Double(1.0))));
        assertTrue(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new Pointer(new Double(1.0))), new LiteralExpression(new Pointer(new Double(2.0))));
        assertFalse(compare.compute(context));
    }

    @Test
    public void testEqualWithMixedTypes() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new LiteralExpression(new Integer(1)), new LiteralExpression("1"));
        EvalContext context = new EvalContext(new InitialContext());
        assertTrue(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new Integer(1)), new LiteralExpression("2"));
        assertFalse(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new Double(1.0)), new LiteralExpression("1.0"));
        assertTrue(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new Double(1.0)), new LiteralExpression("2.0"));
        assertFalse(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new Integer(1)), new LiteralExpression(new Pointer("a[0]")));
        assertFalse(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new Double(1.0)), new LiteralExpression(new Pointer("a[0]")));
        assertFalse(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new Pointer("a[0]")), new LiteralExpression(new Integer(1)));
        assertFalse(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new Pointer("a[0]")), new LiteralExpression(new Double(1.0)));
        assertFalse(compare.compute(context));
    }

    @Test
    public void testEqualWithBoolean() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new LiteralExpression(new Boolean(true)), new LiteralExpression(new Boolean(true)));
        EvalContext context = new EvalContext(new InitialContext());
        assertTrue(compare.compute(context));

        compare = new CoreOperationCompare(
            new LiteralExpression(new Boolean(true)), new LiteralExpression(new Boolean(false)));
        assertFalse(compare.compute(context));
    }

}