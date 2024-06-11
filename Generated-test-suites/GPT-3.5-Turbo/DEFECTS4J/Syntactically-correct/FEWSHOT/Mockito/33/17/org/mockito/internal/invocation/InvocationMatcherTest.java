package org.mockito.internal.invocation;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.hamcrest.Matcher;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;


public class InvocationMatcherTest {
    
    private Invocation invocation;
    private InvocationMatcher invocationMatcher;
    
    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        invocationMatcher = new InvocationMatcher(invocation);
    }
    
    @Test
    public void testGetMethod() {
        Method method = invocation.getMethod();
        // TODO: Add assertions
    }
    
    @Test
    public void testGetInvocation() {
        Invocation inv = invocationMatcher.getInvocation();
        // TODO: Add assertions
    }
    
    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = invocationMatcher.getMatchers();
        // TODO: Add assertions
    }
    
    @Test
    public void testToString() {
        String str = invocationMatcher.toString();
        // TODO: Add assertions
    }
    
    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        boolean result = invocationMatcher.matches(actual);
        // TODO: Add assertions
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        // TODO: Add assertions
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        boolean result = invocationMatcher.hasSameMethod(candidate);
        // TODO: Add assertions
    }
    
    @Test
    public void testGetLocation() {
        Location location = invocationMatcher.getLocation();
        // TODO: Add assertions
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = mock(PrintSettings.class);
        String str = invocationMatcher.toString(printSettings);
        // TODO: Add assertions
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        invocationMatcher.captureArgumentsFrom(i);
        // TODO: Add assertions
    }
    
    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        // TODO: Add assertions
    }
}