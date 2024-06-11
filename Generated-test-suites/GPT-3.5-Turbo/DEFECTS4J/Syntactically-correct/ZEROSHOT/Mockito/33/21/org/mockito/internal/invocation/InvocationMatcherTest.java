import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithMatchers() throws NoSuchMethodException {
        Method method = String.class.getMethod("equals", Object.class);
        Invocation invocation = new Invocation(mock(String.class), method, new Object[]{"Test"});
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcherWithoutMatchers() throws NoSuchMethodException {
        Method method = String.class.getMethod("equals", Object.class);
        Invocation invocation = new Invocation(mock(String.class), method, new Object[]{"Test"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() throws NoSuchMethodException {
        Method method = String.class.getMethod("equals", Object.class);
        Invocation invocation = new Invocation(mock(String.class), method, new Object[]{"Test"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() throws NoSuchMethodException {
        Method method = String.class.getMethod("equals", Object.class);
        Invocation invocation = new Invocation(mock(String.class), method, new Object[]{"Test"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        String expected = "equals(Test)";
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches() throws NoSuchMethodException {
        Method method = String.class.getMethod("equals", Object.class);
        Invocation invocation1 = new Invocation(mock(String.class), method, new Object[]{"Test"});
        Invocation invocation2 = new Invocation(mock(String.class), method, new Object[]{"Test"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testHasSimilarMethod() throws NoSuchMethodException {
        Method method = String.class.getMethod("equals", Object.class);
        Invocation invocation1 = new Invocation(mock(String.class), method, new Object[]{"Test"});
        Invocation invocation2 = new Invocation(mock(String.class), method, new Object[]{"Test"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSameMethod() throws NoSuchMethodException {
        Method method = String.class.getMethod("equals", Object.class);
        Invocation invocation1 = new Invocation(mock(String.class), method, new Object[]{"Test"});
        Invocation invocation2 = new Invocation(mock(String.class), method, new Object[]{"Test"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test
    public void testCaptureArgumentsFrom() throws NoSuchMethodException {
        Method method = String.class.getMethod("equals", Object.class);
        Invocation invocation1 = new Invocation(mock(String.class), method, new Object[]{"Test1"});
        Invocation invocation2 = new Invocation(mock(String.class), method, new Object[]{"Test2"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        Matcher matcher = mock(Matcher.class);

        List<Matcher> matchers = Collections.singletonList(matcher);
        invocationMatcher.captureArgumentsFrom(invocation2);

        verify(matcher).captureFrom("Test2");
    }

    @Test
    public void testCreateFrom() throws NoSuchMethodException {
        Method method1 = String.class.getMethod("equals", Object.class);
        Method method2 = Integer.class.getMethod("equals", Object.class);
        Invocation invocation1 = new Invocation(mock(String.class), method1, new Object[]{"Test"});
        Invocation invocation2 = new Invocation(mock(Integer.class), method2, new Object[]{1});

        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(invocations.size(), invocationMatchers.size());
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }
}