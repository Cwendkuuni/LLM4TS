package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
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
    private Expression arg1;
    private Expression arg2;
    private EvalContext context;

    @Before
    public void setUp() {
        arg1 = mock(Expression.class);
        arg2 = mock(Expression.class);
        operation = new CoreOperationCompare(arg1, arg2) {};
        context = mock(EvalContext.class);
    }

    @Test
    public void testEqual_NullArguments_ReturnsTrue() {
        when(arg1.compute(context)).thenReturn(null);
        when(arg2.compute(context)).thenReturn(null);

        assertTrue(operation.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_TwoEqualStrings_ReturnsTrue() {
        when(arg1.compute(context)).thenReturn("foo");
        when(arg2.compute(context)).thenReturn("foo");

        assertTrue(operation.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_TwoDifferentStrings_ReturnsFalse() {
        when(arg1.compute(context)).thenReturn("foo");
        when(arg2.compute(context)).thenReturn("bar");

        assertFalse(operation.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_TwoEqualBooleans_ReturnsTrue() {
        when(arg1.compute(context)).thenReturn(Boolean.TRUE);
        when(arg2.compute(context)).thenReturn(Boolean.TRUE);

        assertTrue(operation.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_TwoDifferentBooleans_ReturnsFalse() {
        when(arg1.compute(context)).thenReturn(Boolean.TRUE);
        when(arg2.compute(context)).thenReturn(Boolean.FALSE);

        assertFalse(operation.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_TwoEqualNumbers_ReturnsTrue() {
        when(arg1.compute(context)).thenReturn(10);
        when(arg2.compute(context)).thenReturn(10);

        assertTrue(operation.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_TwoDifferentNumbers_ReturnsFalse() {
        when(arg1.compute(context)).thenReturn(10);
        when(arg2.compute(context)).thenReturn(20);

        assertFalse(operation.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_LeftArgumentIsInitialContext_ReturnsTrue() {
        EvalContext evalContext = mock(EvalContext.class);
        InitialContext initialContext = mock(InitialContext.class);
        when(arg1.compute(context)).thenReturn(initialContext);
        when(initialContext.getSingleNodePointer()).thenReturn("foo");
        when(arg2.compute(context)).thenReturn("foo");

        assertTrue(operation.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_RightArgumentIsInitialContext_ReturnsTrue() {
        EvalContext evalContext = mock(EvalContext.class);
        InitialContext initialContext = mock(InitialContext.class);
        when(arg1.compute(context)).thenReturn("foo");
        when(arg2.compute(context)).thenReturn(initialContext);
        when(initialContext.getSingleNodePointer()).thenReturn("foo");

        assertTrue(operation.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_LeftArgumentIsSelfContext_ReturnsTrue() {
        EvalContext evalContext = mock(EvalContext.class);
        SelfContext selfContext = mock(SelfContext.class);
        when(arg1.compute(context)).thenReturn(selfContext);
        when(selfContext.getSingleNodePointer()).thenReturn("foo");
        when(arg2.compute(context)).thenReturn("foo");

        assertTrue(operation.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_RightArgumentIsSelfContext_ReturnsTrue() {
        EvalContext evalContext = mock(EvalContext.class);
        SelfContext selfContext = mock(SelfContext.class);
        when(arg1.compute(context)).thenReturn("foo");
        when(arg2.compute(context)).thenReturn(selfContext);
        when(selfContext.getSingleNodePointer()).thenReturn("foo");

        assertTrue(operation.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_LeftArgumentIsCollectionAndContainsRightArgument_ReturnsTrue() {
        List<String> collection = new ArrayList<String>();
        collection.add("foo");
        collection.add("bar");
        Iterator<String> iterator = collection.iterator();

        when(arg1.compute(context)).thenReturn(iterator);
        when(arg2.compute(context)).thenReturn("bar");

        assertTrue(operation.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_LeftArgumentIsCollectionAndDoesNotContainRightArgument_ReturnsFalse() {
        List<String> collection = new ArrayList<String>();
        collection.add("foo");
        Iterator<String> iterator = collection.iterator();

        when(arg1.compute(context)).thenReturn(iterator);
        when(arg2.compute(context)).thenReturn("bar");

        assertFalse(operation.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_RightArgumentIsCollectionAndContainsLeftArgument_ReturnsTrue() {
        List<String> collection = new ArrayList<String>();
        collection.add("foo");
        collection.add("bar");
        Iterator<String> iterator = collection.iterator();

        when(arg1.compute(context)).thenReturn("bar");
        when(arg2.compute(context)).thenReturn(iterator);

        assertTrue(operation.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_RightArgumentIsCollectionAndDoesNotContainLeftArgument_ReturnsFalse() {
        List<String> collection = new ArrayList<String>();
        collection.add("foo");
        Iterator<String> iterator = collection.iterator();

        when(arg1.compute(context)).thenReturn("bar");
        when(arg2.compute(context)).thenReturn(iterator);

        assertFalse(operation.equal(context, arg1, arg2));
    }
}