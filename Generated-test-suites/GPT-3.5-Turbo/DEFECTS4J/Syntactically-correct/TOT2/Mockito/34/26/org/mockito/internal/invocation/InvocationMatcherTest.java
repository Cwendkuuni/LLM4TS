import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Invocation mockInvocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, matchers);

        Method method = invocationMatcher.getMethod();

        assertEquals(mockInvocation.getMethod(), method);
    }

    @Test
    public void testGetInvocation() {
        Invocation mockInvocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, matchers);

        Invocation actualInvocation = invocationMatcher.getInvocation();

        assertEquals(mockInvocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        Invocation mockInvocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, matchers);

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        assertEquals(matchers, actualMatchers);
    }

    // Additional test cases for edge cases and exceptions handling can be added here

}