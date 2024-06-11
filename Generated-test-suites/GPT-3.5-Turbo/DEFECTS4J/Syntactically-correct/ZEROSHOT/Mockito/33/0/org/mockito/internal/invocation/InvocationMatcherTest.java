import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testGetMethod() {
        Method expected = mock(Method.class);
        when(invocation.getMethod()).thenReturn(expected);

        Method actual = invocationMatcher.getMethod();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetInvocation() {
        Invocation actual = invocationMatcher.getInvocation();

        assertEquals(invocation, actual);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> actual = invocationMatcher.getMatchers();

        assertEquals(matchers, actual);
    }

    @Test
    public void testToString() {
        String expected = "InvocationMatcher toString";
        when(invocation.toString(matchers, new PrintSettings())).thenReturn(expected);

        String actual = invocationMatcher.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(actual.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        when(invocationMatcher.hasSameMethod(actual)).thenReturn(true);
        when(invocationMatcher.safelyArgumentsMatch(actual.getArguments())).thenReturn(true);

        boolean result = invocationMatcher.matches(actual);

        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(invocationMatcher.hasSameMethod(candidate)).thenReturn(true);
        when(invocation.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        when(invocationMatcher.safelyArgumentsMatch(candidate.getArguments())).thenReturn(true);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertTrue(result);
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        Method m1 = mock(Method.class);
        Method m2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(m1);
        when(candidate.getMethod()).thenReturn(m2);
        when(m1.equals(m2)).thenReturn(true);

        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertTrue(result);
    }

    @Test
    public void testGetLocation() {
        Location expected = mock(Location.class);
        when(invocation.getLocation()).thenReturn(expected);

        Location actual = invocationMatcher.getLocation();

        assertEquals(expected, actual);
    }

    @Test
    public void testToStringWithPrintSettings() {
        String expected = "InvocationMatcher toString with print settings";
        when(invocation.toString(matchers, new PrintSettings())).thenReturn(expected);
        PrintSettings printSettings = mock(PrintSettings.class);

        String actual = invocationMatcher.toString(printSettings);

        assertEquals(expected, actual);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Matcher m1 = mock(CapturesArguments.class);
        Matcher m2 = mock(CapturesArguments.class);
        matchers.add(m1);
        matchers.add(m2);
        Object[] arguments = new Object[]{"arg1", "arg2"};
        when(i.getArguments()).thenReturn(arguments);
        when(m1 instanceof CapturesArguments).thenReturn(true);
        when(m2 instanceof CapturesArguments).thenReturn(true);

        invocationMatcher.captureArgumentsFrom(i);

        for (int k = 0; k < matchers.size(); k++) {
            Matcher m = matchers.get(k);
            if (m instanceof CapturesArguments && arguments.length > k) {
                ((CapturesArguments) m).captureFrom(arguments[k]);
            }
        }
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(mock(Invocation.class));

        List<InvocationMatcher> actual = InvocationMatcher.createFrom(invocations);

        assertEquals(1, actual.size());
    }
}