import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.internal.matchers.CapturesArguments;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithMatchers() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        
        // Test
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        // Verify
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcherConstructorWithoutMatchers() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        
        // Test
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Verify
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        // Setup
        Method expectedMethod = InvocationMatcher.class.getDeclaredMethods()[0];
        Invocation invocation = mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(expectedMethod);
        
        // Test
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Verify
        assertEquals(expectedMethod, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(InvocationMatcher.class.getDeclaredMethods()[0]);
        CapturesArguments matcher = mock(CapturesArguments.class);
        Mockito.when(matcher.toString()).thenReturn("matcher string");
        List<Matcher> matchers = Arrays.asList(matcher);
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{1});
        
        // Test
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String result = invocationMatcher.toString();
        
        // Verify
        assertEquals("matcher string", result);
    }

    @Test
    public void testMatches_WithMatchingInvocation() {
        // Setup
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Mockito.when(invocation1.getMock()).thenReturn("mock");
        Mockito.when(invocation2.getMock()).thenReturn("mock");
        Method method = InvocationMatcher.class.getDeclaredMethods()[0];
        Mockito.when(invocation1.getMethod()).thenReturn(method);
        Mockito.when(invocation2.getMethod()).thenReturn(method);
        Mockito.when(invocation1.getArguments()).thenReturn(new Object[]{1});
        Mockito.when(invocation2.getArguments()).thenReturn(new Object[]{1});
        
        // Test
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.matches(invocation2);
        
        // Verify
        assertTrue(result);
    }

    @Test
    public void testMatches_WithDifferentMock() {
        // Setup
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Mockito.when(invocation1.getMock()).thenReturn("mock1");
        Mockito.when(invocation2.getMock()).thenReturn("mock2");
        
        // Test
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.matches(invocation2);
        
        // Verify
        assertFalse(result);
    }

    @Test
    public void testMatches_WithDifferentMethod() {
        // Setup
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Mockito.when(invocation1.getMock()).thenReturn("mock");
        Mockito.when(invocation2.getMock()).thenReturn("mock");
        Mockito.when(invocation1.getArguments()).thenReturn(new Object[]{1});
        Mockito.when(invocation2.getArguments()).thenReturn(new Object[]{2});
        
        // Test
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.matches(invocation2);
        
        // Verify
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_WithSameMethod() {
        // Setup
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Mockito.when(invocation1.getMethod()).thenReturn(InvocationMatcher.class.getDeclaredMethods()[0]);
        Mockito.when(invocation2.getMethod()).thenReturn(InvocationMatcher.class.getDeclaredMethods()[0]);
        Mockito.when(invocation1.isVerified()).thenReturn(false);
        Mockito.when(invocation2.isVerified()).thenReturn(false);
        Mockito.when(invocation1.getMock()).thenReturn("mock");
        Mockito.when(invocation2.getMock()).thenReturn("mock");
        
        // Test
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.hasSimilarMethod(invocation2);
        
        // Verify
        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod_WithOverloadedMethodAndSameArguments() {
        // Setup
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Mockito.when(invocation1.getMethod()).thenReturn(InvocationMatcher.class.getDeclaredMethods()[0]);
        Mockito.when(invocation2.getMethod()).thenReturn(InvocationMatcher.class.getDeclaredMethods()[0]);
        Mockito.when(invocation1.isVerified()).thenReturn(false);
        Mockito.when(invocation2.isVerified()).thenReturn(false);
        Mockito.when(invocation1.getArguments()).thenReturn(new Object[]{1});
        Mockito.when(invocation2.getArguments()).thenReturn(new Object[]{1});
        
        // Test
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.hasSimilarMethod(invocation2);
        
        // Verify
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_WithOverloadedMethodAndDifferentArguments() {
        // Setup
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Mockito.when(invocation1.getMethod()).thenReturn(InvocationMatcher.class.getDeclaredMethods()[0]);
        Mockito.when(invocation2.getMethod()).thenReturn(InvocationMatcher.class.getDeclaredMethods()[0]);
        Mockito.when(invocation1.isVerified()).thenReturn(false);
        Mockito.when(invocation2.isVerified()).thenReturn(false);
        Mockito.when(invocation1.getArguments()).thenReturn(new Object[]{1});
        Mockito.when(invocation2.getArguments()).thenReturn(new Object[]{2});
        
        // Test
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.hasSimilarMethod(invocation2);
        
        // Verify
        assertFalse(result);
    }

    @Test
    public void testHasSameMethod_WithSameMethod() {
        // Setup
        Method method = InvocationMatcher.class.getDeclaredMethods()[0];
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Mockito.when(invocation1.getMethod()).thenReturn(method);
        Mockito.when(invocation2.getMethod()).thenReturn(method);
        
        // Test
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        boolean result = invocationMatcher.hasSameMethod(invocation2);
        
        // Verify
        assertTrue(result);
    }
    
    @Test
    public void testCaptureArgumentsFrom_WithVarArgsMethod() {
        // Setup
        Invocation invocation = mock(Invocation.class);
        Object[] rawArguments = {1, 2};
        Mockito.when(invocation.getMethod()).thenReturn(InvocationMatcher.class.getDeclaredMethods()[0]);
        Mockito.when(invocation.getRawArguments()).thenReturn(rawArguments);
        Mockito.when(invocation.getArgumentAt(Mockito.anyInt(), Mockito.any(Class.class))).thenReturn("arg");
        CapturesArguments matcher = mock(CapturesArguments.class);
        List<Matcher> matchers = Arrays.asList(matcher);
        
        // Test
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(invocation);
        
        // Verify
        verify(matcher).captureFrom(1);
    }

    @Test
    public void testCreateFrom() {
        // Setup
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        
        // Test
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        // Verify
        assertEquals(invocations.size(), invocationMatchers.size());
        assertTrue(invocationMatchers.get(0) instanceof InvocationMatcher);
        assertTrue(invocationMatchers.get(1) instanceof InvocationMatcher);
    }
}