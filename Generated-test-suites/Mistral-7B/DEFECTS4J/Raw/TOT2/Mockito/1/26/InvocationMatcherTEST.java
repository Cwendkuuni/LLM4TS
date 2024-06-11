import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationWithMocks;
import org.mockito.invocation.Invocation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructionWithInvocationAndEmptyMatcherList() {
        Invocation invocation = new InvocationWithMocks(new Object(), new Method(InvocationMatcher.class, "getMethod", new Class[0]));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Test method getter
        Method method = invocationMatcher.getMethod();
        assertThat(method, is(invocation.getMethod()));

        // Test empty matchers list
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertThat(matchers, is(Collections.emptyList()));

        // Test toString
        String toString = invocationMatcher.toString();
        assertThat(toString, is("InvocationMatcher [method=<...>]"));

        // Test matches method
        InvocationMatcher otherInvocationMatcher = new InvocationMatcher(new InvocationWithMocks(new Object(), new Method(InvocationMatcher.class, "getMethod", new Class[0])));
        boolean matches = invocationMatcher.matches(otherInvocationMatcher);
        assertThat(matches, is(true));
    }
}