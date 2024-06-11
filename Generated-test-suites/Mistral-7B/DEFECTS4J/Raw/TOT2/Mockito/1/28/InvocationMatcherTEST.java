import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.junit.Assert.*;

public class InvocationMatcherTest {

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.singletonList(matcher));

        Invocation actualInvocation = mock(Invocation.class);
        assertTrue(invocationMatcher.matches(actualInvocation));
    }
}