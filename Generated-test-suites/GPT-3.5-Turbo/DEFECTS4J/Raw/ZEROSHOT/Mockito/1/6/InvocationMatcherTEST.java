import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.reporting.PrintSettings;
import org.hamcrest.Matcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.internal.matchers.ArgumentsProcessor;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;

    private InvocationMatcher invocationMatcher;
    
    @Before
    public void setUp() {
        invocation = Mockito.mock(Invocation.class);
        matchers = new ArrayList<>();
        
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }
    
    @Test
    public void testGetMethod() {
        Method expectedMethod = invocation.getMethod();
        
        Method actualMethod = invocationMatcher.getMethod();
        
        assertSame(expectedMethod, actualMethod);
    }
    
    @Test
    public void testGetInvocation() {
        Invocation expectedInvocation = invocation;
        
        Invocation actualInvocation = invocationMatcher.getInvocation();
        
        assertSame(expectedInvocation, actualInvocation);
    }
    
    @Test
    public void testGetMatchers() {
        List<Matcher> expectedMatchers = matchers;
        
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        
        assertSame(expectedMatchers, actualMatchers);
    }
    
    @Test
    public void testToString() {
        String expectedString = new PrintSettings().print(matchers, invocation);
        
        String actualString = invocationMatcher.toString();
        
        assertEquals(expectedString, actualString);
    }
    
    @Test
    public void testMatches() {
        Invocation actual = Mockito.mock(Invocation.class);
        Mockito.when(actual.getMock()).thenReturn(invocation.getMock());
        Mockito.when(actual.getMethod()).thenReturn(invocation.getMethod());
        Mockito.when(actual.getArguments()).thenReturn(invocation.getArguments());
        
        boolean result = invocationMatcher.matches(actual);
        
        assertTrue(result);
    }
    
    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[invocation.getArguments().length];
        System.arraycopy(invocation.getArguments(), 0, actualArgs, 0, actualArgs.length);
        
        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);
        
        assertTrue(result);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = Mockito.mock(Invocation.class);
        Method method = invocation.getMethod();
        Mockito.when(candidate.getMethod()).thenReturn(method);
        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(candidate.getMock()).thenReturn(invocation.getMock());
        Mockito.when(candidate.getArguments()).thenReturn(invocation.getArguments());
        
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        assertFalse(result);
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation candidate = Mockito.mock(Invocation.class);
        Method method = invocation.getMethod();
        Mockito.when(candidate.getMethod()).thenReturn(method);
        
        boolean result = invocationMatcher.hasSameMethod(candidate);
        
        assertTrue(result);
    }
    
    @Test
    public void testGetLocation() {
        Location expectedLocation = invocation.getLocation();
        
        Location actualLocation = invocationMatcher.getLocation();
        
        assertSame(expectedLocation, actualLocation);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = Mockito.mock(Invocation.class);
        int[] array = {1, 2, 3};
        Mockito.when(invocation.getMethod()).thenReturn(InvocationMatcher.class.getMethod("captureArgumentsFrom", Invocation.class));
        Mockito.when(invocation.getRawArguments()).thenReturn(new Object[]{array});
        
        try {
            invocationMatcher.captureArgumentsFrom(invocation);
            fail("Exception not thrown");
        } catch (UnsupportedOperationException e) {
            // Test passed
        }
    }
    
    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        invocations.add(invocation1);
        invocations.add(invocation2);
        
        List<InvocationMatcher> expectedInvocationMatchers = new ArrayList<>();
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        expectedInvocationMatchers.add(invocationMatcher1);
        expectedInvocationMatchers.add(invocationMatcher2);
        
        List<InvocationMatcher> actualInvocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertIterableEquals(expectedInvocationMatchers, actualInvocationMatchers);
    }
}