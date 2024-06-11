import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import java.lang.reflect.Method;
import java.util.List;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;
    
    @Before
    public void setup() {
        invocation = mock(Invocation.class);
        matchers = mock(List.class);
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }
    
    @Test
    public void testConstructorWithMatchersNotEmpty() {
        // Create the mock matchers
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        
        // Set up the mock interactions
        when(matchers.isEmpty()).thenReturn(false);
        when(invocation.getArguments()).thenReturn(new Object[] {});
        
        // Create the InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        // Verify the matchers are set correctly
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithMatchersEmpty() {
        // Set up the mock interactions
        when(matchers.isEmpty()).thenReturn(true);
        when(invocation.getArguments()).thenReturn(new Object[] {});
        
        // Create the InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        // Verify that the matchers are generated from the invocation arguments
        assertEquals(ArgumentsProcessor.argumentsToMatchers(invocation.getArguments()), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithInvocation() {
        // Set up the mock interactions
        when(matchers.isEmpty()).thenReturn(true);
        when(invocation.getArguments()).thenReturn(new Object[] {});
        
        // Create the InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Verify that the matchers are generated from the invocation arguments
        assertEquals(ArgumentsProcessor.argumentsToMatchers(invocation.getArguments()), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        // Create the mock method
        Method method = mock(Method.class);
        
        // Set up the mock interactions
        when(invocation.getMethod()).thenReturn(method);
        
        // Verify the method is returned correctly
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testGetInvocation() {
        // Verify the invocation is returned correctly
        assertEquals(invocation, invocationMatcher.getInvocation());
    }
    
    @Test
    public void testGetMatchers() {
        // Verify the matchers are returned correctly
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testToString() {
        // Create the mock printSettings
        PrintSettings printSettings = mock(PrintSettings.class);
        
        // Set up the mock interactions
        when(printSettings.print(matchers, invocation)).thenReturn("Test invocation");
        
        // Verify the toString() method returns the correct string representation
        assertEquals("Test invocation", invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        // Create the mock actual invocation
        Invocation actualInvocation = mock(Invocation.class);
        
        // Set up the mock interactions
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getArguments()).thenReturn(new Object[] {});
        when(invocationMatcher.hasSameMethod(actualInvocation)).thenReturn(true);
        
        // Create the ArgumentsComparator
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        when(argumentsComparator.argumentsMatch(invocationMatcher, actualInvocation)).thenReturn(true);
        
        // Verify that the matches() method returns true
        assertTrue(invocationMatcher.matches(actualInvocation));
    }
    
    @Test
    public void testHasSimilarMethod() {
        // Create the mock candidate invocation
        Invocation candidateInvocation = mock(Invocation.class);
        
        // Set up the mock interactions
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidateInvocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(candidateInvocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidateInvocation.getMock()).thenReturn(mock(Object.class));
        when(invocationMatcher.hasSameMethod(candidateInvocation)).thenReturn(false);
        
        // Create the ArgumentsComparator
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        when(argumentsComparator.argumentsMatch(invocationMatcher, candidateInvocation.getArguments())).thenReturn(true);
        
        // Verify that the hasSimilarMethod() method returns true
        assertTrue(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }
    
    @Test
    public void testHasSameMethod() {
        // Create the mock method
        Method method = mock(Method.class);
        
        // Set up the mock interactions
        when(invocation.getMethod()).thenReturn(method);
        when(invocationMatcher.hasSameMethod(invocation)).thenCallRealMethod();
        
        // Verify that the hasSameMethod() method returns true
        assertTrue(invocationMatcher.hasSameMethod(invocation));
    }
    
    @Test
    public void testGetLocation() {
        // Create the mock location
        Location location = mock(Location.class);
        
        // Set up the mock interactions
        when(invocation.getLocation()).thenReturn(location);
        
        // Verify the location is returned correctly
        assertEquals(location, invocationMatcher.getLocation());
    }
    
    @Test
    public void testCaptureArgumentsFromVarargs() {
        // Create the mock invocation
        Invocation invocation = mock(Invocation.class);
        
        // Set up the mock interactions
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        
        // Capture arguments from varargs
        invocationMatcher.captureArgumentsFrom(invocation);
        
        // Verify that UnsupportedOperationException is thrown
        verify(invocation).getMethod();
        verifyNoMoreInteractions(invocation);
    }
    
    @Test
    public void testCaptureArgumentsFromMatchers() {
        // Create the mock matcher
        Matcher matcher = mock(Matcher.class);
        
        // Set up the mock interactions
        when(matchers.get(0)).thenReturn(matcher);
        when(matcher instanceof CapturesArguments).thenReturn(true);
        when(invocation.getArgumentAt(0, Object.class)).thenReturn(mock(Object.class));
        
        // Capture arguments from matchers
        invocationMatcher.captureArgumentsFrom(invocation);
        
        // Verify that the captureFrom() method is called on the matcher
        verify(matchers).get(0);
        verify(matcher).captureFrom(invocation.getArgumentAt(0, Object.class));
        verifyNoMoreInteractions(matchers);
    }
    
    @Test
    public void testCreateFrom() {
        // Create the mock invocations
        List<Invocation> invocations = mock(List.class);
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        
        // Set up the mock interactions
        when(invocations.get(0)).thenReturn(invocation1);
        when(invocations.get(1)).thenReturn(invocation2);
        
        // Create the InvocationMatcher list
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        // Verify that the InvocationMatcher list is created correctly
        assertEquals(2, invocationMatchers.size());
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }
}