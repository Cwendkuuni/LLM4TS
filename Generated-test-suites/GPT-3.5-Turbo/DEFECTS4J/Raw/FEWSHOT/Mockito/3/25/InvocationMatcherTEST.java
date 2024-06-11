package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(matcher));

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Arrays.asList(matcher), invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = {1, 2, 3};
        when(invocation.getArguments()).thenReturn(arguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Arrays.asList(1, 2, 3), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        when(matcher.toString()).thenReturn("matcher");
        when(invocation.toString()).thenReturn("invocation");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(matcher));

        assertEquals("matcher invocation", invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = {1, 2, 3};
        when(invocation.getArguments()).thenReturn(arguments);
        when(invocation.getMock()).thenReturn(this);
        
        Invocation actual = mock(Invocation.class);
        when(actual.getArguments()).thenReturn(arguments);
        when(actual.getMock()).thenReturn(this);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatchesWithDifferentMock() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(this);
        
        Invocation actual = mock(Invocation.class);
        when(actual.getMock()).thenReturn(new Object());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatchesWithDifferentMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("method1");
        when(method2.getName()).thenReturn("method2");
        
        Invocation actual = mock(Invocation.class);
        when(actual.getMethod()).thenReturn(method2);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("method");
        when(method2.getName()).thenReturn("method");
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(this);
        
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method2);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(this);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethodName() {
        Invocation invocation = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("method1");
        when(method2.getName()).thenReturn("method2");
        
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method2);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithDifferentVerification() {
        Invocation invocation = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("method");
        when(invocation.isVerified()).thenReturn(true);
        when(invocation.getMock()).thenReturn(this);
        
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method2);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(this);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMock() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(this);
        
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMock()).thenReturn(new Object());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("method");
        when(method2.getName()).thenReturn("method");
        
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method2);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethodWithDifferentMethodName() {
        Invocation invocation = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("method1");
        when(method2.getName()).thenReturn("method2");
        
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method2);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethodWithDifferentParameters() {
        Invocation invocation = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("method");
        when(method2.getName()).thenReturn("method");
        when(method1.getParameterTypes()).thenReturn(new Class[]{Integer.class});
        when(method2.getParameterTypes()).thenReturn(new Class[]{String.class});
        
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method2);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        Object argument1 = new Object();
        Object argument2 = new Object();
        Object[] arguments = {argument1, argument2};
        when(invocation.isVarArgs()).thenReturn(false);
        when(invocation.getArgumentAt(0, Object.class)).thenReturn(argument1);
        when(invocation.getArgumentAt(1, Object.class)).thenReturn(argument2);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(matcher1, matcher2));
        
        invocationMatcher.captureArgumentsFrom(invocation);
        
        verify(matcher1).captureFrom(argument1);
        verify(matcher2).captureFrom(argument2);
    }

    @Test
    public void testCaptureArgumentsFromWithVarargs() {
        Invocation invocation = mock(Invocation.class);
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        Object argument1 = new Object();
        Object[] argument2 = {new Object(), new Object()};
        when(invocation.isVarArgs()).thenReturn(true);
        when(invocation.getArgumentAt(0, Object.class)).thenReturn(argument1);
        when(invocation.getArgumentAt(1, Object.class)).thenReturn(argument2);
        when(invocation.getRawArguments()).thenReturn(new Object[]{argument1, argument2[0], argument2[1]});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(matcher1, matcher2));
        
        invocationMatcher.captureArgumentsFrom(invocation);
        
        verify(matcher1).captureFrom(argument1);
        verify(matcher2).captureFrom(argument2[0]);
        verify(matcher2).captureFrom(argument2[1]);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        
        List<InvocationMatcher> expected = Arrays.asList(new InvocationMatcher(invocation1), new InvocationMatcher(invocation2));
        List<InvocationMatcher> actual = InvocationMatcher.createFrom(invocations);
        
        assertEquals(expected, actual);
    }
}