import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    private Object mock;

    @Before
    public void init() {
        mock = Mockito.mock(Object.class);
    }

    @Test
    public void testInvocationMatcher_sameMethodAndArguments() {
        // Given
        Method method = Mockito.methodCalled("testMethod");
        Invocation invocation = new Invocation(mock, method, new Object[]{1, 2});
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(2));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // When & Then
        assertTrue(invocationMatcher.matches(new InvocationOnMock(mock, method, new Object[]{1, 2})));
    }

    @Test
    public void testInvocationMatcher_sameMethodButDifferentArguments() {
        // Given
        Method method = Mockito.methodCalled("testMethod");
        Invocation invocation = new Invocation(mock, method, new Object[]{1, 2});
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(3));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // When & Then
        assertFalse(invocationMatcher.matches(new InvocationOnMock(mock, method, new Object[]{1, 2})));
    }

    @Test
    public void testInvocationMatcher_differentMethods() {
        // Given
        Method method1 = Mockito.methodCalled("testMethod1");
        Invocation invocation1 = new Invocation(mock, method1, new Object[]{1});

        Method method2 = Mockito.methodCalled("testMethod2");
        Invocation invocation2 = new Invocation(mock, method2, new Object[]{2});
        List<Matcher> matchers = Arrays.asList(ArgumentMatchters.eq(1));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);

        // When & Then
        assertFalse(invocationMatcher1.matches(new InvocationOnMock(mock, method2, new Object[]{2})));
    }

    @Test
    public void testInvocationMatcher_differentMocks() {
        // Given
        Object mock1 = Mockito.mock(Object.class);
        Method method = Mockito.methodCalled("testMethod");
        Invocation invocation = new Invocation(mock1, method, new Object[]{1, 2});
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(2));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Object mock2 = Mockito.mock(Object.class);
        Invocation invocation2 = new Invocation(mock2, method, new Object[]{1, 2});
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // When & Then
        assertFalse(invocationMatcher.matches(new InvocationOnMock(mock2, method, new Object[]{1, 2})));
    }

    @Test
    public void testInvocationMatcher_verifyArgumentsMatching() {
        // Given
        Method method = Mockito.methodCalled("testMethod");
        Invocation invocation = new Invocation(mock, method, new Object[]{1, 2});
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(2));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // When
        Object[] actualArgs = new Object[]{1, 2};

        // Then
        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }

    @Test(expected = Throwable.class)
    public void testInvocationMatcher_argumentsDontMatch() {
        // Given
        Method method = Mockito.methodCalled("testMethod");
        Invocation invocation = new Invocation(mock, method, new Object[]{1, 2});
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(3), ArgumentMatchers.eq(2));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // When & Then
        assertFalse(invocationMatcher.matches(new InvocationOnMock(mock, method, new Object[]{1, 2})));
    }

    @Test
    public void testInvocationMatcher_hasSimilarMethod() {
        // Given
        Method method1 = Mockito.methodCalled("testMethod1");
        Invocation invocation1 = new Invocation(mock, method1, new Object[]{1, 2});

        Method method2 = Mockito.methodCalled("testMethod2");
        Invocation invocation2 = new Invocation(mock, method2, new Object[]{1, 2});

        // When
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // Then
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcher_hasDifferentMethods() {
        // Given
        Method method1 = Mockito.methodCalled("testMethod1");
        Invocation invocation1 = new Invocation(mock, method1, new Object[]{1, 2});

        Method method2 = Mockito.methodCalled("testMethod2");
        Invocation invocation2 = new Invocation(mock, method2, new Object[]{1, 2});

        // When
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // Then
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }
}