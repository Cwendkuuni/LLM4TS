package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Invocation invocation3 = Mockito.mock(Invocation.class);

        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Method method3 = Mockito.mock(Method.class);

        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        when(invocation3.getMethod()).thenReturn(method3);

        when(method1.getName()).thenReturn("method1");
        when(method2.getName()).thenReturn("method2");
        when(method3.getName()).thenReturn("method3");

        assertTrue(new InvocationMatcher(invocation1).hasSameMethod(invocation1));
        assertFalse(new InvocationMatcher(invocation1).hasSameMethod(invocation2));
        assertFalse(new InvocationMatcher(invocation1).hasSameMethod(invocation3));
    }

    @Test
    public void testMatches() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(eq("foo"), isNull());

        when(invocation.getArguments()).thenReturn(new Object[]{new Object(), null});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation actualInvocation = Mockito.mock(Invocation.class);
        Object[] actualArguments = new Object[]{new Object(), null};

        InvocationMatcher actualMatcher = new InvocationMatcher(actualInvocation, matchers);

        assertTrue(invocationMatcher.matches(actualMatcher));
        assertTrue(invocationMatcher.matches(actualInvocation));
        assertFalse(invocationMatcher.matches(Mockito.mock(Invocation.class)));

        InvocationMatcher differentMatcher = new InvocationMatcher(invocation, Arrays.asList(eq("bar")));
        assertFalse(invocationMatcher.matches(differentMatcher));
        assertFalse(differentMatcher.matches(invocationMatcher));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(argThat(new CaptureArgumentMatcher()));

        when(invocation.getArguments()).thenReturn(new Object[]{new Object(), "string"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Object[] capturedArguments = new Object[matchers.size()];

        invocationMatcher.captureArgumentsFrom(invocation);

        for (int position = 0; position < matchers.size(); position++) {
            Matcher matcher = matchers.get(position);
            if (matcher instanceof CapturesArguments) {
                CapturesArguments capturedArgument = (CapturesArguments) matcher;
                assertEquals(invocation.getArgumentAt(position, Object.class), capturedArgument.getValue());
                capturedArguments[position] = capturedArgument.getValue();
            }
        }

        assertEquals(new Object[]{new Object(), "string"}, capturedArguments);
    }

    @Test
    public void testIsVariableArgument() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{new Object(), new Object[]{}};

        when(invocation.getArguments()).thenReturn(arguments);
        when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));

        assertTrue(new InvocationMatcher(invocation).isVariableArgument(invocation, 1));
        assertFalse(new InvocationMatcher(invocation).isVariableArgument(invocation, 0));
    }

    @Test
    public void testIsVarargMatcher() {
        Matcher matcher = argThat(new VarargMatcher(String[].class));
        assertTrue(new InvocationMatcher(Mockito.mock(Invocation.class)).isVarargMatcher(matcher));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(Mockito.mock(Invocation.class), Mockito.mock(Invocation.class));

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, invocationMatchers.size());
    }
}

class CaptureArgumentMatcher implements Matcher<Object> {
    private Object capturedValue;

    @Override
    public boolean matches(Object argument) {
        capturedValue = argument;
        return true;
    }

    public Object getValue() {
        return capturedValue;
    }
}