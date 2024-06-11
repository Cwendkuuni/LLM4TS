import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(eq(1));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = invocation.getMethod();
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        when(invocation1.getMethod()).thenReturn(String.class.getDeclaredMethod("length", null));
        when(invocation2.getMethod()).thenReturn(String.class.getDeclaredMethod("length", null));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.matches(invocation2));
    }

    // Add more test cases for the remaining public methods

}