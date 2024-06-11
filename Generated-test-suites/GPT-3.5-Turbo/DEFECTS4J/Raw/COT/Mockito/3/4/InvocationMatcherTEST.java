import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Test;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        // Setup
        Method expectedMethod = mock(Method.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(expectedMethod);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Verify
        assertEquals(expectedMethod, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        // Setup
        Invocation expectedInvocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(expectedInvocation);

        // Verify
        assertEquals(expectedInvocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        // Setup
        Matcher expectedMatcher = mock(Matcher.class);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.getMatchers().add(expectedMatcher);

        // Verify
        assertEquals(expectedMatcher, invocationMatcher.getMatchers().get(0));
    }

    @Test
    public void testToString() {
        // Setup
        String expectedString = "expected string";
        Invocation invocation = mock(Invocation.class);
        PrintSettings printSettings = mock(PrintSettings.class);
        when(printSettings.print(anyList(), any(Invocation.class))).thenReturn(expectedString);
        ReflectionTestUtils.setField(invocationMatcher, "printSettings", printSettings);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Verify
        assertEquals(expectedString, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        Invocation actualInvocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Verify
        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testHasSimilarMethod() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        Invocation candidateInvocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Verify
        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSameMethod() {
        // Setup
        Method method = mock(Method.class);
        Invocation invocation = mock(Invocation.class);
        Invocation candidateInvocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidateInvocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Verify
        assertTrue(invocationMatcher.hasSameMethod(candidateInvocation));
    }

    @Test
    public void testGetLocation() {
        // Setup
        Location expectedLocation = mock(Location.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getLocation()).thenReturn(expectedLocation);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Verify
        assertEquals(expectedLocation, invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Execute
        invocationMatcher.captureArgumentsFrom(invocation);

        // Verify
        // Verify captureFrom() method calls on the matchers
    }

    @Test
    public void testCreateFrom() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        List<Invocation> invocations = new ArrayList<Invocation>();
        invocations.add(invocation);

        // Execute
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // Verify
        assertEquals(1, invocationMatchers.size());
        assertEquals(invocation, invocationMatchers.get(0).getInvocation());
    }
}