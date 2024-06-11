import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.ArgumentsMatcher;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherEmptyMatcherConstuctor() {
        Invocation invocation = mock(Invocation.class);

        InvocationMatcher im = new InvocationMatcher(invocation);
        List<Matcher> matchers = im.getMatchers();
        Assert.assertEquals( Collections.emptyList(), matchers );
    }
}