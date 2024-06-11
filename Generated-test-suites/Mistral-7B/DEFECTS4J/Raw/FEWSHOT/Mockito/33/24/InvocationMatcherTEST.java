package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgThat.argThat;
import static org.mockito.internal.matchers.IsSame.isSame;
import static org.mockito.internal.matchers.TypeChange.type;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatcherList() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testConstructorWithInvocationAndMatcherList() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(argThat(isSame(invocation.getMock())), argThat(type(String.class)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        MockitoInvocation actualInvocation = Mockito.mock(Invocation.class);
        Mockito.when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        Mockito.when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        Mockito.when(actualInvocation.getArguments()).thenReturn(new Object[]{Mockito.mock(String.class), Mockito.mock(String.class)});

        assertTrue(invocationMatcher.matches(invocation));
        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testConstructorWithNullInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(null);
        assertNull(invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method returnedMethod = invocationMatcher.getMethod();

        assertTrue(method.equals(returnedMethod));
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation returnedInvocation = invocationMatcher.getInvocation();

        assertTrue(invocation.equals(returnedInvocation));
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(argThat(isSame(invocation.getMock())), argThat(type(String.class)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> returnedMatchers = invocationMatcher.getMatchers();

        assertTrue(matchers.equals(returnedMatchers));
    }

    @Test
    public void testToString() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(argThat(isSame(invocation.getMock())), argThat(type(String.class)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        PrintSettings printSettings = Mockito.mock(PrintSettings.class);

        String toString = invocationMatcher.toString(printSettings);

        String expectedToString = String.format("%s%n%s()%nInvocation: %s%n", InvocationMatcher.class.getSimpleName(), invocation.getMethod().getName(), invocation);

        assertTrue(expectedToString.equals(toString));
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(argThat(isSame(invocation1.getMock())), argThat(type(String.class)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, matchers);

        MockitoInvocation actualInvocation1 = Mockito.mock(Invocation.class);
        Mockito.when(actualInvocation1.getMock()).thenReturn(invocation1.getMock());
        Mockito.when(actualInvocation1.getMethod()).thenReturn(invocation1.getMethod());
        Mockito.when(actualInvocation1.getArguments()).thenReturn(new Object[]{invocation1.getMock(), Mockito.mock(String.class)});

        MockitoInvocation actualInvocation2 = Mockito.mock(Invocation.class);
        Mockito.when(actualInvocation2.getMock()).thenReturn(invocation2.getMock());
        Mockito.when(actualInvocation2.getMethod()).thenReturn(invocation2.getMethod());
        Mockito.when(actualInvocation2.getArguments()).thenReturn(new Object[]{invocation2.getMock(), Mockito.mock(String.class)});

        assertTrue(invocationMatcher.matches(invocation1));
        assertFalse(invocationMatcher.matches(actualInvocation1));
        assertFalse(invocationMatcher.matches(invocation2));
        assertFalse(invocationMatcher.matches(actualInvocation2));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(argThat(isSame(invocation.getMock())), argThat(type(String.class)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Object[] arguments = new Object[]{Mockito.mock(String.class), Mockito.mock(String.class)};

        assertTrue(invocationMatcher.safelyArgumentsMatch(arguments));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(argThat(isSame(invocation1.getMock())), argThat(type(String.class)));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation1.getMethod()).thenReturn(method1);
        Mockito.when(invocation2.getMethod()).thenReturn(method2);

        assertTrue(invocationMatcher1.hasSameMethod(invocation1));
        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher1));
        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher1));
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(argThat(isSame(invocation1.getMock())), argThat(type(String.class)));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation1.getMethod()).thenReturn(method1);
        Mockito.when(invocation2.getMethod()).thenReturn(method2);

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher1));
        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher1));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Location location = Mockito.mock(Location.class);
        Mockito.when(invocation.getLocation()).thenReturn(location);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Location returnedLocation = invocationMatcher.getLocation();

        assertTrue(location.equals(returnedLocation));
    }
}