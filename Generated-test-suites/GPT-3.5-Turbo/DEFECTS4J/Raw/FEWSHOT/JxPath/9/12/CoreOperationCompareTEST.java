package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {
    private CoreOperationCompare coreOperationCompare;
    private Expression arg1;
    private Expression arg2;

    @Before
    public void setUp() {
        arg1 = new Expression();
        arg2 = new Expression();
        coreOperationCompare = new CoreOperationCompare(arg1, arg2);
    }

    @Test
    public void testGetPrecedence() {
        assertEquals(2, coreOperationCompare.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        assertTrue(coreOperationCompare.isSymmetric());
    }

    @Test
    public void testEqualWithInitialContext() {
        EvalContext context = new InitialContext();
        Expression left = new Expression();
        Expression right = new Expression();
        Object result = coreOperationCompare.equal(context, left, right);
        assertFalse((boolean) result);
    }

    @Test
    public void testEqualWithSelfContext() {
        EvalContext context = new SelfContext();
        Expression left = new Expression();
        Expression right = new Expression();
        Object result = coreOperationCompare.equal(context, left, right);
        assertFalse((boolean) result);
    }

    @Test
    public void testEqualWithCollection() {
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Pointer());
        Collection<Object> collection1 = new HashSet<>();
        collection1.add("one");
        collection1.add("two");
        collection1.add("three");
        Collection<Object> collection2 = new HashSet<>();
        collection2.add("four");
        collection2.add("five");
        collection2.add("six");
        Iterator<Object> iterator1 = collection1.iterator();
        Iterator<Object> iterator2 = collection2.iterator();
        Expression left = new Expression();
        Expression right = new Expression();
        Object result1 = coreOperationCompare.equal(context, left, iterator1);
        assertFalse((boolean) result1);
        Object result2 = coreOperationCompare.equal(context, iterator1, right);
        assertFalse((boolean) result2);
        Object result3 = coreOperationCompare.equal(context, iterator1, iterator2);
        assertFalse((boolean) result3);
    }

    @Test
    public void testEqualWithoutCollection() {
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Pointer());
        Expression left = new Expression();
        Expression right = new Expression();

        Object result1 = coreOperationCompare.equal(context, left, "one");
        assertFalse((boolean) result1);

        Object result2 = coreOperationCompare.equal(context, "one", right);
        assertFalse((boolean) result2);

        Object result3 = coreOperationCompare.equal(context, "one", "two");
        assertFalse((boolean) result3);
    }

    @Test
    public void testContains() {
        Iterator<Object> iterator = new HashMap<Object, Object>().values().iterator();
        Object value = "value";
        boolean result = coreOperationCompare.contains(iterator, value);
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Iterator<Object> leftIterator = new HashSet<Object>().iterator();
        Iterator<Object> rightIterator = new HashSet<Object>().iterator();

        boolean result = coreOperationCompare.findMatch(leftIterator, rightIterator);
        assertFalse(result);
    }

    @Test
    public void testEqualWithPointers() {
        Pointer pointer1 = new Pointer();
        Pointer pointer2 = new Pointer();

        boolean result1 = coreOperationCompare.equal(pointer1, pointer2);
        assertFalse(result1);

        boolean result2 = coreOperationCompare.equal(pointer1, "value");
        assertFalse(result2);

        boolean result3 = coreOperationCompare.equal("value", pointer2);
        assertFalse(result3);
    }

    @Test
    public void testEqualWithBoolean() {
        boolean result = coreOperationCompare.equal(true, false);
        assertFalse(result);
    }

    @Test
    public void testEqualWithNumber() {
        boolean result = coreOperationCompare.equal(1, 2);
        assertFalse(result);
    }

    @Test
    public void testEqualWithString() {
        boolean result = coreOperationCompare.equal("one", "two");
        assertFalse(result);
    }
}