import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.invocation.Invocation;
import org.mockito.matchers.CapturesArguments;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testConstructorWithInvocation() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = createMatchers();
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocationEmptyMatchers() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.argumentsToMatchers()).thenReturn(createMatchers());
        invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(createMatchers(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.toString(any(List.class), any(PrintSettings.class))).thenReturn("mock invocation");
        invocationMatcher = new InvocationMatcher(invocation);

        assertEquals("mock invocation", invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = createInvocation();
        Invocation invocation2 = createInvocation();

        assertTrue(invocationMatcher.matches(invocation1));
        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = createInvocation();
        Invocation candidate1 = createInvocation();
        Invocation candidate2 = createInvocation();
        when(candidate2.isVerified()).thenReturn(true);
        Invocation candidate3 = createInvocation();
        when(candidate3.getMock()).thenReturn(mock(Object.class));
        Invocation candidate4 = createInvocation();
        when(candidate4.getMethod()).thenReturn(mock(Method.class));

        assertTrue(invocationMatcher.hasSimilarMethod(candidate1));
        assertFalse(invocationMatcher.hasSimilarMethod(candidate2));
        assertFalse(invocationMatcher.hasSimilarMethod(candidate3));
        assertFalse(invocationMatcher.hasSimilarMethod(candidate4));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = createInvocation();
        Invocation candidate1 = createInvocation();
        Invocation candidate2 = createInvocation();
        when(candidate2.getMethod()).thenReturn(null);

        assertTrue(invocationMatcher.hasSameMethod(candidate1));
        assertFalse(invocationMatcher.hasSameMethod(candidate2));
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getLocation()).thenReturn(location);
        invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.toString(any(List.class), any(PrintSettings.class))).thenReturn("mock invocation");
        invocationMatcher = new InvocationMatcher(invocation);

        PrintSettings printSettings = mock(PrintSettings.class);
        assertEquals("mock invocation", invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getArguments()).thenReturn(new Object[]{1, 2});
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        List<Matcher> matchers = Arrays.asList(capturesArguments);
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation i = mock(Invocation.class);
        when(i.getArguments()).thenReturn(new Object[]{3, 4});

        invocationMatcher.captureArgumentsFrom(i);

        verify(capturesArguments).captureFrom(3);
        verifyNoMoreInteractions(capturesArguments);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(invocations.size(), invocationMatchers.size());
        assertTrue(invocationMatchers.get(0) instanceof InvocationMatcher);
    }

    private Invocation createInvocation() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        return invocation;
    }

    private List<Matcher> createMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        return matchers;
    }
}