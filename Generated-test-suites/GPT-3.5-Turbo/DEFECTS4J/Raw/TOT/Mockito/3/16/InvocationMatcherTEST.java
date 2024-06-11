import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.*;

public class InvocationMatcherTest {
    
    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private Object mock;
    private Method method;
    private List<Matcher> matchers;
    
    @Before
    public void setUp() {
        mock = mock(Object.class);
        method = method(Object.class, "methodName", String.class, int.class);
        invocation = new Invocation(mock, method, "abc", 123);
        matchers = Arrays.asList(equalTo("abc"), greaterThan(100));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }
    
    @Test
    public void testGetMethod() {
        Method expectedMethod = method(Object.class, "methodName", String.class, int.class);
        Method actualMethod = invocationMatcher.getMethod();
        
        assertEquals(expectedMethod, actualMethod);
    }
    
    @Test
    public void testGetInvocation() {
        Invocation expectedInvocation = new Invocation(mock, method, "abc", 123);
        Invocation actualInvocation = invocationMatcher.getInvocation();
        
        assertEquals(expectedInvocation, actualInvocation);
    }
    
    @Test
    public void testGetMatchers() {
        List<Matcher> expectedMatchers = Arrays.asList(equalTo("abc"), greaterThan(100));
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        
        assertEquals(expectedMatchers, actualMatchers);
    }
    
    @Test
    public void testToString() {
        String expectedString = "Matchers: equalTo(abc), greaterThan(100) - invocation: methodName(abc, 123)";
        String actualString = invocationMatcher.toString();
        
        assertEquals(expectedString, actualString);
    }
    
    @Test
    public void testMatches_True() {
        Invocation actualInvocation = new Invocation(mock, method, "abc", 123);
        
        boolean actualResult = invocationMatcher.matches(actualInvocation);
        
        assertTrue(actualResult);
    }
    
    @Test
    public void testMatches_False_MockDifferent() {
        Object differentMock = mock(Object.class);
        Invocation actualInvocation = new Invocation(differentMock, method, "abc", 123);
        
        boolean actualResult = invocationMatcher.matches(actualInvocation);
        
        assertFalse(actualResult);
    }
    
    @Test
    public void testMatches_False_MethodDifferent() {
        Method differentMethod = method(Object.class, "differentMethodName", String.class, int.class);
        Invocation actualInvocation = new Invocation(mock, differentMethod, "abc", 123);
        
        boolean actualResult = invocationMatcher.matches(actualInvocation);
        
        assertFalse(actualResult);
    }
    
    @Test
    public void testMatches_False_ArgumentsNotMatch() {
        Invocation actualInvocation = new Invocation(mock, method, "xyz", 456);
        
        boolean actualResult = invocationMatcher.matches(actualInvocation);
        
        assertFalse(actualResult);
    }
    
    @Test
    public void testHasSimilarMethod_True() {
        Invocation candidate = new Invocation(mock, method, "xyz", 789);
        
        boolean actualResult = invocationMatcher.hasSimilarMethod(candidate);
        
        assertTrue(actualResult);
    }
    
    @Test
    public void testHasSimilarMethod_False_MethodNameDifferent() {
        Method differentMethod = method(Object.class, "differentMethodName", String.class, int.class);
        Invocation candidate = new Invocation(mock, differentMethod, "abc", 123);
        
        boolean actualResult = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(actualResult);
    }
    
    @Test
    public void testHasSimilarMethod_False_VerifiedInvocation() {
        Invocation candidate = new Invocation(mock, method, "xyz", 789);
        candidate.markVerified();
        
        boolean actualResult = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(actualResult);
    }
    
    @Test
    public void testHasSimilarMethod_False_MockDifferent() {
        Object differentMock = mock(Object.class);
        Invocation candidate = new Invocation(differentMock, method, "xyz", 789);
        
        boolean actualResult = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(actualResult);
    }
    
