package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testMatcherWithEmptyList() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();

        // when
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        // then
        assertThat(matcher, is(notNullValue()));
        assertThat(matcher.getMatchers(), is(Collections.emptyList()));
        assertThat(matcher.getMethod(), is(notNullValue()));
        assertThat(matcher.getInvocation(), is(invocation));
    }

    @Test
    public void testMatcherWithSpecifiedMatchers() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Mockito.anyInt(), Mockito.eq("test"));

        // when
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        // then
        assertThat(matcher, is(notNullValue()));
        assertThat(matcher.getMatchers(), is(matchers));
        assertThat(matcher.getMethod(), is(notNullValue()));
        assertThat(matcher.getInvocation(), is(invocation));
        verify(invocation).getMock();
        verify(invocation).getMethod();
    }

    @Test
    public void testMatcherWithNoMatchers() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);

        // when
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // then
        assertThat(matcher, is(notNullValue()));
        List<Matcher> matchers = matcher.getMatchers();
        assertThat(matchers, is(Collections.emptyList()));
        assertThat(matcher.getMethod(), is(notNullValue()));
        assertThat(matcher.getInvocation(), is(invocation));
    }

    @Test
    public void testToString() {
        // given
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Mockito.anyString(), Mockito.eq("test"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        // when
        String toString = matcher.toString();

        // then
        assertThat(toString, is("InvocationMatcher{\n" +
                "invocation: InvocationMock{\n" +
                "method: Method[\n" +
                "java.lang.String test(java.lang.String)\n" +
                "],\n" +
                "target: <unmocked>\n" +
                "},\n" +
                "matchers: [ArgThat(<any String>), ArgThat(test)]\n" +
                "}"));
    }

    @Test
    public void testMatches() {
        // given
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        // when
        Method method = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);

        // then
        assertThat(matcher1.matches(matcher1), is(true));
        assertThat(matcher1.matches(matcher2), is(false));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{"test1", "test2"};

        // when
        InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(Mockito.anyString()));

        // then
        boolean safelyArgumentsMatch = matcher.safelyArgumentsMatch(arguments);
        assertThat(safelyArgumentsMatch, is(true));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromWithVarargs() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // when
        matcher.captureArgumentsFrom(invocation);

        // then
        // UnsupportedOperationException is expected to be thrown
    }

    @Test
    public void testCreateFrom() {
        // given
        List<Invocation> invocations = Arrays.asList(Mockito.mock(Invocation.class), Mockito.mock(Invocation.class));

        // when
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        // then
        assertThat(matchers, is(notNullValue()));
        assertThat(matchers.size(), is(2));
        for (InvocationMatcher matcher : matchers) {
            assertThat(matcher.getMatchers(), is(Collections.emptyList()));
            assertThat(matcher.getMethod(), is(notNullValue()));
            assertThat(matcher.getInvocation(), is(notNullValue()));
        }
    }
}