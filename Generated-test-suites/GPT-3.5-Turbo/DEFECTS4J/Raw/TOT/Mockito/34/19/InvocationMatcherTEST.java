import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.exceptions.PrintableInvocation;
import org.hamcrest.Matcher;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithMatchers() {
        Invocation invocation = new Invocation("methodName", "mock", new Object[]{"arg1"}, new String[]{"argType"}, null);
        List<Matcher> matchers = Collections.singletonList(new MockMatcher());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcherConstructorWithoutMatchers() {
        Invocation invocation = new Invocation("methodName", "mock", new Object[]{"arg1"}, new String[]{"argType"}, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation("methodName", "mock", new Object[]{"arg1"}, new String[]{"argType"}, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method expectedMethod = invocation.getMethod();
        Method actualMethod = invocationMatcher.getMethod();

        assertEquals(expectedMethod, actualMethod);
    }

    @Test
    public void testMatchesWithMatchingInvocation() {
        Invocation invocation = new Invocation("methodName", "mock", new Object[]{"arg1"}, new String[]{"argType"}, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation actualInvocation = new Invocation("methodName", "mock", new Object[]{"arg1"}, new String[]{"argType"}, null);

        assertTrue(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatchesWithNotMatchingInvocation() {
        Invocation invocation = new Invocation("methodName", "mock", new Object[]{"arg1"}, new String[]{"argType"}, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation actualInvocation = new Invocation("otherMethodName", "mock", new Object[]{"arg1"}, new String[]{"argType"}, null);

        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testSafelyArgumentsMatchWithMatchingArguments() {
        Invocation invocation = new Invocation("methodName", "mock", new Object[]{"arg1"}, new String[]{"argType"}, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Object[] actualArgs = {"arg1"};

        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }

    @Test
    public void testSafelyArgumentsMatchWithNotMatchingArguments() {
        Invocation invocation = new Invocation("methodName", "mock", new Object[]{"arg1"}, new String[]{"argType"}, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Object[] actualArgs = {"arg2"};

        assertFalse(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodName() {
        Invocation invocation = new Invocation("methodName", "mock", new Object[]{"arg1"}, new String[]{"argType"}, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidate = new Invocation("methodName", "mock", new Object[]{"arg1"}, new String[]{"argType"}, null);

        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethodName() {
        Invocation invocation = new Invocation("methodName", "mock", new Object[]{"arg1"}, new String[]{"argType"}, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidate = new Invocation("otherMethodName", "mock", new Object[]{"arg1"}, new String[]{"argType"}, null);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithDifferentVerificationStatus() {
        Invocation invocation = new Invocation("methodName", "mock", new Object[]{"arg1"}, new String[]{"argType"}, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidate = new Invocation("methodName", "mock", new Object[]{"arg1"}, new String[]{"argType"}, null);
        candidate.setVerified(true);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMock() {
        Invocation invocation = new Invocation("methodName", "mock1", new Object[]{"arg1"}, new String[]{"argType"}, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidate = new Invocation("methodName", "mock2", new Object[]{"arg1"}, new String[]{"argType"}, null);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithOverloadedButSameArgs() {
        Invocation invocation = new Invocation("methodName", "mock", new Object[]{"arg1"}, new String[]{"argType"}, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidate = new Invocation("methodName", "mock", new Object[]{"arg1"}, new String[]{"differentArgType"}, null);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethodWithSameMethod() {
        Invocation invocation = new Invocation("methodName", "mock", new Object[]{"arg1"}, new String[]{"argType"}, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidate = new Invocation("methodName", "mock", new Object[]{"arg1"}, new String[]{"argType"}, null);

        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethodWithDifferentMethod() {
        Invocation invocation = new Invocation("methodName", "mock", new Object[]{"arg1"}, new String[]{"argType"}, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidate = new Invocation("otherMethodName", "mock", new Object[]{"arg1"}, new String[]{"argType"}, null);

        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation("methodName", "mock", new Object[]{"arg1"}, new String[]{"argType"}, new Location("class", "file", "line"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Location expectedLocation = invocation.getLocation();
        Location actualLocation = invocationMatcher.getLocation();

        assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testToStringWithDefaultPrintSettings() {
        Invocation invocation = new Invocation("methodName", "mock", new Object[]{"arg1"}, new String[]{"argType"}, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        String expectedString = invocation.toString(invocationMatcher.getMatchers(), new PrintSettings());
        String actualString = invocationMatcher.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testCaptureArgumentsFromWithCapturesArgumentsMatcher() {
        Invocation invocation = new Invocation("methodName", "mock", new Object[]{"arg1"}, new String[]{"argType"}, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.singletonList(new CapturesArgumentsMatcher()));

        Invocation i = new Invocation("methodName", "mock", new Object[]{"arg1"}, new String[]{"argType"}, null);

        invocationMatcher.captureArgumentsFrom(i);

        CapturesArgumentsMatcher matcher = (CapturesArgumentsMatcher) invocationMatcher.getMatchers().get(0);
        assertNotNull(matcher.getCapturedArguments());
        assertEquals("arg1", matcher.getCapturedArguments());
    }

    private static class MockMatcher implements Matcher {

        @Override
        public boolean matches(Object item) {
            return false;
        }

        @Override
        public void describeMismatch(Object item, Description description) {
        }

        @Override
        public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
        }

        @Override
        public void describeTo(Description description) {
        }
    }

    private static class CapturesArgumentsMatcher implements CapturesArguments, Matcher {

        private Object capturedArguments;

        @Override
        public void captureFrom(Object argument) {
            this.capturedArguments = argument;
        }

        @Override
        public boolean matches(Object argument) {
            return false;
        }

        @Override
        public void describeMismatch(Object argument, Description description) {
        }

        @Override
        public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
        }

        @Override
        public void describeTo(Description description) {
        }

        @Override
        public Object getCapturedArguments() {
            return capturedArguments;
        }
    }
}