import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.invocation.Invocation;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Method method = mock(Method.class);
        Object mock = mock(Object.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getMock()).thenReturn(mock);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(method, invocationMatcher.getMethod());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Method method = mock(Method.class);
        Object mock = mock(Object.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getMock()).thenReturn(mock);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(method, invocationMatcher.getMethod());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        Method method = mock(Method.class);
        Object mock = mock(Object.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getMock()).thenReturn(mock);
        List<Matcher> matchers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            matchers.add(mock(Matcher.class));
        }
        String expectedString = "matcher1, matcher2, matcher3";

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String actualString = invocationMatcher.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testMatchesSameMethod() {
        Method method = mock(Method.class);
        Object mock = mock(Object.class);
        Object[] args = new Object[1];
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation1.getMock()).thenReturn(mock);
        when(invocation1.getArguments()).thenReturn(args);
        when(invocation2.getMethod()).thenReturn(method);
        when(invocation2.getMock()).thenReturn(mock);
        when(invocation2.getArguments()).thenReturn(args);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.matches(invocation2);

        assertTrue(result);
    }

    @Test
    public void testMatchesDifferentMethod() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        Object mock = mock(Object.class);
        Object[] args = new Object[1];
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation1.getMock()).thenReturn(mock);
        when(invocation1.getArguments()).thenReturn(args);
        when(invocation2.getMethod()).thenReturn(method2);
        when(invocation2.getMock()).thenReturn(mock);
        when(invocation2.getArguments()).thenReturn(args);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.matches(invocation2);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethodSameMethodName() {
        Method method = mock(Method.class);
        Object mock = mock(Object.class);
        Object[] args1 = new Object[1];
        Object[] args2 = new Object[1];
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation1.getMock()).thenReturn(mock);
        when(invocation1.getArguments()).thenReturn(args1);
        when(invocation1.isVerified()).thenReturn(false);
        when(invocation2.getMethod()).thenReturn(method);
        when(invocation2.getMock()).thenReturn(mock);
        when(invocation2.getArguments()).thenReturn(args2);
        when(invocation2.isVerified()).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.hasSimilarMethod(invocation2);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethodDifferentMethodName() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        Object mock = mock(Object.class);
        Object[] args = new Object[1];
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation1.getMock()).thenReturn(mock);
        when(invocation1.isVerified()).thenReturn(false);
        when(invocation2.getMethod()).thenReturn(method2);
        when(invocation2.getMock()).thenReturn(mock);
        when(invocation2.getArguments()).thenReturn(args);
        when(invocation2.isVerified()).thenReturn(false);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.hasSimilarMethod(invocation2);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethodOverloadedButSameArgs() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        Object mock = mock(Object.class);
        Object[] args = new Object[1];
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation1.getMock()).thenReturn(mock);
        when(invocation1.getArguments()).thenReturn(args);
        when(method1.getName()).thenReturn("testMethod");
        when(invocation2.getMethod()).thenReturn(method2);
        when(invocation2.getMock()).thenReturn(mock);
        when(invocation2.getArguments()).thenReturn(args);
        when(method2.getName()).thenReturn("testMethod");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.hasSimilarMethod(invocation2);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethodNotOverloadedAndDifferentArgs() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        Object mock = mock(Object.class);
        Object[] args1 = new Object[1];
        Object[] args2 = new Object[1];
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation1.getMock()).thenReturn(mock);
        when(invocation1.getArguments()).thenReturn(args1);
        when(method1.getName()).thenReturn("testMethod");
        when(invocation2.getMethod()).thenReturn(method2);
        when(invocation2.getMock()).thenReturn(mock);
        when(invocation2.getArguments()).thenReturn(args2);
        when(method2.getName()).thenReturn("testMethod");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.hasSimilarMethod(invocation2);

        assertTrue(result);
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getLocation()).thenReturn(location);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Location result = invocationMatcher.getLocation();

        assertEquals(location, result);
    }

    @Test
    public void testCaptureArgumentsFromWithVarArgs() {
        Method method = mock(Method.class);
        Object mock = mock(Object.class);
        Object[] args = new Object[1];
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getRawArguments()).thenReturn(args);
        when(method.isVarArgs()).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> {
            invocationMatcher.captureArgumentsFrom(invocation);
        });
        assertEquals("Varargs not supported", exception.getMessage());
    }

    @Test
    public void testCaptureArgumentsFromWithoutVarArgs() {
        Method method = mock(Method.class);
        Object mock = mock(Object.class);
        Object[] args = new Object[1];
        Matcher matcher = mock(Matcher.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getRawArguments()).thenReturn(args);
        when(method.isVarArgs()).thenReturn(false);
        when(matcher instanceof CapturesArguments).thenReturn(true);
        when(invocation.getArgumentAt(anyInt(), any(Class.class))).thenReturn(args[0]);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.singletonList(matcher));
        invocationMatcher.captureArgumentsFrom(invocation);

        verify((CapturesArguments) matcher).captureFrom(args[0]);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation1);
        invocations.add(invocation2);
        InvocationMatcher invocationMatcher1 = mock(InvocationMatcher.class);
        InvocationMatcher invocationMatcher2 = mock(InvocationMatcher.class);
        whenNew(InvocationMatcher.class).withArguments(invocation1).thenReturn(invocationMatcher1);
        whenNew(InvocationMatcher.class).withArguments(invocation2).thenReturn(invocationMatcher2);

        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);

        assertEquals(2, result.size());
        assertTrue(result.contains(invocationMatcher1));
        assertTrue(result.contains(invocationMatcher2));
    }

}