import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.Arguments.notNull;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testMatches() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation actualInvocation = Mockito.mock(Invocation.class);
        Matcher expectedMatcher = Mockito.mock(Matcher.class);
        matchers.add(expectedMatcher);

        assertTrue(invocationMatcher.matches(actualInvocation));
    }
}