import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {
    
    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;
    
    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<Matcher>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }
    
    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        
        Method actualMethod = invocationMatcher.getMethod();
        
        assertEquals(method, actualMethod);
    }
    
    @Test
    public void testGetInvocation() {
        Invocation actualInvocation = invocationMatcher.getInvocation();
        
        assertEquals(invocation, actualInvocation);
    }
    
    @Test
    public void testGetMatchers() {
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        
        assertEquals(matchers, actualMatchers);
    }
    
    @Test
    public void testToString() {
        String expectedString = "expected invocation";
        when(invocation.toString(matchers, new PrintSettings())).thenReturn(expectedString);
        
        String actualString = invocationMatcher.toString();
        
        assertEquals(expectedString, actualString);
    }
    
    @Test
    public void testMatches() {
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(actualInvocation.getArguments()).thenReturn(invocation.getArguments());
        when(actualInvocation.isVerified()).thenReturn(false);
        when(invocationMatcher.safelyArgumentsMatch(invocation.getArguments())).thenReturn(true);
        
        boolean matches = invocationMatcher.matches(actualInvocation);
        
        assertTrue(matches);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(candidate.isVerified()).thenReturn(false);
        when(invocationMatcher.hasSameMethod(candidate)).thenReturn(false);
        when(invocationMatcher.safelyArgumentsMatch(candidate.getArguments())).thenReturn(true);
        
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);
        
        assertTrue(hasSimilarMethod);
    }
    
    @Test
    public void testHasSimilarMethodMethodNameNotEquals() {
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(hasSimilarMethod);
    }
    
    @Test
    public void testHasSimilarMethodCandidateVerified() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.isVerified()).thenReturn(true);
        
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(hasSimilarMethod);
    }
    
    @Test
    public void testHasSimilarMethodMockNotTheSame() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        when(candidate.getMock()).thenReturn(mock(Object.class));
        
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(hasSimilarMethod);
    }
    
    @Test
    public void testHasSimilarMethodNotSameMethod() {
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(invocationMatcher.hasSameMethod(candidate)).thenReturn(false);
        
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(hasSimilarMethod);
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        when(method1.equals(method2)).thenReturn(true);
        
        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidate);
        
        assertTrue(hasSameMethod);
    }
    
    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        
        Location actualLocation = invocationMatcher.getLocation();
        
        assertEquals(location, actualLocation);
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        String expectedString = "expected invocation";
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.toString(matchers, printSettings)).thenReturn(expectedString);
        
        String actualString = invocationMatcher.toString(printSettings);
        
        assertEquals(expectedString, actualString);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Object[] args = {1, "test"};
        when(i.getArguments()).thenReturn(args);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        matchers.add(capturesArguments);
        
        invocationMatcher.captureArgumentsFrom(i);
        
        verify(capturesArguments).captureFrom(args[0]);
        verify(capturesArguments).captureFrom(args[1]);
    }
    
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = new ArrayList<Invocation>();
        invocations.add(invocation1);
        invocations.add(invocation2);
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(2, invocationMatchers.size());
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }

}