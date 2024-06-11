package org.mockito.internal.invocation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.ExactInvocationMatcher;
import org.mockito.InvocationOnMock;
import org.mockito.stub.Answer;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationMatcherBuilder;
import org.mockito.internal.invocation.PrintSettings;

public class InvocationMatcherTest {

    @Test
    public void testSimpleMethodInvocationMatcher() {
        Object mock = mock(Object.class);
        Method method = mockMethod("testMethod", Object.class);
        Invocation invocation = new Invocation(mock, method, new Object[]{new String("test")});

        List<Matcher> matchers = Arrays.asList(is("test"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        InvocationMatcher expectedInvocationMatcher = new InvocationMatcher(invocation);
        assertThat(invocationMatcher, is(expectedInvocationMatcher));

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        when(mock.testMethod(stringArgumentCaptor.capture())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String argument = stringArgumentCaptor.getValue();
                return argument + "_processed";
            }
        });

        String result = invocationMatcher.getMock().testMethod("test");
        String capturedArgument = stringArgumentCaptor.getValue();
        verify(mock).testMethod(capturedArgument);

        assertThat(result, is(capturedArgument + "_processed"));
    }

    @Test
    public void testEmptyMatcherList() {
        Object mock = mock(Object.class);
        Method method = mockMethod("testMethod", Object.class);
        Invocation invocation = new Invocation(mock, method, new Object[]{new String("test")});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = invocationMatcher.getMatchers();
        InvocationMatcher expectedInvocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        assertThat(invocationMatcher, is(expectedInvocationMatcher));
    }

    @Test
    public void testComplexMatcher() {
        Object mock = mock(Object.class);
        Method method = mockMethod("testMethod", Object.class, int.class);
        Invocation invocation = new Invocation(mock, method, new Object[]{new String("test"), 5});

        List<Matcher> matchers = Arrays.asList(is("test"), is(5));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> intArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        when(mock.testMethod((String) Matchers.anyString(), (Integer) Matchers.anyInt())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String argument1 = stringArgumentCaptor.getValue();
                Integer argument2 = intArgumentCaptor.getValue();
                return argument1 + "_" + argument2;
            }
        });

        String result = invocationMatcher.getMock().testMethod("test1", 6);
        String capturedArgument1 = stringArgumentCaptor.getValue();
        Integer capturedArgument2 = intArgumentCaptor.getValue();

        verify(mock).testMethod(capturedArgument1, capturedArgument2);

        assertThat(result, is((Object) (capturedArgument1 + "_" + capturedArgument2)));
    }

    @Test
    public void testDifferentInvocationsWithSameMethod() {
        Object mock = mock(Object.class);
        Method method = mockMethod("testMethod", Object.class);

        Invocation invocation1 = new Invocation(mock, method, new Object[]{new String("test1")});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new Invocation(mock, method, new Object[]{new String("test2")});
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertThat(invocationMatcher1, not(invocationMatcher2));
    }

    @Test
    public void testSameInvocationWithDifferentMatchers() {
        Object mock = mock(Object.class);
        Method method = mockMethod("testMethod", Object.class);
        Invocation invocation = new Invocation(mock, method, new Object[]{new String("test")});

        List<Matcher> matchers1 = Arrays.asList(is("test1"));
        InvocationMatcherMatcher invocationMatcher1 = new InvocationMatcher(invocation, matchers1);

        List<Matcher> matchers2 = Arrays.asList(is("test2"));
        InvocationMatcherMatcher invocationMatcher2 = new InvocationMatcher(invocation, matchers2);

        assertThat(invocationMatcher1, not(invocationMatcher2));
    }

    @Test(expected = AssertionError.class)
    public void testIncompatibleInvocations() {
        Object mock = mock(Object.class);
        Method method1 = mockMethod("testMethod1", Object.class);
        Method method2 = mockMethod("testMethod2", String.class);

        Invocation invocation1 = new Invocation(mock, method1, new Object[]{new String("test")});
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new Invocation(mock, method2, new Object[]{new String("test")});
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertThat(invocationMatcher1, not(invocationMatcher2));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        Object mock = mock(Object.class);
        Method method = mockMethod("testMethod", Object[].class);
        Invocation invocation = new Invocation(mock, method, new Object[]{new String[]{"test1", "test2"}});

        List<Matcher> matchers = Arrays.asList(new ExactInvocationMatcher(new String[]{"test1", "test2"}));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        when(mock.testMethod((Object[]) Matchers.anyArrayOf(String.class))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                return arguments[0];
            }
        });

        Object result = invocationMatcher.getMock().testMethod(new String[]{"test3", "test4"});

        String capturedArgument = (String) invocationMatcher.getMatchers().get(0).captureFrom(invocation.getArguments());

        verify(mock).testMethod(new String[]{"test1", "test2"});

        assertThat(result, is(capturedArgument));
    }

    private Method mockMethod(String name, Class<?>... parameterTypes) {
        try {
            return Class.forName("org.mockito.internal.invocation.InvocationMatcherTest").getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static class ExactInvocationMatcher implements Matcher<Object[]> {
        private final Object[] expected;

        public ExactInvocationMatcher(Object[] expected) {
            this.expected = expected;
        }

        @Override
        public boolean matches(Object arg) {
            return arg != null && Arrays.equals(expected, (Object[]) arg);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("an array with elements: ");
            for (Object element : expected) {
                description.appendValue(element);
                description.appendText(", ");
            }
        }

        @Override
        public void describeMismatch(Object item, Description mismatchDescription) {
            mismatchDescription.appendText("expected: ");
            describeTo(mismatchDescription);
            mismatchDescription.appendText(" but: ");
            mismatchDescription.appendValue(item);
        }
    }
}