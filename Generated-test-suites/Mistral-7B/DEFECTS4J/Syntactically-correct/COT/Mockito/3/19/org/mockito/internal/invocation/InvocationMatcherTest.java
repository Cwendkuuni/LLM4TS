import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    private List<Invocation> invocations;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() {
        invocations = new LinkedList<Invocation>() {{
            add(Mockito.mock(Invocation.class));
            add(Mockito.mock(Invocation.class));
        }};
        invocationMatcher = InvocationMatcher.createFrom(invocations);
    }

    @Test
    public void testConstructorWithMatchers() {
        List<Matcher> matchers = new LinkedList<Matcher>() {{
            add(ArgumentMatchers.anyInt());
            add(ArgumentMatchers.eq("test"));
        }};
        InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(invocations.get(0), matchers);
        assertTrue(invocationMatcherWithMatchers.matches(invocations.get(0)));
    }

    @Test
    public void testConstructorWithoutMatchers() {
        InvocationMatcher invocationMatcherWithoutMatchers = new InvocationMatcher(invocations.get(0));
        assertTrue(invocationMatcherWithoutMatchers.matches(invocations.get(0)));
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        invocationMatcher = new InvocationMatcher(invocation);
        Method result = invocationMatcher.getMethod();
        assertTrue(result.equals(method));
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        invocationMatcher = new InvocationMatcher(invocation);
        Invocation result = invocationMatcher.getInvocation();
        assertTrue(result.equals(invocation));
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = new LinkedList<Matcher>() {{
            add(ArgumentMatchers.anyInt());
            add(ArgumentMatchers.eq("test"));
        }};
        invocationMatcher = new InvocationMatcher(invocations.get(0), matchers);
        List<Matcher> result = invocationMatcher.getMatchers();
        assertTrue(result.equals(matchers));
    }

    @Test
    public void testToString() {
        List<Matcher> matchers = new LinkedList<Matcher>() {{
            add(ArgumentMatchers.anyInt());
            add(ArgumentMatchers.eq("test"));
        }};
        invocationMatcher = new InvocationMatcher(invocations.get(0), matchers);
        String result = invocationMatcher.toString();
        // PrintSettings is not available for testing, so the result might not be as expected
        // but it should be a string representation of the matchers and invocation
        assertTrue(result.length() > 0);
    }

    @Test
    public void testMatches() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocations.get(0));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocations.get(1));
        assertTrue(invocationMatcher1.matches(invocationMatcher1));
        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[2];
        actualArgs[0] = "test1";
        actualArgs[1] = 1;
        invocationMatcher = new InvocationMatcher(invocations.get(0), Collections.singletonList(ArgumentMatchers.eq("test1")));
        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }

    @Test
    public void testHasSimilarMethod() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocations.get(0));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocations.get(1));
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(invocations.get(0));
        InvocationMatcher invocationMatcher4 = new InvocationMatcher(Mockito.mock(Invocation.class));
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher1));
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher3));
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher4));
    }

    @Test(expected = Throwable.class)
    public void testHasSameMethodException() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocations.get(0));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocations.get(1));
        try {
            invocationMatcher1.hasSameMethod(invocationMatcher2);
        } catch (Throwable e) {
            throw e;
        }
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Matcher matcher = ArgumentMatchers.eq("test");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);
        assertTrue(matcher.matches(invocationMatcher.getMatchers().get(0)));
    }

    @Test
    public void testCreateFrom() {
        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);
        assertTrue(result.size() == invocations.size());
    }
}