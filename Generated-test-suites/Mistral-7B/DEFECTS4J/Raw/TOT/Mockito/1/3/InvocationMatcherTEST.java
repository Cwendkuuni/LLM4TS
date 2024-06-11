import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMockAdapter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.platform.commons.util.ReflectionUtils.findMethod;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(JUnitPlatform.class)
public class InvocationMatcherTest {

    @Test
    public void testMatchesInvocationWithSameMethodAndMock() {
        // given
        Object mock = mock(Object.class);
        Invocation invocation1 = new InvocationOnMockAdapter(new InvocationOnMock(mock, new MethodTestClass(), "testMethod", new Object[]{1, 2, 3}));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new InvocationOnMockAdapter(new InvocationOnMock(mock, new MethodTestClass(), "testMethod", new Object[]{1, 2, 3}));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // when
        // no exceptions should be thrown

        // then
        assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesInvocationWithSameMethodNameAndSameArgumentsButDifferentMocks() {
        // given
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Invocation invocation1 = new InvocationOnMockAdapter(new InvocationOnMock(mock1, new MethodTestClass(), "testMethod", new Object[]{1, 2, 3}));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new InvocationOnMockAdapter(new InvocationOnMock(mock2, new MethodTestClass(), "testMethod", new Object[]{1, 2, 3}));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // when
        // no exceptions should be thrown

        // then
        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesInvocationWithDifferentMethodNamesButSameArgumentsAndSameMock() {
        // given
        Object mock = mock(Object.class);
        Invocation invocation1 = new InvocationOnMockAdapter(new InvocationOnMock(mock, new MethodTestClass(), "testMethod1", new Object[]{1, 2, 3}));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new InvocationOnMockAdapter(new InvocationOnMock(mock, new MethodTestClass(), "testMethod2", new Object[]{1, 2, 3}));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // when
        // no exceptions should be thrown

        // then
        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesInvocationWithSameMethodNamesButDifferentArgumentsAndSameMock() {
        // given
        Object mock = mock(Object.class);
        Invocation invocation1 = new InvocationOnMockAdapter(new InvocationOnMock(mock, new MethodTestClass(), "testMethod", new Object[]{1, 2, 3}));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new InvocationOnMockAdapter(new InvocationOnMock(mock, new MethodTestClass(), "testMethod", new Object[]{4, 5, 6}));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // when
        // no exceptions should be thrown

        // then
        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesInvocationWithSameMethodNamesButDifferentMocks() {
        // given
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Invocation invocation1 = new InvocationOnMockAdapter(new InvocationOnMock(mock1, new MethodTestClass(), "testMethod", new Object[]{1, 2, 3}));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new InvocationOnMockAdapter(new InvocationOnMock(mock2, new MethodTestClass(), "testMethod", new Object[]{1, 2, 3}));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // when
        // no exceptions should be thrown

        // then
        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testHasSameMethod() {
        // given
        Object mock = mock(Object.class);
        Method method = findMethod(MethodTestClass.class, "testMethod", Object[].class);
        Invocation invocation = new InvocationOnMockAdapter(new InvocationOnMock(mock, new MethodTestClass(), method, new Object[]{1, 2, 3}));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // when
        // no exceptions should be thrown

        // then
        assertTrue(invocationMatcher.hasSameMethod(invocationMatcher));
    }

    @Test
    public void testHasSameMethod_differentInvocationsWithSameMethod() {
        // given
        Object mock = mock(Object.class);
        Method method = findMethod(MethodTestClass.class, "testMethod", Object[].class);
        Invocation invocation1 = new InvocationOnMockAdapter(new InvocationOnMock(mock, new MethodTestClass(), method, new Object[]{1, 2, 3}));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Object arg = new Object[]{4, 5, 6};
        Invocation invocation2 = new InvocationOnMockAdapter(new InvocationOnMock(mock, new MethodTestClass(), method, arg));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // when
        // no exceptions should be thrown

        // then
        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethod_differentInvocationsWithDifferentMethods() {
        // given
        Object mock = mock(Object.class);
        Method method1 = findMethod(MethodTestClass.class, "testMethod1", Object[].class);
        Method method2 = findMethod(MethodTestClass.class, "testMethod2", Object[].class);
        Invocation invocation1 = new InvocationOnMockAdapter(new InvocationOnMock(mock, new MethodTestClass(), method1, new Object[]{1, 2, 3}));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new InvocationOnMockAdapter(new InvocationOnMock(mock, new MethodTestClass(), method2, new Object[]{1, 2, 3}));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // when
        // no exceptions should be thrown

        // then
        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testToString() {
        // given
        Object mock = mock(Object.class);
        Method method = findMethod(MethodTestClass.class, "testMethod", Object[].class);
        Invocation invocation = new InvocationOnMockAdapter(new InvocationOnMock(mock, new MethodTestClass(), method, new Object[]{1, 2, 3}));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // when
        String toString = invocationMatcher.toString();

        // then
        String expectedToString = String.format("InvocationMatcher%n" +
                "    Invocation: %s%n" +
                "    Method: %s%n" +
                "    Matchers: %s%n", invocation.toString(), method.toString(), Arrays.toString(invocationMatcher.getMatchers().stream().map(Matcher::toString).collect(Collectors.toList())));
        assertEquals(expectedToString, toString);
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        // given
        Object mock = mock(Object.class);
        Method method = findMethod(MethodTestClass.class, "testMethod", Object[].class);
        Invocation invocation = new InvocationOnMockAdapter(new InvocationOnMock(mock, new MethodTestClass(), method, new Object[]{1, 2, 3}));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq("test"));

        // when
        invocationMatcher.captureArgumentsFrom(invocation);

        // then
        for (int i = 0; i < matchers.size(); i++) {
            Matcher matcher = matchers.get(i);
            Object capturedArgument = matcher.getCapture();
            assertEquals(invocation.getArgumentAt(i), capturedArgument);
        }
    }

    @Test
    public void testCaptureArgumentsFromInvocationWithVarargs() {
        // given
        Object mock = mock(Object.class);
        Method method = findMethod(MethodTestClass.class, "testMethodWithVarargs", Object[].class, Object[].class);
        Invocation invocation = new InvocationOnMockAdapter(new InvocationOnMock(mock, new MethodTestClass(), method, new Object[]{1, 2, new Object[]{3, 4, 5}}));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.argThat((Matcher) ArgumentMatchers.containsInAnyOrder(ArgumentMatchers.eq(3), ArgumentMatchers.eq(4), ArgumentMatchers.eq(5))));

        // when
        invocationMatcher.captureArgumentsFrom(invocation);

        // then
        for (int i = 0; i < matchers.size(); i++) {
            Matcher matcher = matchers.get(i);
            Object capturedArgument = matcher.getCapture();
            if (i < 2) {
                assertEquals(invocation.getArgumentAt(i), capturedArgument);
            } else {
                List<?> capturedArguments = (List<?>) capturedArgument;
                assertNotNull(capturedArguments);
                assertEquals(3, capturedArguments.size());
                for (int j = 0; j < capturedArguments.size(); j++) {
                    assertEquals(invocation.getRawArguments()[2][j], capturedArguments.get(j));
                }
            }
        }
    }

    private static class MethodTestClass {
        public void testMethod(int a, int b, int c) {
            // empty body
        }

        public void testMethodWithVarargs(int a, Object... args) {
            // empty body
        }
    }
}