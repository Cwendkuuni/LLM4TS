import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithInvocationAndEmptyMatchers() throws Exception {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = mock(Method.class);

        verify(invocationMatcher).getMethod();
        verify(invocationMatcher).getInvocation();
        verify(invocationMatcher).getMatchers();

        assert method.equals(invocationMatcher.getMethod());
        assert invocation.equals(invocationMatcher.getInvocation());
        assert invocationMatcher.getMatchers().isEmpty();
    }
}