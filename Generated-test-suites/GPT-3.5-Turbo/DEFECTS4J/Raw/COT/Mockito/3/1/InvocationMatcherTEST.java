import org.junit.Test;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        // Create a mock invocation
        Invocation invocation = mock(Invocation.class);

        // Create an InvocationMatcher object
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // Call the getMethod method and assert the result is the same as the mock invocation's method
        assertSame(invocation.getMethod(), matcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        // Create a mock invocation
        Invocation invocation = mock(Invocation.class);

        // Create an InvocationMatcher object
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // Call the getInvocation method and assert the result is the same as the mock invocation
        assertSame(invocation, matcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        // Create a mock invocation
        Invocation invocation = mock(Invocation.class);

        // Create an InvocationMatcher object with matchers
        InvocationMatcher matcher = new InvocationMatcher(invocation, Matchers.singleton(Matchers.anyString()));

        // Call the getMatchers method and assert the result is the same as the matchers used in the constructor
        assertEquals(matchers, matcher.getMatchers());
    }

    @Test
    public void testToString() {
        // Create a mock invocation
        Invocation invocation = mock(Invocation.class);

        // Create an InvocationMatcher object
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // Call the toString method and assert the result is not null
        assertNotNull(matcher.toString());
    }

    @Test
    public void testMatches() {
        // Create a mock invocation
        Invocation invocation = mock(Invocation.class);

        // Create an InvocationMatcher object
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // Create a mock actual invocation
        Invocation actualInvocation = mock(Invocation.class);

        // Set up the mock actual invocation to return true when calling the getMock method
        when(actualInvocation.getMock()).thenReturn(mock(Object.class));

        // Set up the mock actual invocation to return true when calling the hasSameMethod method
        when(actualInvocation.hasSameMethod(matcher)).thenReturn(true);

        // Set up the mock actual invocation to return true when calling the argumentsMatch method
        when(actualInvocation.argumentsMatch(matcher)).thenReturn(true);

        // Call the matches method and assert the result is true
        assertTrue(matcher.matches(actualInvocation));
    }

    @Test
    public void testHasSimilarMethod() {
        // Create a mock invocation
        Invocation invocation = mock(Invocation.class);

        // Create an InvocationMatcher object
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // Create a mock candidate invocation
        Invocation candidateInvocation = mock(Invocation.class);

        // Set up the mock candidate invocation to return true when calling the isVerified method
        when(candidateInvocation.isVerified()).thenReturn(false);

        // Set up the mock candidate invocation to return true when calling the getMock method
        when(candidateInvocation.getMock()).thenReturn(mock(Object.class));

        // Set up the mock candidate invocation to return true when calling the hasSameMethod method
        when(candidateInvocation.hasSameMethod(matcher)).thenReturn(true);

        // Set up the mock candidate invocation to return true when calling the argumentsMatch method
        when(candidateInvocation.argumentsMatch(matcher)).thenReturn(false);

        // Call the hasSimilarMethod method and assert the result is true
        assertTrue(matcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSameMethod() {
        // Create a mock invocation
        Invocation invocation = mock(Invocation.class);

        // Create an InvocationMatcher object
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // Create a mock candidate invocation
        Invocation candidateInvocation = mock(Invocation.class);

        // Set up the mock candidate invocation to return true when calling the getMethod method
        when(candidateInvocation.getMethod()).thenReturn(matcher.getMethod());

        // Call the hasSameMethod method and assert the result is true
        assertTrue(matcher.hasSameMethod(candidateInvocation));
    }

    @Test
    public void testGetLocation() {
        // Create a mock invocation
        Invocation invocation = mock(Invocation.class);

        // Create an InvocationMatcher object
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // Call the getLocation method and assert the result is the same as the mock invocation's location
        assertSame(invocation.getLocation(), matcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // Create a mock invocation
        Invocation invocation = mock(Invocation.class);

        // Create an InvocationMatcher object
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // Create a mock argument capturing matcher
        CapturesArguments capturesArguments = mock(CapturesArguments.class);

        // Create a mock actual invocation
        Invocation actualInvocation = mock(Invocation.class);

        // Set up the mock actual invocation to return an array as raw arguments
        when(actualInvocation.getRawArguments()).thenReturn(new Object[] { "arg1", "arg2" });

        // Call the captureArgumentsFrom method
        matcher.captureArgumentsFrom(actualInvocation);

        // Verify that the captureArguments method of the argument capturing matcher is called for each argument
        verify(capturesArguments).captureFrom("arg1");
        verify(capturesArguments).captureFrom("arg2");
    }

    @Test
    public void testCreateFrom() {
        // Create a list of mock invocations
        List<Invocation> invocations = Arrays.asList(mock(Invocation.class), mock(Invocation.class), mock(Invocation.class));

        // Call the createFrom method and store the result
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        // Assert that the size of the result is the same as the size of the invocations list
        assertEquals(invocations.size(), matchers.size());

        // Assert that each element in the result is an instance of InvocationMatcher
        for (InvocationMatcher matcher : matchers) {
            assertTrue(matcher instanceof InvocationMatcher);
        }
    }
}