import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithInvocationAndEmptyMatcherList() {
        Invocation invocation = new MockitoInvocation("testMethod", new Object[]{"arg1", "arg2"});
        List<Matcher> matchers = Collections.emptyList();

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        // Add your assertions here
    }
}