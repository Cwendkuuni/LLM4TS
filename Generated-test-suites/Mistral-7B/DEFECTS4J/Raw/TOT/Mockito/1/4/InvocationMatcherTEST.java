package org.mockito.internal.invocation;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithSingleArgument() {
        Object mock = Mockito.mock(Object.class);
        Object argument = new Object();

        Invocation invocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getArgumentAt(0, Object.class)).thenReturn(argument);
        Mockito.when(invocation.getMock()).thenReturn(mock);
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> matchers = invocationMatcher.getMatchers();
        matchers.add(ArgumentMatchers.eq(argument));

        InvocationMatcher testedMatcher = new InvocationMatcher(invocation, matchers);

        InvocationOnMock actualInvocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        Mockito.when(actualInvocation.getMock()).thenReturn(mock);
        Mockito.when(actualInvocation.getArguments()).thenReturn(new Object[]{argument});

        assertTrue(testedMatcher.matches(actualInvocation));
    }

    @Test
    public void testInvocationMatcherWithMultipleArguments() {
        Object mock = Mockito.mock(Object.class);
        Object argument1 = new Object();
        Object argument2 = new Object();

        Invocation invocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getArgumentAt(0, Object.class)).thenReturn(argument1);
        Mockito.when(invocation.getArgumentAt(1, Object.class)).thenReturn(argument2);
        Mockito.when(invocation.getMock()).thenReturn(mock);
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> matchers = invocationMatcher.getMatchers();
        matchers.add(ArgumentMatchers.eq(argument1));
        matchers.add(ArgumentMatchers.eq(argument2));

        InvocationMatcher testedMatcher = new InvocationMatcher(invocation, matchers);

        InvocationOnMock actualInvocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        Mockito.when(actualInvocation.getMock()).thenReturn(mock);
        Mockito.when(actualInvocation.getArguments()).thenReturn(new Object[]{argument1, argument2});

        assertTrue(testedMatcher.matches(actualInvocation));
    }

    @Test
    public void testInvocationMatcherWithVarargs() {
        Object mock = Mockito.mock(Object.class);
        Object[] arguments = new Object[]{new Object(), new Object()};

        Invocation invocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMock()).thenReturn(mock);
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation.getArguments()).thenReturn(arguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> matchers = invocationMatcher.getMatchers();
        matchers.add(ArgumentMatchers.arrayContaining(ArgumentMatchers.notNullValue()));

        InvocationMatcher testedMatcher = new InvocationMatcher(invocation, matchers);

        InvocationOnMock actualInvocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        Mockito.when(actualInvocation.getMock()).thenReturn(mock);
        Mockito.when(actualInvocation.getArguments()).thenReturn(arguments);

        assertTrue(testedMatcher.matches(actualInvocation));
    }

    @Test
    public void testInvocationMatcherWithEmptyMatcherList() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMock()).thenReturn(mock);
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        InvocationMatcher testedMatcher = new InvocationMatcher(invocation);

        assertTrue(testedMatcher.matches(invocation));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInvocationMatcherWithVarargsAndIndividualMatcher() {
        Object mock = Mockito.mock(Object.class);
        Object[] arguments = new Object[]{new Object(), new Object()};

        Invocation invocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMock()).thenReturn(mock);
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation.getArguments()).thenReturn(arguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> matchers = invocationMatcher.getMatchers();
        matchers.add(ArgumentMatchers.eq(arguments[0]));

        InvocationMatcher testedMatcher = new InvocationMatcher(invocation, matchers);

        InvocationOnMock actualInvocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        Mockito.when(actualInvocation.getMock()).thenReturn(mock);
        Mockito.when(actualInvocation.getArguments()).thenReturn(arguments);

        throw new UnsupportedOperationException();
    }
}