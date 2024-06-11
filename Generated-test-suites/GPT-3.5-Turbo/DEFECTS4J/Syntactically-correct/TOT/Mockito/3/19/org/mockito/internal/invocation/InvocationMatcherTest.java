import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.*;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class InvocationMatcherTest {

    // Test case for the constructor InvocationMatcher(Invocation invocation, List<Matcher> matchers)
    @Test
    public void testConstructorWithMatchers() {
        // Create a mock Invocation object
        Invocation invocation = mock(Invocation.class);

        // Create mock Matchers
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));

        // Create an instance of InvocationMatcher using the constructor with matchers
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Verify that the InvocationMatcher object is created with the expected invocation and matchers
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), is(matchers));
    }

    // Test case for the constructor InvocationMatcher(Invocation invocation)
    @Test
    public void testConstructorWithoutMatchers() {
        // Create a mock Invocation object
        Invocation invocation = mock(Invocation.class);

        // Create an instance of InvocationMatcher using the constructor without matchers
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Verify that the InvocationMatcher object is created with the expected invocation and empty matchers list
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), is(Collections.emptyList()));
    }

    // Test case for the method getMethod()
    @Test
    public void testGetMethod() {
        // Create a mock Invocation object
        Invocation invocation = mock(Invocation.class);

        // Create a Method object
        Method method = mock(Method.class);

        // Stub the getMethod() method of Invocation to return the Method object
        when(invocation.getMethod()).thenReturn(method);

        // Create an instance of InvocationMatcher
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Verify that the getMethod() method returns the expected Method object
        assertThat(invocationMatcher.getMethod(), is(method));
    }

    // Test case for the method getInvocation()
    @Test
    public void testGetInvocation() {
        // Create a mock Invocation object
        Invocation invocation = mock(Invocation.class);

        // Create an instance of InvocationMatcher
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Verify that the getInvocation() method returns the expected Invocation object
        assertThat(invocationMatcher.getInvocation(), is(invocation));
    }

    // Test case for the method getMatchers()
    @Test
    public void testGetMatchers() {
        // Create a mock Invocation object
        Invocation invocation = mock(Invocation.class);

        // Create a mock Matcher
        Matcher matcher = mock(Matcher.class);

        // Create a List of Matchers
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher);

        // Create an instance of InvocationMatcher
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Verify that the getMatchers() method returns the expected Matchers list
        assertThat(invocationMatcher.getMatchers(), is(matchers));
    }

    // Test case for the method toString()
    @Test
    public void testToString() {
        // Create a mock Invocation object
        Invocation invocation = mock(Invocation.class);

        // Create a mock Matcher
        Matcher matcher = mock(Matcher.class);

        // Create a List of Matchers
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher);

        // Create an instance of InvocationMatcher
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Mock the print() method of PrintSettings to return a string
        when(new PrintSettings().print(any(List.class) , any(Invocation.class))).thenReturn("InvocationMatcher String");

        // Verify that the toString() method returns the expected string
        assertThat(invocationMatcher.toString(), is("InvocationMatcher String"));
    }

    // Test case for the method matches(Invocation actual)
    @Test
    public void testMatches() {
        // Create a mock Invocation object
        Invocation invocation = mock(Invocation.class);

        // Create an instance of InvocationMatcher
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Create a mock Invocation object for actual invocation
        Invocation actualInvocation = mock(Invocation.class);

        // Mock the getMock() method of Invocation to return a mock object
        when(invocation.getMock()).thenReturn(mock(Object.class));

        // Mock the hasSameMethod() method of InvocationMatcher to return true
        when(invocationMatcher.hasSameMethod(actualInvocation)).thenReturn(true);

        // Mock the argumentsMatch() method of ArgumentsComparator to return true
        when(new ArgumentsComparator().argumentsMatch(eq(invocationMatcher), eq(actualInvocation))).thenReturn(true);

        // Verify that the matches() method returns true
        assertThat(invocationMatcher.matches(actualInvocation), is(true));
    }

    // Test case for the method safelyArgumentsMatch(Object[] actualArgs)
    @Test
    public void testSafelyArgumentsMatch() {
        // Create a mock Invocation object
        Invocation invocation = mock(Invocation.class);

        // Create an instance of InvocationMatcher
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Create an array of actual arguments
        Object[] actualArgs = new Object[0];

        // Mock the argumentsMatch() method of ArgumentsComparator to return true
        when(new ArgumentsComparator().argumentsMatch(eq(invocationMatcher), eq(actualArgs))).thenReturn(true);

        // Verify that the safelyArgumentsMatch() method returns true
        assertThat(invocationMatcher.safelyArgumentsMatch(actualArgs), is(true));
    }

    // Test case for the method hasSimilarMethod(Invocation candidate)
    @Test
    public void testHasSimilarMethod() {
        // Create a mock Invocation object
        Invocation invocation = mock(Invocation.class);

        // Create an instance of InvocationMatcher
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Create a mock Invocation object for candidate invocation
        Invocation candidateInvocation = mock(Invocation.class);

        // Mock the getName() method of Method to return the same name
        when(invocation.getMethod().getName()).thenReturn("methodName");
        when(candidateInvocation.getMethod().getName()).thenReturn("methodName");

        // Mock the isVerified() method of Invocation to return false
        when(candidateInvocation.isVerified()).thenReturn(false);

        // Mock the getMock() method of Invocation to return a mock object
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidateInvocation.getMock()).thenReturn(mock(Object.class));

        // Mock the hasSameMethod() method of InvocationMatcher to return true
        when(invocationMatcher.hasSameMethod(candidateInvocation)).thenReturn(true);

        // Mock the safelyArgumentsMatch() method of InvocationMatcher to return false
        when(invocationMatcher.safelyArgumentsMatch(candidateInvocation.getArguments())).thenReturn(false);

        // Verify that the hasSimilarMethod() method returns false
        assertThat(invocationMatcher.hasSimilarMethod(candidateInvocation), is(false));
    }

    // Test case for the method hasSameMethod(Invocation candidate)
    @Test
    public void testHasSameMethod() {
        // Create a mock Invocation object
        Invocation invocation = mock(Invocation.class);

        // Create an instance of InvocationMatcher
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Create a mock Invocation object for candidate invocation
        Invocation candidateInvocation = mock(Invocation.class);

        // Create a Method object
        Method method = mock(Method.class);

        // Stub the getName() method of Method to return the same name
        when(invocation.getMethod().getName()).thenReturn("methodName");
        when(candidateInvocation.getMethod().getName()).thenReturn("methodName");

        // Stub the getParameterTypes() method of Method to return the same parameters
        when(invocation.getMethod()).thenReturn(method);
        when(candidateInvocation.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("methodName");
        when(method.getParameterTypes()).thenReturn(new Class[0]);

        // Verify that the hasSameMethod() method returns true
        assertThat(invocationMatcher.hasSameMethod(candidateInvocation), is(true));
    }

    // Test case for the method getLocation()
    @Test
    public void testGetLocation() {
        // Create a mock Invocation object
        Invocation invocation = mock(Invocation.class);

        // Create a mock Location object
        Location location = mock(Location.class);

        // Stub the getLocation() method of Invocation to return the Location object
        when(invocation.getLocation()).thenReturn(location);

        // Create an instance of InvocationMatcher
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Verify that the getLocation() method returns the expected Location object
        assertThat(invocationMatcher.getLocation(), is(location));
    }

    // Test case for the method captureArgumentsFrom(Invocation invocation)
    @Test
    public void testCaptureArgumentsFrom() {
        // Create a mock Invocation object
        Invocation invocation = mock(Invocation.class);

        // Create a mock Matcher object
        Matcher matcher = mock(Matcher.class);

        // Create a mock CapturesArguments object
        CapturesArguments capturesArguments = mock(CapturesArguments.class);

        // Set up the mock Invocation object with isVarArgs() returning true
        when(invocation.getMethod().isVarArgs()).thenReturn(true);

        // Set up the mock Invocation object with getRawArguments() returning an array of objects
        when(invocation.getRawArguments()).thenReturn(new Object[1]);

        // Set up the mock List of Matchers with one matcher
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher);

        // Set up the mock Matcher object with instanceof returns true for CapturesArguments
        when(matcher instanceof CapturesArguments).thenReturn(true);

        // Create an instance of InvocationMatcher
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Invoke the captureArgumentsFrom() method
        invocationMatcher.captureArgumentsFrom(invocation);

        // Verify that the captureFrom() method is called on the CaptureArguments object for each argument
        verify(capturesArguments).captureFrom(any());
    }

    // Test case for the static method createFrom(List<Invocation> invocations)
    @Test
    public void testCreateFrom() {
        // Create a mock Invocation object
        Invocation invocation = mock(Invocation.class);

        // Create a List of Invocations
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation);

        // Invoke the createFrom() method
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // Verify that the returned list contains an InvocationMatcher object for each invocation
        assertThat(invocationMatchers.size(), is(1));
        assertThat(invocationMatchers.get(0).getInvocation(), is(invocation));
    }
}