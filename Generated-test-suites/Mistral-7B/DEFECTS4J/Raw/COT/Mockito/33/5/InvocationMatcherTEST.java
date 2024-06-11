import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructionWithInvocationAndEmptyMatchersList() {
        Invocation invocation = new Invocation(new Location(1, 1), new Method("testMethod", new Class[0]), new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        assertEquals(method.getName(), "testMethod");
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertTrue(matchers.isEmpty());
    }

    @Test
    public void testInvocationMatcherConstructionWithInvocationAndCustomMatchersList() {
        Invocation invocation = new Invocation(new Location(1, 1), new Method("testMethod", new Class[]{Integer.class, String.class}), new Object[]{1, "test"});
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(ArgumentCaptor.forClass(Integer.class));
        matchers.add(ArgumentCaptor.forClass(String.class).that(new IsStringWithLength(5)));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocationMatcher.getMethod();
        assertEquals(method.getName(), "testMethod");
        List<Matcher> invocationMatcherMatchers = invocationMatcher.getMatchers();
        assertEquals(invocationMatcherMatchers.size(), 2);
    }

    @Test
    public void testInvocationMatcherMatchesSameInvocation() {
        Invocation invocation = new Invocation(new Location(1, 1), new Method("testMethod", new Class[0]), new Object[0]);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation invocationToMatch = invocation;
        assertTrue(invocationMatcher.matches(invocationToMatch));
    }

    @Test
    public void testInvocationMatcherDoesNotMatchDifferentInvocationWithSameMethodName() {
        Invocation invocation = new Invocation(new Location(1, 1), new Method("testMethod", new Class[0]), new Object[0]);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation invocationToMatch = new Invocation(new Location(1, 1), new Method("testMethod", new Class[0]), new Object[0]);
        Invocation differentInvocation = new Invocation(new Location(2, 2), new Method("testMethod", new Class[0]), new Object[0]);
        assertFalse(invocationMatcher.matches(differentInvocation));
    }

    @Test
    public void testInvocationMatcherDoesNotMatchDifferentInvocationWithDifferentMethod() {
        Invocation invocation = new Invocation(new Location(1, 1), new Method("testMethod", new Class[0]), new Object[0]);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation invocationToMatch = new Invocation(new Location(1, 1), new Method("anotherMethod", new Class[0]), new Object[0]);
        assertFalse(invocationMatcher.matches(invocationToMatch));
    }

    @Test
    public void testInvocationMatcherDoesNotMatchUnverifiedInvocationFromDifferentMock() {
        Invocation invocation = new Invocation(new Location(1, 1), new Method("testMethod", new Class[0]), new Object[0]);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation invocationToMatch = new Invocation(new Location(1, 1), new Method("testMethod", new Class[0]), new Object[0]);
        Invocation unverifiedInvocation = new Invocation(new Location(1, 1), new Method("testMethod", new Class[0]), new Object[0], false);
        assertFalse(invocationMatcher.matches(unverifiedInvocation));
    }

    @Test
    public void testInvocationMatcherMatchesSameMethodWithDifferentArguments() {
        Invocation invocation = new Invocation(new Location(1, 1), new Method("testMethod", new Class[0]), new Object[]{1, "test"});
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation invocationToMatch = new Invocation(new Location(1, 1), new Method("testMethod", new Class[0]), new Object[]{2, "test"});
        assertTrue(invocationMatcher.matches(invocationToMatch));
    }

    @Test(expected = AssertionError.class)
    public void testInvocationMatcherThrowsAssertionErrorWhenArgumentsDoNotMatch() {
        Invocation invocation = new Invocation(new Location(1, 1), new Method("testMethod", new Class[0]), new Object[]{1, "test"});
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation invocationToMatch = new Invocation(new Location(1, 1), new Method("testMethod", new Class[0]), new Object[]{2, "notTest"});
        invocationMatcher.matches(invocationToMatch);
    }

    @Test
    public void testHasSimilarMethodReturnsFalseWhenMethodsAreNotSame() {
        Invocation invocation = new Invocation(new Location(1, 1), new Method("testMethod1", new Class[0]), new Object[0]);
        Invocation candidate = new Invocation(new Location(1, 1), new Method("testMethod2", new Class[0]), new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodReturnsTrueWhenMethodsAreTheSame() {
        Invocation invocation = new Invocation(new Location(1, 1), new Method("testMethod", new Class[0]), new Object[0]);
        Invocation candidate = new Invocation(new Location(1, 1), new Method("testMethod", new Class[0]), new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethodReturnsFalseWhenMethodsAreNotTheSame() {
        Invocation invocation = new Invocation(new Location(1, 1), new Method("testMethod1", new Class[0]), new Object[0]);
        Invocation candidate = new Invocation(new Location(1, 1), new Method("testMethod2", new Class[0]), new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethodReturnsTrueWhenMethodsAreTheSame() {
        Invocation invocation = new Invocation(new Location(1, 1), new Method("testMethod", new Class[0]), new Object[0]);
        Invocation candidate = new Invocation(new Location(1, 1), new Method("testMethod", new Class[0]), new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testCaptureArgumentsFromMethodCallsMatcherOnEachArgument() {
        Invocation invocation = new Invocation(new Location(1, 1), new Method("testMethod", new Class[]{Integer.class, String.class}), new Object[]{1, "test"});
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(ArgumentCaptor.forClass(Integer.class));
        matchers.add(ArgumentCaptor.forClass(String.class).that(new IsStringWithLength(5)));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(invocation);

        CapturesArguments integerCaptor = (CapturesArguments) matchers.get(0);
        assertEquals(1, integerCaptor.getValue());

        CapturesArguments stringCaptor = (CapturesArguments) matchers.get(1);
        assertEquals("test", stringCaptor.getValue());
    }

    @Test
    public void testCreateFromMethodCreatesListOfInvocationMatchersFromListOfInvocations() {
        Invocation invocation1 = new Invocation(new Location(1, 1), new Method("testMethod1", new Class[0]), new Object[0]);
        Invocation invocation2 = new Invocation(new Location(1, 1), new Method("testMethod2", new Class[0]), new Object[0]);
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation1);
        invocations.add(invocation2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);

        assertEquals(invocation1.getMethod().getName(), invocationMatcher1.getMethod().getName());
        assertEquals(invocation2.getMethod().getName(), invocationMatcher2.getMethod().getName());
    }
}