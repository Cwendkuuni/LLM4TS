import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.Arguments;
import org.mockito.internal.matchers.Captures;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithEmptyMatchers() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mock, new Method(Object.class, int.class), new Object[]{1});
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Invocation invoked = Mockito.mock(Invocation.class);
        when(invoked.getMethod()).thenReturn(invocation.getMethod());
        when(invoked.getMock()).thenReturn(mock);

        assertTrue(matcher.matches(invoked));
    }

    @Test
    public void testInvocationMatcherWithMatchers() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mock, new Method(Object.class, int.class), new Object[]{1});
        List<Matcher> matchers = Collections.singletonList(ArgThat.is(1));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Invocation invoked = new Invocation(mock, new Method(Object.class, int.class), new Object[]{1});

        assertTrue(matcher.matches(invoked));
    }

    @Test
    public void testInvocationMatcherWithDifferentMocks() {
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        Invocation invocation1 = new Invocation(mock1, new Method(Object.class, int.class), new Object[]{1});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new Invocation(mock2, new Method(Object.class, int.class), new Object[]{1});
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertFalse(matcher1.matches(matcher2));
    }

    @Test
    public void testInvocationMatcherWithDifferentMethods() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation1 = new Invocation(mock, new Method(Object.class, int.class), new Object[]{1});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new Invocation(mock, new Method(Object.class, String.class), new Object[]{"test"});
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertFalse(matcher1.matches(matcher2));
    }

    @Test
    public void testInvocationMatcherWithVerifiedInvocation() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mock, new Method(Object.class, int.class), new Object[]{1});
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Invocation invoked = Mockito.mock(Invocation.class);
        when(invoked.getMethod()).thenReturn(invocation.getMethod());
        when(invoked.getMock()).thenReturn(mock);
        verify(mock).methodName(anyInt());

        assertFalse(matcher.matches(invoked));
    }

    @Test
    public void testInvocationMatcherWithSameMethodButDifferentArguments() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mock, new Method(Object.class, int.class), new Object[]{1});
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Invocation invoked = new Invocation(mock, new Method(Object.class, double.class), new Object[]{2.0});

        assertFalse(matcher.matches(invoked));
    }

    @Test
    public void testInvocationMatcherWithSameMethodButSameArgumentsFromDifferentInvocations() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation1 = new Invocation(mock, new Method(Object.class, int.class), new Object[]{1});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new Invocation(mock, new Method(Object.class, int.class), new Object[]{1});
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertTrue(matcher1.matches(matcher2));
    }

    @Test
    public void testInvocationMatcherWithCapturesArguments() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mock, new Method(Object.class, int.class), new Object[]{1});
        List<Matcher> matchers = Arrays.asList(ArgThat.is(1), Captures.capture("arg1"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Invocation invoked = new Invocation(mock, new Method(Object.class, int.class), new Object[]{2});

        Arguments arguments = new Arguments(invoked.getArguments());
        matcher.captureArgumentsFrom(invoked);

        assertEquals(1, arguments.get(0).getValue());
        assertEquals(2, arguments.get(1).getValue());
        assertEquals(2, matcher.getMatchers().get(1).match(arguments.get(1)));
    }
}