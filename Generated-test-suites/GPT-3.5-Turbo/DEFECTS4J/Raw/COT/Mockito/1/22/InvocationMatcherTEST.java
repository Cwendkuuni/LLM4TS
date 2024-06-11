import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() throws NoSuchMethodException {
        Method method = InvocationMatcher.class.getMethod("getMethod");
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.getMethod()).thenReturn(InvocationMatcher.class.getMethod("toString"));
        when(invocation.getArguments()).thenReturn(new Object[0]);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.singletonList(matcher));
        invocationMatcher.new PrintSettings(printSettings).print(Collections.singletonList(matcher), invocation);

        assertEquals("someToStringRepresentation", invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock");
        when(actualInvocation.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(InvocationMatcher.class.getMethod("matches", Invocation.class));
        when(actualInvocation.getMethod()).thenReturn(InvocationMatcher.class.getMethod("matches", Invocation.class));
        when(invocation.getArguments()).thenReturn(new Object[0]);
        when(actualInvocation.getArguments()).thenReturn(new Object[0]);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(true, invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(InvocationMatcher.class.getMethod("hasSimilarMethod", Invocation.class));
        when(candidate.getMethod()).thenReturn(InvocationMatcher.class.getMethod("hasSimilarMethod", Invocation.class));
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(true);
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");
        when(invocation.getArguments()).thenReturn(new Object[0]);
        when(candidate.getArguments()).thenReturn(new Object[0]);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(false, invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method1 = InvocationMatcher.class.getMethod("hasSameMethod", Invocation.class);
        Method method2 = InvocationMatcher.class.getMethod("hasSameMethod", Invocation.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(true, invocationMatcher.hasSameMethod(candidate));
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
        Invocation capturedInvocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(InvocationMatcher.class.getMethod("captureArgumentsFrom", Invocation.class));
        when(capturedInvocation.getMethod()).thenReturn(InvocationMatcher.class.getMethod("captureArgumentsFrom", Invocation.class));
        when(invocation.getArguments()).thenReturn(new Object[0]);
        when(capturedInvocation.getArguments()).thenReturn(new Object[0]);
        when(capturedInvocation.getRawArguments()).thenReturn(new Object[0]);
        when(capturedInvocation.getArgumentAt(anyInt(), any())).thenReturn(null);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(capturedInvocation);

        verify(invocationMatcher).captureArgumentsFrom(any(Invocation.class));
        // Additional assertions for capturing arguments if necessary
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation = mock(Invocation.class);
        List<Invocation> invocations = Collections.singletonList(invocation);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(1, invocationMatchers.size());
        assertEquals(invocation, invocationMatchers.get(0).getInvocation());
    }
}