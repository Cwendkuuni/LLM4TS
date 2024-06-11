import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.internal.invocation.InvocationMatcher.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.Mockito;

public class InvocationMatcherTest {

    private Invocation invocation;
    private InvocationMatcher invocationMatcher;
    private List<Matcher> matchers;

    @Before
    public void setup() {
        invocation = Mockito.mock(Invocation.class);
        matchers = Arrays.asList(mock(Matcher.class));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = Mockito.mock(Method.class);
        when(invocation.getMethod()).thenReturn(expectedMethod);

        Method actualMethod = invocationMatcher.getMethod();

        assertThat(actualMethod, is(expectedMethod));
    }

    @Test
    public void testGetInvocation() {
        Invocation actualInvocation = invocationMatcher.getInvocation();

        assertThat(actualInvocation, is(invocation));
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        assertThat(actualMatchers, is(matchers));
    }

    @Test
    public void testToString() {
        String expectedString = "test invocation";
        when(invocation.toString(matchers, new PrintSettings())).thenReturn(expectedString);

        String actualString = invocationMatcher.toString();

        assertThat(actualString, is(expectedString));
    }

    @Test
    public void testMatches() {
        Invocation actualInvocation = Mockito.mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(Method.class);
        when(actualInvocation.getMethod()).thenReturn(Method.class);
        when(invocation.argumentsMatch(actualInvocation)).thenReturn(true);

        boolean matches = invocationMatcher.matches(actualInvocation);

        assertThat(matches, is(true));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = Mockito.mock(Invocation.class);
        when(invocation.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(invocation.hasSameMethod(candidate)).thenReturn(true);
        when(invocation.safelyArgumentsMatch(candidate.getArguments())).thenReturn(true);

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertThat(hasSimilarMethod, is(false));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method1, method2);

        boolean hasSameMethod1 = invocationMatcher.hasSameMethod(candidate);

        assertThat(hasSameMethod1, is(true));

        boolean hasSameMethod2 = invocationMatcher.hasSameMethod(candidate);

        assertThat(hasSameMethod2, is(false));
    }

    @Test
    public void testGetLocation() {
        Location expectedLocation = Mockito.mock(Location.class);
        when(invocation.getLocation()).thenReturn(expectedLocation);

        Location actualLocation = invocationMatcher.getLocation();

        assertThat(actualLocation, is(expectedLocation));
    }

    @Test
    public void testToStringWithPrintSettings() {
        String expectedString = "test invocation";
        PrintSettings printSettings = Mockito.mock(PrintSettings.class);
        when(invocation.toString(matchers, printSettings)).thenReturn(expectedString);

        String actualString = invocationMatcher.toString(printSettings);

        assertThat(actualString, is(expectedString));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = Mockito.mock(Invocation.class);
        Matcher m = Mockito.mock(Matcher.class);
        CapturesArguments capturesArguments = Mockito.mock(CapturesArguments.class);
        when(matchers.get(0)).thenReturn(m);
        when(m instanceof CapturesArguments).thenReturn(true);
        when(invocation.getArguments()).thenReturn(new Object[]{});

        invocationMatcher.captureArgumentsFrom(i);

        verify(capturesArguments).captureFrom(any());
    }
}