/*
* The generated test cases will cover all methods in the CoreOperationCompare class.
* To ensure the tests are comprehensive, they will cover different scenarios and edge cases.
*/

package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationCompareTest {
    private CoreOperationCompare coreOperationCompare;
    private Expression arg1;
    private Expression arg2;
    private EvalContext evalContext;
    private Object left;
    private Object right;

    @Before
    public void setUp() {
        arg1 = mock(Expression.class);
        arg2 = mock(Expression.class);
        coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        evalContext = mock(EvalContext.class);
        left = new Object();
        right = new Object();
    }

    @Test
    public void testCoreOperationCompare() {
        Expression[] arguments = { arg1, arg2 };
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        assertArrayEquals(arguments, compare.getArguments());
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
    public void testEqualWithContextLeftAndRightAreIterators() {
        Iterator lit = mock(Iterator.class);
        Iterator rit = mock(Iterator.class);

        when(arg1.compute(evalContext)).thenReturn(lit);
        when(arg2.compute(evalContext)).thenReturn(rit);
        assertFalse(coreOperationCompare.equal(evalContext, arg1, arg2));

        verify(arg1).compute(evalContext);
        verify(arg2).compute(evalContext);
    }

    @Test
    public void testEqualWithContextLeftIsIterator() {
        Iterator lit = mock(Iterator.class);

        when(arg1.compute(evalContext)).thenReturn(lit);
        when(arg2.compute(evalContext)).thenReturn(right);
        assertFalse(coreOperationCompare.equal(evalContext, arg1, arg2));

        verify(arg1).compute(evalContext);
        verify(arg2).compute(evalContext);
    }

    @Test
    public void testEqualWithContextRightIsIterator() {
        Iterator rit = mock(Iterator.class);

        when(arg1.compute(evalContext)).thenReturn(left);
        when(arg2.compute(evalContext)).thenReturn(rit);
        assertFalse(coreOperationCompare.equal(evalContext, arg1, arg2));

        verify(arg1).compute(evalContext);
        verify(arg2).compute(evalContext);
    }

    @Test
    public void testEqualWithContextBothAreValues() {
        when(arg1.compute(evalContext)).thenReturn(left);
        when(arg2.compute(evalContext)).thenReturn(right);

        assertFalse(coreOperationCompare.equal(evalContext, arg1, arg2));

        verify(arg1).compute(evalContext);
        verify(arg2).compute(evalContext);
    }

    @Test
    public void testEqualWithPointers() {
        Pointer pointer1 = mock(Pointer.class);
        Pointer pointer2 = mock(Pointer.class);
        when(arg1.compute(evalContext)).thenReturn(pointer1);
        when(arg2.compute(evalContext)).thenReturn(pointer2);
        when(pointer1.equals(pointer2)).thenReturn(true);

        assertTrue(coreOperationCompare.equal(evalContext, arg1, arg2));

        verify(arg1).compute(evalContext);
        verify(arg2).compute(evalContext);
        verify(pointer1).equals(pointer2);
    }

    @Test
    public void testEqualWithPointersOneIsValue() {
        Pointer pointer1 = mock(Pointer.class);
        when(arg1.compute(evalContext)).thenReturn(pointer1);
        when(arg2.compute(evalContext)).thenReturn(right);
        when(pointer1.getValue()).thenReturn(left);

        assertFalse(coreOperationCompare.equal(evalContext, arg1, arg2));

        verify(arg1).compute(evalContext);
        verify(arg2).compute(evalContext);
        verify(pointer1).getValue();
    }

    @Test
    public void testEqualWithBooleanValues() {
        when(arg1.compute(evalContext)).thenReturn(true);
        when(arg2.compute(evalContext)).thenReturn(false);

        assertFalse(coreOperationCompare.equal(evalContext, arg1, arg2));

        verify(arg1).compute(evalContext);
        verify(arg2).compute(evalContext);
    }

    @Test
    public void testEqualWithNumberValues() {
        when(arg1.compute(evalContext)).thenReturn(1);
        when(arg2.compute(evalContext)).thenReturn(2);

        assertFalse(coreOperationCompare.equal(evalContext, arg1, arg2));

        verify(arg1).compute(evalContext);
        verify(arg2).compute(evalContext);
    }

    @Test
    public void testEqualWithStringValues() {
        when(arg1.compute(evalContext)).thenReturn("foo");
        when(arg2.compute(evalContext)).thenReturn("bar");

        assertFalse(coreOperationCompare.equal(evalContext, arg1, arg2));

        verify(arg1).compute(evalContext);
        verify(arg2).compute(evalContext);
    }

    @Test
    public void testEqualWithBothNullValues() {
        when(arg1.compute(evalContext)).thenReturn(null);
        when(arg2.compute(evalContext)).thenReturn(null);

        assertTrue(coreOperationCompare.equal(evalContext, arg1, arg2));

        verify(arg1).compute(evalContext);
        verify(arg2).compute(evalContext);
    }
}