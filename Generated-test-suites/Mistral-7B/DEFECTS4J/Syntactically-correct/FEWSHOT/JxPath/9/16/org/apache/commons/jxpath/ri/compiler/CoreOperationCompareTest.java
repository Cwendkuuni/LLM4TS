package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.InfoSet;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

/**
 * Test class for CoreOperationCompare
 */
public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext(new InfoSet(), new InitialContext(), new SelfContext());
        CoreOperationCompare compare = new CoreOperationCompare(
                new LiteralExpression("5"),
                new LiteralExpression("5")
        );
        assertTrue(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new LiteralExpression("5"),
                new LiteralExpression("6")
        );
        assertFalse(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new LiteralExpression("5"),
                new LiteralExpression("5.0")
        );
        assertTrue(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new LiteralExpression("true"),
                new LiteralExpression("true")
        );
        assertTrue(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new LiteralExpression("null"),
                new LiteralExpression("null")
        );
        assertTrue(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new LiteralExpression("\"foo\""),
                new LiteralExpression("\"foo\"")
        );
        assertTrue(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new LiteralExpression("\"foo\""),
                new LiteralExpression("\"bar\"")
        );
        assertFalse(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new LiteralExpression(new Pointer("")),
                new LiteralExpression(new Pointer(""))
        );
        assertTrue(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new LiteralExpression(new Pointer("")),
                new LiteralExpression(new Pointer("/a"))
        );
        assertFalse(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new LiteralExpression(new Pointer("1")),
                new LiteralExpression(new Pointer("1"))
        );
        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testContains() {
        EvalContext context = new EvalContext(new InfoSet(), new InitialContext(), new SelfContext());
        CoreOperationCompare compare = null;

        compare = new CoreOperationCompare(
                new LiteralExpression(new Integer[]{1, 2, 3}),
                new LiteralExpression(new Integer[]{1, 2})
        );
        assertTrue(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new LiteralExpression(new Integer[]{1, 2, 3}),
                new LiteralExpression(new Integer[]{2, 3})
        );
        assertTrue(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new LiteralExpression(new Integer[]{1, 2, 3}),
                new LiteralExpression(new Integer[]{4, 5})
        );
        assertFalse(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new LiteralExpression(new String[]{"foo", "bar"}),
                new LiteralExpression("foo")
        );
        assertTrue(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new LiteralExpression(new String[]{"foo", "bar"}),
                new LiteralExpression("baz")
        );
        assertFalse(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new LiteralExpression(new String[]{"foo"}),
                new LiteralExpression(new String[]{"foo", "bar"})
        );
        assertFalse(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new LiteralExpression(new String[]{"foo"}),
                new LiteralExpression(new String[]{"foo"})
        );
        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testFindMatch() {
        EvalContext context = new EvalContext(new InfoSet(), new InitialContext(), new SelfContext());
        CoreOperationCompare compare = null;

        compare = new CoreOperationCompare(
                new LiteralExpression(new Integer[]{1, 2, 3}),
                new LiteralExpression(new Integer[]{1, 2, 3, 4})
        );
        assertTrue(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new LiteralExpression(new Integer[]{1, 2, 3}),
                new LiteralExpression(new Integer[]{2, 3, 4})
        );
        assertFalse(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new LiteralExpression(new Integer[]{1, 2, 3}),
                new LiteralExpression(new Integer[]{1, 4, 3})
        );
        assertFalse(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new LiteralExpression(new String[]{"foo", "bar"}),
                new LiteralExpression(new String[]{"foo", "bar", "baz"})
        );
        assertTrue(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new LiteralExpression(new String[]{"foo", "bar"}),
                new LiteralExpression(new String[]{"foo", "qux"})
        );
        assertFalse(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new LiteralExpression(new String[]{"foo"}),
                new LiteralExpression(new String[]{"foo", "bar"})
        );
        assertFalse(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new LiteralExpression(new String[]{"foo"}),
                new LiteralExpression(new String[]{"foo"})
        );
        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testNull() {
        EvalContext context = new EvalContext(new InfoSet(), new InitialContext(), new SelfContext());
        CoreOperationCompare compare = null;

        compare = new CoreOperationCompare(
                new LiteralExpression("null"),
                new LiteralExpression(null)
        );
        assertTrue(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new LiteralExpression(null),
                new LiteralExpression("null")
        );
        assertTrue(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new LiteralExpression("null"),
                new LiteralExpression("")
        );
        assertFalse(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new LiteralExpression(""),
                new LiteralExpression("null")
        );
        assertFalse(compare.evaluate(context));

        compare = new CoreOperationCompare(
                new LiteralExpression("null"),
                new LiteralExpression(new Pointer(""))
        );
        assertNull(compare);

        compare = new CoreOperationCompare(
                new LiteralExpression(new Pointer("")),
                new LiteralExpression("null")
        );
        assertNull(compare);
    }
}