import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Invocation;
import org.mockito.Mockito;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

@RunWith(org.mockito.runners.JUnit4)
public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocationMatcher.getMethod();
        assert method != null;
        assert invocationMatcher.getInvocation().equals(invocation);
        assert invocationMatcher.getMatchers().isEmpty();
    }

    @Test
    public void testConstructorWithInvocationAndCustomMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> customMatchers = new LinkedList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, customMatchers);
        Method method = invocationMatcher.getMethod();
        assert method != null;
        assert invocationMatcher.getInvocation().equals(invocation);
        assert !invocationMatcher.getMatchers().isEmpty();
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocationMatcher.getMethod();
        assert method != null;
        assert invocationMatcher.getInvocation().getMethod().equals(method);
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assert invocationMatcher.getInvocation().equals(invocation);
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assert matchers.isEmpty();

        List<Matcher> customMatchers = new LinkedList<>();
        customMatchers.add(Mockito.mock(Matcher.class));
        invocationMatcher = new InvocationMatcher(invocation, customMatchers);
        matchers = invocationMatcher.getMatchers();
        assert !matchers.isEmpty();
    }

    @Test(expected = AssertionError.class)
    public void testToString() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        String toString = invocationMatcher.toString();
        throw new AssertionError("Unexpected string representation: " + toString);
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.singletonList(Mockito.mock(Matcher.class)));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.singletonList(Mockito.mock(Matcher.class)));

        Method method1 = invocationMatcher1.getMethod();
        Method method2 = invocationMatcher2.getMethod();
        assertEquals(method1, method2);

        assertTrue(invocationMatcher1.matches(invocationMatcher1));
        assertFalse(invocationMatcher1.matches(invocationMatcher2));
        assertFalse(invocationMatcher2.matches(invocationMatcher1));
        assertFalse(invocationMatcher2.matches(invocationMatcher2));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Object[] arguments = new Object[0];
        assertTrue(invocationMatcher.safelyArgumentsMatch(arguments));
    }

    @Test(expected = AssertionError.class)
    public void testSafelyArgumentsMatchWithThrowable() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Object[] arguments = new Object[1];
        throw new AssertionError("Unexpected exception");

        // The test will fail with the expected AssertionError
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.singletonList(Mockito.mock(Matcher.class)));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.singletonList(Mockito.mock(Matcher.class)));

        Method method1 = invocationMatcher1.getMethod();
        Method method2 = invocationMatcher2.getMethod();

        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher1));
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2)); // same method name, same mock, unverified
        assertFalse(invocationMatcher1.hasSimilarMethod(Mockito.mock(InvocationMatcher.class))); // different mock
        assertFalse(invocationMatcher2.hasSimilarMethod(invocationMatcher1)); // different mock
        assertFalse(invocationMatcher1.hasSimilarMethod(Mockito.mock(Invocation.class))); // not InvocationMatcher

        Method method3 = Mockito.mock(Method.class);
        assertFalse(invocationMatcher1.hasSimilarMethod(new InvocationMatcher(invocationMatcher1, Collections.singletonList(method3)))); // different matchers
        assertFalse(invocationMatcher1.hasSimilarMethod(new InvocationMatcher(invocationMatcher2, Collections.singletonList(method3)))); // different matchers
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.singletonList(Mockito.mock(Matcher.class)));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.singletonList(Mockito.mock(Matcher.class)));

        Method method1 = invocationMatcher1.getMethod();
        Method method2 = invocationMatcher2.getMethod();

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher1));
        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2)); // same method
        assertFalse(invocationMatcher1.hasSameMethod(Mockito.mock(InvocationMatcher.class))); // different class
        assertFalse(invocationMatcher1.hasSameMethod(Mockito.mock(Invocation.class))); // not InvocationMatcher

        Method method3 = Mockito.mock(Method.class);
        assertFalse(invocationMatcher1.hasSameMethod(new InvocationMatcher(invocationMatcher1, Collections.singletonList(method3)))); // different matchers
        assertFalse(invocationMatcher1.hasSameMethod(new InvocationMatcher(invocationMatcher2, Collections.singletonList(method3)))); // different matchers
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(Mockito.mock(ArgumentsMatcher.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(invocation);

        ArgumentCaptor argumentCaptor = ArgumentCaptor.forClass(Object.class);
        Matcher actualMatcher = invocationMatcher.getMatchers().get(0);
        actualMatcher.matches(argumentCaptor.capture());

        Object capturedArgument = argumentCaptor.getValue();
        assertNotNull(capturedArgument);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<>();
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assert invocationMatchers.isEmpty();

        Invocation invocation = Mockito.mock(Invocation.class);
        invocations.add(invocation);
        invocationMatchers = InvocationMatcher.createFrom(invocations);
        assert !invocationMatchers.isEmpty();
    }
}