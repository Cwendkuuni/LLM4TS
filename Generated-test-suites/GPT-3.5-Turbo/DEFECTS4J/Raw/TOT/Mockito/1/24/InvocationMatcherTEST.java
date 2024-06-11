import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    
    @Before
    public void setup() {
        invocation = mock(Invocation.class);
        invocationMatcher = new InvocationMatcher(invocation);
    }
    
    @Test
    public void testConstructorWithInvocationAndMatchers() {
        Matcher matcher = mock(Matcher.class);
        invocationMatcher = new InvocationMatcher(invocation, Collections.singletonList(matcher));
        assertNotNull(invocationMatcher.getMethod());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.singletonList(matcher), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithInvocation() {
        assertNotNull(invocationMatcher.getMethod());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }
    
    @Test
    public void testToString() {
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        when(matcher1.toString()).thenReturn("matcher1");
        when(matcher2.toString()).thenReturn("matcher2");
        invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(matcher1, matcher2));
        
        String expected = "matcher1, matcher2";
        assertEquals(expected, invocationMatcher.toString());
    }
    
    @Test
    public void testMatchesWithEqualInvocation() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(actual.getMock()).thenReturn(invocation.getMock());
        when(actual.getMethod()).thenReturn(invocation.getMethod());
        when(invocation.getMethod().isVarArgs()).thenReturn(false);
        when(actual.getArguments()).thenReturn(new Object[]{});
        
        assertTrue(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testMatchesWithDifferentMock() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(actual.getMethod()).thenReturn(invocation.getMethod());
        when(invocation.getMethod().isVarArgs()).thenReturn(false);
        when(actual.getArguments()).thenReturn(new Object[]{});
        
        assertFalse(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testMatchesWithDifferentMethod() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(actual.getMock()).thenReturn(invocation.getMock());
        when(actual.getMethod()).thenReturn(String.class.getDeclaredMethods()[0]);
        when(invocation.getMethod().isVarArgs()).thenReturn(false);
        when(actual.getArguments()).thenReturn(new Object[]{});
        
        assertFalse(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testMatchesWithDifferentArguments() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(actual.getMock()).thenReturn(invocation.getMock());
        when(actual.getMethod()).thenReturn(invocation.getMethod());
        when(invocation.getMethod().isVarArgs()).thenReturn(false);
        when(actual.getArguments()).thenReturn(new Object[]{"arg"});
        
        assertFalse(invocationMatcher.matches(actual));
    }
    
    @Test
    public void testHasSimilarMethodWithSameMethodName() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(candidate.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(invocation.getMethod().isVarArgs()).thenReturn(false);
        when(candidate.getArguments()).thenReturn(new Object[]{});
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethodWithSameMethodNameButDifferentMock() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(candidate.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(invocation.getMethod().isVarArgs()).thenReturn(false);
        when(candidate.getArguments()).thenReturn(new Object[]{});
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethodWithDifferentMethodName() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(candidate.getMethod()).thenReturn(String.class.getDeclaredMethods()[0]);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(invocation.getMethod().isVarArgs()).thenReturn(false);
        when(candidate.getArguments()).thenReturn(new Object[]{});
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethodWithDifferentArguments() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(candidate.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(invocation.getMethod().isVarArgs()).thenReturn(false);
        when(candidate.getArguments()).thenReturn(new Object[]{"arg"});
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethodWithOverloadedMethod() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(candidate.getMethod()).thenReturn(Object.class.getDeclaredMethods()[1]);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(invocation.getMethod().isVarArgs()).thenReturn(false);
        when(candidate.getArguments()).thenReturn(new Object[]{});
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethodWithEqualMethods() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("test");
        when(method2.getName()).thenReturn("test");
        when(method1.getParameterTypes()).thenReturn(new Class<?>[]{});
        when(method2.getParameterTypes()).thenReturn(new Class<?>[]{});
        
        assertTrue(invocationMatcher.hasSameMethod(invocation));
    }
    
    @Test
    public void testHasSameMethodWithDifferentMethodName() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("test1");
        when(method2.getName()).thenReturn("test2");
        
        assertFalse(invocationMatcher.hasSameMethod(invocation));
    }
    
    @Test
    public void testHasSameMethodWithDifferentArguments() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("test");
        when(method2.getName()).thenReturn("test");
        when(method1.getParameterTypes()).thenReturn(new Class<?>[]{String.class});
        when(method2.getParameterTypes()).thenReturn(new Class<?>[]{Integer.class});
        
        assertFalse(invocationMatcher.hasSameMethod(invocation));
    }
    
    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        
        assertEquals(location, invocationMatcher.getLocation());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromVarArgs() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.isVarArgs()).thenReturn(true);
        
        invocationMatcher.captureArgumentsFrom(invocation);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(matcher1 instanceof CapturesArguments).thenReturn(true);
        when(matcher1).thenReturn(capturesArguments);
        when(matcher2 instanceof CapturesArguments).thenReturn(false);
        when(invocation.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(matcher1, matcher2));
        
        invocationMatcher.captureArgumentsFrom(invocation);
        
        verify(capturesArguments).captureFrom("arg1");
        verifyNoMoreInteractions(capturesArguments);
    }
    
    @Test
    public void testIsVarargMatcher() {
        Matcher matcher = mock(Matcher.class);
        
        assertFalse(invocationMatcher.isVarargMatcher(matcher));
        
        Matcher varargMatcher = mock(VarargMatcher.class);
        
        assertTrue(invocationMatcher.isVarargMatcher(varargMatcher));
        
        MatcherDecorator decorator = mock(MatcherDecorator.class);
        when(decorator.getActualMatcher()).thenReturn(varargMatcher);
        
        assertTrue(invocationMatcher.isVarargMatcher(decorator));
    }
    
    @Test
    public void testIsVariableArgumentWithVarargsAndValidPosition() {
        Method method = mock(Method.class);
        when(method.isVarArgs()).thenReturn(true);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getRawArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        
        assertTrue(invocationMatcher.isVariableArgument(invocation, 1));
    }
    
    @Test
    public void testIsVariableArgumentWithVarargsAndInvalidPosition() {
        Method method = mock(Method.class);
        when(method.isVarArgs()).thenReturn(true);
        when(invocation.getMethod()).thenReturn(method);
        when(invocation.getRawArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        
        assertFalse(invocationMatcher.isVariableArgument(invocation, 0));
    }
    
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        
        List<InvocationMatcher> expected = new LinkedList<>();
        expected.add(new InvocationMatcher(invocation1));
        expected.add(new InvocationMatcher(invocation2));
        
        List<InvocationMatcher> actual = InvocationMatcher.createFrom(invocations);
        
        assertEquals(expected, actual);
    }
    
}