import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;

    @Before
    public void setup() {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<>();
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
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }
    
    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testToString() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        String expected = invocation.toString(matchers, new PrintSettings());
        
        assertEquals(expected, invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        
        when(invocation.getMock()).thenReturn("mock");
        when(actual.getMock()).thenReturn("mock");
        
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(actual.getMethod()).thenReturn(method2);
        
        when(invocation.isVerified()).thenReturn(false);
        when(actual.isVerified()).thenReturn(false);
                
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(invocation, actual)).thenReturn(true);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(false);
        
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");
        
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(invocation, candidate.getArguments())).thenReturn(true);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(false);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(location, invocationMatcher.getLocation());
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = mock(PrintSettings.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        String expected = invocation.toString(invocationMatcher.getMatchers(), printSettings);
        
        assertEquals(expected, invocationMatcher.toString(printSettings));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        when(invocation.getArguments()).thenReturn(new Object[] {});
        matchers.add(matcher);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(i);
        
        verify(matcher, times(0)).captureFrom(any());
    }
    
    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation);
        
        List<InvocationMatcher> expected = new ArrayList<>();
        expected.add(new InvocationMatcher(invocation));

        assertEquals(expected, InvocationMatcher.createFrom(invocations));
    }
}