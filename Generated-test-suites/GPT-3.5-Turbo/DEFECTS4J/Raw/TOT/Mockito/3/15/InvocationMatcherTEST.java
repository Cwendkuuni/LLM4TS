import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {
    
    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private Object[] args;
    
    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        args = new Object[] {1, "test"};
        when(invocation.getArguments()).thenReturn(args);
        invocationMatcher = new InvocationMatcher(invocation);
    }
    
    @Test
    public void testGetMethod() {
        Method expectedMethod = invocation.getMethod();
        Method actualMethod = invocationMatcher.getMethod();
        assertEquals(expectedMethod, actualMethod);
    }
    
    @Test
    public void testGetInvocation() {
        Invocation expectedInvocation = invocation;
        Invocation actualInvocation = invocationMatcher.getInvocation();
        assertEquals(expectedInvocation, actualInvocation);
    }
    
    @Test
    public void testGetMatchersWhenNoMatchersSet() {
        List<Matcher> expectedMatchers = ArgumentsProcessor.argumentsToMatchers(args);
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        assertEquals(expectedMatchers, actualMatchers);
    }
    
    @Test
    public void testGetMatchersWhenMatchersSet() {
        List<Matcher> expectedMatchers = Collections.emptyList();
        invocationMatcher = new InvocationMatcher(invocation, expectedMatchers);
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        assertEquals(expectedMatchers, actualMatchers);
    }
    
    @Test
    public void testToString() {
        String expectedString = "matcher string";
        when(invocation.toString()).thenReturn("invocation string");
        when(new PrintSettings().print(anyList(), eq(invocation))).thenReturn(expectedString);
        
        String actualString = invocationMatcher.toString();
        assertEquals(expectedString, actualString);
    }
    
    @Test
    public void testMatchesWhenInvocationMatches() {
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(actualInvocation.getArguments()).thenReturn(invocation.getArguments());
        
        assertTrue(invocationMatcher.matches(actualInvocation));
    }
    
    @Test
    public void testMatchesWhenInvocationDoesNotMatch() {
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(actualInvocation.getArguments()).thenReturn(new Object[] {2, "test"});
        
        assertFalse(invocationMatcher.matches(actualInvocation));
    }
    
    @Test
    public void testMatchesWhenInvocationArgumentsDoNotMatch() {
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(actualInvocation.getArguments()).thenReturn(new Object[] {1, "different"});
        
        assertFalse(invocationMatcher.matches(actualInvocation));
    }
    
    @Test
    public void testHasSimilarMethodWhenNameEqualsAndUnverifiedAndMockIsSameAndMethodEquals() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock() == candidate.getMock()).thenReturn(true);
        when(invocation.hasSameMethod(candidate)).thenReturn(true);
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethodWhenNameEqualsAndVerifiedAndMockIsSameAndMethodEquals() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(candidate.isVerified()).thenReturn(true);
        when(invocation.getMock() == candidate.getMock()).thenReturn(true);
        when(invocation.hasSameMethod(candidate)).thenReturn(true);
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethodWhenNameNotEqual() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod().getName()).thenReturn("methodName1");
        when(candidate.getMethod().getName()).thenReturn("methodName2");
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethodWhenMockIsNotSame() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(invocation.getMock() == candidate.getMock()).thenReturn(false);
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethodWhenMethodNotEqualsButArgumentsMatch() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(invocation.hasSameMethod(candidate)).thenReturn(false);
        when(invocation.safelyArgumentsMatch(candidate.getArguments())).thenReturn(true);
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethodWhenMethodNotEqualsAndArgumentsNotMatch() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod().getName()).thenReturn("methodName");
        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(invocation.hasSameMethod(candidate)).thenReturn(false);
        when(invocation.safelyArgumentsMatch(candidate.getArguments())).thenReturn(false);
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethodWhenMethodEquals() {
        Method m1 = mock(Method.class);
        Method m2 = mock(Method.class);
        invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(m1);
        when(m1.getName()).thenReturn("methodName");
        when(m2.getName()).thenReturn("methodName");
        when(m1.getParameterTypes()).thenReturn(new Class[]{});
        when(m2.getParameterTypes()).thenReturn(new Class[]{});
        
        assertTrue(invocationMatcher.hasSameMethod(invocation));
    }
    
    @Test
    public void testHasSameMethodWhenMethodNotEquals() {
        Method m1 = mock(Method.class);
        Method m2 = mock(Method.class);
        invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(m1);
        when(m1.getName()).thenReturn("methodName1");
        when(m2.getName()).thenReturn("methodName2");
        
        assertFalse(invocationMatcher.hasSameMethod(invocation));
    }
    
    @Test
    public void testHasSameMethodWhenMethodEqualsWithDifferentParameterTypes() {
        Method m1 = mock(Method.class);
        Method m2 = mock(Method.class);
        invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(m1);
        when(m1.getName()).thenReturn("methodName");
        when(m2.getName()).thenReturn("methodName");
        when(m1.getParameterTypes()).thenReturn(new Class[]{int.class});
        when(m2.getParameterTypes()).thenReturn(new Class[]{String.class});
        
        assertFalse(invocationMatcher.hasSameMethod(invocation));
    }
    
    @Test
    public void testGetLocation() {
        Location expectedLocation = invocation.getLocation();
        Location actualLocation = invocationMatcher.getLocation();
        assertEquals(expectedLocation, actualLocation);
    }
    
    @Test
    public void testCaptureArgumentsFromWhenVarArgsMethod() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(Method.class);
        when(invocation.getRawArguments()).thenReturn(new Object[] {1, "test"});
        
        Matcher matcher = mock(Matcher.class);
        invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.getMatchers().add(matcher);
        
        invocationMatcher.captureArgumentsFrom(invocation);
        
        verify(matcher).captureFrom(1);
        verify(matcher).captureFrom("test");
    }
    
    @Test
    public void testCaptureArgumentsFromWhenNotVarArgsMethod() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(Method.class);
        when(invocation.getRawArguments()).thenReturn(new Object[] {1, "test"});
        
        Matcher matcher = mock(Matcher.class);
        invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.getMatchers().add(matcher);
        
        invocationMatcher.captureArgumentsFrom(invocation);
        
        verify(matcher).captureFrom(1);
        verify(matcher).captureFrom("test");
    }
    
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        
        List<InvocationMatcher> expectedInvocationMatchers = new LinkedList<>();
        for (Invocation i : invocations) {
            expectedInvocationMatchers.add(new InvocationMatcher(i));
        }
        
        List<InvocationMatcher> actualInvocationMatchers = InvocationMatcher.createFrom(invocations);
        assertEquals(expectedInvocationMatchers, actualInvocationMatchers);
    }

}