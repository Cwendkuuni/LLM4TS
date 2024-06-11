import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Location;
import org.mockito.mock.MockCreationSettings;

import java.lang.reflect.Method;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        Invocation invocation = createInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = createInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = createInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Method expectedMethod = invocation.getMethod();
        Method actualMethod = invocationMatcher.getMethod();
        
        assertEquals(expectedMethod, actualMethod);
    }
    
    @Test
    public void testToString() {
        Invocation invocation = createInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        String description = invocationMatcher.toString();
        
        assertEquals("InvocationMatcher for: ExampleClass.exampleMethod(123, abc)", description);
    }
    
    @Test
    public void testMatches_withMatchingInvocation() {
        Invocation invocation = createInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation actualInvocation = createInvocation();
        
        assertTrue(invocationMatcher.matches(actualInvocation));
    }
    
    @Test
    public void testMatches_withDifferentMock() {
        Invocation invocation = createInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation actualInvocation = createInvocation();
        actualInvocation.setMock(mock(ExampleClass.class));
        
        assertFalse(invocationMatcher.matches(actualInvocation));
    }
    
    @Test
    public void testMatches_withDifferentMethod() {
        Invocation invocation = createInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation actualInvocation = createInvocation();
        actualInvocation.setMethod(mock(Method.class));
        
        assertFalse(invocationMatcher.matches(actualInvocation));
    }
    
    @Test
    public void testMatches_withDifferentArguments() {
        Invocation invocation = createInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation actualInvocation = createInvocation();
        actualInvocation.setArguments(new Object[]{456, "def"});
        
        assertFalse(invocationMatcher.matches(actualInvocation));
    }
    
    @Test
    public void testHasSimilarMethod_withSameMethod() {
        Invocation invocation = createInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation candidate = createInvocation();
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethod_withDifferentMethod() {
        Invocation invocation = createInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation candidate = createInvocation();
        candidate.setMethod(mock(Method.class));
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethod_withDifferentMock() {
        Invocation invocation = createInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation candidate = createInvocation();
        candidate.setMock(mock(ExampleClass.class));
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethod_withOverloadedButSameArgs() {
        Invocation invocation = createInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation candidate = createInvocation();
        candidate.setMethod(mock(Method.class));
        candidate.setArguments(new Object[]{123, "abc"});
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSimilarMethod_withSameMethodButDifferentArgs() {
        Invocation invocation = createInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation candidate = createInvocation();
        candidate.setArguments(new Object[]{456, "def"});
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod_withSameMethodAndSameParams() {
        Invocation invocation = createInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation candidate = createInvocation();
        
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod_withSameMethodButDifferentParams() {
        Invocation invocation = createInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation candidate = createInvocation();
        candidate.setArguments(new Object[]{456, "def"});
        
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = createInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Location expectedLocation = invocation.getLocation();
        Location actualLocation = invocationMatcher.getLocation();
        
        assertEquals(expectedLocation, actualLocation);
    }
    
    @Test
    public void testCaptureArgumentsFrom_withNonVarArgsMethod() {
        Invocation invocation = createInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation invocationToCapture = createInvocation();
        invocationToCapture.setArguments(new Object[]{123, "abc"});
        
        invocationMatcher.captureArgumentsFrom(invocationToCapture);
        
        // Verify that the arguments are captured correctly
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertEquals(2, matchers.size());
        
        assertEquals(123, ((MatcherDecorator) matchers.get(0)).getMatcher().getArgument());
        assertEquals("abc", ((MatcherDecorator) matchers.get(1)).getMatcher().getArgument());
    }
    
    @Test
    public void testCaptureArgumentsFrom_withVarArgsMethod() {
        Invocation invocation = createInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation invocationToCapture = createInvocation();
        invocationToCapture.setArguments(new Object[]{123, "abc", "def"});
        
        invocationMatcher.captureArgumentsFrom(invocationToCapture);
        
        // Verify that the arguments are captured correctly
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertEquals(3, matchers.size());
        
        assertEquals(123, ((MatcherDecorator) matchers.get(0)).getMatcher().getArgument());
        assertEquals("abc", ((MatcherDecorator) matchers.get(1)).getMatcher().getArgument());
        assertEquals("def", ((MatcherDecorator) matchers.get(2)).getMatcher().getArgument());
    }
    
    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(createInvocation());
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(invocations.size(), invocationMatchers.size());
        assertEquals(createInvocation(), invocationMatchers.get(0).getInvocation());
    }
    
    private Invocation createInvocation() {
        Invocation invocation = new Invocation(mock(ExampleClass.class), mock(Method.class), new Location());
        invocation.setArguments(new Object[]{123, "abc"});
        
        return invocation;
    }
}