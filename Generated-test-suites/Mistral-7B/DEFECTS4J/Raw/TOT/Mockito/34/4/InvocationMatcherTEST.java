import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Invocation invocation;
    private Object mock;

    @Before
    public void setup() {
        mock = mock(Object.class);
        Method method = Mockito.method(Object.class, "someMethod", new Class[0]);
        invocation = new Invocation(method, mock, new Object[0]);
        matcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testMatcherWithEmptyMatcherList() {
        List<Matcher> emptyMatcherList = Collections.emptyList();
        InvocationMatcher emptyMatcher = new InvocationMatcher(invocation, emptyMatcherList);

        assertTrue(emptyMatcher.matches(invocation));
    }

    @Test
    public void testMatcherWithCustomMatcherList() {
        List<Matcher> matcherList = Arrays.asList(ArgumentMatchers.eq("expectedValue"));
        InvocationMatcher customMatcher = new InvocationMatcher(invocation, matcherList);

        Object[] invocationArguments = new Object[]{"actualValue"};
        Invocation customInvocation = new Invocation(getMethod(), mock, invocationArguments);

        assertFalse(customMatcher.matches(customInvocation));

        // Set up mock to return the expected value when called with the argument.
        Mockito.when(mock.someMethod()).thenReturn("expectedValue");

        // Verify that the invocation matches the custom matcher.
        customInvocation = new Invocation(getMethod(), mock, invocationArguments);
        customMatcher.captureArgumentsFrom(customInvocation);
        verify(mock).someMethod();
        assertTrue(customMatcher.matches(customInvocation));
    }

    @Test(expected = AssertionError.class)
    public void testMatcherThrowsAssertionErrorWhenArgumentsDoNotMatch() {
        List<Matcher> matcherList = Arrays.asList(ArgumentMatchers.not(ArgumentMatchers.eq("expectedValue")));
        InvocationMatcher customMatcher = new InvocationMatcher(invocation, matcherList);

        Object[] invocationArguments = new Object[]{"actualValue"};
        Invocation customInvocation = new Invocation(getMethod(), mock, invocationArguments);

        customMatcher.captureArgumentsFrom(customInvocation);
        customMatcher.matches(customInvocation);
    }

    @Test
    public void testHasSameMethod() {
        InvocationMatcher matcher1 = new InvocationMatcher(invocation);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation);

        assertTrue(matcher1.hasSameMethod(matcher2));
    }

    @Test
    public void testHasDifferentMethod() {
        InvocationMatcher matcher1 = new InvocationMatcher(invocation);
        InvocationMatcher matcher2 = new InvocationMatcher(new Invocation(new Method("differentMethod", new Class[0]), mock, new Object[0]));

        assertFalse(matcher1.hasSameMethod(matcher2));
    }

    @Test
    public void testHasSimilarMethodButDifferentArguments() {
        InvocationMatcher matcher1 = new InvocationMatcher(new Invocation(getMethod(), mock, new Object[0]));
        InvocationMatcher matcher2 = new InvocationMatcher(new Invocation(getMethod(), mock, new Object[]{new Object()}));

        assertFalse(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testHasSimilarMethodButSameArguments() {
        InvocationMatcher matcher1 = new InvocationMatcher(new Invocation(getMethod(), mock, new Object[0]));
        InvocationMatcher matcher2 = new InvocationMatcher(new Invocation(getMethod(), mock, new Object[0]));

        assertTrue(matcher1.hasSimilarMethod(matcher2));
    }
}