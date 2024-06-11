import org.junit.*;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class InvocationMatcherTest {
    private InvocationMatcher invocationMatcher;

    // Test case for the constructor that takes in an invocation and a list of matchers
    @Test
    public void testInvocationMatcherConstructorWithMatchers() {
        // Create a mock invocation
        Invocation mockInvocation = createMockInvocation();

        // Create a list of matchers
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(createMockMatcher());

        // Create the invocation matcher using the constructor
        invocationMatcher = new InvocationMatcher(mockInvocation, matchers);

        // Verify that the invocation and matchers are set correctly
        assertEquals(mockInvocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    // Test case for the constructor that takes in an invocation only
    @Test
    public void testInvocationMatcherConstructorWithoutMatchers() {
        // Create a mock invocation
        Invocation mockInvocation = createMockInvocation();

        // Create the invocation matcher using the constructor
        invocationMatcher = new InvocationMatcher(mockInvocation);

        // Verify that the invocation and empty matchers are set correctly
        assertEquals(mockInvocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    // Test case for the getMethod method
    @Test
    public void testGetMethod() {
        // Create a mock invocation
        Invocation mockInvocation = createMockInvocation();

        // Create the invocation matcher
        invocationMatcher = new InvocationMatcher(mockInvocation);

        // Verify that the getMethod method returns the correct method
        assertEquals(mockInvocation.getMethod(), invocationMatcher.getMethod());
    }

    // Test case for the matches method
    @Test
    public void testMatches() {
        // Create a mock invocation
        Invocation mockInvocation = createMockInvocation();

        // Create the invocation matcher
        invocationMatcher = new InvocationMatcher(mockInvocation);

        // Create another mock invocation
        Invocation anotherMockInvocation = createMockInvocation();

        // Verify that the matches method returns true for the same invocation
        assertTrue(invocationMatcher.matches(mockInvocation));

        // Verify that the matches method returns false for a different invocation
        assertFalse(invocationMatcher.matches(anotherMockInvocation));
    }

    // Test case for the hasSimilarMethod method
    @Test
    public void testHasSimilarMethod() {
        // Create a mock invocation
        Invocation mockInvocation = createMockInvocation();

        // Create the invocation matcher
        invocationMatcher = new InvocationMatcher(mockInvocation);

        // Create another mock invocation with the same method name and mock
        Invocation similarMockInvocation = createSimilarMockInvocation(mockInvocation);

        // Create another mock invocation with a different method name
        Invocation differentMethodMockInvocation = createDifferentMethodMockInvocation(mockInvocation);

        // Create another mock invocation with a different mock
        Invocation differentMockMockInvocation = createDifferentMockMockInvocation(mockInvocation);

        // Verify that the hasSimilarMethod method returns true for the similar invocation
        assertTrue(invocationMatcher.hasSimilarMethod(similarMockInvocation));

        // Verify that the hasSimilarMethod method returns false for the invocation with a different method name
        assertFalse(invocationMatcher.hasSimilarMethod(differentMethodMockInvocation));

        // Verify that the hasSimilarMethod method returns false for the invocation with a different mock
        assertFalse(invocationMatcher.hasSimilarMethod(differentMockMockInvocation));
    }

    // Test case for the captureArgumentsFrom method
    @Test
    public void testCaptureArgumentsFrom() {
        // Create a mock invocation
        Invocation mockInvocation = createMockInvocation();

        // Create the invocation matcher
        invocationMatcher = new InvocationMatcher(mockInvocation);

        // Create a mock invocation to capture arguments from
        Invocation captureArgumentsMockInvocation = createMockInvocation();

        // Capture arguments from the captureArgumentsMockInvocation
        invocationMatcher.captureArgumentsFrom(captureArgumentsMockInvocation);

        // Verify that the arguments are captured correctly
        assertEquals(captureArgumentsMockInvocation.getArguments()[0],
                ((CapturesArguments)invocationMatcher.getMatchers().get(0)).getCapturedArgument());
    }

    // Test case for the createFrom method
    @Test
    public void testCreateFrom() {
        // Create a list of mock invocations
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(createMockInvocation());
        invocations.add(createMockInvocation());
        invocations.add(createMockInvocation());

        // Create a list of invocation matchers using the createFrom method
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // Verify that the size of the invocationMatchers list is the same as the invocations list
        assertEquals(invocations.size(), invocationMatchers.size());

        // Verify that each element in the invocationMatchers list has the corresponding invocation
        for (int i = 0; i < invocations.size(); i++) {
            assertEquals(invocations.get(i), invocationMatchers.get(i).getInvocation());
        }
    }

    private Invocation createMockInvocation() {
        Method method = new Method("testMethod");
        Object[] arguments = {};
        return new Invocation(method, arguments, null, null);
    }

    private Matcher createMockMatcher() {
        return new Matcher();
    }

    private Invocation createSimilarMockInvocation(Invocation invocation) {
        Method method = new Method("testMethod");
        Object[] arguments = invocation.getArguments();
        return new Invocation(method, arguments, null, null);
    }

    private Invocation createDifferentMethodMockInvocation(Invocation invocation) {
        Method method = new Method("differentMethod");
        Object[] arguments = invocation.getArguments();
        return new Invocation(method, arguments, null, null);
    }

    private Invocation createDifferentMockMockInvocation(Invocation invocation) {
        Method method = new Method("testMethod");
        Object[] arguments = invocation.getArguments();
        return new Invocation(method, arguments, null, new Object());
    }
}