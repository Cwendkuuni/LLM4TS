import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.internal.invocation.InvocationMatcher;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        Invocation invocation = createMockInvocation();
        List<Matcher> matchers = createMatchers();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocation() {
        Invocation invocation = createMockInvocation();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    private Invocation createMockInvocation() {
        return new Invocation() {
            @Override
            public Method getMethod() {
                return null;
            }

            @Override
            public Object[] getArguments() {
                return null;
            }

            @Override
            public Object getMock() {
                return null;
            }

            @Override
            public int getSequenceNumber() {
                return 0;
            }

            @Override
            public Location getLocation() {
                return null;
            }
        };
    }

    private List<Matcher> createMatchers() {
        return Collections.emptyList();
    }

}