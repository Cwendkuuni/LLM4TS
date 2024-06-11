import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

public class InvocationMatcherTest {
    
    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;
    
    @Before
    public void setup() throws NoSuchMethodException {
        TestClass testClass = new TestClass();
        Method method = TestClass.class.getMethod("testMethod");
        invocation = new Invocation(testClass, method, new Object[0], null, new Location());
        matchers = new ArrayList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }
    
    @Test
    public void testConstructor_withInvocationAndMatchers() {
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructor_withInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }
    
    @Test
    public void testToString() {
        String expected = invocation.toString(matchers, new PrintSettings());
        assertEquals(expected, invocationMatcher.toString());
    }
    
    @Test
    public void testMatches_sameMockAndMethod() throws NoSuchMethodException {
        TestClass testClass = new TestClass();
        Method method = TestClass.class.getMethod("testMethod");
        Invocation actualInvocation = new Invocation(testClass, method, new Object[0], null, new Location());
        
        assertTrue(invocationMatcher.matches(actualInvocation));
    }
    
    @Test
    public void testMatches_differentMock() throws NoSuchMethodException {
        TestClass testClass = new TestClass();
        TestClass otherTestClass = new TestClass();
        Method method = TestClass.class.getMethod("testMethod");
        Invocation actualInvocation = new Invocation(otherTestClass, method, new Object[0], null, new Location());
        
        assertFalse(invocationMatcher.matches(actualInvocation));
    }
    
    @Test
    public void testMatches_differentMethod() throws NoSuchMethodException {
        TestClass testClass = new TestClass();
        Method method = TestClass.class.getMethod("anotherTestMethod");
        Invocation actualInvocation = new Invocation(testClass, method, new Object[0], null, new Location());
        
        assertFalse(invocationMatcher.matches(actualInvocation));
    }
    
    @Test
    public void testHasSimilarMethod_sameMockAndMethod() throws NoSuchMethodException {
        TestClass testClass = new TestClass();
        Method method = TestClass.class.getMethod("testMethod");
        Invocation candidate = new Invocation(testClass, method, new Object[0], null, new Location());
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethod_differentMock() throws NoSuchMethodException {
        TestClass testClass = new TestClass();
        TestClass otherTestClass = new TestClass();
        Method method = TestClass.class.getMethod("testMethod");
        Invocation candidate = new Invocation(otherTestClass, method, new Object[0], null, new Location());
        
        assertFalse(invocationMatcher.hasSimilarMethods(candidate));
    }
    
    @Test
    public void testHasSimilarMethod_differentMethod() throws NoSuchMethodException {
        TestClass testClass = new TestClass();
        Method method = TestClass.class.getMethod("anotherTestMethod");
        Invocation candidate = new Invocation(testClass, method, new Object[0], null, new Location());
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethod_overloadedButSameArgs() throws NoSuchMethodException {
        TestClass testClass = new TestClass();
        Method method = TestClass.class.getMethod("overloadedMethod", int.class);
        Invocation candidate = new Invocation(testClass, method, new Object[] {1}, null, new Location());
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod_sameMethod() throws NoSuchMethodException {
        TestClass testClass = new TestClass();
        Method method = TestClass.class.getMethod("testMethod");
        Invocation candidate = new Invocation(testClass, method, new Object[0], null, new Location());
        
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod_differentMethod() throws NoSuchMethodException {
        TestClass testClass = new TestClass();
        Method method = TestClass.class.getMethod("anotherTestMethod");
        Invocation candidate = new Invocation(testClass, method, new Object[0], null, new Location());
        
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testToString_PrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String expected = invocation.toString(matchers, printSettings);
        assertEquals(expected, invocationMatcher.toString(printSettings));
    }
    
    @Test
    public void testCaptureArgumentsFrom() throws NoSuchMethodException {
        TestClass testClass = new TestClass();
        Method method = TestClass.class.getMethod("testMethodWithArgs", int.class, String.class);
        Invocation i = new Invocation(testClass, method, new Object[] {1, "test"}, null, new Location());
        
        // Assume we have a matcher that implements CapturesArguments interface
        Matcher mockMatcher = mock(Matcher.class);
        CapturesArguments capturesArgumentsMatcher = (CapturesArguments) mockMatcher;
        
        matchers.add(capturesArgumentsMatcher);
        
        invocationMatcher.captureArgumentsFrom(i);
        
        // Verify the captured arguments
        verify(capturesArgumentsMatcher).captureFrom(eq(1));
        verify(capturesArgumentsMatcher).captureFrom(eq("test"));
    }

}

class TestClass {
    public void testMethod() {
        // Test method implementation
    }
    
    public void anotherTestMethod() {
        // Test method implementation
    }
    
    public void overloadedMethod(int arg) {
        // Test method implementation
    }
    
    public void testMethodWithArgs(int arg1, String arg2) {
        // Test method implementation
    }
}