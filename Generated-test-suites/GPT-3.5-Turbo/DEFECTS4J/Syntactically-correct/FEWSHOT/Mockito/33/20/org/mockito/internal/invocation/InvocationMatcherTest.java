package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.exceptions.util.LocationImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockCreationSettings;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

public class InvocationMatcherTest {
    private Invocation createInvocation(MockCreationSettings<?> settings, Method method, List<Matcher<?>> matchers) {
        InvocationOnMock invocationOnMock = mock(InvocationOnMock.class);
        when(invocationOnMock.getMethod()).thenReturn(method);
        when(invocationOnMock.getMatchers()).thenReturn(matchers);
        when(invocationOnMock.getMockCreationSettings()).thenReturn(settings);

        Location location = new LocationImpl();

        return new Invocation(invocationOnMock, location);
    }

    private Invocation createInvocation(MockCreationSettings<?> settings, Method method) {
        return createInvocation(settings, method, Collections.emptyList());
    }

    private Matcher<?> createMatcher() {
        return mock(Matcher.class);
    }

    private InvocationMatcher createInvocationMatcher(Invocation invocation, List<Matcher> matchers) {
        return new InvocationMatcher(invocation, matchers);
    }

    private InvocationMatcher createInvocationMatcher(Invocation invocation) {
        return new InvocationMatcher(invocation);
    }

    private List<InvocationMatcher> createInvocationMatchers(List<Invocation> invocations) {
        LinkedList<InvocationMatcher> out = new LinkedList<>();

        for (Invocation i : invocations) {
            out.add(createInvocationMatcher(i));
        }

        return out;
    }

    @Test
    public void testConstructorWithMatchers() {
        MockCreationSettings settings = mock(MockCreationSettings.class);
        Method method = mock(Method.class);
        List<Matcher<?>> matchers = new LinkedList<>();
        matchers.add(createMatcher());
        matchers.add(createMatcher());

        Invocation invocation = createInvocation(settings, method);
        InvocationMatcher invocationMatcher = createInvocationMatcher(invocation, matchers);

        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
        Assert.assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        MockCreationSettings settings = mock(MockCreationSettings.class);
        Method method = mock(Method.class);

        Invocation invocation = createInvocation(settings, method);
        InvocationMatcher invocationMatcher = createInvocationMatcher(invocation);

        List<Matcher> expectedMatchers = invocation.argumentsToMatchers();

        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(expectedMatchers, invocationMatcher.getMatchers());
        Assert.assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        MockCreationSettings settings = mock(MockCreationSettings.class);
        Method method = mock(Method.class);
        List<Matcher<?>> matchers = new LinkedList<>();
        matchers.add(createMatcher());
        matchers.add(createMatcher());

        Invocation invocation = createInvocation(settings, method);
        InvocationMatcher invocationMatcher = createInvocationMatcher(invocation, matchers);

        String expectedString = invocation.toString(matchers, new PrintSettings());

        Assert.assertEquals(expectedString, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        MockCreationSettings settings = mock(MockCreationSettings.class);
        Method method = mock(Method.class);
        List<Matcher<?>> matchers = new LinkedList<>();
        matchers.add(createMatcher());

        Invocation invocation = createInvocation(settings, method, matchers);
        InvocationMatcher invocationMatcher = createInvocationMatcher(invocation);

        Invocation actualInvocation = createInvocation(settings, method, matchers);
        boolean expectedMatch = true;
        boolean actualMatch = invocationMatcher.matches(actualInvocation);

        Assert.assertEquals(expectedMatch, actualMatch);
    }

    @Test
    public void testHasSimilarMethod() {
        MockCreationSettings settings = mock(MockCreationSettings.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        List<Matcher<?>> matchers = new LinkedList<>();
        matchers.add(createMatcher());

        Invocation invocation = createInvocation(settings, method1, matchers);
        InvocationMatcher invocationMatcher = createInvocationMatcher(invocation);

        Invocation candidateInvocation = createInvocation(settings, method2, matchers);
        boolean expectedSimilarMethod = false;
        boolean actualSimilarMethod = invocationMatcher.hasSimilarMethod(candidateInvocation);

        Assert.assertEquals(expectedSimilarMethod, actualSimilarMethod);
    }

    @Test
    public void testHasSameMethod() {
        MockCreationSettings settings = mock(MockCreationSettings.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        List<Matcher<?>> matchers = new LinkedList<>();
        matchers.add(createMatcher());

        Invocation invocation = createInvocation(settings, method1, matchers);
        InvocationMatcher invocationMatcher = createInvocationMatcher(invocation);

        Invocation candidateInvocation = createInvocation(settings, method2, matchers);
        boolean expectedSameMethod = false;
        boolean actualSameMethod = invocationMatcher.hasSameMethod(candidateInvocation);

        Assert.assertEquals(expectedSameMethod, actualSameMethod);
    }
}