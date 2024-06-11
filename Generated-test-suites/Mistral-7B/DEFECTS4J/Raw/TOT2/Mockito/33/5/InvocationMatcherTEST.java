import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {
    
    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        when(invocation1.getMethod()).thenReturn(new Method("testMethod", new Class[]{String.class, int.class}));
        when(invocation2.getMethod()).thenReturn(new Method("testMethod", new Class[]{String.class, int.class}));

        assertTrue(invocationMatcher1.matches(invocation2));
    }
}