import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {
    
    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;
    
    @Before
    public void setUp() throws Exception{
        invocation = mock(Invocation.class);
        matchers = mock(List.class);
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }
    
    @Test
    public void testGetMethod() {
        invocation.getMethod();
        assertEquals(invocationMatcher.getMethod(), invocation.getMethod());
    }
    
    @Test
    public void testGetInvocation() {
        assertEquals(invocationMatcher.getInvocation(), invocationMatcher.getInvocation());
    }
    
    @Test
    public void testGetMatchers() {
        assertEquals(invocationMatcher.getMatchers(), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testToString() {
        PrintSettings printSettings = mock(PrintSettings.class);
        when(printSettings.print(matchers, invocation)).thenReturn("Printed");
        
        assertEquals("Printed", invocationMatcher.toString());
        verify(printSettings, times(1)).print(matchers, invocation);
    }
    
    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        
        when(invocation.getMock()).thenReturn(actual.getMock());
        when(invocation.getMethod()).thenReturn(actual.getMethod());
        when(invocation.getArguments()).thenReturn(actual.getArguments());
        
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(invocationMatcher, actual)).thenReturn(true);
        
        assertEquals(true, invocationMatcher.matches(actual));
        verify(invocation, times(1)).getMock();
        verify(invocation, times(1)).getMethod();
        verify(invocation, times(1)).getArguments();
        verify(argumentsComparator, times(1)).argumentsMatch(invocationMatcher, actual);
    }
    
    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[]{1, 2, 3};
        
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        when(argumentsComparator.argumentsMatch(invocationMatcher, actualArgs)).thenReturn(true);
        
        assertEquals(true, invocationMatcher.safelyArgumentsMatch(actualArgs));
        verify(argumentsComparator, times(1)).argumentsMatch(invocationMatcher, actualArgs);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Method wantedMethod = mock(Method.class);
        Method currentMethod = mock(Method.class);
        Object[] arguments = new Object[]{1, 2};
        
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(currentMethod);
        when(candidate.getMethod().getName()).thenReturn(wantedMethod.getName());
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(candidate.getMock());
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(invocation.getMethod()).thenReturn(wantedMethod);
        when(invocation.getMethod().getName()).thenReturn(wantedMethod.getName());
        when(invocation.getArguments()).thenReturn(arguments);
        when(candidate.getArguments()).thenReturn(arguments);
        when(invocationMatcher.hasSameMethod(candidate)).thenReturn(true);
        when(invocationMatcher.safelyArgumentsMatch(candidate.getArguments())).thenReturn(true);
        
        assertEquals(true, invocationMatcher.hasSimilarMethod(candidate));
        verify(wantedMethod, times(1)).getName();
        verify(currentMethod, times(1)).getName();
        verify(candidate, times(1)).getMethod();
        verify(candidate, times(1)).isVerified();
        verify(candidate, times(1)).getMock();
        verify(invocation, times(1)).getMock();
        verify(invocation, times(1)).getMethod();
        verify(invocation, times(1)).getArguments();
        verify(candidate, times(1)).getArguments();
        verify(invocationMatcher, times(1)).hasSameMethod(candidate);
        verify(invocationMatcher, times(1)).safelyArgumentsMatch(candidate.getArguments());
    }
    
    @Test
    public void testHasSameMethod() {
         Method m1 = mock(Method.class);
         Method m2 = mock(Method.class);
         Class[] params1 = {Integer.class, String.class};
         Class[] params2 = {Integer.class, String.class};
         
         when(invocation.getMethod()).thenReturn(m1);
         when(invocation.getMethod().getName()).thenReturn("method1");
         when(m1.getName()).thenReturn("method1");
         when(m1.getParameterTypes()).thenReturn(params1);
         when(m2.getParameterTypes()).thenReturn(params2);
         
         assertEquals(true, invocationMatcher.hasSameMethod(invocation));
         verify(invocation, times(1)).getMethod();
         verify(m1, times(1)).getName();
         verify(m1, times(1)).getParameterTypes();
    }
    
    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        
        assertEquals(location, invocationMatcher.getLocation());
        verify(invocation, times(1)).getLocation();
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        matcher1 = mock(Matcher.class);
        matcher2 = mock(CapturesArguments.class);
        
        when(invocation.getMethod().isVarArgs()).thenReturn(false);
        when(matchers.size()).thenReturn(2);
        when(matchers.get(0)).thenReturn(matcher1);
        when(matchers.get(1)).thenReturn(matcher2);
        when(invocation.getArgumentAt(0, Object.class)).thenReturn(1);
        when(((CapturesArguments) matcher2).captureFrom(1)).thenReturn(1);
        
        invocationMatcher.captureArgumentsFrom(invocation);
        verify(invocation.getMethod(), times(1)).isVarArgs();
        verify(matchers, times(1)).size();
        verify(matchers, times(2)).get(anyInt());
        verify(invocation, times(1)).getArgumentAt(0, Object.class);
        verify((CapturesArguments)matcher2, times(1)).captureFrom(1);
    }
    
    @Test
    public void testCreateFrom() {
        Invocation i1 = mock(Invocation.class);
        Invocation i2 = mock(Invocation.class);
        
        List<Invocation> invocations = Arrays.asList(i1, i2);
        List<InvocationMatcher> expectedOutput = Arrays.asList(new InvocationMatcher(i1), new InvocationMatcher(i2));
        
        assertEquals(expectedOutput, InvocationMatcher.createFrom(invocations));
    }
}