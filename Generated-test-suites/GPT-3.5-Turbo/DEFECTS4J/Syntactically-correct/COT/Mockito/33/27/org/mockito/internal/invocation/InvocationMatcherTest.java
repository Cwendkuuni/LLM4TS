
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Method method = matcher.getMethod();
        assertEquals(invocation.getMethod(), method);
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation actualInvocation = matcher.getInvocation();
        assertEquals(invocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        List<Matcher> actualMatchers = matcher.getMatchers();
        assertEquals(matchers, actualMatchers);
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        String expectedToString = invocation.toString(matchers, new PrintSettings());
        String actualToString = matcher.toString();
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(argumentsComparator.argumentsMatch(any(), any())).thenReturn(true);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        boolean matches = matcher.matches(actual);
        assertTrue(matches);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(argumentsComparator.argumentsMatch(any(), any())).thenReturn(true);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        boolean hasSimilarMethod = matcher.hasSimilarMethod(candidate);
        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        boolean hasSameMethod = matcher.hasSameMethod(candidate);
        assertTrue(hasSameMethod);
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Location actualLocation = matcher.getLocation();
        assertEquals(location, actualLocation);
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        PrintSettings printSettings = mock(PrintSettings.class);
        String expectedToString = invocation.toString(matchers, printSettings);
        String actualToString = matcher.toString(printSettings);
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        InvocationMatcher matcher = mock(InvocationMatcher.class);
        Invocation invocation = mock(Invocation.class);
        Matcher matcher1 = mock(Matcher.class);
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(matcher.getMatchers()).thenReturn(new ArrayList<>(Arrays.asList(matcher1)));
        when(matcher1 instanceof CapturesArguments).thenReturn(true);
        matcher.captureArgumentsFrom(invocation);
        verify((CapturesArguments) matcher1).captureFrom(any());
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(mock(Invocation.class));
        List<InvocationMatcher> expectedMatchers = new ArrayList<>();
        InvocationMatcher matcher = mock(InvocationMatcher.class);
        expectedMatchers.add(matcher);
        when(matcher.getInvocation()).thenReturn(invocations.get(0));
        List<InvocationMatcher> actualMatchers = InvocationMatcher.createFrom(invocations);
        assertEquals(expectedMatchers, actualMatchers);
    }
}

