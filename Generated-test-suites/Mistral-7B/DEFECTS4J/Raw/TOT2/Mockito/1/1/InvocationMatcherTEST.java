import org.junit.jupiter.api.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.DescribedInvocation;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructionWithInvocationAndMatchers() {
        Invocation invocation = mock(DescribedInvocation.class);
        List<Matcher> matchers = Collections.singletonList(is(Object.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), is(matchers));
    }
}