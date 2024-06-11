import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationMatcher;
import org.mockito.internal.invocation.ArgumentsComparator;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    private Invocation invocationMock;
    private Method methodMock;
    private InvocationMatcher invocationMatcher;
    private List<Matcher> matchers;

    @Before
    public void setUp() throws Exception {
        invocationMock = mock(Invocation.class);
        methodMock = mock(Method.class);
        matchers = Collections.emptyList();
        invocationMatcher = new InvocationMatcher(invocationMock, matchers);
    }

    @Test
    public void testGetMethod() {
        assertEquals(invocationMock.getMethod(), invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        assertEquals(invocationMock, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        String expected = invocationMock.toString(matchers, new PrintSettings());
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        when(invocationMock.getMock()).thenReturn(mock(Object.class));
        when(invocationMock.getMethod()).thenReturn(methodMock);
        when(methodMock.getName()).thenReturn("testMethod");
        when(invocationMock.getArguments()).thenReturn(new Object[0]);
        when(actual.getMock()).thenReturn(invocationMock.getMock());
        when(actual.getMethod()).thenReturn(methodMock);
        when(actual.getArguments()).thenReturn(new Object[0]);
        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        when(invocationMock.getMethod()).thenReturn(methodMock);
        when(methodMock.getName()).thenReturn("testMethod");
        when(candidate.getMethod()).thenReturn(methodMock);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocationMock.getMock());
        when(candidate.getArguments()).thenReturn(new Object[0]);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        when(invocationMock.getMethod()).thenReturn(methodMock);
        when(candidate.getMethod()).thenReturn(methodMock);
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Location locationMock = mock(Location.class);
        when(invocationMock.getLocation()).thenReturn(locationMock);
        assertEquals(locationMock, invocationMatcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettingsMock = mock(PrintSettings.class);
        String expected = invocationMock.toString(matchers, printSettingsMock);
        assertEquals(expected, invocationMatcher.toString(printSettingsMock));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(matchers.get(0)).thenReturn(capturesArguments);
        Object argument = new Object();
        Object[] arguments = { argument };
        when(i.getArguments()).thenReturn(arguments);
        invocationMatcher.captureArgumentsFrom(i);
        verify(capturesArguments).captureFrom(argument);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<Invocation>();
        invocations.add(invocationMock);
        List<InvocationMatcher> expected = new LinkedList<InvocationMatcher>();
        expected.add(invocationMatcher);
        assertEquals(expected, InvocationMatcher.createFrom(invocations));
    }

}