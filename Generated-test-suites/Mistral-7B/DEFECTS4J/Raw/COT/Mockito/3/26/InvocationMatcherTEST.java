import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        assertThat(method, is(invocation.getMethod()));
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertThat(matchers, is(Collections.emptyList()));
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocationMatcher.getMethod();
        assertThat(method, is(invocation.getMethod()));
        List<Matcher> matchersList = invocationMatcher.getMatchers();
        assertThat(matchersList, is(matchers));
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation retrievedInvocation = invocationMatcher.getInvocation();
        assertThat(retrievedInvocation, is(invocation));
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String toString = invocationMatcher.toString();
        InvocationMatcher spyInvocationMatcher = spy(invocationMatcher);
        String print = spyInvocationMatcher.toString();

        assertThat(toString, is(print));
    }

    @Test
    public void testMatches_sameInvocation() {
        Invocation invocation1 = mock(Invocation.class);
        List<Matcher> matchers1 = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers1);

        Invocation invocation2 = mock(Invocation.class);
        List<Matcher> matchers2 = matchers1;
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers2);

        assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatches_differentInvocations_differentMocks() {
        Invocation invocation1 = mock(Invocation.class);
        List<Matcher> matchers1 = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers1);

        Invocation invocation2 = mock(Invocation.class, "AnotherMock");
        List<Matcher> matchers2 = matchers1;
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers2);

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatches_differentInvocations_sameMocks_differentMethods() {
        Invocation invocation1 = mock(Invocation.class);
        Method method1 = mock(Method.class, "Method1");
        when(invocation1.getMethod()).thenReturn(method1);
        List<Matcher> matchers1 = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers1);

        Invocation invocation2 = mock(Invocation.class);
        Method method2 = mock(Method.class, "Method2");
        when(invocation2.getMethod()).thenReturn(method2);
        List<Matcher> matchers2 = matchers1;
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers2);

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatches_differentInvocations_sameMocks_sameMethods_differentArguments() {
        Invocation invocation1 = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method);
        Object[] arguments1 = new Object[]{"Argument1", 1L};
        when(invocation1.getArguments()).thenReturn(arguments1);
        List<Matcher> matchers1 = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers1);

        Invocation invocation2 = mock(Invocation.class);
        Object[] arguments2 = new Object[]{"Argument2", 2L};
        when(invocation2.getArguments()).thenReturn(arguments2);
        List<Matcher> matchers2 = matchers1;
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers2);

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatches_sameInvocations_sameMocks_sameMethods_differentMatchers() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        List<Matcher> matchers1 = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, matchers1);

        List<Matcher> matchers2 = Arrays.asList(mock(Matcher.class), mock(Matcher.class), mock(Matcher.class));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, matchers2);

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatches_sameInvocations_sameMocks_sameMethods_sameMatchers() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        invocationMatcher.getLocation();
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class, "Invocation");
        Object[] arguments = new Object[]{1, "test", 3.5f};
        when(invocation.getArguments()).thenReturn(arguments);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(CapturesArguments.class), mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(invocation);

        Matcher capturesArguments = matchers.get(1);
        Object argumentCaptured = capturesArguments.getValue();
        assertEquals(arguments[1], argumentCaptured);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(mock(Invocation.class), mock(Invocation.class), mock(Invocation.class));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertTrue(invocationMatchers.size() == invocations.size());

        for (int i = 0; i < invocationMatchers.size(); i++) {
            InvocationMatcher invocationMatcher = invocationMatchers.get(i);
            Invocation invocation = invocations.get(i);
            Method method = invocation.getMethod();
            List<Matcher> matchers = invocationMatcher.getMatchers();

            assertEquals(method, invocationMatcher.getMethod());
            assertEquals(method, invocation.getMethod());
            assertEquals(matchers.size(), invocation.getArgumentTypes().length);
        }
    }
}