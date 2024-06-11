import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.internal.reporting.PrintSettings;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Collections;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = createMock(Invocation.class);
        List<Matcher> matchers = createMock(List.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = createMock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = createMock(Invocation.class);
        Method method = createMock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        Invocation invocation = createMock(Invocation.class);
        List<Matcher> matchers = createMock(List.class);
        PrintSettings printSettings = createMock(PrintSettings.class);
        when(invocation.getArguments()).thenReturn(new Object[] { "arg1", "arg2" });
        when(printSettings.print(matchers, invocation)).thenReturn("InvocationMatcher String");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.toString();

        verify(printSettings).print(matchers, invocation);
    }

    @Test
    public void testMatches() {
        Invocation invocation = createMock(Invocation.class);
        Invocation actual = createMock(Invocation.class);
        Object[] expectedArgs = new Object[] { "arg1", "arg2" };
        Object[] actualArgs = new Object[] { "arg1", "arg2" };
        when(invocation.getMock()).thenReturn("mock");
        when(actual.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(createMock(Method.class));
        when(invocation.getArguments()).thenReturn(expectedArgs);
        when(actual.getArguments()).thenReturn(actualArgs);
        when(new ArgumentsComparator().argumentsMatch(any(InvocationMatcher.class), any(Invocation.class))).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());
        boolean result = invocationMatcher.matches(actual);

        assertTrue(result);
        verify(invocation).getMock();
        verify(actual).getMock();
        verify(invocation).getMethod();
        verify(invocation).getArguments();
        verify(actual).getArguments();
        verify(new ArgumentsComparator()).argumentsMatch(invocationMatcher, actual);
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = createMock(Invocation.class);
        Object[] actualArgs = new Object[] { "arg1", "arg2" };
        when(new ArgumentsComparator().argumentsMatch(any(InvocationMatcher.class), aryEq(actualArgs))).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());
        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);

        assertTrue(result);
        verify(new ArgumentsComparator()).argumentsMatch(invocationMatcher, actualArgs);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = createMock(Invocation.class);
        Invocation candidate = createMock(Invocation.class);
        Method method = createMock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");
        when(new ArgumentsComparator().argumentsMatch(any(InvocationMatcher.class), any(Object[].class))).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertTrue(result);
        verify(invocation).getMethod();
        verify(candidate).getMethod();
        verify(invocation).isVerified();
        verify(candidate).isVerified();
        verify(invocation).getMock();
        verify(candidate).getMock();
        verify(new ArgumentsComparator()).argumentsMatch(invocationMatcher, candidate.getArguments());
    }

    @Test
    public void testHasSimilarMethodOverloadedButSameArgs() {
        Invocation invocation = createMock(Invocation.class);
        Invocation candidate = createMock(Invocation.class);
        Method method = createMock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        when(invocation.getName()).thenReturn("methodName");
        when(candidate.getName()).thenReturn("methodName");
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");
        when(new ArgumentsComparator().argumentsMatch(any(InvocationMatcher.class), any(Object[].class))).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
        verify(invocation).getMethod();
        verify(candidate).getMethod();
        verify(invocation).getName();
        verify(candidate).getName();
        verify(invocation).isVerified();
        verify(candidate).isVerified();
        verify(invocation).getMock();
        verify(candidate).getMock();
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = createMock(Invocation.class);
        Invocation candidate = createMock(Invocation.class);
        Method method = createMock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("methodName");
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(false);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());
        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertTrue(result);
        verify(invocation, times(3)).getMethod();
        verify(candidate, times(2)).getMethod();
        verify(method).getName();
        verify(invocation).isVerified();
        verify(candidate).isVerified();
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = createMock(Invocation.class);
        Location location = createMock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(location, invocationMatcher.getLocation());
        verify(invocation).getLocation();
    }
}