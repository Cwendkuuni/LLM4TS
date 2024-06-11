import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentMatcher;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.hamcrest.Matcher;
import org.mockito.internal.reporting.PrintSettings;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(ArgumentMatcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(method, invocationMatcher.getMethod());
    }

    // Additional test cases for other methods should be added here

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Invocation i = mock(Invocation.class);
        ArgumentMatcher matcher = mock(ArgumentMatcher.class);
        when(i.getArguments()).thenReturn(new Object[]{"arg1"});
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.singletonList(matcher));

        invocationMatcher.captureArgumentsFrom(i);

        verify(matcher).captureFrom("arg1");
    }

    // Edge cases and exception handling scenarios can be tested here

}