import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class InvocationMatcherTest {
    
    private Invocation invocation;
    private InvocationMatcher invocationMatcher;
    
    @Before
    public void setUp() throws NoSuchMethodException {
        Object mock = Mockito.mock(Object.class);
        Method method = Object.class.getMethod("toString");
        invocation = new Invocation(mock, method);
        invocationMatcher = new InvocationMatcher(invocation);
    }
    
    @Test
    public void testGetMethod() {
        Method expectedMethod = Object.class.getMethod("toString");
        Method actualMethod = invocationMatcher.getMethod();
        assertEquals(expectedMethod, actualMethod);
    }
    
    @Test
    public void testGetInvocation() {
        Invocation actualInvocation = invocationMatcher.getInvocation();
        assertEquals(invocation, actualInvocation);
    }
    
    @Test
    public void testGetMatchers() {
        Throwable[] expectedMatchers = new Throwable[0];
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        assertArrayEquals(expectedMatchers, actualMatchers);
    }
    
    @Test
    public void testToString() {
        String expectedString = "InvocationMatcher: " + invocation.toString();
        String actualString = invocationMatcher.toString();
        assertEquals(expectedString, actualString);
    }
    
    @Test
    public void testMatches_sameInvocation() {
        assertTrue(invocationMatcher.matches(invocation));
    }
    
    @Test
    public void testMatches_differentInvocation() throws NoSuchMethodException {
        Object mock = Mockito.mock(Object.class);
        Method method = Object.class.getMethod("hashCode");
        Invocation differentInvocation = new Invocation(mock, method);
        assertFalse(invocationMatcher.matches(differentInvocation));
    }
    
    @Test
    public void testHasSimilarMethod_sameMethodName() {
        Object mock = Mockito.mock(Object.class);
        Method method = Object.class.getMethod("toString");
        Invocation candidate = new Invocation(mock, method);
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethod_differentMethodName() {
        Object mock = Mockito.mock(Object.class);
        Method method = Object.class.getMethod("hashCode");
        Invocation candidate = new Invocation(mock, method);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethod_verifiedCandidate() {
        Object mock = Mockito.mock(Object.class);
        Method method = Object.class.getMethod("toString");
        Invocation candidate = new Invocation(mock, method);
        candidate.markVerified();
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethod_differentMock() {
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        Method method = Object.class.getMethod("toString");
        Invocation candidate = new Invocation(mock2, method);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethod_overloadedButSameArgs() {
        Object mock = Mockito.mock(Object.class);
        Method method = Object.class.getMethod("toString");
        Invocation candidate = new Invocation(mock, method);
        Object[] args = new Object[] {"arg1", "arg2"};
        candidate.setArguments(args);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod_sameMethod() {
        Object mock = Mockito.mock(Object.class);
        Method method = Object.class.getMethod("toString");
        Invocation candidate = new Invocation(mock, method);
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod_differentMethod() throws NoSuchMethodException {
        Object mock = Mockito.mock(Object.class);
        Method method1 = Object.class.getMethod("toString");
        Method method2 = Object.class.getMethod("hashCode");
        Invocation candidate = new Invocation(mock, method2);
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testGetLocation() {
        Location expectedLocation = null; // Not provided in the given Java class
        Location actualLocation = invocationMatcher.getLocation();
        assertEquals(expectedLocation, actualLocation);
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        // Create a custom PrintSettings object
        PrintSettings printSettings = new PrintSettings();
        
        String expectedString = "InvocationMatcher: " + invocation.toString(matchers, printSettings);
        String actualString = invocationMatcher.toString(printSettings);
        assertEquals(expectedString, actualString);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        // Create an Invocation object with arguments
        Object mock = Mockito.mock(Object.class);
        Method method = Object.class.getMethod("toString", int.class);
        Object[] arguments = {5};
        Invocation i = new Invocation(mock, method, arguments);
        
        // Create a CapturesArguments matcher
        CaputuresArgumentsMatcher capturesArgumentsMatcher = Mockito.mock(CapturesArgumentsMatcher.class);
        
        // Create an InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(i, Matchers.toList(capturesArgumentsMatcher));
        
        // Call captureArgumentsFrom() method
        invocationMatcher.captureArgumentsFrom(invocation);
        
        // Verify that the argument was captured
        Mockito.verify(capturesArgumentsMatcher).captureFrom(5);
    }
    
    @Test
    public void testCreateFrom() {
        // Create a list of invocations
        List<Invocation> invocations = new LinkedList<>();
        
        Object mock1 = Mockito.mock(Object.class);
        Method method1 = Object.class.getMethod("toString");
        Invocation invocation1 = new Invocation(mock1, method1);
        invocations.add(invocation1);
        
        Object mock2 = Mockito.mock(Object.class);
        Method method2 = Object.class.getMethod("hashCode");
        Invocation invocation2 = new Invocation(mock2, method2);
        invocations.add(invocation2);
        
        // Call createFrom() method
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        // Verify that the list of InvocationMatchers was created correctly
        assertEquals(2, invocationMatchers.size());
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }
}