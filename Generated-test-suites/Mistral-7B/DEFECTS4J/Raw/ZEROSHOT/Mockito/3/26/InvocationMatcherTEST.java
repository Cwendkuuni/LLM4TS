package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.*;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithEmptyMatcherList() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertThat(matchers, is(empty()));
    }

    @Test
    public void testInvocationMatcherWithMatchers() {
        // given
        Object[] arguments = new Object[]{1, 2, "three"};
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getArguments()).thenReturn(arguments);

        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq("three"));

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // then
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        assertThat(actualMatchers, is(matchers));
    }

    @Test
    public void testMatchesInvocationWithSameMethodAndArguments() {
        // given
        Object[] arguments = new Object[]{1, 2, "three"};
        Invocation invocation1 = Mockito.mock(Invocation.class);
        when(invocation1.getArguments()).thenReturn(arguments);
        when(invocation1.getMethod()).thenReturn(mockMethodWithName("testMethod"));

        Object[] argumentsForAnotherInvocation = new Object[]{1, 2, "three"};
        Invocation invocation2 = Mockito.mock(Invocation.class);
        when(invocation2.getArguments()).thenReturn(argumentsForAnotherInvocation);
        when(invocation2.getMethod()).thenReturn(mockMethodWithName("testMethod"));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // when
        boolean matches = invocationMatcher1.matches(invocationMatcher2);

        // then
        assertTrue(matches);
    }

    @Test
    public void testMatchesInvocationWithDifferentMethod() {
        // given
        Object[] arguments = new Object[]{1, 2, "three"};
        Invocation invocation1 = Mockito.mock(Invocation.class);
        when(invocation1.getArguments()).thenReturn(arguments);
        when(invocation1.getMethod()).thenReturn(mockMethodWithName("testMethod1"));

        Object[] argumentsForAnotherInvocation = new Object[]{1, 2, "three"};
        Invocation invocation2 = Mockito.mock(Invocation.class);
        when(invocation2.getArguments()).thenReturn(argumentsForAnotherInvocation);
        when(invocation2.getMethod()).thenReturn(mockMethodWithName("testMethod2"));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // when
        boolean matches = invocationMatcher1.matches(invocationMatcher2);

        // then
        assertFalse(matches);
    }

    @Test
    public void testMatchesInvocationWithSameMethodButDifferentArguments() {
        // given
        Object[] arguments = new Object[]{1, 2, "three"};
        Invocation invocation1 = Mockito.mock(Invocation.class);
        when(invocation1.getArguments()).thenReturn(arguments);
        when(invocation1.getMethod()).thenReturn(mockMethodWithName("testMethod"));

        Object[] argumentsForAnotherInvocation = new Object[]{2, 3, "three"};
        Invocation invocation2 = Mockito.mock(Invocation.class);
        when(invocation2.getArguments()).thenReturn(argumentsForAnotherInvocation);
        when(invocation2.getMethod()).thenReturn(mockMethodWithName("testMethod"));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // when
        boolean matches = invocationMatcher1.matches(invocationMatcher2);

        // then
        assertFalse(matches);
    }

    @Test
    public void testMatchesInvocationWithSameMethodButDifferentArgumentsButSameValues() {
        // given
        Object[] arguments = new Object[]{1, 2, "three"};
        Invocation invocation1 = Mockito.mock(Invocation.class);
        when(invocation1.getArguments()).thenReturn(arguments);
        when(invocation1.getMethod()).thenReturn(mockMethodWithName("testMethod"));

        Object[] argumentsForAnotherInvocation = new Object[]{new Integer(1), new Integer(2), "three"};
        Invocation invocation2 = Mockito.mock(Invocation.class);
        when(invocation2.getArguments()).thenReturn(argumentsForAnotherInvocation);
        when(invocation2.getMethod()).thenReturn(mockMethodWithName("testMethod"));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // when
        boolean matches = invocationMatcher1.matches(invocationMatcher2);

        // then
        assertTrue(matches);
    }

    @Test
    public void testHasSimilarMethod() {
        // given
        Object[] arguments = new Object[]{1, 2, "three"};
        Invocation invocation1 = Mockito.mock(Invocation.class);
        when(invocation1.getArguments()).thenReturn(arguments);
        when(invocation1.getMethod()).thenReturn(mockMethodWithName("testMethod"));

        Object[] argumentsForAnotherInvocation = new Object[]{1, 2, "three"};
        Invocation invocation2 = Mockito.mock(Invocation.class);
        when(invocation2.getArguments()).thenReturn(argumentsForAnotherInvocation);
        when(invocation2.getMethod()).thenReturn(mockMethodWithName("testMethodWithSameNameButDifferentClass"));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // when
        boolean hasSimilarMethod = invocationMatcher1.hasSimilarMethod(invocationMatcher2);

        // then
        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethodWithSameMethod() {
        // given
        Object[] arguments = new Object[]{1, 2, "three"};
        Invocation invocation1 = Mockito.mock(Invocation.class);
        when(invocation1.getArguments()).thenReturn(arguments);
        when(invocation1.getMethod()).thenReturn(mockMethodWithName("testMethod"));

        Object[] argumentsForAnotherInvocation = new Object[]{1, 2, "three"};
        Invocation invocation2 = Mockito.mock(Invocation.class);
        when(invocation2.getArguments()).thenReturn(argumentsForAnotherInvocation);
        when(invocation2.getMethod()).thenReturn(mockMethodWithName("testMethod"));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // when
        boolean hasSimilarMethod = invocationMatcher1.hasSimilarMethod(invocationMatcher2);

        // then
        assertTrue(hasSimilarMethod);
    }

    @Test
    public void testToString() {
        // given
        Object[] arguments = new Object[]{1, 2, "three"};
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getArguments()).thenReturn(arguments);
        when(invocation.getMethod()).thenReturn(mockMethodWithName("testMethod"));
        when(invocation.getLocation()).thenReturn(Mockito.mock(Location.class));

        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq("three"));

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // then
        String toString = invocationMatcher.toString();

        // then
        String expected = String.format("Invocation [%s, %s]", invocation.getMethod().getName(), matchers);
        assertEquals(expected, toString);
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        // given
        Object[] arguments = new Object[]{1, 2, "three"};
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getArguments()).thenReturn(arguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.argThat(new ArgumentMatcher<Integer>() {
            @Override
            public boolean matches(Object argument) {
                return equals(1, argument);
            }
        }), ArgumentMatchers.argThat(new ArgumentMatcher<String>() {
            @Override
            public boolean matches(Object argument) {
                return equals("three", argument);
            }
        }));

        invocationMatcher.captureArgumentsFrom(invocation);

        // then
        Matcher[] capturedArguments = invocationMatcher.getMatchers().toArray(new Matcher[0]);

        Matcher firstCapturedArgument = capturedArguments[0];
        Matcher secondCapturedArgument = capturedArguments[1];

        // then
        assertThat(firstCapturedArgument, is(ArgumentMatchers.argThat(new ArgumentMatcher<Integer>() {
            @Override
            public boolean matches(Object argument) {
                return equals(1, argument);
            }
        })));

        assertThat(secondCapturedArgument, is(ArgumentMatchers.argThat(new ArgumentMatcher<String>() {
            @Override
            public boolean matches(Object argument) {
                return equals("three", argument);
            }
        })));
    }

    @Test
    public void testCreateFromInvocations() {
        // given
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);

        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        // when
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // then
        List<Matcher> matchers1 = Arrays.asList(ArgumentMatchers.isNull());
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers1);

        List<Matcher> matchers2 = Arrays.asList(ArgumentMatchers.isNull());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers2);

        // then
        assertEquals(invocationMatcher1, invocationMatchers.get(0));
        assertEquals(invocationMatcher2, invocationMatchers.get(1));
    }

    private Method mockMethodWithName(String methodName) {
        try {
            return Class.forName("test.InvocationMatcherTest")
                    .getMethod(methodName, InvocationOnMock.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}