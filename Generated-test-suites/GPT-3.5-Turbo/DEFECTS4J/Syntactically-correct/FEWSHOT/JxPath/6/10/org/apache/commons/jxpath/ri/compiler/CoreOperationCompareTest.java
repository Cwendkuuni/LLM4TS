package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationCompareTest {
    private CoreOperationCompare operation;

    @Before
    public void setUp() {
        Expression arg1 = new Constant(10);
        Expression arg2 = new Constant(20);
        operation = new CoreOperationCompare(arg1, arg2) {
            @Override
            protected boolean equal(
                    EvalContext context, 
                    Expression left, 
                    Expression right) {
                return false;
            }
        };
    }

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext();
        Expression left = new Constant(10);
        Expression right = new Constant(10);
        assertTrue(operation.equal(context, left, right));
    }

    @Test
    public void testEqualWithInitialContext() {
        EvalContext context = new InitialContext();
        Expression left = new InitialContext();
        Expression right = new Constant(10);
        assertFalse(operation.equal(context, left, right));
    }

    @Test
    public void testEqualWithSelfContext() {
        EvalContext context = new SelfContext();
        Expression left = new SelfContext();
        Expression right = new Constant(10);
        assertFalse(operation.equal(context, left, right));
    }

    @Test
    public void testEqualWithCollection() {
        List<Integer> list = new ArrayList<>(Arrays.asList(10, 20, 30));
        EvalContext context = new EvalContext(list);

        Expression left = new Constant(list);
        Expression right = new Constant(20);
        assertTrue(operation.equal(context, left, right));
    }

    @Test
    public void testEqualWithIterator() {
        List<Integer> list = new ArrayList<>(Arrays.asList(10, 20, 30));
        EvalContext context = new EvalContext(list.iterator());

        Expression left = new Constant(list.iterator());
        Expression right = new Constant(20);
        assertTrue(operation.equal(context, left, right));
    }

    @Test
    public void testEqualWithDifferentIterators() {
        List<Integer> list1 = new ArrayList<>(Arrays.asList(10, 20, 30));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(15, 25, 35));

        EvalContext context = new EvalContext(list1.iterator());

        Expression left = new Constant(list1.iterator());
        Expression right = new Constant(list2.iterator());
        assertFalse(operation.equal(context, left, right));
    }

    @Test
    public void testEqualWithPointer() {
        Pointer pointer1 = new Pointer("foo");
        Pointer pointer2 = new Pointer("foo");

        EvalContext context = new EvalContext(pointer1);

        Expression left = new Constant(pointer1);
        Expression right = new Constant(pointer2);
        assertTrue(operation.equal(context, left, right));
    }

    @Test
    public void testEqualWithDifferentPointer() {
        Pointer pointer1 = new Pointer("foo");
        Pointer pointer2 = new Pointer("bar");

        EvalContext context = new EvalContext(pointer1);

        Expression left = new Constant(pointer1);
        Expression right = new Constant(pointer2);
        assertFalse(operation.equal(context, left, right));
    }

    @Test
    public void testEqualWithBoolean() {
        EvalContext context = new EvalContext();
        Expression left = new Constant(true);
        Expression right = new Constant(false);
        assertFalse(operation.equal(context, left, right));
    }

    @Test
    public void testEqualWithNumber() {
        EvalContext context = new EvalContext();
        Expression left = new Constant(10);
        Expression right = new Constant(20);
        assertFalse(operation.equal(context, left, right));
    }

    @Test
    public void testEqualWithString() {
        EvalContext context = new EvalContext();
        Expression left = new Constant("foo");
        Expression right = new Constant("bar");
        assertFalse(operation.equal(context, left, right));
    }

    @Test
    public void testEqualWithNull() {
        EvalContext context = new EvalContext();
        Expression left = new Constant(null);
        Expression right = new Constant(null);
        assertTrue(operation.equal(context, left, right));
    }
}