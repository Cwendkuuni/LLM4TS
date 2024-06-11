import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.mock.MockCreationSettings;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Test the getMethod() method
        invocationMatcher.getMethod();
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Test the getInvocation() method
        invocationMatcher.getInvocation();
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Test the getMatchers() method
        invocationMatcher.getMatchers();
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Test the toString() method
        invocationMatcher.toString();
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actualInvocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Test the matches() method
        invocationMatcher.matches(actualInvocation);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Test the hasSimilarMethod() method
        invocationMatcher.hasSimilarMethod(candidate);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Test the hasSameMethod() method
        invocationMatcher.hasSameMethod(candidate);
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Test the getLocation() method
        invocationMatcher.getLocation();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFrom_varArgs() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Test the captureArgumentsFrom() method with varargs
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Test the captureArgumentsFrom() method
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation = mock(Invocation.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        when(invocation.getMock()).thenReturn(settings);
        when(settings.isSerializable()).thenReturn(false);
        
        // Test the createFrom() method
        InvocationMatcher.createFrom(Collections.singletonList(invocation));
    }
}