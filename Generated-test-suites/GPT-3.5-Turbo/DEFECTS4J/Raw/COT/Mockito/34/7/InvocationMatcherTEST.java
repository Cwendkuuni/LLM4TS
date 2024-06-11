
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.exceptions.PrintableInvocation;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.invocation.Invocation;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    private Invocation invocation;
    private Matcher matcher1;
    private Matcher matcher2;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        matcher1 = mock(Matcher.class);
        matcher2 = mock(Matcher.class);
    }

    @Test
    public void testInvocationMatcher() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Method expectedMethod = invocation.getMethod();
        List<Matcher> expectedMatchers = Collections.emptyList();
        
        assertEquals(expectedMethod, invocationMatcher.getMethod());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(expectedMatchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testInvocationMatcherWithMatchers() {
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        Method expectedMethod = invocation.getMethod();
        List<Matcher> expectedMatchers = matchers;
        
        assertEquals(expectedMethod, invocationMatcher.getMethod());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(expectedMatchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testToString() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());
        
        PrintSettings printSettings = new PrintSettings();
        String expectedResult = invocation.toString(invocationMatcher.getMatchers(), printSettings);
        
        assertEquals(expectedResult, invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        when(actual.getMock()).thenReturn(invocation.getMock());
        when(invocation.getMethod()).thenReturn(actual.getMethod());
        when(invocation.argumentsToMatchers()).thenReturn(Arrays.asList(matcher1, matcher2));
        when(actual.getArguments()).thenReturn(new Object[] {});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[] {};
        when(invocation.getArgumentsToMatchers()).thenReturn(Arrays.asList(matcher1, matcher2));
        when(matcher1.matches(actualArgs[0])).thenReturn(true);
        when(matcher2.matches(actualArgs[1])).thenReturn(true);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        when(invocation.getMethod().getName()).thenReturn("methodName");
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());
        
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testGetLocation() {
        Location expectedLocation = mock(Location.class);
        when(invocation.getLocation()).thenReturn(expectedLocation);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());
        
        assertEquals(expectedLocation, invocationMatcher.getLocation());
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());
        
        PrintSettings printSettings = new PrintSettings();
        String expectedResult = invocation.toString(invocationMatcher.getMatchers(), printSettings);
        
        assertEquals(expectedResult, invocationMatcher.toString(printSettings));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Object[] arguments = new Object[] {1, 2, 3};
        when(i.getArguments()).thenReturn(arguments);
        when(matcher1 instanceof CapturesArguments).thenReturn(true);
        when(matcher2 instanceof CapturesArguments).thenReturn(true);
        
        CapturesArguments capturesArguments1 = (CapturesArguments) matcher1;
        CapturesArguments capturesArguments2 = (CapturesArguments) matcher2;
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(matcher1, matcher2));
        invocationMatcher.captureArgumentsFrom(i);
        
        verify(capturesArguments1).captureFrom(arguments[0]);
        verify(capturesArguments2).captureFrom(arguments[1]);
        verifyNoMoreInteractions(matcher1, matcher2);
    }
}
