
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.CaptureArguments;
import org.mockito.internal.reporting.PrintSettings;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndEmptyListOfMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Check invocationMatcher instance
        Assert.assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndListOfMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Check invocationMatcher instance
        Assert.assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(mock(Invocation.class), Arrays.asList(mock(Matcher.class)));
        Method method = invocationMatcher.getMethod();

        // Check method instance
        Assert.assertNotNull(method);
        assertEquals("testMethod", method.getName()); // Replace 'testMethod' with your method name
    }

    @Test
    public void testEmptyInvocationMatcher() {
        InvocationMatcher invocationMatcher = new InvocationMatcher();

        // Check invocationMatcher instance
        Assert.assertNotNull(invocationMatcher);
        assertNull(invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testInvocationMatcherConstructorWithNullInvocation() {
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(null, matchers);
    }

    @Test
    public void testInvocationMatcherConstructorWithNullMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, null);

        // Check invocationMatcher instance
        Assert.assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testHasSimilarMethodWithMismatchedMethodNames() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mock(Invocation.class), Arrays.asList(mock(Matcher.class)));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mock(Invocation.class), Arrays.asList(mock(Matcher.class)));

        // Assert hasSimilarMethod returns false
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMocks() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mock(Invocation.class, "mock1"), Arrays.asList(mock(Matcher.class)));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mock(Invocation.class, "mock2"), Arrays.asList(mock(Matcher.class)));

        // Assert hasSimilarMethod returns false
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodWithUnverifiedInvocations() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mock(Invocation.class, "mock1"), Arrays.asList(mock(Matcher.class)));
        InvocationMatcher invocationMatcher2 = new VerifiedInvocationMatcher(mock(Invocation.class, "mock2"), Arrays.asList(mock(Matcher.class)));

        // Assert hasSimilarMethod returns false
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethodWithMismatchedArgumentTypes() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mock(Invocation.class), Arrays.asList(Integer.TYPE, Integer.TYPE));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mock(Invocation.class), Arrays.asList(String.class, Integer.TYPE));

        // Assert hasSameMethod returns false
        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }
}

class VerifiedInvocationMatcher extends InvocationMatcher {
    public VerifiedInvocationMatcher(Invocation invocation, List<Matcher> matchers) {
        super(invocation, matchers);
    }

    @Override
    public boolean matches(Invocation actual) {
        return super.matches(actual) && invocation.isVerified();
    }
}
