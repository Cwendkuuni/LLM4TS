import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.InvocationMatcher;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private Object mock;
    private InvocationMatcher matcher;

    @Before
    public void setUp() {
        mock = mock(Object.class);
    }

    @Test
    public void testInvocationMatcherWithEmptyMatchers() {
        Invocation invocation = new Invocation(mock, new Object[]{}, new Method(Object.class, "method", new Class[0]), new Location("TestClass.java", 10));
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation);

        assertThat(matcher, is(expectedMatcher));
    }

    @Test
    public void testInvocationMatcherWithMatchers() {
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        Invocation invocation = new Invocation(mock, new Object[]{"test"}, new Method(Object.class, "method", new Class[]{String.class}), new Location("TestClass.java", 10));

        List<Matcher> matchers = Arrays.asList(is("test"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, matchers);

        assertThat(matcher, is(expectedMatcher));
    }

    @Test
    public void testInvocationMatcherMatches() {
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        Invocation invocation = new Invocation(mock, new Object[]{"test"}, new Method(Object.class, "method", new Class[]{String.class}), new Location("TestClass.java", 10));
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.singletonList(is("test")));

        Invocation actualInvocation = new InvocationOnMock(mock);
        actualInvocation.setMethod(new Method(Object.class, "method", new Class[]{String.class}));
        actualInvocation.setArguments(new Object[]{"test"});

        InvocationMatcher actualMatcher = new InvocationMatcher(actualInvocation);

        assertThat(matcher, matches(actualMatcher));
    }

    @Test
    public void testInvocationMatcherDoesNotMatch() {
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        Invocation invocation = new Invocation(mock, new Object[]{"test"}, new Method(Object.class, "method", new Class[]{Integer.class}), new Location("TestClass.java", 10));
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.singletonList(is("test")));

        Invocation actualInvocation = new InvocationOnMock(mock);
        actualInvocation.setMethod(new Method(Object.class, "method", new Class[]{Integer.class}));
        actualInvocation.setArguments(new Object[]{1});

        InvocationMatcher actualMatcher = new InvocationMatcher(actualInvocation);

        assertThat(matcher, not(matches(actualMatcher)));
    }

    @Test
    public void testInvocationMatcherWithOverloadedMethods() {
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        Invocation invocationWithStringArgument = new Invocation(mock, new Object[]{"test"}, new Method(Object.class, "method", new Class[]{String.class}), new Location("TestClass.java", 10));
        InvocationMatcher matcherWithStringArgument = new InvocationMatcher(invocationWithStringArgument, Collections.singletonList(is("test")));

        Invocation invocationWithIntegerArgument = new Invocation(mock, new Object[]{1}, new Method(Object.class, "method", new Class[]{Integer.class}), new Location("TestClass.java", 20));
        InvocationMatcher matcherWithIntegerArgument = new InvocationMatcher(invocationWithIntegerArgument, Collections.singletonList(is(1)));

        InvocationMatcher matcher = new InvocationMatcher(invocationWithStringArgument);

        assertThat(matcher, not(matches(matcherWithIntegerArgument)));
    }

    @Test
    public void testInvocationMatcherWithMatchingArguments() {
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        Invocation invocationWithStringArguments = new Invocation(mock, new Object[]{"test1", "test2"}, new Method(Object.class, "method", new Class[]{String.class, String.class}), new Location("TestClass.java", 10));
        InvocationMatcher matcherWithStringArguments = new InvocationMatcher(invocationWithStringArguments, Arrays.asList(is("test1"), is("test2")));

        Invocation actualInvocationWithStringArguments = new InvocationOnMock(mock);
        actualInvocationWithStringArguments.setMethod(new Method(Object.class, "method", new Class[]{String.class, String.class}));
        actualInvocationWithStringArguments.setArguments(new Object[]{"test1", "test2"});

        InvocationMatcher actualMatcherWithStringArguments = new InvocationMatcher(actualInvocationWithStringArguments);

        assertThat(matcherWithStringArguments, matches(actualMatcherWithStringArguments));
    }
}