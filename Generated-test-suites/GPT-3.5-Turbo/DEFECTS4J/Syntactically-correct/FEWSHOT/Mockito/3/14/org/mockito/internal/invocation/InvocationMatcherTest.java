import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.*;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {
    
    private static final Invocation INVOCATION = mock(Invocation.class);
    
    @Test
    public void testConstructorWithMatchers() {
        List<Matcher<?>> matchers = createMatchers();
        InvocationMatcher invocationMatcher = new InvocationMatcher(INVOCATION, matchers);
        
        assertEquals(INVOCATION, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithoutMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(INVOCATION);
        
        assertEquals(INVOCATION, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }
    
    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(INVOCATION.getMethod()).thenReturn(method);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(INVOCATION);
        
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testToString() {
        String expectedToString = "InvocationMatcher.toString()";
        PrintSettings printSettings = mock(PrintSettings.class);
        when(printSettings.print(anyListOf(Matcher.class), any(Invocation.class))).thenReturn(expectedToString);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(INVOCATION);
        String actualToString = invocationMatcher.toString();
        
        assertEquals(expectedToString, actualToString);
    }
    
    @Test
    public void testMatches_WithMatchingInvocation() {
        Invocation actual = mock(Invocation.class);
        when(INVOCATION.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(INVOCATION.getMock());
        when(actual.isVerified()).thenReturn(false);
        when(actual.getArguments()).thenReturn(createArguments());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(INVOCATION);
        boolean matches = invocationMatcher.matches(actual);
        
        assertTrue(matches);
    }
    
    @Test
    public void testMatches_WithNonMatchingInvocation() {
        Invocation actual = mock(Invocation.class);
        when(INVOCATION.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(INVOCATION.getMock());
        when(actual.isVerified()).thenReturn(false);
        when(actual.getArguments()).thenReturn(createDifferentArguments());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(INVOCATION);
        boolean matches = invocationMatcher.matches(actual);
        
        assertFalse(matches);
    }
    
    @Test
    public void testHasSimilarMethod_WithSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMethod()).thenReturn(INVOCATION.getMethod());
        when(candidate.getMock()).thenReturn(INVOCATION.getMock());
        when(candidate.getArguments()).thenReturn(createDifferentArguments());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(INVOCATION);
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);
        
        assertTrue(hasSimilarMethod);
    }
    
    @Test
    public void testHasSimilarMethod_WithDifferentMethod() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMock()).thenReturn(INVOCATION.getMock());
        when(candidate.getArguments()).thenReturn(createDifferentArguments());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(INVOCATION);
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(hasSimilarMethod);
    }
    
    @Test
    public void testHasSimilarMethod_WithVerifiedInvocation() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.isVerified()).thenReturn(true);
        when(candidate.getMethod()).thenReturn(INVOCATION.getMethod());
        when(candidate.getMock()).thenReturn(INVOCATION.getMock());
        when(candidate.getArguments()).thenReturn(createDifferentArguments());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(INVOCATION);
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(hasSimilarMethod);
    }
    
    @Test
    public void testHasSimilarMethod_WithDifferentMock() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMethod()).thenReturn(INVOCATION.getMethod());
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(candidate.getArguments()).thenReturn(createDifferentArguments());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(INVOCATION);
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(hasSimilarMethod);
    }
    
    @Test
    public void testHasSimilarMethod_WithSameMethodAndArguments() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMethod()).thenReturn(INVOCATION.getMethod());
        when(candidate.getMock()).thenReturn(INVOCATION.getMock());
        when(candidate.getArguments()).thenReturn(createArguments());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(INVOCATION);
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(hasSimilarMethod);
    }
    
    @Test
    public void testCaptureArgumentsFrom_WithVarArgs() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getRawArguments()).thenReturn(createVarArgsArguments());
        
        Matcher<Object> matcher1 = mock(Matcher.class);
        Matcher<Object> matcher2 = mock(Matcher.class);
        Matcher<Object> matcher3 = mock(Matcher.class);
        
        List<Matcher<?>> matchers = Arrays.asList(matcher1, matcher2, matcher3);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(invocation);
        
        verify((CapturesArguments) matcher1).captureFrom(eq("arg1"));
        verify((CapturesArguments) matcher2).captureFrom(eq("arg2"));
        verify((CapturesArguments) matcher3).captureFrom(eq("arg3"));
    }
    
    @Test
    public void testCaptureArgumentsFrom_WithoutVarArgs() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArgumentAt(0, Object.class)).thenReturn("arg1");
        when(invocation.getArgumentAt(1, Object.class)).thenReturn("arg2");
        when(invocation.getArgumentAt(2, Object.class)).thenReturn("arg3");
        
        Matcher<Object> matcher1 = mock(Matcher.class);
        Matcher<Object> matcher2 = mock(Matcher.class);
        Matcher<Object> matcher3 = mock(Matcher.class);
        
        List<Matcher<?>> matchers = Arrays.asList(matcher1, matcher2, matcher3);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(invocation);
        
        verify((CapturesArguments) matcher1).captureFrom(eq("arg1"));
        verify((CapturesArguments) matcher2).captureFrom(eq("arg2"));
        verify((CapturesArguments) matcher3).captureFrom(eq("arg3"));
    }
    
    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = createInvocations();
        List<InvocationMatcher> expectedInvocationMatchers = createInvocationMatchers();
        
        List<InvocationMatcher> actualInvocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(expectedInvocationMatchers, actualInvocationMatchers);
    }
    
    private List<Matcher<?>> createMatchers() {
        Matcher<Object> matcher1 = mock(Matcher.class);
        Matcher<Object> matcher2 = mock(Matcher.class);
        Matcher<Object> matcher3 = mock(Matcher.class);
        
        return Arrays.asList(matcher1, matcher2, matcher3);
    }
    
    private List<Object> createArguments() {
        return Arrays.asList("arg1", "arg2", "arg3");
    }
    
    private List<Object> createDifferentArguments() {
        return Arrays.asList("differentArg1", "differentArg2", "differentArg3");
    }
    
    private List<Object> createVarArgsArguments() {
        return Arrays.asList("arg1", "arg2", new String[] {"arg3"});
    }
    
    private List<Invocation> createInvocations() {
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(mock(Invocation.class));
        invocations.add(mock(Invocation.class));
        invocations.add(mock(Invocation.class));
        return invocations;
    }
    
    private List<InvocationMatcher> createInvocationMatchers() {
        List<InvocationMatcher> invocationMatchers = new ArrayList<>();
        for (Invocation invocation : createInvocations()) {
            invocationMatchers.add(new InvocationMatcher(invocation));
        }
        return invocationMatchers;
    }
}