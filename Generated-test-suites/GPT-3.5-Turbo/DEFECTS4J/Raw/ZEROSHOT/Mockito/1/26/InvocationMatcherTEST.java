import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.internal.invocation.ArgumentsComparator;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
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
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        PrintSettings printSettings = mock(PrintSettings.class);
        when(printSettings.print(matchers, invocation)).thenReturn("testString");
        assertEquals("testString", invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, actualInvocation)).thenReturn(true);

        assertTrue(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, candidate.getArguments())).thenReturn(false);

        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("methodName");
        when(method2.getName()).thenReturn("methodName");
        when(method1.getParameterTypes()).thenReturn(new Class[]{});
        when(method2.getParameterTypes()).thenReturn(new Class[]{});
        
        assertTrue(invocationMatcher.hasSameMethod(invocation));
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().isVarArgs()).thenReturn(false);
        when(invocation.getRawArguments()).thenReturn(new Object[]{});

        invocationMatcher.captureArgumentsFrom(invocation);
        verify(invocation, never()).getArgumentAt(anyInt(), any(Class.class));
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation = mock(Invocation.class);
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(invocations.size(), invocationMatchers.size());
        assertTrue(invocationMatchers.contains(invocationMatcher));
    }
}