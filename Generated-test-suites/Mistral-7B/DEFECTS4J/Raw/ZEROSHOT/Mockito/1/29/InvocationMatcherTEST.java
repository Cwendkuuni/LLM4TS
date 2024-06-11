package org.mockito.internal.invocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stub.Answer;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_WhenSameInvocation_ThenMatches() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.anyInt(), ArgumentMatchers.eq("test"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        InvocationMatcher invokedMatcher = new InvocationMatcher(invocation, matchers);

        // then
        assertTrue(invocationMatcher.matches(invokedMatcher));
    }

    @Test
    public void testInvocationMatcher_WhenDifferentInvocation_ThenDoesNotMatch() {
        // given
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Mockito.when(invocation1.getMethod()).thenReturn(method1);

        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation2.getMethod()).thenReturn(method2);

        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.anyInt(), ArgumentMatchers.eq("test"));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // when
        Invocation onMock = Mockito.mock(Object.class, new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return null;
            }
        });

        // then
        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcher_WhenSameMethodAndArguments_ThenMatches() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);

        Object[] arguments = new Object[]{"test1", 1, new Object[]{1, 2}};
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.contains(ArgumentMatchers.any(Object[].class))));

        // when
        InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationOnMock.getArguments()).thenReturn(arguments);
        Mockito.when(invocationOnMock.getMethod()).thenReturn(method);

        // then
        assertTrue(invocationMatcher.matches(invocationOnMock));
    }

    @Test
    public void testInvocationMatcher_WhenDifferentArguments_ThenDoesNotMatch() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);

        Object[] arguments1 = new Object[]{"test1", 1, new Object[]{1, 2}};
        Object[] arguments2 = new Object[]{"test2", 2, new Object[]{1, 3}};

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.contains(ArgumentMatchers.any(Object[].class))));

        // when
        InvocationOnMock invocationOnMock1 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationOnMock1.getArguments()).thenReturn(arguments1);
        Mockito.when(invocationOnMock1.getMethod()).thenReturn(method);

        InvocationOnMock invocationOnMock2 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationOnMock2.getArguments()).thenReturn(arguments2);
        Mockito.when(invocationOnMock2.getMethod()).thenReturn(method);

        // then
        assertFalse(invocationMatcher.matches(invocationOnMock1));
        assertFalse(invocationMatcher.matches(invocationOnMock2));
    }

    @Test
    public void testInvocationMatcher_WhenSameMethodButDifferentArguments_ThenDoesNotMatch() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);

        Object[] arguments1 = new Object[]{"test1", 1, new Object[]{1, 2}};
        Object[] arguments2 = new Object[]{"test2", 2, new Object[]{1, 3}};

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()));

        // when
        InvocationOnMock invocationOnMock1 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationOnMock1.getArguments()).thenReturn(arguments1);
        Mockito.when(invocationOnMock1.getMethod()).thenReturn(method);

        InvocationOnMock invocationOnMock2 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationOnMock2.getArguments()).thenReturn(arguments2);
        Mockito.when(invocationOnMock2.getMethod()).thenReturn(method);

        // then
        assertFalse(invocationMatcher.matches(invocationOnMock1));
        assertFalse(invocationMatcher.matches(invocationOnMock2));
    }

    @Test
    public void testInvocationMatcher_WhenSameMethodSameArgumentsButDifferentMocks_ThenDoesNotMatch() {
        // given
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Mockito.when(invocation1.getMethod()).thenReturn(method1);

        Object[] arguments = new Object[]{"test", 1};
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()));

        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation2.getMethod()).thenReturn(method2);

        // when
        InvocationOnMock invocationOnMock1 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationOnMock1.getArguments()).thenReturn(arguments);
        Mockito.when(invocationOnMock1.getMethod()).thenReturn(method1);

        InvocationOnMock invocationOnMock2 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationOnMock2.getArguments()).thenReturn(arguments);
        Mockito.when(invocationOnMock2.getMethod()).thenReturn(method2);

        // then
        assertFalse(invocationMatcher1.matches(invocationOnMock2));
    }

    @Test
    public void testInvocationMatcher_WhenSameMethodSameArgumentsSameMocksButDifferentInvocationOrder_ThenMatches() {
        // given
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation1.getMethod()).thenReturn(method);

        Object[] arguments = new Object[]{"test", 1};
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()));

        Invocation invocation2 = Mockito.mock(Invocation.class);
        Mockito.when(invocation2.getMethod()).thenReturn(method);

        // when
        InvocationOnMock invocationOnMock1 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationOnMock1.getArguments()).thenReturn(arguments);
        Mockito.when(invocationOnMock1.getMethod()).thenReturn(method);

        Mockito.when(invocation2.getRawArguments()).thenReturn(new Object[]{invocationOnMock1.getRawArguments()[0], invocationOnMock1.getRawArguments()[1]});

        // then
        assertTrue(invocationMatcher1.matches(invocation2));
    }

    @Test
    public void testInvocationMatcher_WhenSameMethodSameArgumentsSameMocksButDifferentInvocationOrderWithVarargs_ThenMatches() {
        // given
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation1.getMethod()).thenReturn(method);

        Object[] arguments = new Object[]{"test", 1};
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()));

        Invocation invocation2 = Mockito.mock(Invocation.class);
        Mockito.when(invocation2.getMethod()).thenReturn(method);

        Object[] varargs = new Object[]{arguments[0], arguments[1], new Object[]{1, 2}};
        Mockito.when(invocation2.getRawArguments()).thenReturn(varargs);

        // then
        assertTrue(invocationMatcher1.matches(invocation2));
    }

    @Test
    public void testInvocationMatcher_WhenVarargsMatcher_ThenCapturesArguments() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.contains(ArgumentMatchers.any(Object[].class))));

        Object[] arguments = new Object[]{"test1", 1, new Object[]{1, 2}};

        // when
        InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationOnMock.getArguments()).thenReturn(arguments);
        Mockito.when(invocationOnMock.getMethod()).thenReturn(invocation.getMethod());

        invocationMatcher.captureArgumentsFrom(invocationOnMock);

        // then
        Object[] capturedArguments = ((CapturesArguments) invocationMatcher.getMatchers().get(0)).getCapturedValues();
        assertEquals(arguments[1], capturedArguments[0]);
        assertEquals(Arrays.asList(1, 2), Arrays.asList(capturedArguments[1]));
    }

    @Test
    public void testInvocationMatcher_WhenVarargsMatcherCaptureAll_ThenCapturesAllArguments() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.contains(ArgumentMatchers.any(Object[].class))));

        Object[] arguments = new Object[]{"test1", 1, new Object[]{1, 2, 3, 4}};

        // when
        InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationOnMock.getArguments()).thenReturn(arguments);
        Mockito.when(invocationOnMock.getMethod()).thenReturn(invocation.getMethod());

        invocationMatcher.captureArgumentsFrom(invocationOnMock);

        // then
        Object[] capturedArguments = ((CapturesArguments) invocationMatcher.getMatchers().get(0)).getCapturedValues();
        assertEquals(arguments.length, capturedArguments.length);
        for (int i = 0; i < capturedArguments.length; i++) {
            assertEquals(arguments[i], capturedArguments[i]);
        }
    }
}