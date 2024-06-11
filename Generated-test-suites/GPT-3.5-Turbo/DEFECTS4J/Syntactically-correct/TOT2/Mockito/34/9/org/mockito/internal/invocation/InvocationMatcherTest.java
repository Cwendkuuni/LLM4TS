import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() throws NoSuchMethodException {
        Method testMethod = String.class.getMethod("length");
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        when(invocation.getMethod()).thenReturn(testMethod);
        
        assertEquals(testMethod, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        List<Matcher> matchers = new ArrayList<>();
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // add verification of the toString method
    }

    @Test
    public void testMatches() {
        // add test for the matches method
    }

    @Test
    public void testHasSimilarMethod() {
        // add test for the hasSimilarMethod method
    }

    @Test
    public void testHasSameMethod() {
        // add test for the hasSameMethod method
    }

    @Test
    public void testGetLocation() {
        // add test for the getLocation method
    }

    @Test
    public void testToStringWithPrintSettings() {
        // add test for the toString method with PrintSettings
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // add test for the captureArgumentsFrom method
    }
}