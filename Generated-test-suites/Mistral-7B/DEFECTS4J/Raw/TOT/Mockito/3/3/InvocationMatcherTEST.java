import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.*;
import static org.mockito.internal.matchers.Arguments.notNull;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithEmptyMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Invocation otherInvocation = mock(Invocation.class);
        MatcherDecorator otherMatcher = new MatcherDecorator(ArgumentMatchers.any());
        InvocationMatcher otherMatcherWrapped = new InvocationMatcher(otherInvocation, Collections.singletonList(otherMatcher));

        assertFalse(matcher.matches(otherMatcherWrapped));
        verify(invocation, never()).equals(otherInvocation);
    }

    @Test
    public void testInvocationMatcherWithMatchingInvocation() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.eq(1));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Invocation otherInvocation = mock(Invocation.class);
        InvocationMatcher otherMatcher = new InvocationMatcher(otherInvocation, matchers);

        assertTrue(matcher.matches(otherMatcher));
        verify(invocation).equals(otherInvocation);
    }

    @Test
    public void testInvocationMatcherWithDifferentInvocationsButSameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);

        Invocation invocation2 = mock(Invocation.class);
        Method method2 = mock(Method.class);
        when(invocation2.getMethod()).thenReturn(method1);

        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, matchers);

        assertTrue(matcher1.hasSimilarMethod(matcher2));
        assertTrue(matcher1.matches(matcher2));
    }

    @Test
    public void testInvocationMatcherWithDifferentInvocationsAndDifferentMethods() {
        Invocation invocation1 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);

        Invocation invocation2 = mock(Invocation.class);
        Method method2 = mock(Method.class);
        when(invocation2.getMethod()).thenReturn(method2);

        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, matchers);

        assertFalse(matcher1.hasSimilarMethod(matcher2));
        assertFalse(matcher1.matches(matcher2));
    }

    @Test
    public void testInvocationMatcherWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.eq(1)));

        Invocation otherInvocation = mock(Invocation.class);
        InvocationMatcher otherMatcher = new InvocationMatcher(otherInvocation, Arrays.asList(ArgumentMatchers.eq("test"), notNull(ArgumentMatchers.any())));

        assertFalse(matcher.matches(otherMatcher));
        verify(invocation, never()).equals(otherInvocation);
    }

    @Test(expected = AssertionError.class)
    public void testInvocationMatcherWithNoMatchingArguments() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));

        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq("test"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Invocation otherInvocation = mock(Invocation.class);
        when(otherInvocation.getMethod()).thenReturn(mock(Method.class));
        when(otherInvocation.getArguments()).thenReturn(new Object[0]);

        InvocationMatcher otherMatcher = new InvocationMatcher(otherInvocation);

        assertFalse(matcher.matches(otherMatcher));
    }

    @Test
    public void testInvocationMatcherWithVarArgs() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.eq(1), ArgumentMatchers.eq("world"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Object[] arguments = new Object[]{"test", 1, "world"};
        Invocation otherInvocation = mock(Invocation.class);
        when(otherInvocation.getMethod()).thenReturn(method);
        when(otherInvocation.getArguments()).thenReturn(arguments);

        InvocationMatcher otherMatcher = new InvocationMatcher(otherInvocation);

        assertTrue(matcher.matches(otherMatcher));
        verify(invocation).equals(otherInvocation);
    }
}