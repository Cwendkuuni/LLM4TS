package org.mockito.internal.invocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockConfiguration;
import org.mockito.stub.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

@RunWith(org.mockito.runners.JUnit4.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_whenInvocationAndEmptyMatcherList_thenNewInstanceIsCreated() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        assertThat(invocationMatcher, is(notNullValue()));
        assertThat(invocationMatcher.getMatchers(), is(Collections.emptyList()));
    }

    @Test
    public void testInvocationMatcher_whenInvocationAndMatcherList_thenNewInstanceIsCreated() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("someString"), ArgumentMatchers.is(123L));

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // then
        assertThat(invocationMatcher, is(notNullValue()));
        assertThat(invocationMatcher.getMatchers(), is(sameInstance(matchers)));
    }

    @Test
    public void testGetMethod() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        assertThat(invocationMatcher.getMethod(), is(sameInstance(method)));
    }

    @Test
    public void testGetInvocation() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        assertThat(invocationMatcher.getInvocation(), is(sameInstance(invocation)));
    }

    @Test
    public void testGetMatchers() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("someString"), ArgumentMatchers.is(123L));
        when(invocation.argumentsToMatchers()).thenReturn(matchers);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        assertThat(invocationMatcher.getMatchers(), is(sameInstance(matchers)));
    }

    @Test
    public void testToString() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("someString"), ArgumentMatchers.is(123L));
        when(invocation.toString(ArgumentMatchers.anyListOf(Matcher.class), any(PrintSettings.class))).thenReturn("invocationToString");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        String result = invocationMatcher.toString();

        // then
        assertThat(result, is("invocationToString"));
    }

    @Test
    public void testMatches() {
        // given
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("someString"), ArgumentMatchers.is(123L));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers);

        when(invocation1.getMock()).thenReturn(Mockito.mock(Object.class, new MockConfiguration().mockUseDefaultAnswer(Answer.RETURN_DEFAULTS)));
        when(invocation2.getMock()).thenReturn(Mockito.mock(Object.class, new MockConfiguration().mockUseDefaultAnswer(Answer.RETURN_DEFAULTS)));

        // when
        boolean result1 = invocationMatcher1.matches(invocation1);
        boolean result2 = invocationMatcher1.matches(invocation2);

        // then
        assertThat(result1, is(true));
        assertThat(result2, is(false));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        // given
        Object[] actualArgs = new Object[]{"someString", 123L};

        InvocationMatcher invocationMatcher = new InvocationMatcher(null, Arrays.asList(ArgumentMatchers.eq("someString"), ArgumentMatchers.is(123L)));

        // when
        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void testHasSimilarMethod() {
        // given
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);

        when(invocation1.getMock()).thenReturn(Mockito.mock(Object.class, new MockConfiguration().mockUseDefaultAnswer(Answer.RETURN_DEFAULTS)));
        when(invocation2.getMock()).thenReturn(Mockito.mock(Object.class, new MockConfiguration().mockUseDefaultAnswer(Answer.RETURN_DEFAULTS)));

        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);

        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);

        // when
        boolean result = invocationMatcher.hasSimilarMethod(invocationMatcher);

        // then
        assertThat(result, is(true));

        // given
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);

        when(method1.getName()).thenReturn("someMethodName1");
        when(method2.getName()).thenReturn("someMethodName2");

        // when
        result = invocationMatcher.hasSimilarMethod(invocationMatcher);

        // then
        assertThat(result, is(false));

        // given
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);

        when(method1.getName()).thenReturn("someMethodName");
        when(method2.getName()).thenReturn("someMethodName");

        Object[] actualArgs = new Object[]{"someString1", "someString2", 123L};

        // when
        InvocationMatcher invocationMatcherWithArgs = new InvocationMatcher(invocation1, Arrays.asList(ArgumentMatchers.eq("someString1"), ArgumentMatchers.eq("someString2"), ArgumentMatchers.is(123L)));

        // then
        boolean resultWithArgs = invocationMatcherWithArgs.hasSimilarMethod(new InvocationMatcher(invocation2, Arrays.asList(ArgumentMatchers.eq("someString1"), ArgumentMatchers.eq("someString2"), ArgumentMatchers.is(123L))));

        // then
        assertThat(resultWithArgs, is(true));
    }

    @Test
    public void testHasSameMethod() {
        // given
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);

        when(invocation1.getMock()).thenReturn(Mockito.mock(Object.class, new MockConfiguration().mockUseDefaultAnswer(Answer.RETURN_DEFAULTS)));
        when(invocation2.getMock()).thenReturn(Mockito.mock(Object.class, new MockConfiguration().mockUseDefaultAnswer(Answer.RETURN_DEFAULTS)));

        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);

        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);

        // when
        boolean result = invocationMatcher.hasSameMethod(invocationMatcher);

        // then
        assertThat(result, is(true));

        // given
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);

        when(method1.getName()).thenReturn("someMethodName1");
        when(method2.getName()).thenReturn("someMethodName2");

        // when
        result = invocationMatcher.hasSameMethod(invocationMatcher);

        // then
        assertThat(result, is(false));

        // given
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);

        when(method1.getName()).thenReturn("someMethodName");
        when(method2.getName()).thenReturn("someMethodName");

        when(method1.getParameterTypes()).thenReturn(new Class[]{String.class, Integer.TYPE, Integer.TYPE});
        when(method2.getParameterTypes()).thenReturn(new Class[]{String.class, Integer.TYPE, Integer.class});

        // when
        result = invocationMatcher.hasSameMethod(invocationMatcher);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void testCreateFrom() {
        // given
        List<Invocation> invocations = Arrays.asList(Mockito.mock(InvocationOnMock.class), Mockito.mock(InvocationOnMock.class), Mockito.mock(InvocationOnMock.class));

        // when
        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);

        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(3));
    }
}