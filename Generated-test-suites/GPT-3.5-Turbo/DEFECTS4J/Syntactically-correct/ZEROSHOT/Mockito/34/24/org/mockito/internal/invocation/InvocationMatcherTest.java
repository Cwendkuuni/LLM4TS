import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.exceptions.VerificationAwareInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        matchers = mock(List.class);
    }

    @Test
    public void testConstructor1() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertNotNull(invocationMatcher);
    }

    @Test
    public void testConstructor2() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertNotNull(invocationMatcher);
    }

    @Test
    public void testGetMethod() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Method method = invocationMatcher.getMethod();
        assertNotNull(method);
    }

    @Test
    public void testGetInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation inv = invocationMatcher.getInvocation();
        assertNotNull(inv);
    }

    @Test
    public void testGetMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertNotNull(matchers);
    }

    @Test
    public void testToString() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String str = invocationMatcher.toString();
        assertNotNull(str);
    }

    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.matches(actual);
        assertFalse(result);
    }

    @Test
    public void testSafelyArgumentsMatch() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.safelyArgumentsMatch(null);
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        assertFalse(result);
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.hasSameMethod(candidate);
        assertFalse(result);
    }

    @Test
    public void testGetLocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Location location = invocationMatcher.getLocation();
        assertNull(location);
    }

    @Test
    public void testToString2() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        PrintSettings printSettings = mock(PrintSettings.class);
        String str = invocationMatcher.toString(printSettings);
        assertNotNull(str);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(matchers.get(anyInt())).thenReturn(capturesArguments);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(i);
        verify(capturesArguments, times(1)).captureFrom(any());
    }
}