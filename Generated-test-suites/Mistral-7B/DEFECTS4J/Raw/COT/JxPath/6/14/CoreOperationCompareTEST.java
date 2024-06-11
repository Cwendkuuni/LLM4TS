package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationCompareTest {

    private CoreOperationCompare coreOperationCompare;
    private Expression arg1;
    private Expression arg2;
    private EvalContext context;

    @Before
    public void setup() {
        arg1 = Mockito.mock(Expression.class);
        arg2 = Mockito.mock(Expression.class);
        context = Mockito.spy(new EvalContext() {
            @Override
            public Pointer getSingleNodePointer() {
                return Mockito.mock(Pointer.class);
            }
        });

        coreOperationCompare = new CoreOperationCompare(arg1, arg2);
    }

    @Test
    public void testEqual() {
        Object l = "test";
        Object r = "test";

        Mockito.when(arg1.compute(context)).thenReturn(l);
        Mockito.when(arg2.compute(context)).thenReturn(r);

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithNulls() {
        Mockito.when(arg1.compute(context)).thenReturn(null);
        Mockito.when(arg2.compute(context)).thenReturn(null);

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithDifferentTypes() {
        Object l = "test";
        Object r = 123;

        Mockito.when(arg1.compute(context)).thenReturn(l);
        Mockito.when(arg2.compute(context)).thenReturn(r);

        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualWithIterators() {
        Set<Object> set = new HashSet<>();
        set.add("test1");
        set.add("test2");

        Iterator<Object> iterator = set.iterator();

        Mockito.when(arg1.compute(context)).thenReturn(iterator);
        Mockito.when(arg2.compute(context)).thenReturn("test1");

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testContains() {
        List<Object> list = Collections.singletonList("test1");
        Iterator<Object> iterator = list.iterator();

        Mockito.when(arg1.compute(context)).thenReturn(iterator);
        Mockito.when(arg2.compute(context)).thenReturn("test1");

        assertTrue(coreOperationCompare.contains(iterator, arg2));
    }

    @Test
    public void testFindMatch() {
        Set<Object> left = new HashSet<>();
        left.add("test1");
        left.add("test2");

        Set<Object> right = new HashSet<>();
        right.add("test1");
        right.add("test3");

        Iterator<Object> leftIterator = left.iterator();
        Iterator<Object> rightIterator = right.iterator();

        Mockito.when(arg1.compute(context)).thenReturn(left);
        Mockito.when(arg2.compute(context)).thenReturn(right);

        assertTrue(coreOperationCompare.findMatch(leftIterator, rightIterator));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFindMatchWithNulls() {
        Set<Object> left = new HashSet<>();
        left.add("test1");

        Iterator<Object> leftIterator = left.iterator();
        Mockito.when(arg1.compute(context)).thenReturn(leftIterator);

        Iterator<Object> right = null;
        Mockito.when(arg2.compute(context)).thenReturn(right);

        coreOperationCompare.findMatch(leftIterator, right);
    }
}