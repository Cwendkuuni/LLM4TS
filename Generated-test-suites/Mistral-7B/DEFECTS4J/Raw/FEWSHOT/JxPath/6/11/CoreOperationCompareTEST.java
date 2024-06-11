package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {

    @Test
    public void testEqualFalse() {
        Expression left = new LiteralExpression(new Integer(1));
        Expression right = new LiteralExpression(new Integer(2));
        CoreOperationCompare op = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance(), null, null);
        assertFalse(op.evaluate(context));
    }

    @Test
    public void testEqualTrue() {
        Expression left = new LiteralExpression(new Integer(1));
        Expression right = new LiteralExpression(new Integer(1));
        CoreOperationCompare op = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance(), null, null);
        assertTrue(op.evaluate(context));
    }

    @Test
    public void testEqualStringTrue() {
        Expression left = new LiteralExpression("foo");
        Expression right = new LiteralExpression("foo");
        CoreOperationCompare op = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance(), null, null);
        assertTrue(op.evaluate(context));
    }

    @Test
    public void testEqualStringFalse() {
        Expression left = new LiteralExpression("foo");
        Expression right = new LiteralExpression("bar");
        CoreOperationCompare op = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance(), null, null);
        assertFalse(op.evaluate(context));
    }

    @Test
    public void testEqualBooleanTrue() {
        Expression left = new LiteralExpression(Boolean.TRUE);
        Expression right = new LiteralExpression(Boolean.TRUE);
        CoreOperationCompare op = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance(), null, null);
        assertTrue(op.evaluate(context));
    }

    @Test
    public void testEqualBooleanFalse() {
        Expression left = new LiteralExpression(Boolean.TRUE);
        Expression right = new LiteralExpression(Boolean.FALSE);
        CoreOperationCompare op = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance(), null, null);
        assertFalse(op.evaluate(context));
    }

    @Test
    public void testEqualNullAndNull() {
        Expression left = new LiteralExpression(null);
        Expression right = new LiteralExpression(null);
        CoreOperationCompare op = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance(), null, null);
        assertTrue(op.evaluate(context));
    }

    @Test
    public void testEqualNullAndNonNull() {
        Expression left = new LiteralExpression(null);
        Expression right = new LiteralExpression("foo");
        CoreOperationCompare op = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance(), null, null);
        assertFalse(op.evaluate(context));
    }

    @Test
    public void testContainsIteratorTrue() {
        Iterator it = new java.util.ArrayList(java.util.Arrays.asList(1, 2, 3)).iterator();
        Expression right = new LiteralExpression(2);
        CoreOperationCompare op = new CoreOperationCompare(it, right);
        EvalContext context = new EvalContext(InitialContext.getInstance(), null, null);
        assertTrue(op.evaluate(context));
    }

    @Test
    public void testContainsIteratorFalse() {
        Iterator it = new java.util.ArrayList(java.util.Arrays.asList(1, 2, 3)).iterator();
        Expression right = new LiteralExpression(4);
        CoreOperationCompare op = new CoreOperationCompare(it, right);
        EvalContext context = new EvalContext(InitialContext.getInstance(), null, null);
        assertFalse(op.evaluate(context));
    }

    @Test
    public void testFindMatchTrue() {
        Iterator lit = new java.util.ArrayList(java.util.Arrays.asList(1, 2, 3)).iterator();
        Iterator rit = new java.util.ArrayList(java.util.Arrays.asList(2, 3, 4)).iterator();
        CoreOperationCompare op = new CoreOperationCompare(lit, rit);
        EvalContext context = new EvalContext(InitialContext.getInstance(), null, null);
        assertTrue(op.evaluate(context));
    }

    @Test
    public void testFindMatchFalse() {
        Iterator lit = new java.util.ArrayList(java.util.Arrays.asList(1, 2, 3)).iterator();
        Iterator rit = new java.util.ArrayList(java.util.Arrays.asList(5, 6, 7)).iterator();
        CoreOperationCompare op = new CoreOperationCompare(lit, rit);
        EvalContext context = new EvalContext(InitialContext.getInstance(), null, null);
        assertFalse(op.evaluate(context));
    }

    @Test
    public void testComparePointerValueTrue() {
        Pointer left = new Pointer("id('root')/node()[1]");
        Pointer right = new Pointer("id('root')/node()[1]");
        CoreOperationCompare op = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance(), null, null);
        assertTrue(op.evaluate(context));
    }

    @Test
    public void testComparePointerValueFalse() {
        Pointer left = new Pointer("id('root')/node()[1]");
        Pointer right = new Pointer("id('root')/node()[2]");
        CoreOperationCompare op = new CoreOperationCompare(left, right);
        EvalContext context = new EvalContext(InitialContext.getInstance(), null, null);
        assertFalse(op.evaluate(context));
    }

}