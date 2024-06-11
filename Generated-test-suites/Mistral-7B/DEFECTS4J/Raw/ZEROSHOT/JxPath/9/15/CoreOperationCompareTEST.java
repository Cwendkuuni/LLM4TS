package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare.Expression;
import org.junit.Test;

/**
 * Test case for CoreOperationCompare class
 */
public class CoreOperationCompareTest {

    @Test
    public void testCompareEqual() {
        Expression expr1 = new Expression(new Const("1"));
        Expression expr2 = new Expression(new Const("1"));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(expr1, expr2);
        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testCompareUnequal() {
        Expression expr1 = new Expression(new Const("1"));
        Expression expr2 = new Expression(new Const("2"));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(expr1, expr2);
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testCompareEqualBoolean() {
        Expression expr1 = new Expression(new Const(true));
        Expression expr2 = new Expression(new Const(true));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(expr1, expr2);
        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testCompareUnequalBoolean() {
        Expression expr1 = new Expression(new Const(true));
        Expression expr2 = new Expression(new Const(false));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(expr1, expr2);
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testCompareEqualNumber() {
        Expression expr1 = new Expression(new Const(1.0));
        Expression expr2 = new Expression(new Const(1.0));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(expr1, expr2);
        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testCompareUnequalNumber() {
        Expression expr1 = new Expression(new Const(1.0));
        Expression expr2 = new Expression(new Const(2.0));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(expr1, expr2);
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testCompareEqualString() {
        Expression expr1 = new Expression(new Const("one"));
        Expression expr2 = new Expression(new Const("one"));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(expr1, expr2);
        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testCompareUnequalString() {
        Expression expr1 = new Expression(new Const("one"));
        Expression expr2 = new Expression(new Const("two"));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(expr1, expr2);
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testCompareEqualPointer() {
        Expression expr1 = new Expression(new Const(new Pointer(1)));
        Expression expr2 = new Expression(new Const(new Pointer(1)));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(expr1, expr2);
        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testCompareUnequalPointer() {
        Expression expr1 = new Expression(new Const(new Pointer(1)));
        Expression expr2 = new Expression(new Const(new Pointer(2)));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(expr1, expr2);
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testCompareEqualIterators() {
        List list = Arrays.asList(1, 2, 3);
        Expression expr1 = new Expression(new IteratorExpression(list.iterator()));
        List list2 = Arrays.asList(1, 2, 3);
        Expression expr2 = new Expression(new IteratorExpression(list2.iterator()));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(expr1, expr2);
        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testCompareUnequalIterators() {
        List list = Arrays.asList(1, 2, 3);
        Expression expr1 = new Expression(new IteratorExpression(list.iterator()));
        List list2 = Collections.singletonList(4);
        Expression expr2 = new Expression(new IteratorExpression(list2.iterator()));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(expr1, expr2);
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testCompareEqualIteratorAndConstant() {
        List list = Arrays.asList(1, 2, 3);
        Expression expr1 = new Expression(new IteratorExpression(list.iterator()));
        Expression expr2 = new Expression(new Const(3));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(expr1, expr2);
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testCompareEqualConstantAndIterator() {
        List list = Arrays.asList(1, 2, 3);
        Expression expr1 = new Expression(new Const(3));
        Expression expr2 = new Expression(new IteratorExpression(list.iterator()));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(expr1, expr2);
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testCompareEqualCollectionAndCollection() {
        List list1 = Arrays.asList(1, 2, 3);
        Expression expr1 = new Expression(new IteratorExpression(list1.iterator()));
        List list2 = Arrays.asList(1, 2, 3);
        Expression expr2 = new Expression(new IteratorExpression(list2.iterator()));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(expr1, expr2);
        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testCompareUnequalCollectionAndCollection() {
        List list1 = Arrays.asList(1, 2, 3);
        Expression expr1 = new Expression(new IteratorExpression(list1.iterator()));
        List list2 = Arrays.asList(1, 2, 4);
        Expression expr2 = new Expression(new IteratorExpression(list2.iterator()));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(expr1, expr2);
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testCompareEqualSelfContext() {
        Expression expr1 = new Expression(new SelfContext());
        Expression expr2 = new Expression(new SelfContext());
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(expr1, expr2);
        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testCompareUnequalSelfContext() {
        Expression expr1 = new Expression(new SelfContext());
        Expression expr2 = new Expression(new InitialContext());
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(expr1, expr2);
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.evaluate(context));
    }

}