import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMockImpl;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.Arguments;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_withArguments_matches() {
        // Given
        Object mock = mock(SomeMockClass.class);
        Method method = SomeMockClass.class.getMethod("someMethod", String.class, int.class);
        Invocation invocation = new InvocationOnMockImpl(mock, method, "arg1", "arg2", 1);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("arg1"), ArgumentMatchers.eq(1));
        InvocationMatcher testSubject = new InvocationMatcher(invocation, matchers);

        // When
        Invocation actual = new InvocationOnMockImpl(mock, method, "arg1", "arg2", 1);

        // Then
        assertTrue(testSubject.matches(actual));
    }

    @Test
    public void testInvocationMatcher_withArguments_doesNotMatch() {
        // Given
        Object mock = mock(SomeMockClass.class);
        Method method = SomeMockClass.class.getMethod("someMethod", String.class, int.class);
        Invocation invocation = new InvocationOnMockImpl(mock, method, "arg1", "arg2", 1);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("arg1"), ArgumentMatchers.eq("arg3"));
        InvocationMatcher testSubject = new InvocationMatcher(invocation, matchers);

        // When
        Invocation actual = new InvocationOnMockImpl(mock, method, "arg1", "arg2", 1);

        // Then
        assertFalse(testSubject.matches(actual));
    }

    @Test
    public void testInvocationMatcher_withMismatchedMethods_doesNotMatch() {
        // Given
        Object mock = mock(SomeMockClass.class);
        Method method1 = SomeMockClass.class.getMethod("method1", String.class);
        Method method2 = SomeMockClass.class.getMethod("method2", int.class);
        Invocation invocation1 = new InvocationOnMockImpl(mock, method1, "arg1");
        Invocation invocation2 = new InvocationOnMockImpl(mock, method2, 1);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("arg1"));
        InvocationMatcher testSubject = new InvocationMatcher(invocation1, matchers);

        // When
        Invocation actual = invocation2;

        // Then
        assertFalse(testSubject.matches(actual));
    }

    @Test
    public void testInvocationMatcher_withNullArgMatchers_usesDefaultMatchers() {
        // Given
        Object mock = mock(SomeMockClass.class);
        Method method = SomeMockClass.class.getMethod("someMethod", String.class, int.class);
        Invocation invocation = new InvocationOnMockImpl(mock, method, "arg1", "arg2", 1);
        InvocationMatcher testSubject = new InvocationMatcher(invocation);

        // When
        Invocation actual = new InvocationOnMockImpl(mock, method, "arg1", "arg2", 1);

        // Then
        assertTrue(testSubject.matches(actual));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        // Given
        Object mock = mock(SomeMockClass.class);
        Method method = SomeMockClass.class.getMethod("someMethod", String.class, int.class);
        Invocation invocation = new InvocationOnMockImpl(mock, method, "arg1", "arg2", 1);
        InvocationMatcher testSubject = new InvocationMatcher(invocation);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        // When
        mock.someMethod("expectedArg1", 1);
        testSubject.captureArgumentsFrom(mock);

        // Then
        String capturedArg = stringArgumentCaptor.getValue();
        assertEquals("expectedArg1", capturedArg);
    }

    @Test(expected = AssertionError.class)
    public void testCaptureArgumentsFromInvocation_unmatched() {
        // Given
        Object mock = mock(SomeMockClass.class);
        Method method = SomeMockClass.class.getMethod("someMethod", String.class, int.class);
        Invocation invocation = new InvocationOnMockImpl(mock, method, "arg1", "arg2", 1);
        InvocationMatcher testSubject = new InvocationMatcher(invocation);
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(int.class);

        // When
        mock.someMethod("expectedArg1", 1);
        testSubject.captureArgumentsFrom(mock);

        // Then (expection)
        Integer capturedArg = integerArgumentCaptor.getValue();
        fail("Expected an exception but none was thrown.");
    }

    private static class SomeMockClass {
        public void someMethod(String arg1, int arg2) {}
    }
}