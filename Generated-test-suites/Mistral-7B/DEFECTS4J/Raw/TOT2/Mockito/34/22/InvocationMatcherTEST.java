package org.mockito.tests.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.internal.*;
import org.mockito.internal.invocation.*;
import org.mockito.internal.matchers.*;
import org.mockito.internal.reporting.*;
import org.mockito.junit.*;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithInvocationAndEmptyMatcherList() {
        Invocation invocation = mock(Invocation.class);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals(invocation, matcher.getInvocation());
        assertNull(matcher.getMatchers());
    }

    @Test
    public void testInvocationMatcherWithInvocationAndMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(argThat(isA(String.class)), argThat(isA(Integer.class)));

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, matcher.getInvocation());
        assertEquals(matchers, matcher.getMatchers());
    }

    @Test
    public void testInvocationMatcherWithMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);

        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());

        assertEquals(method, matcher.getMethod());
    }

    @Test
    public void testInvocationMatcherWithMatchersOnly() {
        List<Matcher> matchers = Arrays.asList(argThat(isA(String.class)), argThat(isA(Integer.class)));

        InvocationMatcher matcher = new InvocationMatcher(Collections.<Invocation>emptyList(), matchers);

        assertEquals(Collections.<Invocation>emptyList(), matcher.getInvocations());
        assertEquals(matchers, matcher.getMatchers());
    }

    @Test
    public void testInvocationMatcherWhenNoMatchers() {
        Invocation invocation = mock(Invocation.class);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testInvocationMatcherWhenMatches() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(argThat(isA(String.class)), argThat(isA(Integer.class)));

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Object[] arguments = new Object[2];
        arguments[0] = "test";
        arguments[1] = 1;

        assertTrue(matcher.matches(createInvocation("testMethod", arguments)));
    }

    @Test
    public void testInvocationMatcherWhenDoesntMatch() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(argThat(isA(String.class)), argThat(isA(Float.class)));

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Object[] arguments = new Object[2];
        arguments[0] = "test";
        arguments[1] = 1.0f;

        assertFalse(matcher.matches(createInvocation("testMethod", arguments)));
    }

    @Test
    public void testInvocationMatcherWhenMatchesWithCapturedArguments() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(argThat(new TypeSafeMatcher<Object>() {
            @Override
            public boolean matchesSafely(Object item) {
                assertEquals("test", item);
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("matches test");
            }
        }));

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Object[] arguments = new Object[1];
        arguments[0] = "test";

        matcher.captureArgumentsFrom(invocation);

        assertEquals("test", matcher.getCapturedArgument(0));
        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testInvocationMatcherWithSameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        when(invocation1.getMethod()).thenReturn(new Method("testMethod", new Class[0]));
        when(invocation2.getMethod()).thenReturn(new Method("testMethod", new Class[0]));

        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertTrue(matcher.hasSameMethod(invocation2));
    }

    @Test
    public void testInvocationMatcherWithDifferentMethods() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        when(invocation1.getMethod()).thenReturn(new Method("testMethodA", new Class[0]));
        when(invocation2.getMethod()).thenReturn(new Method("testMethodB", new Class[0]));

        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertFalse(matcher.hasSameMethod(invocation2));
    }

    @Test
    public void testInvocationMatcherWithDifferentMocks() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertFalse(matcher.matches(invocation2));
    }

    @Test
    public void testInvocationMatcherWithSameMockButDifferentArguments() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation1.getArguments()).thenReturn(new Object[0]);
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getArguments()).thenReturn(new Object[]{1});

        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertFalse(matcher.matches(invocation2));
    }

    @Test(expected = NullPointerException.class)
    public void testInvocationMatcherWithNullMethod() {
        Invocation invocation = mock(Invocation.class);

        new InvocationMatcher(invocation, null);
    }

    @Test(expected = NullPointerException.class)
    public void testInvocationMatcherWithNullMatcher() {
        Invocation invocation = mock(Invocation.class);

        new InvocationMatcher(invocation, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvocationMatcherWithNullMock() {
        InvocationMatcher matcher = new InvocationMatcher(null, Collections.<Matcher>emptyList());
    }

    private Invocation createInvocation(String methodName, Object[] arguments) {
        try {
            Method method = Class.forName("org.mockito.internal.invocation.InvocationMatcherTest").getMethod(methodName, (Class[]) null);
            return mock(InternalInvocation.class, new Object[]{method, arguments});
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}