package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher() {
        // Given
        Object mock = Mockito.mock(Object.class);
        Object[] arguments = new Object[]{1, "test"};
        Method method = Mockito.method(Object.class, "testMethod",new Class[]{Integer.class, String.class});
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(arguments);
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.isVerified()).thenReturn(false);

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Then
        Method actualMethod = invocationMatcher.getMethod();
        assertSame(method, actualMethod);
        assertEquals(invocationMatcher.toString(), String.format("invocation on %s %s(%s)", invocationMatcher.getInvocation(), method.getName(), Arrays.toString(arguments)));

        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertTrue(matchers.isEmpty());

        // Then matcher
        Matcher matcher = Matchers.equalTo("test");
        InvocationMatcher invocationMatcherWithMatcher = new InvocationMatcher(invocation, Arrays.asList(matcher));
        List<Matcher> matchersWithMatcher = invocationMatcherWithMatcher.getMatchers();

        // When capture arguments
        ArgumentCaptor<Integer> integerCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);

        invocationMatcherWithMatcher.captureArgumentsFrom(invocationOnMock);
        verify(invocationOnMock).getArgumentAt(0, Integer.class);
        verify(invocationOnMock).getArgumentAt(1, String.class);

        Matcher capturedMatcher = matchersWithMatcher.get(0);
        assertTrue(capturedMatcher.matches(stringCaptor.getValue()));
    }

    @Test
    public void testInvocationMatcherWithVarargs() {
        // Given
        Object mock = Mockito.mock(Object.class);
        Object[] arguments = new Object[]{1, 2, 3};
        Method method = Mockito.method(Object.class, "testMethod", new Class[]{Integer.class});
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(arguments);
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.isVerified()).thenReturn(false);

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Then
        assertFalse(invocationMatcher.matches(Mockito.mock(Invocation.class)));

        // Then matcher with varargs
        Object[] varargs = new Object[]{4, 5};
        Matcher matcher = new VarargMatcher<Object>(Object.class, Arrays.asList(Matchers.equalTo(varargs[0]), Matchers.instanceOf(VarargMatcher.class)));
        InvocationMatcher invocationMatcherWithVarargMatcher = new InvocationMatcher(invocation, Arrays.asList(matcher));

        // When capturing arguments
        InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
        invocationMatcherWithVarargMatcher.captureArgumentsFrom(invocationOnMock);

        verify(invocationOnMock).getArgumentAt(0, Integer.class);
        verify(invocationOnMock, times(2)).getArguments();
        verify(invocationOnMock).getArguments()[1].getClass().isArray();
        Object[] capturedVarargs = (Object[]) invocationOnMock.getArguments()[1];
        assertArrayEquals(new Object[]{varargs[0], varargs}, capturedVarargs);
    }

    @Test
    public void testInvocationMatcherWithEmptyList() {
        // Given
        Object mock = Mockito.mock(Object.class);
        Object[] arguments = new Object[]{1, "test"};
        Method method = Mockito.method(Object.class, "testMethod",new Class[]{Integer.class, String.class});
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(arguments);
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.isVerified()).thenReturn(false);

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Then
        assertTrue(invocationMatcher.matches(Mockito.mock(Invocation.class)));
    }

    @Test
    public void testInvocationMatcherWithSameMethod() {
        // Given
        Object mock = Mockito.mock(Object.class);
        Object[] firstArguments = new Object[]{1, "test1"};
        Object[] secondArguments = new Object[]{1, "test2"};
        Method method = Mockito.method(Object.class, "testMethod",new Class[]{Integer.class, String.class});
        Invocation invocation1 = Mockito.mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation1.getArguments()).thenReturn(firstArguments);
        when(invocation1.getMock()).thenReturn(mock);
        when(invocation1.isVerified()).thenReturn(false);

        Invocation invocation2 = Mockito.mock(Invocation.class);
        when(invocation2.getMethod()).thenReturn(method);
        when(invocation2.getArguments()).thenReturn(secondArguments);
        when(invocation2.getMock()).thenReturn(mock);
        when(invocation2.isVerified()).thenReturn(false);

        // When
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // Then
        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcherWithDifferentMethod() {
        // Given
        Object mock = Mockito.mock(Object.class);
        Object[] firstArguments = new Object[]{1, "test1"};
        Object[] secondArguments = new Object[]{2, "test2"};
        Method method1 = Mockito.method(Object.class, "testMethod1",new Class[]{Integer.class, String.class});
        Method method2 = Mockito.method(Object.class, "testMethod2",new Class[]{Integer.class, String.class});
        Invocation invocation1 = Mockito.mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation1.getArguments()).thenReturn(firstArguments);
        when(invocation1.getMock()).thenReturn(mock);
        when(invocation1.isVerified()).thenReturn(false);

        Invocation invocation2 = Mockito.mock(Invocation.class);
        when(invocation2.getMethod()).thenReturn(method2);
        when(invocation2.getArguments()).thenReturn(secondArguments);
        when(invocation2.getMock()).thenReturn(mock);
        when(invocation2.isVerified()).thenReturn(false);

        // When
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // Then
        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Captor
    private ArgumentCaptor<Integer> integerCaptor;
    @Captor
    private ArgumentCaptor<List<InvocationMatcher>> invocationMatcherCaptor;

    private void when(Invocation invocation, boolean isVarargs, Object... arguments) {
        Method method = Mockito.method(Object.class, "testMethod", new Class[]{Integer.class});
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(arguments);
        when(invocation.getMock()).thenReturn(Mockito.mock(Object.class));
        when(invocation.isVerified()).thenReturn(false);

        if (isVarargs) {
            when(invocation.getRawArguments()).thenReturn(new Object[]{arguments});
        }
    }
}