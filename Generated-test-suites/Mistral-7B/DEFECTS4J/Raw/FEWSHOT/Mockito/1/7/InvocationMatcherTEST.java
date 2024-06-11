package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintSettings.PrintType;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.MethodType;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_emptyMatcherList() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertTrue(matchers.isEmpty());
    }

    @Test
    public void testInvocationMatcher_nonEmptyMatcherList() {
        Invocation invocation = mock(Invocation.class);
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher1);
        matchers.add(matcher2);
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertTrue(matchers.size() > 0);
        assertTrue(matcher.getMatchers().size() > 0);
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher1);
        matchers.add(matcher2);
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String actual = matcher.toString();
        String expected = PrintSettings.defaultPrintSettings().print(matchers, invocation, PrintType.FULL_VERBOSE);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());
        when(invocation.getMethod()).thenReturn(method);

        Method actual = matcher.getMethod();
        assertEquals(method, actual);
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());

        Invocation actual = matcher.getInvocation();
        assertEquals(invocation, actual);
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> actual = matcher.getMatchers();
        assertEquals(matchers, actual);
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        when(invocation1.getMock()).thenReturn(invocation2.getMock());
        when(invocation1.getMethod()).thenReturn(invocation2.getMethod());
        when(invocation1.isVerified()).thenReturn(false);
        when(invocation2.isVerified()).thenReturn(false);
        when(invocation1.getArguments()).thenReturn(new Object[0]);
        when(invocation2.getArguments()).thenReturn(new Object[0]);

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertTrue(matcher1.matches(matcher2));
    }

    @Test
    public void testSafelyArgumentsMatch_nullActualArgs() {
        InvocationMatcher matcher = new InvocationMatcher(mock(Invocation.class), Collections.emptyList());

        assertFalse(matcher.safelyArgumentsMatch(null));
    }

    @Test
    public void testSafelyArgumentsMatch_differentArguments() {
        InvocationMatcher matcher = new InvocationMatcher(mock(Invocation.class), Collections.emptyList());
        Object[] arguments = new Object[0];
        Object[] differentArguments = { new Object() };

        assertFalse(matcher.safelyArgumentsMatch(differentArguments));
    }

    @Test
    public void testSafelyArgumentsMatch_sameArguments() {
        InvocationMatcher matcher = new InvocationMatcher(mock(Invocation.class), Collections.emptyList());
        Object[] arguments = new Object[0];

        assertTrue(matcher.safelyArgumentsMatch(arguments));
    }

    @Test
    public void testHasSimilarMethod_differentMethods() {
        InvocationMatcher matcher1 = new InvocationMatcher(mock(Invocation.class));
        InvocationMatcher matcher2 = new InvocationMatcher(mock(Invocation.class));
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(method1.getName()).thenReturn("method1");
        when(method2.getName()).thenReturn("method2");

        assertFalse(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testHasSimilarMethod_sameMethods() {
        InvocationMatcher matcher1 = new InvocationMatcher(mock(Invocation.class));
        InvocationMatcher matcher2 = new InvocationMatcher(mock(Invocation.class));
        Method method = mock(Method.class);

        when(matcher1.getMethod()).thenReturn(method);
        when(matcher2.getMethod()).thenReturn(method);

        assertTrue(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testHasSimilarMethod_overloadedMethods() {
        InvocationMatcher matcher1 = new InvocationMatcher(mock(Invocation.class));
        InvocationMatcher matcher2 = new InvocationMatcher(mock(Invocation.class));
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(method1.getName()).thenReturn("method");
        when(method1.getParameterTypes()).thenReturn(new Class[] { Integer.TYPE, String.class });
        when(method2.getName()).thenReturn("method");
        when(method2.getParameterTypes()).thenReturn(new Class[] { String.class, Integer.TYPE });

        assertFalse(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testHasSimilarMethod_sameMethodsWithDifferentInvocations() {
        InvocationMatcher matcher1 = new InvocationMatcher(mock(Invocation.class));
        InvocationMatcher matcher2 = new InvocationMatcher(mock(Invocation.class));
        Method method = mock(Method.class);

        when(matcher1.getMethod()).thenReturn(method);
        when(matcher2.getMethod()).thenReturn(method);
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        when(matcher1.getInvocation()).thenReturn(invocation1);
        when(matcher2.getInvocation()).thenReturn(invocation2);

        assertFalse(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testHasSimilarMethod_sameMethodsWithSameInvocations() {
        InvocationMatcher matcher1 = new InvocationMatcher(mock(Invocation.class));
        InvocationMatcher matcher2 = new InvocationMatcher(mock(Invocation.class));
        Method method = mock(Method.class);
        Invocation invocation = mock(Invocation.class);

        when(matcher1.getMethod()).thenReturn(method);
        when(matcher2.getMethod()).thenReturn(method);
        when(matcher1.getInvocation()).thenReturn(invocation);
        when(matcher2.getInvocation()).thenReturn(invocation);

        assertTrue(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testHasSameMethod() {
        InvocationMatcher matcher1 = new InvocationMatcher(mock(InvocationOnMock.class));
        InvocationMatcher matcher2 = new InvocationMatcher(mock(InvocationOnMock.class));
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        when(matcher1.getMethod()).thenReturn(method1);
        when(matcher2.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("method");
        when(method2.getName()).thenReturn("method");

        assertTrue(matcher1.hasSameMethod(matcher2));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        when(invocation.getLocation()).thenReturn(location);

        Location actual = matcher.getLocation();
        assertEquals(location, actual);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        List<Matcher> matchers = new LinkedList<>();
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        matchers.add(matcher1);
        matchers.add(matcher2);
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[] { "arg1" });

        matcher.captureArgumentsFrom(invocation);

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(matcher1).captureFrom(captor.capture("matcher1"));
        verify(matcher2).captureFrom(captor.capture("matcher2"));
        assertEquals("arg1", matcher1.getValue());
        assertEquals("arg1", matcher2.getValue());
    }

    @Test
    public void testCaptureArgumentsFrom_varargs() {
        Invocation invocation = mock(Invocation.class);
        Matcher matcher = mock(VarargMatcher.class);
        Matcher capturedMatcher = mock(CapturesArguments.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[] { new Object[1] });
        InvocationMatcher matcherInstance = spy(new InvocationMatcher(invocation));

        matcherInstance.captureArgumentsFrom(invocation);

        ArgumentCaptor<InvocationMatcher> matcherCaptor = ArgumentCaptor.forClass(InvocationMatcher.class);
        verify(matcher).captureFrom(matcherCaptor.capture("matcher"));
        assertEquals(matcherInstance, matcherCaptor.getValue());

        Object[] capturedArgs = matcherCaptor.getValue().getValue();
        assertTrue(capturedArgs.length > 0);
        verifyNoMoreInteractions(capturedMatcher);
    }

    @Test
    public void testCaptureArgumentsFrom_multipleMatchers() {
        Invocation invocation = mock(Invocation.class);
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        List<Matcher> matchers = new LinkedList<>();
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        Matcher matcher3 = mock(Matcher.class);
        matchers.add(matcher1);
        matchers.add(matcher2);
        matchers.add(matcher3);
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[] { "arg1", "arg2" });

        matcher.captureArgumentsFrom(invocation);

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(matcher1).captureFrom(captor.capture("matcher1"));
        verify(matcher2).captureFrom(captor.capture("matcher2"));
        verify(matcher3).captureFrom(captor.capture("matcher3"));
        assertEquals("arg1", matcher1.getValue());
        assertEquals("arg2", matcher2.getValue());
        assertNull(matcher3.getValue());
    }

    @Test
    public void testIsVariableArgument() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[0];
        when(invocation.getRawArguments()).thenReturn(arguments);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().isVarArgs()).thenReturn(true);
        when(invocation.getRawArguments()[arguments.length - 1]).thenReturn(new Object[0]);

        assertTrue(new InvocationMatcher(invocation).isVariableArgument(invocation, arguments.length - 1));
    }

    @Test
    public void testIsVariableArgument_notLastArgument() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[0];
        when(invocation.getRawArguments()).thenReturn(arguments);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().isVarArgs()).thenReturn(true);

        assertFalse(new InvocationMatcher(invocation).isVariableArgument(invocation, 0));
    }

    @Test
    public void testIsVariableArgument_nonVarArgsMethod() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[0];
        when(invocation.getRawArguments()).thenReturn(arguments);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().isVarArgs()).thenReturn(false);

        assertFalse(new InvocationMatcher(invocation).isVariableArgument(invocation, 0));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(mock(Invocation.class), mock(Invocation.class));
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, matchers.size());
    }

    private void assertEquals(Object expected, Object actual) {
        MatcherAssert.assertThat(expected, equalTo(actual));
    }

    private void verify(Object mock) {
        verifyNoMoreInteractions(mock);
    }

    private void verifyZeroInteractions(Object mock) {
        verifyZeroInteractions(mock);
    }

    private Matcher equalTo(Object expected) {
        return IsInstanceOf.instanceOf(expected.getClass());
    }
}