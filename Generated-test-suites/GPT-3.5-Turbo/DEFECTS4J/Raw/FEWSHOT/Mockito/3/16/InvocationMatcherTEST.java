package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {
    private InvocationMatcher invocationMatcher;
    private Invocation invocation;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testConstructor() {
        List<Matcher> matchers = Collections.singletonList(Mockito.mock(Matcher.class));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertSame(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        PrintSettings printSettings = mock(PrintSettings.class);
        whenNew(PrintSettings.class).withNoArguments().thenReturn(printSettings);
        String expected = "testToString";
        when(printSettings.print(anyList(), any(Invocation.class))).thenReturn(expected);
        assertEquals(expected, invocationMatcher.toString());
        verifyNew(PrintSettings.class).withNoArguments();
        verify(printSettings).print(anyList(), any(Invocation.class));
    }

    @Test
    public void testMatches_sameMockAndMethodDifferentArguments() {
        when(invocation.getMock()).thenReturn("mock1");
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMethod()).thenReturn(getClass().getMethods()[0]);
        when(invocation.getArguments()).thenReturn(new Object[] { "arg1" });

        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn("mock1");
        when(actualInvocation.isVerified()).thenReturn(false);
        when(actualInvocation.getMethod()).thenReturn(getClass().getMethods()[0]);
        when(actualInvocation.getArguments()).thenReturn(new Object[] { "arg2" });

        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatches_differentMocks() {
        when(invocation.getMock()).thenReturn("mock1");

        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn("mock2");

        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatches_isVerified() {
        when(invocation.isVerified()).thenReturn(true);

        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.isVerified()).thenReturn(false);

        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatches_sameMockAndMethodAndArguments() {
        when(invocation.getMock()).thenReturn("mock1");
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMethod()).thenReturn(getClass().getMethods()[0]);
        when(invocation.getArguments()).thenReturn(new Object[] { "arg1" });

        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn("mock1");
        when(actualInvocation.isVerified()).thenReturn(false);
        when(actualInvocation.getMethod()).thenReturn(getClass().getMethods()[0]);
        when(actualInvocation.getArguments()).thenReturn(new Object[] { "arg1" });

        assertTrue(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatches_exceptionInArgumentsMatch() {
        when(invocation.getMock()).thenReturn("mock1");
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMethod()).thenReturn(getClass().getMethods()[0]);
        when(invocation.getArguments()).thenReturn(new Object[] { "arg1" });

        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn("mock1");
        when(actualInvocation.isVerified()).thenReturn(false);
        when(actualInvocation.getMethod()).thenReturn(getClass().getMethods()[0]);
        when(actualInvocation.getArguments()).thenReturn(new Object[] { "arg1" });

        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Object[].class))).thenThrow(new RuntimeException());

        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testHasSimilarMethod_differentMethodName() {
        when(invocation.getMethod()).thenReturn(getClass().getMethods()[0]);

        Invocation candidateInvocation = mock(Invocation.class);
        when(candidateInvocation.getMethod()).thenReturn(getClass().getMethods()[1]);

        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSimilarMethod_nonUnverifiedInvocation() {
        when(invocation.getMethod()).thenReturn(getClass().getMethods()[0]);
        when(candidateInvocation.isVerified()).thenReturn(true);

        Invocation candidateInvocation = mock(Invocation.class);
        when(candidateInvocation.getMethod()).thenReturn(getClass().getMethods()[0]);
        when(candidateInvocation.isVerified()).thenReturn(false);

        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSimilarMethod_differentMocks() {
        when(invocation.getMock()).thenReturn("mock1");

        Invocation candidateInvocation = mock(Invocation.class);
        when(candidateInvocation.getMock()).thenReturn("mock2");

        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSimilarMethod_sameMethodAndMock_unverifiedButOverloadedArgs() {
        when(invocation.getMethod()).thenReturn(getClass().getMethods()[0]);
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock1");

        Invocation candidateInvocation = mock(Invocation.class);
        when(candidateInvocation.getMethod()).thenReturn(getClass().getMethods()[0]);
        when(candidateInvocation.isVerified()).thenReturn(false);
        when(candidateInvocation.getMock()).thenReturn("mock1");
        when(candidateInvocation.getArguments()).thenReturn(new Object[] { "arg1" });

        assertTrue(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSimilarMethod_sameMethodAndMock_verified() {
        when(invocation.getMethod()).thenReturn(getClass().getMethods()[0]);
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock1");

        Invocation candidateInvocation = mock(Invocation.class);
        when(candidateInvocation.getMethod()).thenReturn(getClass().getMethods()[0]);
        when(candidateInvocation.isVerified()).thenReturn(true);
        when(candidateInvocation.getMock()).thenReturn("mock1");

        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSimilarMethod_sameMethodAndMock_unverifiedAndSameArgs() {
        when(invocation.getMethod()).thenReturn(getClass().getMethods()[0]);
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock1");
        when(invocation.getArguments()).thenReturn(new Object[] { "arg1" });

        Invocation candidateInvocation = mock(Invocation.class);
        when(candidateInvocation.getMethod()).thenReturn(getClass().getMethods()[0]);
        when(candidateInvocation.isVerified()).thenReturn(false);
        when(candidateInvocation.getMock()).thenReturn("mock1");
        when(candidateInvocation.getArguments()).thenReturn(new Object[] { "arg1" });

        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSimilarMethod_sameMethodAndMock_unverifiedAndOverloadedArgs() {
        when(invocation.getMethod()).thenReturn(getClass().getMethods()[0]);
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock1");
        when(invocation.getArguments()).thenReturn(new Object[] { "arg1" });

        Invocation candidateInvocation = mock(Invocation.class);
        when(candidateInvocation.getMethod()).thenReturn(getClass().getMethods()[0]);
        when(candidateInvocation.isVerified()).thenReturn(false);
        when(candidateInvocation.getMock()).thenReturn("mock1");
        when(candidateInvocation.getArguments()).thenReturn(new Object[] { "arg2" });

        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSameMethod_methodNamesNotEqual() {
        Method method1 = getClass().getMethods()[0];
        Method method2 = getClass().getMethods()[1];
        when(invocation.getMethod()).thenReturn(method1);
        when(candidateInvocation.getMethod()).thenReturn(method2);

        assertFalse(invocationMatcher.hasSameMethod(candidateInvocation));
    }

    @Test
    public void testHasSameMethod_namesAreEqual() {
        Method method1 = getClass().getMethods()[0];
        Method method2 = getClass().getMethods()[0];
        when(invocation.getMethod()).thenReturn(method1);
        when(candidateInvocation.getMethod()).thenReturn(method2);

        assertTrue(invocationMatcher.hasSameMethod(candidateInvocation));
    }

    @Test
    public void testCaptureArgumentsFrom_variableArgumentsMethod() {
        Method method = getClass().getMethods()[1];
        when(invocation.getMethod()).thenReturn(method);

        Object[] originalArgs = new Object[] { "arg1", "arg2", "arg3" };
        when(invocation.getRawArguments()).thenReturn(originalArgs);

        CapturesArguments capturesArguments1 = mock(CapturesArguments.class);
        CapturesArguments capturesArguments2 = mock(CapturesArguments.class);
        when(invocationMatcher.getMatchers()).thenReturn(Arrays.asList(capturesArguments1, capturesArguments2));

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(capturesArguments1).captureFrom("arg1");
        verify(capturesArguments2).captureFrom("arg2");
        verify(capturesArguments2).captureFrom("arg3");
    }

    @Test
    public void testCaptureArgumentsFrom_nonVariableArgumentsMethod() {
        Method method = getClass().getMethods()[0];
        when(invocation.getMethod()).thenReturn(method);

        Object[] originalArgs = new Object[] { "arg1", "arg2" };
        when(invocation.getArguments()).thenReturn(originalArgs);

        CapturesArguments capturesArguments1 = mock(CapturesArguments.class);
        CapturesArguments capturesArguments2 = mock(CapturesArguments.class);
        when(invocationMatcher.getMatchers()).thenReturn(Arrays.asList(capturesArguments1, capturesArguments2));

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(capturesArguments1).captureFrom("arg1");
        verify(capturesArguments2).captureFrom("arg2");
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, invocationMatchers.size());

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        assertEquals(invocation1, invocationMatcher1.getInvocation());

        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);
        assertEquals(invocation2, invocationMatcher2.getInvocation());
    }
    
    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        
        assertEquals(location, invocationMatcher.getLocation());
    }

}