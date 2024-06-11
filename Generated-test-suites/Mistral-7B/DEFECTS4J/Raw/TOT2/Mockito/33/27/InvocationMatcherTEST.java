import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class InvocationMatcherTest {
    InvocationMatcher invocationMatcher;
    Invocation invocation;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testMatchesInvocationWithSameMethodAndMock() {
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        assertTrue(invocationMatcher.matches(actualInvocation));
    }

    @Test(expected = AssertionError.class)
    public void testMatchesInvocationWithDifferentMethod() {
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(actualInvocation.getMethod()).thenReturn(new Method("differentMethod", new Class[0]));
        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatchesInvocationWithSameMethodAndDifferentArguments() {
        Invocation actualInvocation = mock(Invocation.class);
        Object[] actualArgs = new Object[1];
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(actualInvocation.getArguments()).thenReturn(actualArgs);
        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatchesInvocationWithSameMethodAndSameArguments() {
        Invocation actualInvocation = mock(Invocation.class);
        Object[] actualArgs = new Object[1];
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(actualInvocation.getArguments()).thenReturn(actualArgs);
        when(actualArgs[0]).thenReturn("expectedArg");
        assertTrue(invocationMatcher.matches(actualInvocation));
    }

    // Add more test cases for other methods and edge cases
}