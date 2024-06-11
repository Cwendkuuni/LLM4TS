import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Invocation;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.hamcrest.Matcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.debugging.Location;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {
    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        InvocationMatcher invMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invMatcher.getInvocation());
        assertEquals(matchers, invMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invMatcher.getInvocation());
        assertTrue(invMatcher.getMatchers().isEmpty());
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        
        InvocationMatcher invMatcher = new InvocationMatcher(invocation);
        
        assertEquals(method, invMatcher.getMethod());
    }
    
    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invMatcher = new InvocationMatcher(invocation);
        
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(invMatcher.getMatchers(), printSettings)).thenReturn("toStringResult");
        
        assertEquals("toStringResult", invMatcher.toString(printSettings));
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actualInvocation = mock(Invocation.class);
        Object[] args = new Object[0];
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(args);
        
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(invocation, actualInvocation)).thenReturn(true);
        
        InvocationMatcher invMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invMatcher.matches(actualInvocation));
    }
    
    @Test
    public void testMatches_withDifferentMocks() {
        Invocation invocation = mock(Invocation.class);
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMock()).thenReturn(mock(Object.class));
        
        InvocationMatcher invMatcher = new InvocationMatcher(invocation);
        
        assertFalse(invMatcher.matches(actualInvocation));
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        Object[] args = new Object[0];
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.isVerified()).thenReturn(true);
        when(candidate.getArguments()).thenReturn(args);
        
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(invocation, args)).thenReturn(true);
        
        InvocationMatcher invMatcher = new InvocationMatcher(invocation);
        
        assertFalse(invMatcher.hasSimilarMethod(candidate));
        
        when(candidate.isVerified()).thenReturn(true);
        
        assertFalse(invMatcher.hasSimilarMethod(candidate));
        
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.isVerified()).thenReturn(false);
        
        assertTrue(invMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        
        InvocationMatcher invMatcher = new InvocationMatcher(invocation);
        
        assertFalse(invMatcher.hasSameMethod(candidate));
        
        when(candidate.getMethod()).thenReturn(method1);
        
        assertTrue(invMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        
        InvocationMatcher invMatcher = new InvocationMatcher(invocation);
        
        assertEquals(location, invMatcher.getLocation());
    }
    
    @Test
    public void testToStringWithoutPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invMatcher = new InvocationMatcher(invocation);
        
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(invMatcher.getMatchers(), new PrintSettings())).thenReturn("toStringResult");
        
        assertEquals("toStringResult", invMatcher.toString());
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Invocation i = mock(Invocation.class);
        Object[] args = new Object[2];
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        
        List<Matcher> matchers = mock(List.class);
        when(matchers.get(0)).thenReturn(matcher1);
        when(matchers.get(1)).thenReturn(matcher2);
        when(i.getArguments()).thenReturn(args);
        
        CapturesArguments capturesArguments1 = mock(CapturesArguments.class);
        CapturesArguments capturesArguments2 = mock(CapturesArguments.class);
        when(matcher1 instanceof CapturesArguments).thenReturn(true);
        when(matcher2 instanceof CapturesArguments).thenReturn(true);
        when(capturesArguments1.captureFrom(args[0])).thenReturn(true);
        when(capturesArguments2.captureFrom(args[1])).thenReturn(true);
        
        InvocationMatcher invMatcher = new InvocationMatcher(invocation, matchers);
        
        invMatcher.captureArgumentsFrom(i);
        
        assertTrue(capturesArguments1.captureFrom(args[0]));
        assertTrue(capturesArguments2.captureFrom(args[1]));
    }
}