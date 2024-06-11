import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationOnMockImpl;
import org.mockito.internal.matchers.ArgumentsComparator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(org.mockito.runners.JUnit4.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndEmptyMatchersList() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocation.getMethod();

        assertThat(invocationMatcher.getMatchers(), is(Collections.emptyList()));
        assertThat(invocationMatcher.getMethod(), is(method));
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndMatchersList() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        matchers.add(matcher1);
        matchers.add(matcher2);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertThat(invocationMatcher.getMatchers(), is(matchers));
        assertThat(invocationMatcher.getMethod(), is(invocation.getMethod()));
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation actualInvocation = invocationMatcher.getInvocation();
        assertThat(actualInvocation, is(invocation));
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        matchers.add(matcher1);
        matchers.add(matcher2);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        assertThat(actualMatchers, is(matchers));
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        matchers.add(matcher1);
        matchers.add(matcher2);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String actualToString = invocationMatcher.toString();
        String expectedToString = invocation.toString(matchers, new PrintSettings());

        assertThat(actualToString, is(expectedToString));
    }

    @Test
    public void testMatches_sameInvocation() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation actualInvocation = invocationMatcher;
        InvocationMatcher actualMatcher = new InvocationMatcher(actualInvocation, matchers);

        boolean actual = invocationMatcher.matches(actualMatcher);
        boolean expected = true;

        assertThat(actual, is(expected));
    }

    @Test
    public void testMatches_differentInvocationsButSameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        Matcher matcher = mock(Matcher.class);
        matchers.add(matcher);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers);

        Method method = invocation1.getMethod();
        boolean actual = invocationMatcher1.matches(invocationMatcher2);
        boolean expected = false;

        assertThat(actual, is(expected));
    }

    @Test
    public void testMatches_differentInvocationsAndDifferentMethods() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Matcher> matchers1 = new ArrayList<>();
        Matcher matcher1 = mock(Matcher.class);
        matchers1.add(matcher1);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers1);
        List<Matcher> matchers2 = new ArrayList<>();
        Matcher matcher2 = mock(Matcher.class);
        matchers2.add(matcher2);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers2);

        Method method1 = invocation1.getMethod();
        Method method2 = invocation2.getMethod();
        boolean actual = invocationMatcher1.matches(invocationMatcher2);
        boolean expected = false;

        assertThat(actual, is(expected));
    }

    @Test(expected = AssertionError.class)
    public void testMatches_differentInvocationsButSameMethodAndDifferentArguments() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        Matcher matcher = mock(Matcher.class);
        matchers.add(matcher);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Method method = invocation1.getMethod();
        invocationMatcher1.matches(invocationMatcher2);
    }

    @Test
    public void testSafelyArgumentsMatch_sameArguments() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[]{1, 2, 3};
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(mock(Matcher.class)));

        boolean actual = invocationMatcher.safelyArgumentsMatch(arguments);
        boolean expected = true;

        assertThat(actual, is(expected));
    }

    @Test
    public void testSafelyArgumentsMatch_differentArguments() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments1 = new Object[]{1, 2, 3};
        Object[] arguments2 = new Object[]{2, 3, 3};
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(mock(Matcher.class)));

        boolean actual = invocationMatcher.safelyArgumentsMatch(arguments2);
        boolean expected = false;

        assertThat(actual, is(expected));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatch_throwsException() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[]{1, 2, new IllegalStateException()};
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(mock(Matcher.class)));

        invocationMatcher.safelyArgumentsMatch(arguments);
    }

    @Test
    public void testHasSimilarMethod_sameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method = mock(Method.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(mock(Matcher.class)));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(mock(Matcher.class)));

        invocationMatcher1.getMethod();
        invocationMatcher2.getMethod();
        boolean actual = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        boolean expected = true;

        assertThat(actual, is(expected));
    }

    @Test
    public void testHasSimilarMethod_differentMethods() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(mock(Matcher.class)));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(mock(Matcher.class)));

        invocationMatcher1.getMethod();
        invocationMatcher2.getMethod();
        boolean actual = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        boolean expected = false;

        assertThat(actual, is(expected));
    }

    @Test
    public void testHasSimilarMethod_overloadedButSameArguments() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(mock(Matcher.class)));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Arrays.asList(mock(Matcher.class)));

        invocationMatcher1.getMethod();
        invocationMatcher2.getMethod();
        boolean actual = invocationMatcher1.hasSameMethod(invocationMatcher2);
        boolean expected = true;

        assertThat(actual, is(expected));

        Object[] arguments = new Object[]{1, 2, 3};
        invocationMatcher1.safelyArgumentsMatch(arguments);
        boolean actual2 = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        boolean expected2 = false;

        assertThat(actual2, is(expected2));
    }
}