import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {
    
    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Method method = invocationMatcher.getMethod();
        
        assertNotNull(method);
    }
    
    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation result = invocationMatcher.getInvocation();
        
        assertNotNull(result);
        assertEquals(invocation, result);
    }
    
    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        List<Matcher> result = invocationMatcher.getMatchers();
        
        assertNotNull(result);
        assertEquals(matchers, result);
    }
    
    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        PrintSettings printSettings = mock(PrintSettings.class);
        String expectedResult = "invocation.toString";
        when(invocation.toString(matchers, printSettings)).thenReturn(expectedResult);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        String result = invocationMatcher.toString(printSettings);
        
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Object[] arguments = new Object[2];
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        InvocationMatcher invocationMatcher = spy(new InvocationMatcher(invocation1));
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.getArguments()).thenReturn(arguments);
        when(argumentsComparator.argumentsMatch(invocationMatcher, invocation2)).thenReturn(true);
        
        boolean result = invocationMatcher.matches(invocation2);
        
        assertNotNull(result);
        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("method1");
        when(method2.getName()).thenReturn("method1");
        when(invocation1.isVerified()).thenReturn(false);
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        boolean result = invocationMatcher.hasSimilarMethod(invocation2);
        
        assertNotNull(result);
        assertFalse(result);
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        boolean result = invocationMatcher.hasSameMethod(invocation2);
        
        assertNotNull(result);
        assertFalse(result);
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Location result = invocationMatcher.getLocation();
        
        assertNotNull(result);
        assertEquals(location, result);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Matcher matcher1 = mock(CapturesArguments.class);
        Matcher matcher2 = mock(CapturesArguments.class);
        Object[] arguments = new Object[2];
        when(matcher1 instanceof CapturesArguments).thenReturn(true);
        when(matcher2 instanceof CapturesArguments).thenReturn(true);
        when(invocation.getArguments()).thenReturn(arguments);
        when(invocation.getArguments()[0]).thenReturn(mock(Object.class));
        when(invocation.getArguments()[1]).thenReturn(mock(Object.class));
        when(((CapturesArguments) matcher1).captureFrom(invocation.getArguments()[0])).thenReturn(true);
        when(((CapturesArguments) matcher2).captureFrom(invocation.getArguments()[1])).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.getMatchers().add(matcher1);
        invocationMatcher.getMatchers().add(matcher2);
        
        invocationMatcher.captureArgumentsFrom(invocation);
        
        verify((CapturesArguments) matcher1).captureFrom(invocation.getArguments()[0]);
        verify((CapturesArguments) matcher2).captureFrom(invocation.getArguments()[1]);
    }

}