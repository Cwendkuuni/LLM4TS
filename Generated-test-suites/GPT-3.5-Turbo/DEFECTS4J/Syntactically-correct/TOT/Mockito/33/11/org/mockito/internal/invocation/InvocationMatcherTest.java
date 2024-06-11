import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
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

    @Before
    public void setup() {
        invocation = mock(Invocation.class);
        matchers = Collections.emptyList();
    }

    @Test
    public void testConstructorWithMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertNotNull(invocationMatcher.getMatchers());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method expectedMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(expectedMethod);

        assertEquals(expectedMethod, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(matchers, printSettings)).thenReturn("toString value");
        
        assertEquals("toString value", invocationMatcher.toString());
    }

    @Test
    public void testMatches_withMatchingInvocation() {
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn("mock");
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));

        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(invocation, actualInvocation)).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.matches(actualInvocation);

        assertTrue(result);
    }

    @Test
    public void testMatches_withDifferentMock() {
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn("differentMock");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.matches(actualInvocation);

        assertFalse(result);
    }

    @Test
    public void testMatches_withDifferentMethod() {
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn("mock");
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.matches(actualInvocation);

        assertFalse(result);
    }

    @Test
    public void testSafelyArgumentsMatch_withSuccessfulComparison() {
        Object[] actualArgs = new Object[1];
        
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(invocation, actualArgs)).thenReturn(true);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);
        
        assertTrue(result);
    }

    @Test
    public void testSafelyArgumentsMatch_withException() {
        Object[] actualArgs = new Object[1];
        
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(invocation, actualArgs)).thenThrow(new RuntimeException());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);
        
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withDifferentMethodName() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withVerifiedCandidate() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(true);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withDifferentMock() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn("differentMock");
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withOverloadedButSameArgs() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn("mock");
        when(candidate.getArguments()).thenReturn(new Object[0]);
        
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(invocation, candidate.getArguments())).thenReturn(true);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withDifferentMethodAndArgs() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn("mock");
        when(candidate.getArguments()).thenReturn(new Object[0]);
        
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(invocation, candidate.getArguments())).thenReturn(false);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertTrue(result);
    }

    @Test
    public void testHasSameMethod_withSameMethod() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        
        Method m1 = mock(Method.class);
        Method m2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(m1);
        when(candidate.getMethod()).thenReturn(m2);
        when(m1.equals(m2)).thenReturn(true);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.hasSameMethod(candidate);
        
        assertTrue(result);
    }

    @Test
    public void testHasSameMethod_withDifferentMethod() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        
        Method m1 = mock(Method.class);
        Method m2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(m1);
        when(candidate.getMethod()).thenReturn(m2);
        when(m1.equals(m2)).thenReturn(false);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = invocationMatcher.hasSameMethod(candidate);
        
        assertFalse(result);
    }

    @Test
    public void testGetLocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Location expectedLocation = mock(Location.class);
        when(invocation.getLocation()).thenReturn(expectedLocation);
        
        assertEquals(expectedLocation, invocationMatcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(matchers, printSettings)).thenReturn("toString value");
        
        assertEquals("toString value", invocationMatcher.toString(printSettings));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        when(i.getArguments()).thenReturn(new Object[] {1, 2, 3});
        
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        matchers = new ArrayList<Matcher>();
        matchers.add(matcher1);
        matchers.add(matcher2);
        
        CapturesArguments capturesArguments1 = mock(CapturesArguments.class);
        CapturesArguments capturesArguments2 = mock(CapturesArguments.class);
        when(capturesArguments1.captureFrom(1)).thenReturn(true);
        when(capturesArguments2.captureFrom(2)).thenReturn(true);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(i);
        
        verify(capturesArguments1).captureFrom(1);
        verify(capturesArguments2).captureFrom(2);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<Invocation>();
        invocations.add(mock(Invocation.class));
        invocations.add(mock(Invocation.class));
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(invocations.size(), invocationMatchers.size());
        assertTrue(invocationMatchers.get(0) instanceof InvocationMatcher);
        assertTrue(invocationMatchers.get(1) instanceof InvocationMatcher);
    }
}