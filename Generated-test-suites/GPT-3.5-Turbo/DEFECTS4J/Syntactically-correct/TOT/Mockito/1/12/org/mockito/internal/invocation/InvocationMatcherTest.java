import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.MethodInvocationReport;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.session.DefaultMockitoSession;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matcher;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;
    
    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }
    
    @Test
    public void testConstructorWithArguments() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new Equals("arg1"));
        matchers.add(new Equals("arg2"));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithoutArguments() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
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
        PrintSettings printSettings = mock(PrintSettings.class);
        String expectedResult = "MockitoInvocationMatcher";
        when(printSettings.print(matchers, invocation)).thenReturn(expectedResult);
        
        String result = invocationMatcher.toString();
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        Object mock = mock(Object.class);
        Object[] args = {"arg1", "arg2"};
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(args);
        when(actual.getMock()).thenReturn(mock);
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(actual.getArguments()).thenReturn(args);
        
        boolean result = invocationMatcher.matches(actual);
        
        assertTrue(result);
    }
    
    @Test
    public void testMatches_WithDifferentMocks() {
        Invocation actual = mock(Invocation.class);
        Object mock = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock);
        when(actual.getMock()).thenReturn(mock(Object.class));
        
        boolean result = invocationMatcher.matches(actual);
        
        assertFalse(result);
    }
    
    @Test
    public void testMatches_WithDifferentMethods() {
        Invocation actual = mock(Invocation.class);
        Object mock = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMock()).thenReturn(mock);
        when(actual.getMethod()).thenReturn(mock(Method.class, "differentMethod"));
        
        boolean result = invocationMatcher.matches(actual);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        Object mock = mock(Object.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock);
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertTrue(result);
    }
    
    @Test
    public void testHasSimilarMethod_WithDifferentMethodNames() {
        Invocation candidate = mock(Invocation.class);
        Object mock = mock(Object.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class, "method1"));
        when(candidate.getMethod()).thenReturn(mock(Method.class, "method2"));
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock);
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethod_WithVerifiedCandidate() {
        Invocation candidate = mock(Invocation.class);
        Object mock = mock(Object.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(true);
        when(candidate.getMock()).thenReturn(mock);
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethod_WithDifferentMocks() {
        Invocation candidate = mock(Invocation.class);
        Object mock = mock(Object.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock(Object.class));
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethod_WithOverloadedButSameArgs() {
        Invocation candidate = mock(Invocation.class);
        Object mock = mock(Object.class);
        Object[] args = {"arg1", "arg2"};
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock);
        when(invocation.getArguments()).thenReturn(args);
        when(candidate.getArguments()).thenReturn(args);
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethod_WithDifferentArgs() {
        Invocation candidate = mock(Invocation.class);
        Object mock = mock(Object.class);
        Object[] args1 = {"arg1", "arg2"};
        Object[] args2 = {"arg3", "arg4"};
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock);
        when(invocation.getArguments()).thenReturn(args1);
        when(candidate.getArguments()).thenReturn(args2);
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertTrue(result);
    }
    
    @Test
    public void testHasSameMethod() {
        Method method = mock(Method.class);
        Method candidateMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(candidateMethod);
        when(method.getName()).thenReturn("method");
        when(candidateMethod.getName()).thenReturn("method");
        
        boolean result = invocationMatcher.hasSameMethod(candidate);
        
        assertTrue(result);
    }
    
    @Test
    public void testHasSameMethod_WithDifferentMethodNames() {
        Method method = mock(Method.class, "method1");
        Method candidateMethod = mock(Method.class, "method2");
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(candidateMethod);
        
        boolean result = invocationMatcher.hasSameMethod(candidate);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSameMethod_WithDifferentParameterTypes() {
        Method method = mock(Method.class);
        Method candidateMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(candidateMethod);
        when(method.getName()).thenReturn("method");
        when(candidateMethod.getName()).thenReturn("method");
        when(method.getParameterTypes()).thenReturn(new Class[] {String.class});
        when(candidateMethod.getParameterTypes()).thenReturn(new Class[] {Integer.class});
        
        boolean result = invocationMatcher.hasSameMethod(candidate);
        
        assertFalse(result);
    }
    
    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        
        Location result = invocationMatcher.getLocation();
        
        assertEquals(location, result);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        matchers.add(matcher);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArgumentAt(0, Object.class)).thenReturn("arg1");
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(matcher instanceof CapturesArguments).thenReturn(true);
        when(matcher).thenReturn(capturesArguments);
        
        invocationMatcher.captureArgumentsFrom(invocation);
        
        verify(capturesArguments).captureFrom("arg1");
    }
    
    @Test
    public void testCaptureArgumentsFrom_WithVarArgs() {
        Invocation invocation = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        matchers.add(matcher);
        when(invocation.getMethod()).thenReturn(mock(Method.class, "method", String[].class));
        when(invocation.getRawArguments()).thenReturn(new Object[] {"arg1", "arg2"});
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(matcher instanceof CapturesArguments).thenReturn(true);
        when(matcher).thenReturn(capturesArguments);
        
        invocationMatcher.captureArgumentsFrom(invocation);
        
        verify(capturesArguments).captureFrom("arg1");
        verify(capturesArguments).captureFrom("arg2");
    }
    
    @Test
    public void testIsVarargMatcher() {
        Matcher matcher = mock(Matcher.class);
        VarargMatcher varargMatcher = mock(VarargMatcher.class);
        when(matcher instanceof VarargMatcher).thenReturn(true);
        
        boolean result = invocationMatcher.isVarargMatcher(matcher);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsVarargMatcher_WithMatcherDecorator() {
        MatcherDecorator matcherDecorator = mock(MatcherDecorator.class);
        Matcher matcher = mock(Matcher.class);
        VarargMatcher varargMatcher = mock(VarargMatcher.class);
        when(matcherDecorator.getActualMatcher()).thenReturn(varargMatcher);
        when(matcher instanceof MatcherDecorator).thenReturn(true);
        when(matcher).thenReturn(matcherDecorator);
        
        boolean result = invocationMatcher.isVarargMatcher(matcher);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsVarargMatcher_WithNoVarargMatcher() {
        Matcher matcher = mock(Matcher.class);
        
        boolean result = invocationMatcher.isVarargMatcher(matcher);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsVariableArgument() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class, "method", String[].class));
        when(invocation.getRawArguments()).thenReturn(new Object[] {"arg1", "arg2"});
        
        boolean result = invocationMatcher.isVariableArgument(invocation, 1);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsVariableArgument_WithVarargs() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class, "method", String[].class));
        when(invocation.getRawArguments()).thenReturn(new Object[] {"arg1", new String[] {"arg2", "arg3"}});
        
        boolean result = invocationMatcher.isVariableArgument(invocation, 1);
        
        assertTrue(result);
    }
    
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation1);
        invocations.add(invocation2);
        
        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);
        
        assertEquals(2, result.size());
        assertEquals(invocation1, result.get(0).getInvocation());
        assertEquals(invocation2, result.get(1).getInvocation());
    }
}