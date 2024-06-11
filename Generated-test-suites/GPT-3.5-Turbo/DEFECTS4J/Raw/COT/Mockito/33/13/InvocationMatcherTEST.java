import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.singletonList(matcher));
        assertEquals(Collections.singletonList(matcher), invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        String expected = invocation.toString(Collections.emptyList(), new PrintSettings());
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.setArgumentsComparator(argumentsComparator);

        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation.getLocation(), invocationMatcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        PrintSettings printSettings = mock(PrintSettings.class);
        String expected = invocation.toString(Collections.emptyList(), printSettings);
        assertEquals(expected, invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Invocation i = mock(Invocation.class);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        Matcher matcher = mock(Matcher.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.singletonList(matcher));

        invocationMatcher.captureArgumentsFrom(i);

        assertTrue(capturesArguments.captureFromCalled());
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation = mock(Invocation.class);
        List<Invocation> invocations = Collections.singletonList(invocation);
        List<InvocationMatcher> expected = Collections.singletonList(new InvocationMatcher(invocation));
        assertEquals(expected, InvocationMatcher.createFrom(invocations));
    }
}