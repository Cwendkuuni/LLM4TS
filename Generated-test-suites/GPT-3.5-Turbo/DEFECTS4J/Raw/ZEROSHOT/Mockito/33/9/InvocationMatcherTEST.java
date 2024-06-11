import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.ArrayList;
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
        matchers = new ArrayList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }
    
    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        
        Method result = invocationMatcher.getMethod();
        
        assertSame(method, result);
    }
    
    @Test
    public void testGetInvocation() {
        Invocation result = invocationMatcher.getInvocation();
        
        assertSame(invocation, result);
    }
    
    @Test
    public void testGetMatchers() {
        List<Matcher> result = invocationMatcher.getMatchers();
        
        assertSame(matchers, result);
    }
    
    @Test
    public void testToString() {
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(matchers, printSettings)).thenReturn("test");
        
        String result = invocationMatcher.toString();
        
        assertEquals("test", result);
    }
    
    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(invocation.argumentsToMatchers()).thenReturn(matchers);
        when(invocationMatcher.hasSameMethod(actual)).thenReturn(true);
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, actual)).thenReturn(true);
        
        boolean result = invocationMatcher.matches(actual);
        
        assertTrue(result);
    }
    
    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[] {1, 2, 3};
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, actualArgs)).thenReturn(true);
        
        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);
        
        assertTrue(result);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getName()).thenReturn("test");
        when(candidate.getMethod().getName()).thenReturn("test");
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(invocationMatcher.hasSameMethod(candidate)).thenReturn(true);
        when(invocationMatcher.safelyArgumentsMatch(candidate.getArguments())).thenReturn(false);
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        when(method1.equals(method2)).thenReturn(true);
        
        boolean result = invocationMatcher.hasSameMethod(candidate);
        
        assertTrue(result);
    }
    
    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        
        Location result = invocationMatcher.getLocation();
        
        assertSame(location, result);
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(matchers, printSettings)).thenReturn("test");
        
        String result = invocationMatcher.toString(printSettings);
        
        assertEquals("test", result);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Matcher matcher = mock(CapturesArguments.class);
        when(i.getArguments()).thenReturn(new Object[] {1, 2, 3});
        when(matchers.get(0)).thenReturn(matcher);
        when(matcher instanceof CapturesArguments).thenReturn(true);
        
        invocationMatcher.captureArgumentsFrom(i);
        
        verify((CapturesArguments) matcher).captureFrom(1);
    }
    
    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        Invocation i1 = mock(Invocation.class);
        Invocation i2 = mock(Invocation.class);
        invocations.add(i1);
        invocations.add(i2);
        
        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);
        
        assertEquals(2, result.size());
        assertSame(i1, result.get(0).getInvocation());
        assertSame(i2, result.get(1).getInvocation());
    }
}