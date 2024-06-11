import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {
    
    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;
    
    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        matchers = Collections.emptyList();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }
    
    @Test
    public void testGetMethod() {
        Method expected = invocation.getMethod();
        Method actual = invocationMatcher.getMethod();
        assertSame(expected, actual);
    }
    
    @Test
    public void testGetInvocation() {
        Invocation expected = invocation;
        Invocation actual = invocationMatcher.getInvocation();
        assertSame(expected, actual);
    }
    
    @Test
    public void testGetMatchers() {
        List<Matcher> expected = matchers;
        List<Matcher> actual = invocationMatcher.getMatchers();
        assertSame(expected, actual);
    }
    
    @Test
    public void testToString() {
        String expected = new PrintSettings().print(matchers, invocation);
        String actual = invocationMatcher.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(actual.isVerified()).thenReturn(false);
        ArgumentsComparator argumentsComparatorMock = mock(ArgumentsComparator.class);
        when(argumentsComparatorMock.argumentsMatch(eq(invocationMatcher), eq(actual))).thenReturn(true);
        invocationMatcher = spy(invocationMatcher);
        doReturn(argumentsComparatorMock).when(invocationMatcher).getArgumentsComparator();
        
        boolean expected = invocationMatcher.matches(actual);
        assertTrue(expected);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        Method wantedMethod = mock(Method.class);
        Method currentMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(wantedMethod);
        when(candidate.getMethod()).thenReturn(currentMethod);
        when(wantedMethod.getName()).thenReturn("method");
        when(currentMethod.getName()).thenReturn("method");
        when(candidate.isVerified()).thenReturn(false);
        when(getInvocation().getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        ArgumentsComparator argumentsComparatorMock = mock(ArgumentsComparator.class);
        when(invocationMatcher.getArgumentsComparator()).thenReturn(argumentsComparatorMock);
        when(argumentsComparatorMock.argumentsMatch(eq(invocationMatcher), any(Object[].class))).thenReturn(false);
        
        boolean expected = invocationMatcher.hasSimilarMethod(candidate);
        assertFalse(expected);
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        Method wantedMethod = mock(Method.class);
        Method currentMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(wantedMethod);
        when(candidate.getMethod()).thenReturn(currentMethod);
        when(wantedMethod.getName()).thenReturn("method1");
        when(currentMethod.getName()).thenReturn("method2");
        when(wantedMethod.getParameterTypes()).thenReturn(new Class[]{});
        when(currentMethod.getParameterTypes()).thenReturn(new Class[]{});
        
        boolean expected = invocationMatcher.hasSameMethod(candidate);
        assertFalse(expected);
    }
    
    @Test
    public void testGetLocation() {
        Location expected = invocation.getLocation();
        Location actual = invocationMatcher.getLocation();
        assertSame(expected, actual);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromVarArgs() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.isVarArgs()).thenReturn(true);
        
        invocationMatcher.captureArgumentsFrom(invocation);
    }
    
    // Additional tests can be added here
    
}