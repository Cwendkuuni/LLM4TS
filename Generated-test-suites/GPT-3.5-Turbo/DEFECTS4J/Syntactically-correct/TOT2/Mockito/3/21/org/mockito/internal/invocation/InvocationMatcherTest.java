import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.*;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testInvocationMatcherConstructorWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    // Add more individual test cases for other public methods in InvocationMatcher class

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        when(invocation1.getMethod()).thenReturn(InvocationMatcher.class.getDeclaredMethods()[0]);
        when(invocation1.isVerified()).thenReturn(false);
        when(invocation1.getMock()).thenReturn(this);
        
        when(invocation2.getMethod()).thenReturn(InvocationMatcher.class.getDeclaredMethods()[0]);
        when(invocation2.isVerified()).thenReturn(false);
        when(invocation2.getMock()).thenReturn(this);
        
        assertTrue(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        when(invocation1.getMethod()).thenReturn(InvocationMatcher.class.getDeclaredMethods()[0]);
        when(invocation2.getMethod()).thenReturn(InvocationMatcher.class.getDeclaredMethods()[0]);
        
        assertTrue(invocationMatcher.hasSameMethod(invocation2));
    }

    // Add test cases for potential edge cases and exceptions

}