    @Test
    public void testHasSimilarMethod_False_MethodEquals_ArgumentsNotMatch() {
        Invocation candidate = new Invocation(mock, method, "xyz", 456);
        
        boolean actualResult = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(actualResult);
    }
    
    @Test
    public void testHasSimilarMethod_True_MethodNotEquals_ArgumentsMatch() {
        Method differentMethod = method(Object.class, "differentMethodName", String.class, int.class);
        Invocation candidate = new Invocation(mock, differentMethod, "abc", 123);
        
        boolean actualResult = invocationMatcher.hasSimilarMethod(candidate);
        
        assertTrue(actualResult);
    }
    
    @Test
    public void testHasSameMethod_True() {
        Invocation candidate = new Invocation(mock, method, "xyz", 789);
        
        boolean actualResult = invocationMatcher.hasSameMethod(candidate);
        
        assertTrue(actualResult);
    }
    
    @Test
    public void testHasSameMethod_False_MethodNameDifferent() {
        Method differentMethod = method(Object.class, "differentMethodName", String.class, int.class);
        Invocation candidate = new Invocation(mock, differentMethod, "abc", 123);
        
        boolean actualResult = invocationMatcher.hasSameMethod(candidate);
        
        assertFalse(actualResult);
    }
    
    @Test
    public void testHasSameMethod_False_ArgumentTypesDifferent() {
        Method differentMethod = method(Object.class, "methodName", Integer.class, String.class);
        Invocation candidate = new Invocation(mock, differentMethod, "abc", 123);
        
        boolean actualResult = invocationMatcher.hasSameMethod(candidate);
        
        assertFalse(actualResult);
    }
    
    @Test
    public void testHasSameMethod_False_NumberOfArgumentsDifferent() {
        Method differentMethod = method(Object.class, "methodName", String.class);
        Invocation candidate = new Invocation(mock, differentMethod, "abc");
        
        boolean actualResult = invocationMatcher.hasSameMethod(candidate);
        
        assertFalse(actualResult);
    }
    
    @Test
    public void testGetLocation() {
        Location expectedLocation = mock(Location.class);
        when(invocation.getLocation()).thenReturn(expectedLocation);
        
        Location actualLocation = invocationMatcher.getLocation();
        
        assertEquals(expectedLocation, actualLocation);
    }
    
    @Test
    public void testCaptureArgumentsFrom_VarArgsFalse() {
        Object[] rawArguments = new Object[]{"arg1", "arg2", "arg3"};
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getRawArguments()).thenReturn(rawArguments);
        when(invocation.getArgumentAt(0, Object.class)).thenReturn("arg1");
        when(invocation.getArgumentAt(1, Object.class)).thenReturn("arg2");
        when(invocation.getArgumentAt(2, Object.class)).thenReturn("arg3");
        
        invocationMatcher.captureArgumentsFrom(invocation);
        
        verify(matchers.get(0)).captureFrom("arg1");
        verify(matchers.get(1)).captureFrom("arg2");
        verify(matchers.get(2)).captureFrom("arg3");
    }
    
    @Test
    public void testCaptureArgumentsFrom_VarArgsTrue() {
        Object[] rawArguments = new Object[]{"arg1", "arg2", "arg3"};
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getRawArguments()).thenReturn(rawArguments);
        when(invocation.isVarArgs()).thenReturn(true);
        when(invocation.getArgumentAt(0, Object.class)).thenReturn("arg1");
        when(invocation.getArgumentAt(1, Object.class)).thenReturn("arg2");
        when(invocation.getRawArguments()).thenReturn(rawArguments);
        
        invocationMatcher.captureArgumentsFrom(invocation);
        
        verify(matchers.get(0)).captureFrom("arg1");
        verify(matchers.get(1)).captureFrom("arg2");
        verify(matchers.get(2)).captureFrom("arg3");
    }
    
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        
        List<InvocationMatcher> actualInvocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(2, actualInvocationMatchers.size());
        assertEquals(invocation1, actualInvocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, actualInvocationMatchers.get(1).getInvocation());
    }

}