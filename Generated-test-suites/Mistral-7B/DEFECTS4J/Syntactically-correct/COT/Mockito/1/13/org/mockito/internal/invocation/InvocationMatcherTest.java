import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyListOfMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertThat(invocationMatcher, is(notNullValue()));
        assertThat(invocationMatcher.getMatchers(), is(Collections.emptyList()));
    }

    @Test
    public void testConstructorWithInvocationAndNonEmptyListOfMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertThat(invocationMatcher, is(notNullValue()));
        assertThat(invocationMatcher.getMatchers(), is(matchers));
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method returnedMethod = invocationMatcher.getMethod();
        verify(invocation).getMethod();
        assertThat(returnedMethod, is(method));
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation returnedInvocation = invocationMatcher.getInvocation();
        verify(invocation).getInvocation();
        assertThat(returnedInvocation, is(invocation));
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        List<Matcher> returnedMatchers = invocationMatcher.getMatchers();
        verify(invocation).getArguments();
        assertThat(returnedMatchers, is(matchers));
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String toString = invocationMatcher.toString();
        verify(invocation).getArguments();
        verify(invocation).getMock();
        verify(invocation).getMethod();
        String expectedToString = String.format("%s(%s)", method.getName(), matchers.toString());
        assertThat(toString, is(expectedToString));
    }

    @Test
    public void testMatchesWithSameInvocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation sameInvocation = mock(Invocation.class);
        when(sameInvocation.getMock()).thenReturn(invocation.getMock());
        assertTrue(invocationMatcher.matches(sameInvocation));
    }

    @Test
    public void testMatchesWithDifferentInvocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation differentInvocation = mock(Invocation.class);
        assertFalse(invocationMatcher.matches(differentInvocation));
    }

    @Test
    public void testSafelyArgumentsMatchArray() {
        Invocation invocation = mock(Invocation.class);
        Object[] actualArgs = new Object[]{mock(String.class), mock(Integer.class)};
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(mock(Matcher.class)));
        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchNull() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(mock(Matcher.class)));
        invocationMatcher.safelyArgumentsMatch(null);
    }

    @Test
    public void testHasSimilarMethodWithSameInvocation() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation sameInvocation = mock(Invocation.class);
        when(sameInvocation.getMethod()).thenReturn(method);
        assertTrue(invocationMatcher.hasSimilarMethod(sameInvocation));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethodName() {
        Invocation invocation = mock(Invocation.class);
        Method method1 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        Method method2 = mock(Method.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        when(method2.getName()).thenReturn("differentMethodName");
        assertFalse(invocationMatcher.hasSimilarMethod(new InvocationMatcher(mock(Invocation.class), Arrays.asList(method2))));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMock() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation differentInvocation = mock(Invocation.class);
        when(differentInvocation.getMock()).thenReturn(mock(Object.class));
        assertFalse(invocationMatcher.hasSimilarMethod(new InvocationMatcher(differentInvocation)));
    }

    @Test
    public void testHasSimilarMethodWithOverloadedMethods() {
        Invocation invocation = mock(Invocation.class);
        Method method1 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        Method method2 = mock(Method.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        when(method1.getName()).thenReturn("methodName");
        when(method2.getName()).thenReturn("methodName");
        when(method1.getParameterTypes()).thenReturn(new Class[]{String.class, Integer.class});
        when(method2.getParameterTypes()).thenReturn(new Class[]{String.class, Double.class});
        assertFalse(invocationMatcher.hasSimilarMethod(new InvocationMatcher(mock(Invocation.class), Arrays.asList(method2))));
    }

    @Test
    public void testHasSameMethodWithSameArguments() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Object[] arguments = new Object[]{mock(String.class), mock(Integer.class)};
        InvocationMatcher invocationMatcherWithArguments = new InvocationMatcher(invocation, Arrays.asList(mock(Matcher.class), new VarargMatcher<>(new Class[]{String[].class})));
        assertTrue(invocationMatcherWithArguments.matches(new InvocationMatcher(mock(Invocation.class), Arrays.asList(mock(Matcher.class), Arrays.asList(arguments)))));
    }

    @Test
    public void testHasSameMethodWithDifferentArguments() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Object[] arguments1 = new Object[]{mock(String.class), mock(Integer.class)};
        Object[] arguments2 = new Object[]{mock(String.class), mock(Double.class)};
        InvocationMatcher invocationMatcherWithArguments1 = new InvocationMatcher(invocation, Arrays.asList(mock(Matcher.class), new VarargMatcher<>(new Class[]{Object[].class})));
        InvocationMatcher invocationMatcherWithArguments2 = new InvocationMatcher(invocation, Arrays.asList(mock(Matcher.class), new VarargMatcher<>(new Class[]{Object[].class})));
        assertFalse(invocationMatcherWithArguments1.matches(new InvocationMatcher(mock(Invocation.class), Arrays.asList(mock(Matcher.class), arguments2))));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod().isVarArgs()).thenReturn(false);
        InvocationMatcher invocationMatcher = spy(new InvocationMatcher(invocation, Arrays.asList(mock(Matcher.class), mock(CapturesArguments.class))));
        Object[] arguments = new Object[]{mock(String.class), mock(Integer.class)};
        invocationMatcher.captureArgumentsFrom(invocation);
        verify(invocationMatcher.getMatchers().get(1), is(CapturesArguments.class));
        verify(invocationMatcher.getMatchers().get(1)).captureFrom(arguments[0]);
        verify(invocationMatcher.getMatchers().get(1)).captureFrom(arguments[1]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromWithVarargs() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod().isVarArgs()).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(mock(Matcher.class), mock(CapturesArguments.class)));
        invocationMatcher.captureArgumentsFrom(invocation);
    }
}