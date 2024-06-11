import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.CapturesArguments;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Location;
import org.mockito.internal.invocation.PrintSettings;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructionWithInvocationAndEmptyMatcherList() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocation.getMethod();
        List<Matcher> matchers = invocationMatcher.getMatchers();

        assertThat(matchers, is(Collections.emptyList()));
        assertThat(invocationMatcher.getMethod(), is(method));
    }

    @Test
    public void testInvocationMatcherConstructionWithInvocationAndMatcherList() {
        Invocation invocation = Mockito.mock(Invocation.class);
        CapturesArguments captureArguments = mock(CapturesArguments.class);
        List<Matcher> matchers = Arrays.asList(captureArguments);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> matchersFromInvocationMatcher = invocationMatcher.getMatchers();

        assertThat(matchersFromInvocationMatcher, is(matchers));
    }

    @Test
    public void testGetterMethods() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocation.getMethod();
        Invocation invocationFromMatcher = invocationMatcher.getInvocation();
        List<Matcher> matchersFromMatcher = invocationMatcher.getMatchers();

        assertThat(invocationFromMatcher, is(invocation));
        assertThat(matchersFromMatcher, is(matchers));
        assertThat(invocationMatcher.getMethod(), is(method));
    }

    @Test
    public void testMatchesMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.isVerified()).thenReturn(false);
        when(invocation2.isVerified()).thenReturn(false);
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertThat(invocationMatcher1.matches(invocationMatcher2), is(false));
    }

    @Test
    public void testMatchesMethodWithSameInvocations() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation);

        assertThat(invocationMatcher1.matches(invocationMatcher2), is(true));
    }

    @Test
    public void testMatchesMethodWithSameInvocationAndDifferentMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers1 = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        List<Matcher> matchers2 = Arrays.asList(mock(Matcher.class), mock(Matcher.class), mock(Matcher.class));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, matchers1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, matchers2);

        assertThat(invocationMatcher1.matches(invocationMatcher2), is(false));
    }

    @Test
    public void testMatchesMethodWithSameInvocationsAndSameMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, matchers);

        assertThat(invocationMatcher1.matches(invocationMatcher2), is(true));
    }

    @Test
    public void testHasSimilarMethodMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("someMethod");
        when(method2.getName()).thenReturn("someMethod");
        when(method1.equals(method2)).thenReturn(true);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertThat(invocationMatcher1.hasSimilarMethod(invocationMatcher2), is(true));
    }

    @Test
    public void testHasSimilarMethodMethodWithDifferentInvocationsAndSameMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        when(invocation1.isVerified()).thenReturn(false);
        when(invocation2.isVerified()).thenReturn(false);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertThat(invocationMatcher1.hasSimilarMethod(invocationMatcher2), is(false));
    }

    @Test
    public void testHasSimilarMethodMethodWithSameInvocationsAndOverloadedMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("someMethod");
        when(method1.getParameterCount()).thenReturn(2);

        Method method3 = mock(Method.class);
        when(method3.getName()).thenReturn("someMethod");
        when(method3.getParameterCount()).thenReturn(3);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, Arrays.asList(mock(Matcher.class), mock(Matcher.class), mock(Matcher.class)));

        assertThat(invocationMatcher1.hasSimilarMethod(invocationMatcher2), is(false));
    }

    @Test
    public void testHasSimilarMethodMethodWithSameInvocationsAndSameMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("someMethod");

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, Collections.emptyList());

        assertThat(invocationMatcher1.hasSimilarMethod(invocationMatcher2), is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSafelyArgumentsMatchWhenDifferentTypes() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{1, "two"};
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(is(Integer.class), is(String.class)));

        invocationMatcher.safelyArgumentsMatch(arguments);
    }

    @Test(expected = AssertionError.class)
    public void testArgumentsComparatorThrowsExceptionWhenArgumentsDifferent() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] invocationArguments = new Object[]{1, "two"};
        List<Matcher> matchers = Arrays.asList(is(Integer.class), is(String.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(Mockito.mock(Invocation.class).getArguments());

        invocationMatcher.safelyArgumentsMatch(invocationArguments);
    }

    @Test
    public void testArgumentsComparatorMatchesWhenArgumentsEqual() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] invocationArguments = new Object[]{1, "two"};
        List<Matcher> matchers = Arrays.asList(is(Integer.class), is("two"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(Mockito.mock(Invocation.class).getArguments());

        boolean result = invocationMatcher.safelyArgumentsMatch(invocationArguments);

        assertThat(result, is(true));
    }

    @Test
    public void testLocationGetter() {
        Location location = Mockito.mock(Location.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getLocation()).thenReturn(location);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        Location result = invocationMatcher.getLocation();

        assertThat(result, is(location));
    }

    @Test
    public void testToStringMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        PrintSettings printSettings = mock(PrintSettings.class);

        String result = invocationMatcher.toString(printSettings);

        verifyZeroInteractions(invocation);
        verifyZeroInteractions(matchers);
        verify(printSettings).print(isA(String.class));
    }

    @Test
    public void testToStringMethodWithEmptyMatcherList() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        String result = invocationMatcher.toString();

        verifyZeroInteractions(invocation);
        verifyZeroInteractions(matchers);
    }
}