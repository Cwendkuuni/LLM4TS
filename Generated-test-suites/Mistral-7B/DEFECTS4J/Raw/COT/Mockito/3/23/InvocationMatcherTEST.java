import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatcherList() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocation.getMethod();
        List<Matcher> matchers = Collections.emptyList();

        InvocationMatcher expectedInvocationMatcher = new InvocationMatcher(invocation, matchers);

        assertThat(expectedInvocationMatcher.getInvocation(), is(invocation));
        assertThat(expectedInvocationMatcher.getMatchers(), is(matchers));
        assertThat(expectedInvocationMatcher.getMethod(), is(method));
    }

    @Test
    public void testConstructorWithInvocationAndMatcherList() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> expectedMatchers = matchers;

        assertThat(expectedMatchers, is(invocationMatcher.getMatchers()));
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocation.getMethod();

        Method expectedMethod = invocationMatcher.getMethod();

        assertThat(expectedMethod, is(method));
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation expectedInvocation = invocation;

        assertThat(expectedInvocation, is(invocationMatcher.getInvocation()));
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> expectedMatchers = matchers;

        assertThat(expectedMatchers, is(invocationMatcher.getMatchers()));
    }

    @Test
    public void testToString() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String expectedToString = new PrintSettings().print(invocationMatcher.getMatchers(), invocationMatcher);

        assertThat(expectedToString, not(is("")));
    }

    @Test
    public void testMatchesWithSameInvocation() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        Invocation sameInvocation = invocation;

        InvocationMatcher expectedInvocationMatcher = new InvocationMatcher(sameInvocation, Collections.emptyList());

        assertTrue(invocationMatcher.matches(expectedInvocationMatcher));
    }

    @Test
    public void testMatchesWithDifferentInvocation() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);

        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesWithSameMethodButDifferentInvocation() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method = mock(Method.class);

        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList(), method);

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesWithNullInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(null, Collections.emptyList());
    }

    @Test
    public void testHasSimilarMethodWithSameInvocation() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, Collections.emptyList());

        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodWithDifferentInvocation() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);

        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodButDifferentInvocation() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Method method1 = mock(Method.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList(), method1);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method2 = mock(Method.class);

        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList(), method2);

        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodAndSameArguments() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object[] arguments = new Object[]{1, 2};
        Method method = mock(Method.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(2)), method, arguments);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(2)), method, arguments);

        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodAndDifferentArguments() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object[] arguments1 = new Object[]{1, 2};
        Object[] arguments2 = new Object[]{1, 3};
        Method method = mock(Method.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(2)), method, arguments1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(2)), method, arguments2);

        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasSimilarMethodWithNullInvocation() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(null, Collections.emptyList(), null);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mock(InvocationOnMock.class), Collections.emptyList(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasSimilarMethodWithNullMethod() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mock(InvocationOnMock.class), Collections.emptyList(), null);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mock(InvocationOnMock.class), Collections.emptyList(), (Method) null);
    }

    @Test
    public void testSafelyArgumentsMatchWithSameArguments() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(mock(InvocationOnMock.class), Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(2)));
        Object[] arguments = new Object[]{1, 2};

        assertTrue(invocationMatcher.safelyArgumentsMatch(arguments));
    }

    @Test(expected = AssertionError.class)
    public void testSafelyArgumentsMatchWithDifferentArguments() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(mock(InvocationOnMock.class), Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(2)));
        Object[] arguments = new Object[]{1, 3};

        //An AssertionError is thrown by ArgumentsComparator.argumentsMatch()
    }

    @Test
    public void testHasSameMethodWithSameInvocation() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, Collections.emptyList());

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethodWithDifferentInvocationsButSameMethod() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method = mock(Method.class);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList(), method);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList(), method);

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethodWithDifferentInvocationsButDifferentMethods() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Method method1 = mock(Method.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method2 = mock(Method.class);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList(), method1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList(), method2);

        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasSameMethodWithNullInvocation() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(null, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mock(InvocationOnMock.class), Collections.emptyList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasSameMethodWithNullMethod() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mock(InvocationOnMock.class), Collections.emptyList(), null);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mock(InvocationOnMock.class), Collections.emptyList(), mock(Method.class));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object[] invocationArguments = new Object[]{1, 2};
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(2));
        CapturesArguments capturesArguments = Mockito.spy(ArgumentCaptor.forClass(Object.class));

        new InvocationMatcher(invocation, matchers).captureArgumentsFrom(invocation);

        verify(capturesArguments).captureFrom(invocationArguments[0]);
        verify(capturesArguments).captureFrom(invocationArguments[1]);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(mock(InvocationOnMock.class), mock(InvocationOnMock.class));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        List<Matcher> expectedMatchers = Arrays.asList(new InvocationMatcher(invocations.get(0)), new InvocationMatcher(invocations.get(1)));

        List<Matcher> actualMatchers = Arrays.asList(invocationMatchers.get(0).getMatchers(), invocationMatchers.get(1).getMatchers());

        assertThat(actualMatchers, is(expectedMatchers));
    }
}