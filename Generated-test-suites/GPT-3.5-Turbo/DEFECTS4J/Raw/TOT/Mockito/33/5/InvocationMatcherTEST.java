import static org.junit.Assert.*;
import org.junit.Test;
import org.hamcrest.Matchers;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        // typical use case
        Invocation invocation = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        // typical use case
        Invocation invocation = mock(Invocation.class);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        // typical use case
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testMatches() {
        // typical use case
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMock()).thenReturn("mock1");
        when(invocation2.getMock()).thenReturn("mock1");
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.isVerified()).thenReturn(false);
        when(invocation2.isVerified()).thenReturn(false);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(), any())).thenReturn(true);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        assertTrue(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testHasSimilarMethod() {
        // typical use case
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.isVerified()).thenReturn(false);
        when(invocation2.isVerified()).thenReturn(false);
        when(invocation1.getMock()).thenReturn("mock");
        when(invocation2.getMock()).thenReturn("mock");
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(), any())).thenReturn(false);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        assertTrue(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSameMethod() {
        // typical use case
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        assertTrue(invocationMatcher.hasSameMethod(invocation2));
    }
    
    @Test
    public void testToString() {
        // typical use case
        Invocation invocation = mock(Invocation.class);
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(any(), any())).thenReturn("testString");
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals("testString", invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // typical use case
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher);
        when(invocation1.getArguments()).thenReturn(new Object[]{"arg1"});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation2, matchers);
        invocationMatcher.captureArgumentsFrom(invocation1);
        
        verify(matcher, times(1)).captureFrom("arg1");
    }

    @Test
    public void testCreateFrom() {
        // typical use case
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation1);
        invocations.add(invocation2);
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(2, invocationMatchers.size());
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }
}