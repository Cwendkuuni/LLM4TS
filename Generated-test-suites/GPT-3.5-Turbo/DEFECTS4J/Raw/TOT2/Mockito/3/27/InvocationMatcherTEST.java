import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(anyString());
        InvocationMatcher invMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invMatcher.getInvocation());
        assertEquals(matchers, invMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcherWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invMatcher.getInvocation());
        assertEquals(ArgumentsProcessor.argumentsToMatchers(invocation.getArguments()), invMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher invMatcher = new InvocationMatcher(invocation);

        assertEquals(method, invMatcher.getMethod());
    }

    // Add more test cases for other methods as required
}