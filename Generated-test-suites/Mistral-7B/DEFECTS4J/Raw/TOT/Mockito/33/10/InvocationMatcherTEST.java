import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "testMethod", new Class[]{Integer.class, String.class});
        Invocation invocation = Mockito.on(mock).invoke(method, 1, "test");
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq("test"));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertTrue(invocationMatcher.matches(invocation));

        Invocation otherInvocation = Mockito.on(mock).invoke(method, 2, "test");
        assertFalse(invocationMatcher.matches(otherInvocation));

        Invocation otherInvocationWithSameMethod = Mockito.on(mock).invoke(method, 1, "test2");
        assertFalse(invocationMatcher.matches(otherInvocationWithSameMethod));

        Invocation otherInvocationWithSameMethodAndSameArguments = Mockito.on(mock).invoke(method, 1, "test");
        assertTrue(invocationMatcher.matches(otherInvocationWithSameMethodAndSameArguments));
    }

    @Test
    public void testEmptyMatcherList() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "testMethod", new Class[]{Integer.class, String.class});
        Invocation invocation = Mockito.on(mock).invoke(method, 1, "test");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test(expected = AssertionError.class)
    public void testMismatchedArguments() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "testMethod", new Class[]{Integer.class, String.class});
        Invocation invocation = Mockito.on(mock).invoke(method, 1, "test");

        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(2), ArgumentMatchers.eq("test"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.matches(invocation);
    }
}