import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.hamcrest.Matcher;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Method expectedMethod = invocation.getMethod();
        Method actualMethod = invocationMatcher.getMethod();
        
        assertEquals(expectedMethod, actualMethod);
    }
    
    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation expectedInvocation = invocation;
        Invocation actualInvocation = invocationMatcher.getInvocation();
        
        assertEquals(expectedInvocation, actualInvocation);
    }
    
    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        List<Matcher> expectedMatchers = matchers;
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        
        assertEquals(expectedMatchers, actualMatchers);
    }
    
    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        PrintSettings printSettings = mock(PrintSettings.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        String expectedString = invocation.toString(matchers, printSettings);
        String actualString = invocationMatcher.toString();
        
        assertEquals(expectedString, actualString);
    }
    
    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.isVerified()).thenReturn(false);
        when(invocation2.isVerified()).thenReturn(true);
        when(argumentsComparator.argumentsMatch(invocationMatcher, invocation2)).thenReturn(true);
        
        boolean expectedResult = false;
        boolean actualResult = invocationMatcher.matches(invocation2);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.isVerified()).thenReturn(false);
        when(invocation2.isVerified()).thenReturn(true);
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        when(argumentsComparator.argumentsMatch(invocationMatcher, invocation2.getArguments())).thenReturn(true);
        
        boolean expectedResult = false;
        boolean actualResult = invocationMatcher.hasSimilarMethod(invocation2);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        
        boolean expectedResult = false;
        boolean actualResult = invocationMatcher.hasSameMethod(invocation2);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Location expectedLocation = invocation.getLocation();
        Location actualLocation = invocationMatcher.getLocation();
        
        assertEquals(expectedLocation, actualLocation);
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        PrintSettings printSettings = mock(PrintSettings.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        String expectedString = invocation.toString(matchers, printSettings);
        String actualString = invocationMatcher.toString(printSettings);
        
        assertEquals(expectedString, actualString);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        when(invocation1.getArguments()).thenReturn(new Object[]{});
        when(invocation2.getArguments()).thenReturn(new Object[]{});
        when(matcher instanceof CapturesArguments).thenReturn(true);
        when(matcher.captureFrom(invocation2.getArguments()[0])).thenReturn(true);
        when(matcher.captureFrom(invocation2.getArguments()[1])).thenReturn(true);
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getArguments()[0]).thenReturn(mock(Object.class));
        when(invocation2.getArguments()[1]).thenReturn(mock(Object.class));
        
        invocationMatcher.captureArgumentsFrom(invocation2);
        
        verify(capturesArguments, times(1)).captureFrom(invocation2.getArguments()[0]);
        verify(capturesArguments, times(1)).captureFrom(invocation2.getArguments()[1]);
    }
}