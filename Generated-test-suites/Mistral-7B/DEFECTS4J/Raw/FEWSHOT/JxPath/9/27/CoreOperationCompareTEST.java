package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationCompareTest {

    @Test
    public void testCoreOperationCompare() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new StringLiteral("foo")),
                new LiteralExpression(new StringLiteral("foo")));
        assertNotNull(coreOperationCompare);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new NumberLiteral(1)),
                new LiteralExpression(new NumberLiteral(1)));
        assertEquals(2, coreOperationCompare.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new StringLiteral("foo")),
                new LiteralExpression(new StringLiteral("bar")));
        assertTrue(coreOperationCompare.isSymmetric());
    }

    @Test
    public void testEqualNullContext() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new NumberLiteral(1)),
                new LiteralExpression(new NumberLiteral(1)));

        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.equal(context, null, null));
    }

    @Test
    public void testEqualNullLeft() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new NumberLiteral(1)),
                new LiteralExpression(new NumberLiteral(1)));

        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.equal(context, null, new LiteralExpression(new NumberLiteral(1))));
    }

    @Test
    public void testEqualNullRight() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new NumberLiteral(1)),
                new LiteralExpression(new NumberLiteral(1)));

        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.equal(context, new LiteralExpression(new NumberLiteral(1)), null));
    }

    @Test
    public void testEqualBooleanTrueTrue() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new BooleanLiteral(true)),
                new LiteralExpression(new BooleanLiteral(true)));

        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.equal(context, new LiteralExpression(new BooleanLiteral(true)), new LiteralExpression(new BooleanLiteral(true))));
    }

    @Test
    public void testEqualBooleanTrueFalse() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new BooleanLiteral(true)),
                new LiteralExpression(new BooleanLiteral(false)));

        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.equal(context, new LiteralExpression(new BooleanLiteral(true)), new LiteralExpression(new BooleanLiteral(false))));
    }

    @Test
    public void testEqualBooleanFalseTrue() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new BooleanLiteral(false)),
                new LiteralExpression(new BooleanLiteral(true)));

        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.equal(context, new LiteralExpression(new BooleanLiteral(false)), new LiteralExpression(new BooleanLiteral(true))));
    }

    @Test
    public void testEqualBooleanFalseFalse() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new BooleanLiteral(false)),
                new LiteralExpression(new BooleanLiteral(false)));

        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.equal(context, new LiteralExpression(new BooleanLiteral(false)), new LiteralExpression(new BooleanLiteral(false))));
    }

    @Test
    public void testEqualNumberEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new NumberLiteral(1.0)),
                new LiteralExpression(new NumberLiteral(1.0)));

        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.equal(context, new LiteralExpression(new NumberLiteral(1.0)), new LiteralExpression(new NumberLiteral(1.0))));
    }

    @Test
    public void testEqualNumberNotEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new NumberLiteral(1.0)),
                new LiteralExpression(new NumberLiteral(2.0)));

        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.equal(context, new LiteralExpression(new NumberLiteral(1.0)), new LiteralExpression(new NumberLiteral(2.0))));
    }

    @Test
    public void testEqualStringEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new StringLiteral("foo")),
                new LiteralExpression(new StringLiteral("foo")));

        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.equal(context, new LiteralExpression(new StringLiteral("foo")), new LiteralExpression(new StringLiteral("foo"))));
    }

    @Test
    public void testEqualStringNotEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new StringLiteral("foo")),
                new LiteralExpression(new StringLiteral("bar")));

        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.equal(context, new LiteralExpression(new StringLiteral("foo")), new LiteralExpression(new StringLiteral("bar"))));
    }

    @Test
    public void testEqualPointerEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new VariableExpression("$x"),
                new VariableExpression("$x"));

        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Pointer("nodeValue"));
        context.setVariable("$x", context.getSingleNodePointer());

        assertTrue(coreOperationCompare.equal(context, new VariableExpression("$x"), new VariableExpression("$x")));
    }

    @Test
    public void testEqualPointerNotEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new VariableExpression("$x"),
                new VariableExpression("$y"));

        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Pointer("nodeValueX"));
        context.setVariable("$x", context.getSingleNodePointer());
        context.setSingleNodePointer(new Pointer("nodeValueY"));
        context.setVariable("$y", context.getSingleNodePointer());

        assertFalse(coreOperationCompare.equal(context, new VariableExpression("$x"), new VariableExpression("$y")));
    }

    @Test
    public void testContainsIteratorWithSameElements() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new StringLiteral("foo")),
                new LiteralExpression(new StringLiteral("foo")));

        EvalContext context = new EvalContext();
        Iterator<String> it = new ArrayList<String>() {{ add("foo"); }}.iterator();

        assertTrue(coreOperationCompare.contains(context, it, new LiteralExpression(new StringLiteral("foo"))));
    }

    @Test
    public void testContainsIteratorWithDifferentElements() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new StringLiteral("foo")),
                new LiteralExpression(new StringLiteral("bar")));

        EvalContext context = new EvalContext();
        Iterator<String> it = new ArrayList<String>() {{ add("foo"); add("bar"); }}.iterator();

        assertFalse(coreOperationCompare.contains(context, it, new LiteralExpression(new StringLiteral("foo"))));
    }

    @Test
    public void testFindMatchIteratorsWithSameElements() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new StringLiteral("foo")),
                new LiteralExpression(new StringLiteral("foo")));

        EvalContext context = new EvalContext();
        Iterator<String> lit = new ArrayList<String>() {{ add("foo"); }}.iterator();
        Iterator<String> rit = new ArrayList<String>() {{ add("foo"); }}.iterator();

        assertTrue(coreOperationCompare.findMatch(context, lit, rit));
    }

    @Test
    public void testFindMatchIteratorsWithDifferentElements() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new StringLiteral("foo")),
                new LiteralExpression(new StringLiteral("bar")));

        EvalContext context = new EvalContext();
        Iterator<String> lit = new ArrayList<String>() {{ add("foo"); }}.iterator();
        Iterator<String> rit = new ArrayList<String>() {{ add("bar"); }}.iterator();

        assertFalse(coreOperationCompare.findMatch(context, lit, rit));
    }

    @Test
    public void testFindMatchIteratorsWithEmptySecondIterator() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new StringLiteral("foo")),
                new LiteralExpression(null));

        EvalContext context = new EvalContext();
        Iterator<String> lit = new ArrayList<String>() {{ add("foo"); }}.iterator();

        assertFalse(coreOperationCompare.findMatch(context, lit, null));
    }
}