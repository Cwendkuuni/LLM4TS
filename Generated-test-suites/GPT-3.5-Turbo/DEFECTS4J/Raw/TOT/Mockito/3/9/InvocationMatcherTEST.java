import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<>();
    }

    @Test
    public void testInvocationMatcherConstructorWithMatchers() {
        matchers.add(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcherConstructorWithoutMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        PrintSettings printSettings = mock(PrintSettings.class);
        whenNew(PrintSettings.class).withNoArguments().thenReturn(printSettings);
        when(printSettings.print(matchers, invocation)).thenReturn("toString");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals("toString", invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(new Object());
        when(actual.getMock()).thenReturn(new Object());
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(mock(ArgumentsComparator.class));
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(mock(ArgumentsComparator.class));
        when(mock(ArgumentsComparator.class).argumentsMatch(any(), any())).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(method1.getName()).thenReturn("method1");
        when(method2.getName()).thenReturn("method2");
        when(invocation.getMethod()).thenReturn(method1);
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(new Object());
        when(candidate.getMethod()).thenReturn(method1);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(new Object());
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(mock(ArgumentsComparator.class));
        when(mock(ArgumentsComparator.class).argumentsMatch(any(), any())).thenReturn(true).thenReturn(false);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));

        invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));

        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));

        when(invocation.getMethod()).thenReturn(method2);
        when(candidate.getMethod()).thenReturn(method2);
        invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(method1.getName()).thenReturn("method1");
        when(method2.getName()).thenReturn("method2");
        when(invocation.getMethod()).thenReturn(method1);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertTrue(invocationMatcher.hasSameMethod(invocation));

        when(invocation.getMethod()).thenReturn(method2);
        assertFalse(invocationMatcher.hasSameMethod(invocation));

        when(invocation.getMethod()).thenReturn(method1);
        when(invocation.getMethod()).thenReturn(method2);
        assertFalse(invocationMatcher.hasSameMethod(invocation));

        when(invocation.getMethod()).thenReturn(method2);
        when(invocation.getMethod()).thenReturn(method2);
        assertFalse(invocationMatcher.hasSameMethod(invocation));
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation1 = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().isVarArgs()).thenReturn(true);
        when(invocation.getRawArguments()).thenReturn(new Object[]{});
        when(invocation.getArgumentAt(eq(0), any())).thenReturn(new Object());
        when(invocation.getArgumentAt(eq(1), any())).thenReturn(new Object());
        when(invocation.getArgumentAt(eq(2), any())).thenReturn(new Object());
        when(matchers.get(anyInt())).thenReturn(mock(CapturesArguments.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(invocation1);

        verify((CapturesArguments) matchers.get(0)).captureFrom(any());
        verify((CapturesArguments) matchers.get(1)).captureFrom(any());
        verify((CapturesArguments) matchers.get(2)).captureFrom(any());

        reset(matchers);
        invocation1 = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().isVarArgs()).thenReturn(false);
        when(invocation.getArgumentAt(anyInt(), any())).thenReturn(new Object());
        when(matchers.get(anyInt())).thenReturn(mock(CapturesArguments.class));

        invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(invocation1);

        for (int i = 0; i < matchers.size(); i++) {
            verify((CapturesArguments) matchers.get(i)).captureFrom(any());
        }
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        InvocationMatcher invocationMatcher1 = mock(InvocationMatcher.class);
        InvocationMatcher invocationMatcher2 = mock(InvocationMatcher.class);
        whenNew(InvocationMatcher.class).withArguments(invocation1).thenReturn(invocationMatcher1);
        whenNew(InvocationMatcher.class).withArguments(invocation2).thenReturn(invocationMatcher2);

        List<InvocationMatcher> expectedMatchers = Arrays.asList(invocationMatcher1, invocationMatcher2);

        List<InvocationMatcher> actualMatchers = InvocationMatcher.createFrom(invocations);
        assertEquals(expectedMatchers, actualMatchers);
    }

}