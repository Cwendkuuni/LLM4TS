import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);

        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertThat(invocationMatcher.getMethod(), is(method));
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertThat(invocationMatcher.getInvocation(), is(invocation));
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertThat(invocationMatcher.getMatchers(), is(invocation.argumentsToMatchers()));
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        List<Matcher> matchers = Collections.emptyList();
        PrintSettings printSettings = mock(PrintSettings.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertThat(invocationMatcher.toString(printSettings), is(invocation.toString(matchers, printSettings)));
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);

        Object[] args = {1, "test"};
        when(actual.getArguments()).thenReturn(args);

        boolean expectedResult = true;
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Invocation.class))).thenReturn(expectedResult);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertThat(invocationMatcher.matches(actual), is(expectedResult));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);

        boolean expectedResult = true;

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(new Object());

        assertThat(invocationMatcher.hasSimilarMethod(candidate), is(expectedResult));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertThat(invocationMatcher.hasSameMethod(candidate), is(method.equals(method)));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertThat(invocationMatcher.getLocation(), is(location));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Object[] arguments = new Object[]{"test", 1};
        when(i.getArguments()).thenReturn(arguments);

        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(capturesArguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(mock(Invocation.class), matchers);

        invocationMatcher.captureArgumentsFrom(i);

        verify(capturesArguments, times(1)).captureFrom(arguments[0]);
        verifyNoMoreInteractions(capturesArguments);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation = mock(Invocation.class);
        List<Invocation> invocations = Collections.singletonList(invocation);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertThat(invocationMatchers.size(), is(1));
        assertThat(invocationMatchers.get(0).getInvocation(), is(invocation));
    }
}