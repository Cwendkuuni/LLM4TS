package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
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
        Expression left = new LiteralExpression("1");
        Expression right = new LiteralExpression("1");
        CoreOperationCompare coreOperationCompare = new EqualExpression(left, right);
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{});
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testNotEqual() {
        Expression left = new LiteralExpression("1");
        Expression right = new LiteralExpression("2");
        CoreOperationCompare coreOperationCompare = new NotEqualExpression(left, right);
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{});
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqualWithInitialContexts() {
        Expression left = new InitialContext();
        Expression right = new InitialContext();
        CoreOperationCompare coreOperationCompare = new EqualExpression(left, right);
        EvalContext context = new EvalContext(left, new Object[]{});;
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqualWithSelfContexts() {
        Expression left = new SelfContext();
        Expression right = new SelfContext();
        CoreOperationCompare coreOperationCompare = new EqualExpression(left, right);
        EvalContext context = new EvalContext(left, new Object[]{"self"});
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqualWithLiteralAndString() {
        Expression left = new LiteralExpression("1");
        Expression right = new LiteralExpression("1");
        CoreOperationCompare coreOperationCompare = new EqualExpression(left, right);
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{});
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqualWithIteratorAndString() {
        Expression left = new LiteralExpression(Arrays.asList("1", "2", "3"));
        Expression right = new LiteralExpression("1");
        CoreOperationCompare coreOperationCompare = new EqualExpression(left, right);
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{});
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqualWithCollectionAndCollection() {
        Expression left = new LiteralExpression(Arrays.asList("1", "2", "3"));
        Expression right = new LiteralExpression(Arrays.asList("1", "2", "3"));
        CoreOperationCompare coreOperationCompare = new EqualExpression(left, right);
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{});
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqualWithPointerAndPointer() {
        Expression left = new LiteralExpression(Pointer.newInstance(1));
        Expression right = new LiteralExpression(Pointer.newInstance(1));
        CoreOperationCompare coreOperationCompare = new EqualExpression(left, right);
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{});
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testNotEqualWithPointerAndPointer() {
        Expression left = new LiteralExpression(Pointer.newInstance(1));
        Expression right = new LiteralExpression(Pointer.newInstance(2));
        CoreOperationCompare coreOperationCompare = new NotEqualExpression(left, right);
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{});
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testContainsStringIterator() {
        Expression stringList = new LiteralExpression(Arrays.asList("apple", "banana", "orange"));
        Expression stringToFind = new LiteralExpression("banana");
        CoreOperationCompare coreOperationCompare = new ContainsExpression(stringList, stringToFind);
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{});
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testContainsIteratorString() {
        Expression stringList = new LiteralExpression(Arrays.asList("apple", "banana", "orange"));
        Expression stringToFind = new LiteralExpression("banana");
        CoreOperationCompare coreOperationCompare = new ContainsExpression(stringToFind, stringList);
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{});
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testFindMatchIteratorIterator() {
        Expression list1 = new LiteralExpression(Arrays.asList(1, 2, 3, 4));
        Expression list2 = new LiteralExpression(Arrays.asList(4, 5, 6, 1));
        CoreOperationCompare coreOperationCompare = new FindMatchExpression(list1.iterator(), list2.iterator());
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{});
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testFindMatchIteratorIteratorFalse() {
        Expression list1 = new LiteralExpression(Arrays.asList(1, 2, 3, 4));
        Expression list2 = new LiteralExpression(Arrays.asList(5, 6, 7, 8));
        CoreOperationCompare coreOperationCompare = new FindMatchExpression(list1.iterator(), list2.iterator());
        EvalContext context = new EvalContext(new InitialContext(), new Object[]{});
        assertFalse(coreOperationCompare.evaluate(context));
    }
}