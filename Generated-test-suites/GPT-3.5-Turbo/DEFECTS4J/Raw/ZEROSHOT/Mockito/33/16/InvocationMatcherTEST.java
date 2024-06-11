import org.junit.Test;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        PrintSettings printSettings = mock(PrintSettings.class);
        String expectedString = "Test InvocationMatcher";
        when(invocation.toString(matchers, printSettings)).thenReturn(expectedString);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(expectedString, invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Object mock = mock(Object.class);
        Invocation actual = mock(Invocation.class);
        Object actualMock = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMock()).thenReturn(actualMock);
        when(actual.getMethod()).thenReturn(mock(Method.class));
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Invocation.class))).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = mock(Invocation.class);
        Object[] actualArgs = new Object[]{1, 2, 3};
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Object[].class))).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Object[].class))).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(location, invocationMatcher.getLocation());
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        PrintSettings printSettings = mock(PrintSettings.class);
        String expectedString = "Test InvocationMatcher";
        when(invocation.toString(matchers, printSettings)).thenReturn(expectedString);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(expectedString, invocationMatcher.toString(printSettings));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Invocation i = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(i.getArguments()).thenReturn(new Object[]{});
        when(matcher instanceof CapturesArguments).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        invocationMatcher.captureArgumentsFrom(i);
        
        verify((CapturesArguments)matcher, times(0)).captureFrom(any(Object.class));
    }
    
    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<Invocation>();
        invocations.add(mock(Invocation.class));
        List<InvocationMatcher> expectedInvocationMatchers = new LinkedList<InvocationMatcher>();
        expectedInvocationMatchers.add(mock(InvocationMatcher.class));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        List<InvocationMatcher> actualInvocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(expectedInvocationMatchers, actualInvocationMatchers);
    }
}