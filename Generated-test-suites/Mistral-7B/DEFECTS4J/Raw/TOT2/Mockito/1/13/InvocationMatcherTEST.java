import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructionWithInvocationAndEmptyMatcherList() {
        Invocation invocation = new MockitoInvocation("testMethod", "MockClass.class", new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        // ... additional assertions here
    }
}