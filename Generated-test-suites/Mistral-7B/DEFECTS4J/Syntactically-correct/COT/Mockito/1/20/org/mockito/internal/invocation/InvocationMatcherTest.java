import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.internal.invocation.InvocationMatcher;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatcherList() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocation.getMethod();
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assert (matchers.isEmpty());
        assert (invocationMatcher.matches(invocation));
    }

    @Test
    public void testConstructorWithInvocationAndNonEmptyMatcherList() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Matcher mockMatcher = Mockito.mock(Matcher.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(mockMatcher));
        Method method = invocation.getMethod();
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assert (!matchers.isEmpty());
        assert (matchers.get(0) == mockMatcher);
        assert (invocationMatcher.matches(invocation));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsWhenInvocationHasVarargs() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(method1.getName()).thenReturn("testMethod");
        Mockito.when(method2.getName()).thenReturn("testMethod");
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, Arrays.asList(method1));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, Arrays.asList(method2));
        assert (invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethodWhenMethodsAreDifferent() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(method1.getName()).thenReturn("testMethod1");
        Mockito.when(method2.getName()).thenReturn("testMethod2");
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, Arrays.asList(method1));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, Arrays.asList(method2));
        assert (!invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testMatches() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Mockito.when(method.getName()).thenReturn("testMethod");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Matcher mockMatcher = Mockito.mock(Matcher.class);
        List<Matcher> matchers = Arrays.asList(mockMatcher);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation actual = Mockito.mock(Invocation.class);
        Method actualMethod = Mockito.mock(Method.class);
        Mockito.when(actual.getMethod()).thenReturn(actualMethod);
        Mockito.when(actualMethod.getName()).thenReturn("testMethod");
        InvocationMatcher actualMatcher = new InvocationMatcher(actual, Collections.<Matcher>emptyList());
        assert (invocationMatcher.matches(actualMatcher));
    }

    @Test
    public void testMatchesWhenInvocationsAreDifferent() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Mockito.when(method.getName()).thenReturn("testMethod");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Invocation actual = Mockito.mock(Invocation.class);
        Method actualMethod = Mockito.mock(Method.class);
        Mockito.when(actual.getMethod()).thenReturn(actualMethod);
        Mockito.when(actualMethod.getName()).thenReturn("testMethod2");
        List<Matcher> matchers = Arrays.asList(Mockito.mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        InvocationMatcher actualMatcher = new InvocationMatcher(actual, Collections.<Matcher>emptyList());
        assert (!invocationMatcher.matches(actualMatcher));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[1];
        arguments[0] = "test";
        Mockito.when(invocation.getArguments()).thenReturn(arguments);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assert (invocationMatcher.safelyArgumentsMatch(arguments));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchWhenArgumentsDoNotMatch() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[1];
        arguments[0] = "test";
        Mockito.when(invocation.getArguments()).thenReturn(arguments);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(Mockito.mock(Matcher.class)));
        Object[] actualArguments = new Object[1];
        actualArguments[0] = "test2";
        assert (invocationMatcher.safelyArgumentsMatch(actualArguments)); // Should throw an exception
    }

    @Test
    public void testToString() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Mockito.when(method.getName()).thenReturn("testMethod");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Matcher mockMatcher = Mockito.mock(Matcher.class);
        List<Matcher> matchers = Arrays.asList(mockMatcher);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String expected = "InvocationMatcher{method=testMethod, matchers=[Matcher()]";
        String result = invocationMatcher.toString();
        assert (result.equals(expected));
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Invocation> invocations = new LinkedList<Invocation>();
        invocations.add(invocation1);
        invocations.add(invocation2);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assert (invocationMatchers.size() == 2);
        assert (invocationMatchers.get(0) instanceof InvocationMatcher);
        assert (invocationMatchers.get(1) instanceof InvocationMatcher);
    }
}