package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new IntValue(1)),
                new LiteralExpression(new IntValue(1)));

        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testNotEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new IntValue(1)),
                new LiteralExpression(new IntValue(2)));

        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqualWithNull() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(null),
                new LiteralExpression(null));

        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testNotEqualWithNull() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(null),
                new LiteralExpression(new StringValue("foo")));

        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqualWithBoolean() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new BooleanValue(true)),
                new LiteralExpression(new BooleanValue(true)));

        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testNotEqualWithBoolean() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new BooleanValue(true)),
                new LiteralExpression(new BooleanValue(false)));

        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqualWithNumber() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new FloatValue(1.0f)),
                new LiteralExpression(new FloatValue(1.0f)));

        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testNotEqualWithNumber() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new FloatValue(1.0f)),
                new LiteralExpression(new FloatValue(2.0f)));

        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqualWithString() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new StringValue("foo")),
                new LiteralExpression(new StringValue("foo")));

        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testNotEqualWithString() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new StringValue("foo")),
                new LiteralExpression(new StringValue("bar")));

        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqualWithIteratorAndCollection() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new ArrayList<Object>(Arrays.asList(1, 2))),
                new LiteralExpression(new ArrayList<Object>(Arrays.asList(1, 2))));

        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testNotEqualWithIteratorAndDifferentCollection() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new ArrayList<Object>(Arrays.asList(1, 2))),
                new LiteralExpression(new ArrayList<Object>(Arrays.asList(1, 3))));

        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqualWithPointer() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new VariableExpression("$v1"),
                new PointerExpression("/foo/bar", new InitialContext()));

        EvalContext context = new EvalContext();
        context.setVariable("$v1", new Pointer("/foo/bar", new InitialContext()));
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testNotEqualWithDifferentPointer() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new VariableExpression("$v1"),
                new PointerExpression("/foo/bar", new InitialContext()));

        EvalContext context = new EvalContext();
        context.setVariable("$v1", new Pointer("/foo/baz", new InitialContext()));
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqualWithInitialContext() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new InitialContext()),
                new LiteralExpression(new InitialContext()));

        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualWithNullInitialContext() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(null),
                new LiteralExpression(new InitialContext()));

        EvalContext context = new EvalContext();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEqualWithNullInitialContext() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new LiteralExpression(new InitialContext()),
                new LiteralExpression(null));

        EvalContext context = new EvalContext();
    }

    @Test
    public void testEqualWithSelfContext() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new SelfContext(),
                new SelfContext());

        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testNotEqualWithDifferentSelfContext() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new SelfContext(),
                new SelfContext(new Pointer("/foo/bar", new InitialContext())));

        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }
}