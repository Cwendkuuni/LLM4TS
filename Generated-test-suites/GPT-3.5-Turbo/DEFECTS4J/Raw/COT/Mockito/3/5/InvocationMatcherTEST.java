import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.*;

public class InvocationMatcherTest {
    
    @Test
    public void testConstructor1() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.<Matcher>emptyList(), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructor2() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.singletonList(Mockito.mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Method method = invocation.getMethod();
        
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.singletonList(Mockito.mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        String expected = new PrintSettings().print(matchers, invocation);
        assertEquals(expected, invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Arrays.asList(Mockito.mock(Matcher.class), Mockito.mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertTrue(invocationMatcher.matches(invocation));
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        List<Matcher> matchers = Collections.singletonList(Mockito.mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        List<Matcher> matchers = Collections.singletonList(Mockito.mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Location location = invocation.getLocation();
        
        assertEquals(location, invocationMatcher.getLocation());
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation invocation2 = new Invocation();
        invocationMatcher.captureArgumentsFrom(invocation2);
        
        // assert something
    }
    
    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(new Invocation(), new Invocation());
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(invocations.size(), invocationMatchers.size());
        for (int i = 0; i < invocationMatchers.size(); i++) {
            assertEquals(invocations.get(i), invocationMatchers.get(i).getInvocation());
        }
    }
}