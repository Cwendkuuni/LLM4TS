import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.Contains;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new Contains("test"));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        assertThat(invocationMatcher.getMatchers().size(), is(1));
        assertThat(invocationMatcher.getMatchers().get(0), is(new Contains("test")));
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        assertThat(invocationMatcher.getMatchers().size(), is(0));
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        
        assertThat(invocationMatcher.matches(invocation2), is(false));
    }

    // Add more test cases for the remaining methods in the InvocationMatcher class

}