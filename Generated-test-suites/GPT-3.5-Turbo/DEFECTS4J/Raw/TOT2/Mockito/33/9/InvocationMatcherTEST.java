import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Method method = Mockito.mock(Method.class);
        Invocation invocation = new Invocation(method);
        InvocationMatcher matcher = new InvocationMatcher(invocation, new ArrayList<Matcher>());

        assertEquals(method, matcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Method method = Mockito.mock(Method.class);
        Invocation invocation = new Invocation(method);
        InvocationMatcher matcher = new InvocationMatcher(invocation, new ArrayList<Matcher>());

        assertEquals(invocation, matcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Method method = Mockito.mock(Method.class);
        Invocation invocation = new Invocation(method);
        List<Matcher> matchers = Arrays.asList(Mockito.mock(Matcher.class));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertEquals(matchers, matcher.getMatchers());
    }

    @Test
    public void testToString() {
        Method method = Mockito.mock(Method.class);
        Invocation invocation = new Invocation(method);
        InvocationMatcher matcher = new InvocationMatcher(invocation, new ArrayList<Matcher>());

        String expectedString = "Mockito invocation toString";
        assertEquals(expectedString, matcher.toString());
    }

    // Add more test cases here for other methods

    @Test
    public void testCaptureArgumentsFrom() {
        Method method = Mockito.mock(Method.class);
        Invocation invocation = new Invocation(method);
        List<Matcher> matchers = Arrays.asList(Mockito.mock(Matcher.class));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Invocation i = Mockito.mock(Invocation.class);
        matcher.captureArgumentsFrom(i);

        // Add assertions for CapturesArguments logic
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(Mockito.mock(Invocation.class), Mockito.mock(Invocation.class));
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        // Add assertions for creating InvocationMatchers from Invocations
    }

    // Add additional test cases for edge cases and exceptions

}