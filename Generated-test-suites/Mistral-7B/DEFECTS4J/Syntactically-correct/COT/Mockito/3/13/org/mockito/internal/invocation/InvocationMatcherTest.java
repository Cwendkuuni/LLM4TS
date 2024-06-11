import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Location;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatchersList() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Method method = matcher.getMethod();

        assertEquals(method.getName(), invocation.getMethod().getName());
        assertEquals(matcher.getInvocation(), invocation);
        assertEquals(matcher.getMatchers().size(), 0);
        assertEquals(matcher.toString(), new PrintSettings().print(Collections.emptyList(), invocation));
    }

    @Test
    public void testConstructorWithInvocationAndMatchersList() {
        Invocation invocation = createMockInvocation();
        List<Matcher> matchers = Arrays.asList(createMockMatcher(), createMockMatcher());
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        Method method = matcher.getMethod();

        assertEquals(method.getName(), invocation.getMethod().getName());
        assertEquals(matcher.getInvocation(), invocation);
        assertEquals(matcher.getMatchers(), matchers);
        assertEquals(matcher.toString(), new PrintSettings().print(matchers, invocation));
    }

    @Test
    public void testMatchesInvocation() {
        Invocation invocation = createMockInvocation();
        List<Matcher> matchers = Arrays.asList(createMockMatcher(), createMockMatcher());
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        Invocation anotherInvocation = createAnotherMockInvocation();

        assertTrue(matcher.matches(invocation));
        assertFalse(matcher.matches(anotherInvocation));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = createMockInvocation();
        Object[] actualArgs = new Object[]{null, 1L, new Object[]{}};
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertTrue(matcher.safelyArgumentsMatch(actualArgs));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchThrowsException() {
        Invocation invocation = createMockInvocation();
        Object[] actualArgs = new Object[]{null, 1L, new Object[]{}};
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertFalse(matcher.safelyArgumentsMatch(actualArgs));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = createMockInvocation();
        Invocation invocation2 = createAnotherMockInvocation();
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertFalse(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = createMockInvocation();
        Invocation invocation2 = createAnotherMockInvocationWithSameMethod();
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertTrue(matcher1.hasSameMethod(matcher2));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        Invocation invocation = createMockInvocation();
        List<Matcher> matchers = Arrays.asList(createMockArgumentMatcher(), createMockArgumentMatcher());
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Object[] invocationArguments = invocation.getArguments();
        matcher.captureArgumentsFrom(invocation);

        Matcher firstMatcher = matchers.get(0);
        Matcher secondMatcher = matchers.get(1);

        if (firstMatcher instanceof CapturesArguments) {
            assertEquals(firstMatcher.matches(invocationArguments[0]), true);
        }

        if (secondMatcher instanceof CapturesArguments) {
            assertEquals(secondMatcher.matches(invocationArguments[1]), true);
        }
    }

    @Test
    public void testCreateFromInvocations() {
        List<Invocation> invocations = Arrays.asList(createMockInvocation(), createAnotherMockInvocation());
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        assertEquals(matchers.size(), 2);
        for (InvocationMatcher matcher : matchers) {
            assertEquals(matcher.getInvocation(), matchers.get(0).getInvocation());
            assertEquals(matcher.getMatchers(), matchers.get(0).getMatchers());
        }
    }

    private Invocation createMockInvocation() {
        Mockito mockito = mock(Mockito.class);
        Method method = Mockito.method(String.class, "testMethod", String.class, int.class);
        Invocation invocation = new Invocation(mockito, method, new Object[]{"arg1"}, new Location(1, 1));
        return invocation;
    }

    private Invocation createAnotherMockInvocation() {
        Mockito mockito = mock(Mockito.class);
        Method method = Mockito.method(String.class, "testMethod", String[].class);
        Invocation invocation = new Invocation(mockito, method, new Object[]{new String[]{"arg1"}}, new Location(1, 1));
        return invocation;
    }

    private Invocation createAnotherMockInvocationWithSameMethod() {
        Mockito mockito = mock(Mockito.class);
        Method method = Mockito.method(String.class, "testMethod", String.class, int.class);
        Invocation invocation = new Invocation(mockito, method, new Object[]{"arg1"}, new Location(1, 1));
        return invocation;
    }

    private Matcher createMockMatcher() {
        return mock(Matcher.class);
    }

    private Matcher createMockArgumentMatcher() {
        return new ArgumentsMatcher() {
            @Override
            public boolean matches(Object argument) {
                return true;
            }
        };
    }
}