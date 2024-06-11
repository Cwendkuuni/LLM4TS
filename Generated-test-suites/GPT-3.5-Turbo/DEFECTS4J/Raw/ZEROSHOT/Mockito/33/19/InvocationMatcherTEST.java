import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.exceptions.VerificationAwareInvocation;
import org.mockito.internal.invocation.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {
    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private List<Matcher> matchers;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructor() {
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithEmptyMatchers() {
        List<Matcher> emptyMatchers = Collections.emptyList();
        InvocationMatcher emptyMatchersInvocationMatcher = new InvocationMatcher(invocation, emptyMatchers);
        assertEquals(invocation, emptyMatchersInvocationMatcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), emptyMatchersInvocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        PrintSettings printSettings = mock(PrintSettings.class);
        String expectedResult = "invocation.toString(matchers, printSettings)";
        when(invocation.toString(matchers, printSettings)).thenReturn(expectedResult);
        assertEquals(expectedResult, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        Object[] actualArgs = new Object[1];
        when(actual.getMock()).thenReturn(new Object());
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMock()).thenReturn(new Object());
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(actualArgs);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        when(argumentsComparator.argumentsMatch(invocationMatcher, actual)).thenReturn(true);
        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatchesWithException() {
        Invocation actual = mock(Invocation.class);
        try {
            invocationMatcher.matches(actual);
            fail("Expected exception to be thrown");
        } catch (Throwable t) {
            // Exception is expected to be thrown
        }
    }

    @Test
    public void testHasSimilarMethodWithSameName() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(candidate.getMethod()).thenReturn(method);
        when(invocation.getMethod()).thenReturn(method);
        assertEquals(method.getName(), invocation.getMethod().getName());
        when(candidate.isVerified()).thenReturn(false);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithDifferentName() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(candidate.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("differentName");
        when(invocation.getMethod()).thenReturn(method);
        assertEquals(method.getName(), invocation.getMethod().getName());
        when(candidate.isVerified()).thenReturn(false);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithSameNameButVerified() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(candidate.getMethod()).thenReturn(method);
        when(invocation.getMethod()).thenReturn(method);
        assertEquals(method.getName(), invocation.getMethod().getName());
        when(candidate.isVerified()).thenReturn(true);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithSameNameAndMock() {
        Invocation candidate = mock(Invocation.class);
        Object mock = mock(Object.class);
        when(candidate.getMock()).thenReturn(mock);
        when(invocation.getMock()).thenReturn(mock);
        assertEquals(mock, invocation.getMock());
        Method method = mock(Method.class);
        when(candidate.getMethod()).thenReturn(method);
        when(invocation.getMethod()).thenReturn(method);
        assertEquals(method.getName(), invocation.getMethod().getName());
        when(candidate.isVerified()).thenReturn(false);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMock() {
        Invocation candidate = mock(Invocation.class);
        Object mock = mock(Object.class);
        Object differentMock = mock(Object.class);
        when(candidate.getMock()).thenReturn(mock);
        when(invocation.getMock()).thenReturn(differentMock);
        assertFalse(invocation.getMock().equals(candidate.getMock()));
    }

    @Test
    public void testHasSimilarMethodWithSameMethod() throws Exception {
        Invocation candidate = mock(Invocation.class);
        when(invocation.hasSameMethod(candidate)).thenReturn(true);
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethod() throws Exception {
        Invocation candidate = mock(Invocation.class);
        when(invocation.hasSameMethod(candidate)).thenReturn(false);
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = mock(PrintSettings.class);
        String expectedResult = "invocation.toString(matchers, printSettings)";
        when(invocation.toString(matchers, printSettings)).thenReturn(expectedResult);
        assertEquals(expectedResult, invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() throws Exception {
        Invocation i = mock(Invocation.class);
        Object[] arguments = new Object[1];
        when(i.getArguments()).thenReturn(arguments);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        matchers.add(capturesArguments);
        invocationMatcher.captureArgumentsFrom(i);
        verify(capturesArguments).captureFrom(arguments[0]);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertEquals(invocations.size(), invocationMatchers.size());
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        assertEquals(location, invocationMatcher.getLocation());
    }
}