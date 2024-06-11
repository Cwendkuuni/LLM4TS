import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertSame;

public class InvocationMatcherTest {
    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method expectedMethod = mock(Method.class);
        assertSame(expectedMethod, invocationMatcher.getMethod());
    }
}