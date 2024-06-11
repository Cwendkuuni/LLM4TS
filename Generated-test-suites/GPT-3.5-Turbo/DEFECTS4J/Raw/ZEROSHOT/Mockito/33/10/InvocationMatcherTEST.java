import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;

public class InvocationMatcherTest {
    
    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testGetInvocation() {
        assertEquals(invocation, invocationMatcher.getInvocation());
    }
    
    @Test
    public void testGetMatchers() {
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testToString() {
        String expectedResult = "test string";
        
        when(invocation.toString(matchers, new PrintSettings())).thenReturn(expectedResult);
        
        assertEquals(expectedResult, invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        
        when(invocation.getMock()).thenReturn("mock");
        when(actual.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(argumentsComparator.argumentsMatch(invocationMatcher, actual)).thenReturn(true);
        
        assertTrue(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testHasSimilarMethodWithDifferentMethodName() {
        Invocation candidate = mock(Invocation.class);
        
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(true);
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(candidate.getArguments()).thenReturn(new Object[]{});
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithDifferentIsVerified() {
        Invocation candidate = mock(Invocation.class);
        
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(true);
        when(candidate.isVerified()).thenReturn(true);
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(candidate.getArguments()).thenReturn(new Object[]{});
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMock() {
        Invocation candidate = mock(Invocation.class);
        
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock1");
        when(candidate.getMock()).thenReturn("mock2");
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(candidate.getArguments()).thenReturn(new Object[]{});
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethod() {
        Invocation candidate = mock(Invocation.class);
        
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(candidate.getArguments()).thenReturn(new Object[]{});
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodButOverloaded() {
        Invocation candidate = mock(Invocation.class);
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");
        when(argumentsComparator.argumentsMatch(invocationMatcher, candidate.getArguments())).thenReturn(true);
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        
        when(invocation.getMethod()).thenReturn(method1);
        when(method1.equals(method2)).thenReturn(true);
        
        assertTrue(invocationMatcher.hasSameMethod(invocation));
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        
        matchers.add(matcher);
        when(matcher instanceof CapturesArguments).thenReturn(true);
        when(i.getArguments()).thenReturn(new Object[]{});
        
        invocationMatcher.captureArgumentsFrom(i);
        
        verify(capturesArguments, times(1)).captureFrom(anyObject());
    }
    
    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = mock(List.class);
        List<InvocationMatcher> invocationMatchers = mock(List.class);
        
        when(invocations.contains(invocation)).thenReturn(true);
        when(invocations.iterator()).thenReturn(invocationMatchers.iterator());
        
        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);
        
        assertEquals(invocationMatchers, result);
    }
}