import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    // Test for the first constructor of InvocationMatcher
    @Test
    public void testInvocationMatcherConstructorWithMatchers() {
        // Create mock invocation
        Invocation invocation = mock(Invocation.class);

        // Create list of matchers
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(Matchers.anyString());

        // Create InvocationMatcher object using the first constructor
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Verify that the invocation and matchers are set correctly
        assert invocationMatcher.getInvocation().equals(invocation);
        assert invocationMatcher.getMatchers().equals(matchers);
    }

    // Test for the second constructor of InvocationMatcher
    @Test
    public void testInvocationMatcherConstructorWithoutMatchers() {
        // Create mock invocation
        Invocation invocation = mock(Invocation.class);

        // Create InvocationMatcher object using the second constructor
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Verify that the invocation and matchers are set correctly
        assert invocationMatcher.getInvocation().equals(invocation);
        assert invocationMatcher.getMatchers().equals(Collections.<Matcher>emptyList());
    }

    // Test for getMethod() method
    @Test
    public void testGetMethod() {
        // Create mock invocation
        Invocation invocation = mock(Invocation.class);

        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Verify that getMethod() method returns the correct method
        assert invocationMatcher.getMethod().equals(invocation.getMethod());
    }

    // Test for hasSimilarMethod() method
    @Test
    public void testHasSimilarMethod() {
        // Create mock invocations
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Invocation invocation3 = mock(Invocation.class);

        // Set up method names
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        Method method3 = mock(Method.class);
        mockMethodNames(method1, "method1");
        mockMethodNames(method2, "method2");
        mockMethodNames(method3, "method1");

        // Set up mock invocations
        mockInvocations(invocation1, method1);
        mockInvocations(invocation2, method2);
        mockInvocations(invocation3, method3);

        // Create InvocationMatcher objects
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(invocation3);

        // Verify that hasSimilarMethod() returns the correct results
        assert invocationMatcher1.hasSimilarMethod(invocation2);
        assert !invocationMatcher1.hasSimilarMethod(invocation3);
        assert !invocationMatcher2.hasSimilarMethod(invocation3);
    }

    // Test for hasSameMethod() method
    @Test
    public void testHasSameMethod() {
        // Create mock invocations
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        // Set up method names
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        mockMethodNames(method1, "method1");
        mockMethodNames(method2, "method2");

        // Set up mock invocations
        mockInvocations(invocation1, method1);
        mockInvocations(invocation2, method2);

        // Create InvocationMatcher objects
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // Verify that hasSameMethod() returns the correct results
        assert invocationMatcher1.hasSameMethod(invocation2);
        assert !invocationMatcher1.hasSameMethod(invocation1);
        assert !invocationMatcher2.hasSameMethod(invocation1);
    }

    // Test for getLocation() method
    @Test
    public void testGetLocation() {
        // Create mock invocation
        Invocation invocation = mock(Invocation.class);

        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Verify that getLocation() method returns the correct location
        assert invocationMatcher.getLocation().equals(invocation.getLocation());
    }

    // Test for captureArgumentsFrom() method
    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromWithVarArgs() {
        // Create mock invocation
        Invocation invocation = mock(Invocation.class);

        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Capture arguments from invocation (should throw UnsupportedOperationException)
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    // Test for captureArgumentsFrom() method
    @Test
    public void testCaptureArgumentsFrom() {
        // Create mock invocation
        Invocation invocation = mock(Invocation.class);

        // Set up arguments
        Object[] arguments = new Object[]{"arg1", "arg2"};

        // Set up raw arguments
        Object[] rawArguments = new Object[]{"rawArg1", "rawArg2"};

        // Mock getMethod() method
        Method method = mock(Method.class);
        mockMethodNames(method, "method1");

        // Mock getRawArguments() method
        mockRawArguments(invocation, rawArguments);

        // Mock getArgumentAt() method
        mockArgumentAt(invocation, 0, arguments[0]);
        mockArgumentAt(invocation, 1, arguments[1]);

        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Capture arguments from invocation
        invocationMatcher.captureArgumentsFrom(invocation);

        // Verify that the arguments are captured correctly
        verifyArguments(arguments);
    }

    // Utility methods for mocking

    private void mockMethodNames(Method method, String methodName) {
        when(method.getName()).thenReturn(methodName);
    }

    private void mockInvocations(Invocation invocation, Method method) {
        when(invocation.getMethod()).thenReturn(method);
    }

    private void mockRawArguments(Invocation invocation, Object[] rawArguments) {
        when(invocation.getRawArguments()).thenReturn(rawArguments);
    }

    private void mockArgumentAt(Invocation invocation, int position, Object argument) {
        when(invocation.getArgumentAt(position, Object.class)).thenReturn(argument);
    }

    private void verifyArguments(Object[] arguments) {
        // Verify that arguments are captured correctly
        verify(arguments[0]);
        verify(arguments[1]);
    }

}