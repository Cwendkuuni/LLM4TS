import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructor() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithNoMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        PrintSettings printSettings = mock(PrintSettings.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.toString();

        verify(printSettings, times(1)).print(matchers, invocation);
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(invocation.getMock());
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(invocation.getMethod());
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(invocation, actual)).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.matches(actual);

        assertTrue(result);
    }

    @Test
    public void testSafelyArgumentsMatch() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        Object[] actualArgs = new Object[] {1, 2, 3};
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(invocation, actualArgs)).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);

        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(invocation.getArguments()).thenReturn(new Object[] {1, 2, 3});
        when(candidate.getArguments()).thenReturn(new Object[] {1, 2, 3});
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(invocation, candidate)).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertTrue(result);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertTrue(result);
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Location result = invocationMatcher.getLocation();

        assertEquals(location, result);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getRawArguments()).thenReturn(new Object[] {1, 2, 3, 4});
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        Matcher matcher = mock(Matcher.class);
        when(matcher instanceof CapturesArguments).thenReturn(true);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(invocation);

        verify(capturesArguments, times(3)).captureFrom(any());
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);

        assertEquals(2, result.size());
    }
}