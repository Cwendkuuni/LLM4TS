import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.mock.MockCreationSettings;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    // 

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertSame(invocation, invocationMatcher.getInvocation());
        assertSame(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertSame(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() throws NoSuchMethodException {
        Method method = String.class.getMethod("equals", Object.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertSame(method, invocationMatcher.getMethod());
    }

    @Test
    public void testMatches_ReturnsTrue() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        when(invocation1.getMethod()).thenReturn(String.class.getMethod("equals", Object.class));
        when(invocation2.getMethod()).thenReturn(String.class.getMethod("equals", Object.class));
        when(invocation1.getArguments()).thenReturn(new Object[] { "Test" });
        when(invocation2.getArguments()).thenReturn(new Object[] { "Test" });

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.matches(invocation2);

        assertTrue(result);
    }

    @Test
    public void testMatches_ReturnsFalse() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        when(invocation1.getMethod()).thenReturn(String.class.getMethod("equals", Object.class));
        when(invocation2.getMethod()).thenReturn(String.class.getMethod("hashCode"));
        when(invocation1.getArguments()).thenReturn(new Object[] { "Test" });
        when(invocation2.getArguments()).thenReturn(new Object[0]);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.matches(invocation2);

        assertFalse(result);
    }

    // 
}