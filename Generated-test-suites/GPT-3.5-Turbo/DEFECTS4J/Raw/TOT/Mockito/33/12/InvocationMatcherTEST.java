import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invokeMatcher;

    @Before
    public void setUp() {
        invocation = Mockito.mock(Invocation.class);
        matchers = new ArrayList<Matcher>();
        invokeMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        assertNotNull(invokeMatcher);
        assertEquals(invocation, invokeMatcher.getInvocation());
        assertEquals(matchers, invokeMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocation() {
        InvocationMatcher invokeMatcherWithoutMatchers = new InvocationMatcher(invocation);
        assertNotNull(invokeMatcherWithoutMatchers);
        assertEquals(Collections.<Matcher>emptyList(), invokeMatcherWithoutMatchers.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        assertEquals(method, invokeMatcher.getMethod());
    }

    @Test
    public void testMatchesWithMatchingInvocation() {
        Invocation actualInvocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMock()).thenReturn(actualInvocation.getMock());
        Mockito.when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        Mockito.when(invocation.getMethod()).thenReturn(actualInvocation.getMethod());
        Mockito.when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        Mockito.when(invocation.argumentsMatch(actualInvocation)).thenReturn(true);
        assertTrue(invokeMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatchesWithNonMatchingInvocation() {
        Invocation actualInvocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMock()).thenReturn(actualInvocation.getMock());
        Mockito.when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        Mockito.when(invocation.getMethod()).thenReturn(actualInvocation.getMethod());
        Mockito.when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        Mockito.when(invocation.argumentsMatch(actualInvocation)).thenReturn(false);
        assertFalse(invokeMatcher.matches(actualInvocation));
    }

    @Test
    public void testHasSimilarMethodWithSameMethod() {
        Invocation candidate = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(candidate.getMethod());
        Mockito.when(candidate.getMethod()).thenReturn(invocation.getMethod());
        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(invocation.isVerified()).thenReturn(false);
        Mockito.when(candidate.getMock()).thenReturn(invocation.getMock());
        Mockito.when(invocation.getMock()).thenReturn(candidate.getMock());
        Mockito.when(candidate.getArguments()).thenReturn(new Object[] {});
        Mockito.when(invocation.argumentsMatch(candidate.getArguments())).thenReturn(true);
        assertFalse(invokeMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethod() {
        Invocation candidate = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(candidate.getMethod());
        Mockito.when(candidate.getMethod()).thenReturn(invocation.getMethod());
        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(invocation.isVerified()).thenReturn(false);
        Mockito.when(candidate.getMock()).thenReturn(invocation.getMock());
        Mockito.when(invocation.getMock()).thenReturn(candidate.getMock());
        Mockito.when(candidate.getArguments()).thenReturn(new Object[] {});
        Mockito.when(invocation.argumentsMatch(candidate.getArguments())).thenReturn(false);
        assertTrue(invokeMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethodName() {
        Invocation candidate = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(candidate.getMethod());
        Mockito.when(candidate.getMethod()).thenReturn(invocation.getMethod());
        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(invocation.isVerified()).thenReturn(false);
        Mockito.when(candidate.getMock()).thenReturn(invocation.getMock());
        Mockito.when(invocation.getMock()).thenReturn(candidate.getMock());
        Mockito.when(invocation.getMethod().getName()).thenReturn("method1");
        Mockito.when(candidate.getMethod().getName()).thenReturn("method2");
        assertFalse(invokeMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Location location = Mockito.mock(Location.class);
        Mockito.when(invocation.getLocation()).thenReturn(location);
        assertEquals(location, invokeMatcher.getLocation());
    }

    @Test
    public void testToString() {
        PrintableInvocation printableInvocation = Mockito.mock(PrintableInvocation.class);
        PrintSettings printSettings = Mockito.mock(PrintSettings.class);
        Mockito.when(printableInvocation.toString(matchers, printSettings)).thenReturn("toString");
        assertEquals("toString", invokeMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[] {};
        Mockito.when(i.getArguments()).thenReturn(arguments);
        CapturesArguments capturesArguments = Mockito.mock(CapturesArguments.class);
        matchers.add(capturesArguments);
        invokeMatcher.captureArgumentsFrom(i);
        Mockito.verify(capturesArguments, Mockito.times(1)).captureFrom(arguments[0]);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Invocation> invocations = new ArrayList<Invocation>();
        invocations.add(invocation1);
        invocations.add(invocation2);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertEquals(2, invocationMatchers.size());
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }
}