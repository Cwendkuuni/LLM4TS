package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare.CoreOperationCompareType;

public class CoreOperationCompareTest {

    private CoreOperationCompare testee;

    @Before
    public void setup() {
        Expression arg1 = new FakeExpression("arg1");
        Expression arg2 = new FakeExpression("arg2");
        testee = new CoreOperationCompare(arg1, arg2);
    }

    @Test
    public void testEqual() {
        Object value1 = new Integer(5);
        Object value2 = new Integer(5);
        EvalContext context = new EvalContext(new InitialContext());

        assertTrue(testee.equal(context, testee.getArguments()[0], testee.getArguments()[1]) == value1.equals(value2));
    }

    @Test
    public void testEqual_nullValue() {
        EvalContext context = new EvalContext(new InitialContext());

        assertFalse(testee.equal(context, testee.getArguments()[0], null));
        assertFalse(testee.equal(context, null, testee.getArguments()[1]));
        assertFalse(testee.equal(context, null, null));
    }

    @Test
    public void testEqual_differentTypes() {
        Object value1 = new Integer(5);
        Object value2 = new String("5");
        EvalContext context = new EvalContext(new InitialContext());

        assertFalse(testee.equal(context, testee.getArguments()[0], testee.getArguments()[1]));
    }

    @Test
    public void testEqual_differentObjects() {
        Object value1 = new Integer(5);
        Object value2 = new Integer(6);
        EvalContext context = new EvalContext(new InitialContext());

        assertFalse(testee.equal(context, testee.getArguments()[0], testee.getArguments()[1]));
    }

    @Test
    public void testEqual_iterators() {
        Collection collection1 = Arrays.asList(1, 2, 3);
        Collection collection2 = Arrays.asList(1, 2, 3);
        EvalContext context = new EvalContext(new InitialContext());

        Iterator iterator1 = collection1.iterator();
        Iterator iterator2 = collection2.iterator();

        assertTrue(testee.equal(context, testee.getArguments()[0], testee.getArguments()[1]) == testee.contains(iterator1, iterator2.next()) && iterator2.hasNext());
    }

    @Test
    public void testEqual_iterators_reverse() {
        Collection collection1 = Arrays.asList(1, 2, 3);
        Collection collection2 = Arrays.asList(1, 2, 3);
        EvalContext context = new EvalContext(new InitialContext());

        Iterator iterator1 = collection1.iterator();
        Iterator iterator2 = collection2.iterator();

        while (iterator2.hasNext()) {
            Object value = iterator2.next();
            assertTrue(testee.equal(context, testee.getArguments()[1], testee.getArguments()[0]) == testee.contains(iterator2, iterator1.next()) && iterator1.hasNext());
        }

        assertFalse(testee.equal(context, testee.getArguments()[1], testee.getArguments()[0]));
    }

    @Test(expected = AssertionError.class)
    public void testEqual_iterators_differentSize() {
        Collection collection1 = Arrays.asList(1, 2, 3);
        Collection collection2 = Arrays.asList(1, 2, 3, 4);
        EvalContext context = new EvalContext(new InitialContext());

        Iterator iterator1 = collection1.iterator();
        Iterator iterator2 = collection2.iterator();

        testee.equal(context, testee.getArguments()[0], testee.getArguments()[1]);
    }

    @Test
    public void testContains() {
        List list = new ArrayList();
        list.add(new Integer(5));
        list.add(new Integer(6));
        EvalContext context = new EvalContext(new InitialContext());

        Iterator iterator = list.iterator();

        assertTrue(testee.contains(iterator, new Integer(5)));
    }

    @Test
    public void testContains_emptyIterator() {
        List list = new ArrayList();
        EvalContext context = new EvalContext(new InitialContext());

        Iterator iterator = list.iterator();

        assertFalse(testee.contains(iterator, new Integer(5)));
    }

    @Test
    public void testFindMatch() {
        List list1 = Arrays.asList(1, 2, 3);
        List list2 = Arrays.asList(1, 2, 3);
        EvalContext context = new EvalContext(new InitialContext());

        Iterator iterator1 = list1.iterator();
        Iterator iterator2 = list2.iterator();

        assertTrue(testee.findMatch(iterator1, iterator2));
    }

    @Test
    public void testFindMatch_emptyIterator1() {
        List list1 = new ArrayList();
        List list2 = Arrays.asList(1, 2, 3);
        EvalContext context = new EvalContext(new InitialContext());

        Iterator iterator1 = list1.iterator();
        Iterator iterator2 = list2.iterator();

        assertFalse(testee.findMatch(iterator1, iterator2));
    }

    @Test
    public void testFindMatch_emptyIterator2() {
        List list1 = Arrays.asList(1, 2, 3);
        List list2 = new ArrayList();
        EvalContext context = new EvalContext(new InitialContext());

        Iterator iterator1 = list1.iterator();
        Iterator iterator2 = list2.iterator();

        assertFalse(testee.findMatch(iterator1, iterator2));
    }

    @Test
    public void testFindMatch_differentSizes() {
        List list1 = Arrays.asList(1, 2, 3);
        List list2 = Arrays.asList(1, 2, 3, 4, 5);
        EvalContext context = new EvalContext(new InitialContext());

        Iterator iterator1 = list1.iterator();
        Iterator iterator2 = list2.iterator();

        assertFalse(testee.findMatch(iterator1, iterator2));
    }

    @Test
    public void testType() {
        assertEquals(CoreOperationCompareType.EQUAL, testee.getType());
    }
}

class FakeExpression implements Expression {

    private String name;

    public FakeExpression(String name) {
        this.name = name;
    }

    @Override
    public Object compute(EvalContext context) {
        return new Object();
    }

    @Override
    public String toString() {
        return name;
    }
}