import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {

    // Test case for constructor with matchers
    @Test
    public void testInvocationMatcherWithMatchers() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);

        // Test
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(matcher));

        // Verify
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Arrays.asList(matcher), invocationMatcher.getMatchers());
    }

    // Test case for constructor without matchers
    @Test
    public void testInvocationMatcherWithoutMatchers() {
        // Setup
        Invocation invocation = mock(Invocation.class);

        // Test
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Verify
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    // Test case for getMethod()
    @Test
    public void testGetMethod() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        // Test
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method actualMethod = invocationMatcher.getMethod();

        // Verify
        assertEquals(method, actualMethod);
    }

    // Test case for matches() with matching invocation
    @Test
    public void testMatchesMatchingInvocation() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("testMethod");
        when(invocation.getArguments()).thenReturn(new Object[] {});        

        // Test
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.matches(invocation);

        // Verify
        assertTrue(result);
    }

    // Test case for matches() with non-matching invocation
    @Test
    public void testMatchesNonMatchingInvocation() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("testMethod");
        when(invocation.getArguments()).thenReturn(new Object[] {});

        Invocation otherInvocation = mock(Invocation.class);
        when(otherInvocation.getMock()).thenReturn(mock(Object.class));
        when(otherInvocation.getMethod()).thenReturn(method);
        when(otherInvocation.getArguments()).thenReturn(new Object[] {});
        
        // Test
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.matches(otherInvocation);

        // Verify
        assertFalse(result);
    }

    // Test case for hasSimilarMethod() with similar method
    @Test
    public void testHasSimilarMethodWithSimilarMethod() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[] {});

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[] {});

        // Test
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Verify
        assertFalse(result);
    }

    // Test case for hasSimilarMethod() with overloaded but same args
    @Test
    public void testHasSimilarMethodWithOverloadedButSameArgs() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[] { "arg1", 123 });

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[] { "arg1", 123 });

        // Test
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Verify
        assertFalse(result);
    }

    // Test case for getLocation()
    @Test
    public void testGetLocation() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        // Test
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Location actualLocation = invocationMatcher.getLocation();

        // Verify
        assertEquals(location, actualLocation);
    }

    // Test case for captureArgumentsFrom() with varargs
    @Test
    public void testCaptureArgumentsFromVarargs() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(method.isVarArgs()).thenReturn(true);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(new Object[] { "arg1", "arg2" });

        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        when(matcher1 instanceof CapturesArguments).thenReturn(true);
        when(matcher2 instanceof CapturesArguments).thenReturn(true);

        // Test
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(matcher1, matcher2));
        invocationMatcher.captureArgumentsFrom(invocation);

        // Verify
        verify((CapturesArguments) matcher1).captureFrom("arg1");
        verify((CapturesArguments) matcher2).captureFrom("arg2");
    }

    // Test case for captureArgumentsFrom() without varargs
    @Test
    public void testCaptureArgumentsFromNoVarargs() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(method.isVarArgs()).thenReturn(false);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(new Object[] { "arg1", "arg2" });

        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        when(matcher1 instanceof CapturesArguments).thenReturn(true);
        when(matcher2 instanceof CapturesArguments).thenReturn(true);

        // Test
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(matcher1, matcher2));
        invocationMatcher.captureArgumentsFrom(invocation);

        // Verify
        verify((CapturesArguments) matcher1).captureFrom("arg1");
        verify((CapturesArguments) matcher2).captureFrom("arg2");
    }

    // Test case for createFrom()
    @Test
    public void testCreateFrom() {
        // Setup
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        // Test
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // Verify
        assertEquals(2, invocationMatchers.size());
        assertTrue(invocationMatchers.get(0) instanceof InvocationMatcher);
        assertTrue(invocationMatchers.get(1) instanceof InvocationMatcher);
    }

}