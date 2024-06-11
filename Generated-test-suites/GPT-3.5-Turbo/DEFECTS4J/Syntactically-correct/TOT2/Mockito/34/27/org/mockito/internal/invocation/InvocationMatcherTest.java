import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.Equals;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = new Invocation(null, "testMethod", null);
        List<Equals> matchers = new ArrayList<>();
        matchers.add(new Equals("arg1"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = new Invocation(null, "testMethod", null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method method = someMethod; // Define a sample method
        Invocation invocation = new Invocation(null, method.getName(), null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(method, invocationMatcher.getMethod());
    }

    // More test cases for other methods can be added here

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation(null, "testMethod", new Object[]{"arg1"});
        List<Equals> matchers = new ArrayList<>();
        matchers.add(new Equals("arg1"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Invocation i = new Invocation(null, "testMethod", new Object[]{"capturedArg"});
        ((CapturesArguments)matchers.get(0)).captureFrom(i.getArguments()[0]);

        assertEquals("capturedArg", matchers.get(0).getCapturedValue());
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation(null, "testMethod", null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals("toString result", invocationMatcher.toString());
    }

    // Edge cases and exception handling test cases can be added here

}