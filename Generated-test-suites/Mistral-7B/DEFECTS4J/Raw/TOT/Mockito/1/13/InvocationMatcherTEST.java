import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_withEmptyMatchers_matchesSameInvocation() {
        Invocation invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher emptyMatcher = new InvocationMatcher(invocation);
        InvocationMatcher sameInvocation = new InvocationMatcher(invocation);

        List<Matcher> noMatchers = Collections.emptyList();
        InvocationMatcher noMatchersMatcher = new InvocationMatcher(invocation, noMatchers);

        assertThat(emptyMatcher, is(sameInstance(noMatchersMatcher)));
        assertThat(emptyMatcher, is(sameInstance(sameInvocation)));

        verify(invocation).matches(sameInvocation);
        verify(invocation).matches(emptyMatcher);
        verify(invocation).matches(noMatchersMatcher);
    }

    @Test
    public void testInvocationMatcher_withMatchers_matchesDifferentInvocations() {
        Invocation invocation1 = mock(InvocationOnMock.class);
        Invocation invocation2 = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);

        List<Matcher> matchers1 = Arrays.asList(
                Mockito.argThat(arg -> arg.equals(1)),
                Mockito.argThat(arg -> arg.equals("test"))
        );

        InvocationMatcher matchersMatcher1 = new InvocationMatcher(invocation1, matchers1);
        InvocationMatcher differentInvocationMatcher = new InvocationMatcher(invocation2);

        Object[] arguments1 = new Object[]{1, "test"};
        Object[] arguments2 = new Object[]{2, "test"};

        verify(invocation1).matches(matchersMatcher1);
        verify(invocation1).matches(differentInvocationMatcher);
        verify(invocation1).matches(matchersMatcher1, arguments1);
        verify(invocation1).matches(differentInvocationMatcher, arguments1);

        verify(invocation2).matches(matchersMatcher1);
        verify(invocation2).matches(differentInvocationMatcher);
        verify(invocation2).matches(matchersMatcher1, arguments2);
        verify(invocation2).matches(differentInvocationMatcher, arguments2);

        // Different invocations should not match
        verify(invocation1).doesNotMatch(differentInvocationMatcher);
        verify(invocation2).doesNotMatch(matchersMatcher1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInvocationMatcher_withVarargs_cannotCapture() {
        Invocation invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        when(method.isVarArgs()).thenReturn(true);

        InvocationMatcher matchersMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(arg -> arg.equals(1)));

        InvocationMatcher matchersWithVarargsMatcher = new InvocationMatcher(invocation, matchers);

        // InvocationMatcher should not be able to capture arguments when method is varargs
        matchersWithVarargsMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        List<Matcher> matchers = Arrays.asList(
                Mockito.argThat(arg -> arg.equals(1)),
                Mockito.argThat(arg -> arg.equals("test"))
        );

        InvocationMatcher matchersMatcher = new InvocationMatcher(invocation, matchers);

        String expected = "Mockito matchers:\n" +
                "  1. <1>\n" +
                "  2. <test>\n" +
                "Invocation:\n" +
                "  this.getMock() -> org.mockito.internal.stubbing.MockConfiguration$1@3a0a6f\n" +
                "  getClass() -> class org.mockito.internal.invocation.InvocationMatcher\n" +
                "  getMethod() -> public void testMethod(int,java.lang.String)\n" +
                "  getLocation() -> InvocationInterceptor$MockInvocationSite@37114d\n";

        String actual = new PrintSettings().print(matchers, invocation);

        assertThat(actual, is(equalTo(expected)));
    }
}