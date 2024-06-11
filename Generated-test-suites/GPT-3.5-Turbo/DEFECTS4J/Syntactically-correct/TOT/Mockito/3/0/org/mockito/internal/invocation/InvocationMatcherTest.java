import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {
    
    // Test for the constructor with matchers
    @Test
    public void testInvocationMatcherWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    // Test for the constructor without matchers
    @Test
    public void testInvocationMatcherWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }
    
    // Test for getMethod method
    @Test
    public void testGetMethod() {
        Method expectedMethod = mock(Method.class);
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(expectedMethod);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(expectedMethod, invocationMatcher.getMethod());
    }
    
    // Test for toString method
    @Test
    public void testToString() {
        List<Matcher> matchers = new ArrayList<>();
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        matchers.add(matcher1);
        matchers.add(matcher2);

        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getMatchers()).thenReturn(matchers);
        when(invocation.getLocation()).thenReturn(location);
        
        String description = "InvocationMatcher@" + Integer.toHexString(invocationMatcher.hashCode()) + ", hashCode: " + Integer.toHexString(matchers.hashCode());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(description, invocationMatcher.toString());
    }
    
    // Test for matches method with identical invocation
    @Test
    public void testMatchesIdenticalInvocation() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = invocation;
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.matches(actual));
    }
    
    // Test for matches method with non-identical invocation
    @Test
    public void testMatchesNonIdenticalInvocation() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertFalse(invocationMatcher.matches(actual));
    }
    
    // Test for hasSimilarMethod method with similar invocation
    @Test
    public void testHasSimilarMethodWithSimilarInvocation() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[]{});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    // Test for hasSimilarMethod method with overloaded but same args invocation
    @Test
    public void testHasSimilarMethodWithOverloadedButSameArgsInvocation() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(invocation.argumentsMatch(candidate)).thenReturn(true);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }
    
    // Test for hasSameMethod method with same method
    @Test
    public void testHasSameMethodWithSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("methodName");
        when(method2.getName()).thenReturn("methodName");
        when(method1.getParameterTypes()).thenReturn(new Class[]{});
        when(method2.getParameterTypes()).thenReturn(new Class[]{});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }
    
    // Test for hasSameMethod method with different method
    @Test
    public void testHasSameMethodWithDifferentMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("methodName1");
        when(method2.getName()).thenReturn("methodName2");
        when(method1.getParameterTypes()).thenReturn(new Class[]{});
        when(method2.getParameterTypes()).thenReturn(new Class[]{});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }
    
    // Test for hasSameMethod method with different parameter types
    @Test
    public void testHasSameMethodWithDifferentParameterTypes() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(candidate.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("methodName");
        when(method2.getName()).thenReturn("methodName");
        when(method1.getParameterTypes()).thenReturn(new Class[]{String.class});
        when(method2.getParameterTypes()).thenReturn(new Class[]{Integer.class});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }
    
    // Test for getLocation method
    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(location, invocationMatcher.getLocation());
    }
    
    // Test for captureArgumentsFrom method with varargs invocation
    @Test
    public void testCaptureArgumentsFromVarargsInvocation() {
        Invocation invocation = mock(Invocation.class);
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        Matcher matcher3 = mock(Matcher.class);
        Matcher matcher4 = mock(Matcher.class);
        when(matcher1 instanceof CapturesArguments).thenReturn(true);
        when(matcher2 instanceof CapturesArguments).thenReturn(true);
        when(matcher3 instanceof CapturesArguments).thenReturn(true);
        when(matcher4 instanceof CapturesArguments).thenReturn(true);
        List matchers = new ArrayList<>(Arrays.asList(matcher1, matcher2, matcher3, matcher4));
        when(invocation.getMethod().isVarArgs()).thenReturn(true);
        when(invocation.getRawArguments()).thenReturn(new Object[]{1, 2, 3, new Object[]{4, 5, 6}});
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(invocation);
        
        for (Matcher matcher : matchers) {
            if (matcher instanceof CapturesArguments) {
                CapturesArguments capturesArguments = (CapturesArguments) matcher;
                verify(capturesArguments).captureFrom(any());
            } else {
                fail("Matcher should be an instance of CapturesArguments");
            }
        }
    }
    
    // Test for captureArgumentsFrom method without varargs invocation
    @Test
    public void testCaptureArgumentsFromNonVarargsInvocation() {
        Invocation invocation = mock(Invocation.class);
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        Matcher matcher3 = mock(Matcher.class);
        Matcher matcher4 = mock(Matcher.class);
        when(matcher1 instanceof CapturesArguments).thenReturn(true);
        when(matcher2 instanceof CapturesArguments).thenReturn(true);
        when(matcher3 instanceof CapturesArguments).thenReturn(true);
        when(matcher4 instanceof CapturesArguments).thenReturn(true);
        List matchers = new ArrayList<>(Arrays.asList(matcher1, matcher2, matcher3, matcher4));
        when(invocation.getMethod().isVarArgs()).thenReturn(false);
        when(invocation.getArgumentAt(anyInt(), any())).thenReturn(1, 2, 3, 4);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(invocation);
        
        for (Matcher matcher : matchers) {
            if (matcher instanceof CapturesArguments) {
                CapturesArguments capturesArguments = (CapturesArguments) matcher;
                verify(capturesArguments).captureFrom(any());
            } else {
                fail("Matcher should be an instance of CapturesArguments");
            }
        }
    }
    
    // Test for createFrom method
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = new ArrayList<>(Arrays.asList(invocation1, invocation2));
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        Iterator<Invocation> iterator = invocations.iterator();
        for (InvocationMatcher invocationMatcher : invocationMatchers) {
            assertEquals(iterator.next(), invocationMatcher.getInvocation());
        }
    }
}