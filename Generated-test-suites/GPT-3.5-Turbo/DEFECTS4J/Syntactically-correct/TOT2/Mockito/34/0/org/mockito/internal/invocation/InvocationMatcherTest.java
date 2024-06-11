import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

public class InvocationMatcherTest {

    private Invocation invocation;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(invocation.getArguments()).thenReturn(new Object[] { "arg1", "arg2" });

        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testGetMethod() {
        Method method = invocationMatcher.getMethod();
        
        assertEquals(Object.class.getDeclaredMethods()[0], method);
    }

    @Test
    public void testGetInvocation() {
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testToString() {
        String expected = "toString";
        assertEquals(expected, invocationMatcher.toString());
    }

    // Add edge cases and exception handling scenarios for the other methods

}