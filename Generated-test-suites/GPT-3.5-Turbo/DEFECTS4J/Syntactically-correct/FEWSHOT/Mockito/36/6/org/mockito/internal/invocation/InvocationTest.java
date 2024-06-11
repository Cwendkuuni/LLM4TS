package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.MatchersPrinter;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;

public class InvocationTest {

    @Test
    public void testInvocationConstructor() {
        Object mockObject = mock(Object.class);
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] args = new Object[] { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mockObject, mockitoMethod, args, sequenceNumber, realMethod);

        assertSame(mockObject, invocation.getMock());
        assertSame(mockitoMethod, invocation.getMethod());
        assertSame(args, invocation.getArguments());
        assertFalse(invocation.isVerified());
        assertEquals(sequenceNumber, invocation.getSequenceNumber().intValue());
        assertFalse(invocation.isVerifiedInOrder());
        assertSame(realMethod, invocation.realMethod);
    }

    @Test
    public void testEquals() {
        Object mockObject = mock(Object.class);
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] args1 = new Object[] { "arg1", "arg2" };
        Object[] args2 = new Object[] { "arg3", "arg4" };
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation1 = new Invocation(mockObject, mockitoMethod, args1, 1, realMethod);
        Invocation invocation2 = new Invocation(mockObject, mockitoMethod, args1, 1, realMethod);
        Invocation invocation3 = new Invocation(mockObject, mockitoMethod, args2, 1, realMethod);
        Invocation invocation4 = new Invocation(mockObject, mockitoMethod, args1, 2, realMethod);
        Invocation invocation5 = new Invocation(mockObject, mockitoMethod, args1, 1, mock(RealMethod.class));

        assertTrue(invocation1.equals(invocation2));
        assertFalse(invocation1.equals(invocation3));
        assertFalse(invocation1.equals(invocation4));
        assertFalse(invocation1.equals(invocation5));
    }

    @Test
    public void testHashCode() {
        Invocation invocation = new Invocation(null, null, null, 0, null);

        try {
            invocation.hashCode();
        } catch (RuntimeException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testToString() {
        Object mockObject = mock(Object.class);
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] args = new Object[] { "arg1", "arg2" };
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mockObject, mockitoMethod, args, 1, realMethod);

        String toStringResult = invocation.toString();

        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains(mockitoMethod.getName()));
        assertTrue(toStringResult.contains(MockUtil.getMockName(mockObject)));
        assertTrue(toStringResult.contains(MatchersPrinter.ARGUMENTS_OPEN));
        assertTrue(toStringResult.contains(MatchersPrinter.ARGUMENTS_CLOSE));
        assertTrue(toStringResult.contains("arg1"));
        assertTrue(toStringResult.contains("arg2"));
    }

    @Test
    public void testArgumentsToMatchers() {
        Object[] args = new Object[] { "arg1", new int[] { 1, 2, 3 }, 4 };
        Invocation invocation = new Invocation(null, null, args, 1, null);

        List<Matcher> matchers = invocation.argumentsToMatchers();

        assertNotNull(matchers);
        assertEquals(3, matchers.size());
        assertSame(Equals.class, matchers.get(0).getClass());
        assertSame(ArrayEquals.class, matchers.get(1).getClass());
        assertSame(Equals.class, matchers.get(2).getClass());
    }

    @Test
    public void testIsToString() {
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);

        InvocationOnMock invocation1 = mock(InvocationOnMock.class);
        MockitoMethod mockitoMethod1 = mock(MockitoMethod.class);
        MockitoMethod mockitoMethod2 = mock(MockitoMethod.class);
        MockitoMethod mockitoMethod3 = mock(MockitoMethod.class);
        MockitoMethod mockitoMethod4 = mock(MockitoMethod.class);
        MockitoMethod mockitoMethod5 = mock(MockitoMethod.class);

        MockitoMethod[] methods = { mockitoMethod1, mockitoMethod2, mockitoMethod3, mockitoMethod4, mockitoMethod5 };
        boolean[] expectedResults = { true, false, false, false, false };

        for (int i = 0; i < methods.length; i++) {
            Invocation invocation = new Invocation(null, methods[i], null, 0, null);

            assertEquals(expectedResults[i], Invocation.isToString(invocation));
        }
    }

    @Test(expected = RuntimeException.class)
    public void testHashCodeRuntimeException() {
        Invocation invocation = new Invocation(null, null, null, 0, null);

        invocation.hashCode();
    }

    @Test
    public void testToStringPrintSettings() {
        Object mockObject = mock(Object.class);
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] args = new Object[] { "arg1", "arg2" };
        RealMethod realMethod = mock(RealMethod.class);
        PrintSettings printSettings = new PrintSettings().multiline(true);

        Invocation invocation = new Invocation(mockObject, mockitoMethod, args, 1, realMethod);

        String toStringResult = invocation.toString(printSettings);

        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains(mockitoMethod.getName()));
        assertTrue(toStringResult.contains(MockUtil.getMockName(mockObject)));
        assertTrue(toStringResult.contains(MatchersPrinter.ARGUMENTS_OPEN));
        assertTrue(toStringResult.contains(MatchersPrinter.ARGUMENTS_CLOSE));
        assertTrue(toStringResult.contains("arg1"));
        assertTrue(toStringResult.contains("arg2"));
    }

    @Test
    public void testMarkVerified() {
        Invocation invocation = new Invocation(null, null, null, 0, null);

        assertFalse(invocation.isVerified());

        invocation.markVerified();

        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Invocation invocation = new Invocation(null, null, null, 0, null);

        assertFalse(invocation.isVerified());
        assertFalse(invocation.isVerifiedInOrder());

        invocation.markVerifiedInOrder();

        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }

}