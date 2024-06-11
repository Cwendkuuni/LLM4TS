import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertNotNull(invocationMatcher.getMethod());
        assertEquals(method, invocationMatcher.getMethod());
        
        verify(invocation, times(1)).getMethod();
    }
    
    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(matcher);
        
        when(invocation.getArguments()).thenReturn(new Object[] {1});
        when(matcher.toString()).thenReturn("matcher");
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        String expected = "matcher";
        String actual = invocationMatcher.toString();
        
        assertEquals(expected, actual);
        
        verify(invocation, times(1)).getArguments();
        verify(matcher, times(1)).toString();
    }
    
    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Object[] args1 = {1};
        Object[] args2 = {2};
        
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        when(invocation1.getArguments()).thenReturn(args1);
        when(invocation2.getArguments()).thenReturn(args2);
        
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(), any())).thenReturn(true);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        boolean expected1 = false;
        boolean expected2 = true;
        
        boolean actual1 = invocationMatcher.matches(invocation2);
        boolean actual2 = invocationMatcher.matches(invocation1);
        
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
        
        verify(invocation1, times(1)).getMock();
        verify(invocation2, times(1)).getMock();
        verify(invocation1, times(1)).getArguments();
        verify(invocation2, times(1)).getArguments();
        verify(argumentsComparator, times(1)).argumentsMatch(any(), any());
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        when(invocation1.isVerified()).thenReturn(false);
        when(invocation2.isVerified()).thenReturn(false);
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        when(method1.getName()).thenReturn("method");
        when(method2.getName()).thenReturn("method");
        
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(any(), any())).thenReturn(true);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        boolean expected1 = false;
        boolean expected2 = true;
        
        boolean actual1 = invocationMatcher.hasSimilarMethod(invocation2);
        boolean actual2 = invocationMatcher.hasSimilarMethod(invocation1);
        
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
        
        verify(invocation1, times(2)).getMethod();
        verify(invocation2, times(4)).getMethod();
        verify(invocation1, times(2)).isVerified();
        verify(invocation2, times(2)).isVerified();
        verify(invocation1, times(1)).getMock();
        verify(invocation2, times(1)).getMock();
        verify(method1, times(1)).getName();
        verify(method2, times(1)).getName();
        verify(argumentsComparator, times(1)).argumentsMatch(any(), any());
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("method");
        when(method2.getName()).thenReturn("method");
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        boolean expected1 = true;
        boolean expected2 = false;
        
        boolean actual1 = invocationMatcher.hasSameMethod(invocation2);
        boolean actual2 = invocationMatcher.hasSameMethod(invocation1);
        
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
        
        verify(invocation1, times(2)).getMethod();
        verify(invocation2, times(2)).getMethod();
        verify(method1, times(1)).getName();
        verify(method2, times(1)).getName();
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        
        Object[] args = {1};
        Object[] rawArgs = {1, 2, 3};
        
        when(invocation.getMethod()).thenReturn(method);
        when(method.isVarArgs()).thenReturn(false);
        when(invocation.getArgumentAt(anyInt(), any())).thenReturn(1);
        when(invocation.getRawArguments()).thenReturn(rawArgs);
        
        Matcher matcher = mock(Matcher.class);
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(matcher);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        invocationMatcher.captureArgumentsFrom(invocation);
        
        verify(invocation, times(1)).getMethod();
        verify(method, times(1)).isVarArgs();
        verify(invocation, times(1)).getArgumentAt(0, Object.class);
        verify(invocation, times(1)).getRawArguments();
        verify(matcher, times(1)).captureFrom(1);
    }
    
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        
        List<Invocation> invocations = new ArrayList<Invocation>();
        invocations.add(invocation1);
        invocations.add(invocation2);
        
        List<InvocationMatcher> expected = new ArrayList<InvocationMatcher>();
        expected.add(new InvocationMatcher(invocation1));
        expected.add(new InvocationMatcher(invocation2));
        
        List<InvocationMatcher> actual = InvocationMatcher.createFrom(invocations);
        
        assertEquals(expected, actual);
        
        verify(invocation1, times(1)).getMethod();
        verify(invocation2, times(1)).getMethod();
    }
}