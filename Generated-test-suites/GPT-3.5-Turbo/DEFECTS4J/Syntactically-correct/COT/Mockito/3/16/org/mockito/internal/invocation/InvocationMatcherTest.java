
import org.hamcrest.Matcher;
import org.mockito.internal.invocation.ArgumentsComparator;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        // Create a mock Invocation object
        Invocation invocation = mock(Invocation.class);

        // Create a mock Matcher object
        Matcher matcher = mock(Matcher.class);

        // Create a list of Matchers
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher);

        // Create an InvocationMatcher object using the constructor with Matchers
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Verify that the Invocation and Matchers are set correctly
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        // Create a mock Invocation object
        Invocation invocation = mock(Invocation.class);

        // Create an InvocationMatcher object using the constructor without Matchers
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Verify that the Invocation is set correctly
        assertEquals(invocation, invocationMatcher.getInvocation());

        // Verify that the Matchers list is empty
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        // Create a mock Method object
        Method method = mock(Method.class);

        // Create a mock Invocation object
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);

        // Create an InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Verify that the correct Method is returned
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        // Create a mock Invocation object
        Invocation invocation = mock(Invocation.class);

        // Create an InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Verify that the correct string representation is returned
        assertEquals(invocationMatcher.toString(), new PrintSettings().print(invocationMatcher.getMatchers(), invocation));
    }

    @Test
    public void testMatches() {
        // Create mock Invocation objects
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        // Create an ArgumentsComparator object
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Invocation.class))).thenReturn(true);

        // Create an InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Verify that matches() returns true when the Invocations match
        assertTrue(invocationMatcher.matches(invocation1));

        // Verify that matches() returns false when the Invocations do not match
        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testHasSimilarMethod() {
        // Create mock Invocation objects
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        // Create an ArgumentsComparator object
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Object[].class))).thenReturn(true);

        // Create an InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Verify that hasSimilarMethod() returns true when the Invocations have similar methods
        assertTrue(invocationMatcher.hasSimilarMethod(invocation1));

        // Verify that hasSimilarMethod() returns false when the Invocations have different methods
        assertFalse(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSameMethod() {
        // Create mock Method objects
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        // Create mock Invocation objects
        Invocation invocation1 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(method1);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation2.getMethod()).thenReturn(method2);

        // Create an InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Verify that hasSameMethod() returns true when the Invocations have the same method
        assertTrue(invocationMatcher.hasSameMethod(invocation1));

        // Verify that hasSameMethod() returns false when the Invocations have different methods
        assertFalse(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test
    public void testGetLocation() {
        // Create a mock Location object
        Location location = mock(Location.class);

        // Create a mock Invocation object
        Invocation invocation = mock(Invocation.class);
        when(invocation.getLocation()).thenReturn(location);

        // Create an InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Verify that the correct Location is returned
        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // Create mock Invocation objects
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation2.getMethod()).thenReturn(method);

        // Create mock Matcher objects
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);

        // Create a list of Matchers
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher1);
        matchers.add(matcher2);

        // Create an InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, matchers);

        // Capture arguments from the Invocation
        invocationMatcher.captureArgumentsFrom(invocation2);

        // Verify that arguments are captured from the Invocation
        verify(matcher1).captureFrom(any());
        verify(matcher2).captureFrom(any());
    }

    @Test
    public void testCreateFrom() {
        // Create mock Invocation objects
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        // Create a list of Invocations
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation1);
        invocations.add(invocation2);

        // Create InvocationMatchers from the Invocations
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // Verify that the correct number of InvocationMatchers is created
        assertEquals(invocations.size(), invocationMatchers.size());

        // Verify that each InvocationMatcher is created correctly
        for (int i = 0; i < invocations.size(); i++) {
            assertEquals(invocations.get(i), invocationMatchers.get(i).getInvocation());
        }
    }
}
