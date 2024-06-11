import org.junit.Test;
import static org.mockito.Mockito.*;
import java.lang.reflect.Method;
import java.util.List;
import org.hamcrest.Matcher;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {
    
    // Test for the constructor with matchers parameter
    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        // Verify that the invocation and matchers are correctly set
        assertSame(invocation, invocationMatcher.getInvocation());
        assertSame(matchers, invocationMatcher.getMatchers());
    }
    
    // Test for the constructor without matchers parameter
    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Verify that the invocation is correctly set and matchers is empty
        assertSame(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }
    
    // Test for getMethod method
    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Verify that the correct method is returned
        assertSame(method, invocationMatcher.getMethod());
    }
    
    // Test for matches method
    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        when(invocation1.isVerified()).thenReturn(false);
        when(invocation2.isVerified()).thenReturn(false);
        when(invocation1.getArguments()).thenReturn(new Object[] {1, 2});
        when(invocation2.getArguments()).thenReturn(new Object[] {1, 2});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        // Verify that the matches method correctly checks for matching invocation
        assertTrue(invocationMatcher.matches(invocation2));
    }
    
    // Test for hasSimilarMethod method
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.getMethod().getName()).thenReturn("method1");
        when(invocation2.getMethod().getName()).thenReturn("method1");
        when(invocation1.isVerified()).thenReturn(false);
        when(invocation2.isVerified()).thenReturn(false);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        // Verify that the hasSimilarMethod method correctly checks for similar method
        assertTrue(invocationMatcher.hasSimilarMethod(invocation2));
    }
    
    // Test for hasSameMethod method
    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("method1");
        when(method2.getName()).thenReturn("method1");
        when(method1.getParameterTypes()).thenReturn(new Class<?>[] {int.class, String.class});
        when(method2.getParameterTypes()).thenReturn(new Class<?>[] {int.class, String.class});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        // Verify that the hasSameMethod method correctly checks for same method
        assertTrue(invocationMatcher.hasSameMethod(invocation2));
    }
    
    // Test for getLocation method
    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Verify that the correct location is returned
        assertSame(location, invocationMatcher.getLocation());
    }
    
    // Test for captureArgumentsFrom method
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(matcher instanceof CapturesArguments).thenReturn(true);
        when(matcher).thenReturn(capturesArguments);
        when(invocation1.getMethod().isVarArgs()).thenReturn(false);
        when(invocation1.getArgumentAt(anyInt(), eq(Object.class))).thenReturn("argument");
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        invocationMatcher.getMatchers().add(matcher);
        
        invocationMatcher.captureArgumentsFrom(invocation2);
        
        // Verify that the arguments are captured from the invocation2
        verify(capturesArguments).captureFrom("argument");
    }
    
    // Test for createFrom method
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = List.of(invocation1, invocation2);
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        // Verify that InvocationMatchers are correctly created from the invocations list
        assertEquals(2, invocationMatchers.size());
        assertSame(invocation1, invocationMatchers.get(0).getInvocation());
        assertSame(invocation2, invocationMatchers.get(1).getInvocation());
    }
}