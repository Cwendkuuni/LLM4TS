import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.exceptions.PrintableInvocation;
import java.util.List;

public class InvocationMatcherTest {
    
    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Mockito.mock(List.class);
        
        InvocationMatcher invMatcher = new InvocationMatcher(invocation, matchers);
        
        assertSame(invocation, invMatcher.getInvocation());
        assertSame(matchers, invMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        
        InvocationMatcher invMatcher = new InvocationMatcher(invocation);
        
        assertSame(invocation, invMatcher.getInvocation());
        assertSame(invocation.argumentsToMatchers(), invMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invMatcher = new InvocationMatcher(invocation);
        
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        assertSame(method, invMatcher.getMethod());
    }
    
    @Test
    public void testToString() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invMatcher = new InvocationMatcher(invocation);
        
        String expected = Mockito.mock(String.class);
        PrintSettings printSettings = Mockito.mock(PrintSettings.class);
        Mockito.when(invocation.toString(invMatcher.getMatchers(), printSettings)).thenReturn(expected);
        
        assertEquals(expected, invMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invMatcher = new InvocationMatcher(invocation);
        Invocation actual = Mockito.mock(Invocation.class);
        
        boolean expected = true;
        Mockito.when(invocation.getMock().equals(actual.getMock())).thenReturn(expected);
        
        boolean result = invMatcher.matches(actual);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invMatcher = new InvocationMatcher(invocation);
        Object[] actualArgs = new Object[1];
        
        boolean expected = true;
        ArgumentsComparator argumentsComparator = Mockito.mock(ArgumentsComparator.class);
        Mockito.when(argumentsComparator.argumentsMatch(invMatcher, actualArgs)).thenReturn(expected);
        Mockito.mockStatic(ArgumentsComparator.class);
        Mockito.when(ArgumentsComparator.class, "newInstance").thenReturn(argumentsComparator);
        
        boolean result = invMatcher.safelyArgumentsMatch(actualArgs);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invMatcher = new InvocationMatcher(invocation);
        Invocation candidate = Mockito.mock(Invocation.class);
        
        String wantedMethodName = "methodName";
        String currentMethodName = "methodName";
        final boolean methodNameEquals = wantedMethodName.equals(currentMethodName);
        Mockito.when(invMatcher.getMethod().getName()).thenReturn(wantedMethodName);
        Mockito.when(candidate.getMethod().getName()).thenReturn(currentMethodName);
        
        final boolean isUnverified = false;
        Mockito.when(candidate.isVerified()).thenReturn(!isUnverified);
        
        boolean mockIsTheSame = true;
        Mockito.when(invMatcher.getInvocation().getMock() == candidate.getMock()).thenReturn(mockIsTheSame);
        
        final boolean methodEquals = true;
        Mockito.when(invMatcher.hasSameMethod(candidate)).thenReturn(methodEquals);
        
        final boolean overloadedButSameArgs = false;
        Mockito.when(invMatcher.safelyArgumentsMatch(candidate.getArguments())).thenReturn(overloadedButSameArgs);

        boolean result = invMatcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invMatcher = new InvocationMatcher(invocation);
        Invocation candidate = Mockito.mock(Invocation.class);
        
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(candidate.getMethod()).thenReturn(method);
        
        boolean result = invMatcher.hasSameMethod(candidate);
        
        assertTrue(result);
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invMatcher = new InvocationMatcher(invocation);
        
        Location location = Mockito.mock(Location.class);
        Mockito.when(invocation.getLocation()).thenReturn(location);
        
        Location result = invMatcher.getLocation();
        
        assertSame(location, result);
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invMatcher = new InvocationMatcher(invocation);
        
        String expected = Mockito.mock(String.class);
        PrintSettings printSettings = Mockito.mock(PrintSettings.class);
        Mockito.when(invocation.toString(invMatcher.getMatchers(), printSettings)).thenReturn(expected);
        
        assertEquals(expected, invMatcher.toString(printSettings));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invMatcher = new InvocationMatcher(invocation);
        Invocation i = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[1];
        Mockito.when(i.getArguments()).thenReturn(arguments);
        
        Matcher matcher = Mockito.mock(Matcher.class);
        CapturesArguments capturesArguments = Mockito.mock(CapturesArguments.class);
        Mockito.when(matcher instanceof CapturesArguments).thenReturn(capturesArguments);
        
        invMatcher.getMatchers().add(matcher);
        
        invMatcher.captureArgumentsFrom(i);
        
        Mockito.verify(capturesArguments).captureFrom(arguments[0]);
    }
}