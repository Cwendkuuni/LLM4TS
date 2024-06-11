import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {
    
    private Invocation invocation = mock(Invocation.class);
    private List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
    private InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
    
    @Test
    public void testConstructor() {
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithEmptyMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> argumentsToMatchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        when(invocation.argumentsToMatchers()).thenReturn(argumentsToMatchers);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(argumentsToMatchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testToString() {
        String expected = "invocation.toString()";
        when(invocation.toString(matchers, new PrintSettings())).thenReturn(expected);
        
        assertEquals(expected, invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        when(argumentsComparator.argumentsMatch(eq(invocationMatcher), eq(actual))).thenReturn(true);
        
        assertTrue(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testMatchesWithDifferentMock() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        
        assertFalse(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testMatchesWithDifferentMethod() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        
        assertFalse(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testMatchesWithDifferentArguments() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        when(argumentsComparator.argumentsMatch(eq(invocationMatcher), eq(actual))).thenReturn(false);
        
        assertFalse(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        when(argumentsComparator.argumentsMatch(eq(invocationMatcher), any(Object[].class))).thenReturn(true);
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethodWithDifferentMethodName() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethodWithDifferentVerification() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(true);
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethodWithDifferentMock() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethodWithOverloadedButSameArguments() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        when(argumentsComparator.argumentsMatch(eq(invocationMatcher), any(Object[].class))).thenReturn(true);
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        
        assertEquals(location, invocationMatcher.getLocation());
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = mock(PrintSettings.class);
        String expected = "invocation.toString(matchers, printSettings)";
        when(invocation.toString(matchers, printSettings)).thenReturn(expected);
        
        assertEquals(expected, invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Object[] args = {"arg1", "arg2"};
        when(i.getArguments()).thenReturn(args);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(matchers.get(0)).thenReturn(capturesArguments);
        
        invocationMatcher.captureArgumentsFrom(i);
        
        verify(capturesArguments).captureFrom(args[0]);
    }
}