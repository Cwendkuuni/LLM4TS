import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_withConstructorWithInvocationAndMatchers_shouldCreateMatcher() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = Mockito.mock(Method.class);
        assertEquals("Should set the method correctly", method, invocationMatcher.getMethod());
    }

    @Test
    public void testInvocationMatcher_withConstructorWithInvocation_shouldCreateMatcherWithEmptyMatchersList() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = Mockito.mock(Method.class);
        assertEquals("Should set the method correctly", method, invocationMatcher.getMethod());
        assertEmpty("Should have empty matchers list", invocationMatcher.getMatchers());
    }

    @Test
    public void testMatches_shouldReturnTrueWhenInvocationsHaveSameMethodAndSameArguments() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);

        Mockito.when(invocation1.getMethod()).thenReturn(method);
        Mockito.when(invocation2.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        Object[] arguments1 = new Object[]{1, "test"};
        Object[] arguments2 = new Object[]{1, "test"};

        Mockito.when(invocation1.getArguments()).thenReturn(arguments1);
        Mockito.when(invocation2.getArguments()).thenReturn(arguments2);

        Invocation mock = Mockito.mock(Object.class);
        Mockito.when(invocation1.getMock()).thenReturn(mock);
        Mockito.when(invocation2.getMock()).thenReturn(mock);

        assertTrue("Should return true when invocations have the same method and arguments", invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatches_shouldReturnFalseWhenInvocationsHaveDifferentMethods() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);

        Mockito.when(invocation1.getMethod()).thenReturn(method1);
        Mockito.when(invocation2.getMethod()).thenReturn(method2);
        Mockito.when(method1.getName()).thenReturn("testMethod1");
        Mockito.when(method2.getName()).thenReturn("testMethod2");

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        assertFalse("Should return false when invocations have different methods", invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatches_shouldReturnFalseWhenInvocationsHaveDifferentMocks() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);

        Mockito.when(invocation1.getMethod()).thenReturn(method);
        Mockito.when(invocation2.getMethod()).thenReturn(method);

        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);

        Mockito.when(invocation1.getMock()).thenReturn(mock1);
        Mockito.when(invocation2.getMock()).thenReturn(mock2);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        assertFalse("Should return false when invocations have different mocks", invocationMatcher1.matches(invocationMatcher2));
    }

    @Test(expected = AssertionError.class)
    public void testMatches_shouldThrowAnAssertionErrorWhenInvocationsHaveOverloadedMethodsButDifferentArguments() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);

        Mockito.when(invocation1.getMethod()).thenReturn(method);
        Mockito.when(invocation2.getMethod()).thenReturn(method);

        Object[] arguments1 = new Object[]{1};
        Object[] arguments2 = new Object[]{2};

        Mockito.when(invocation1.getArguments()).thenReturn(arguments1);
        Mockito.when(invocation2.getArguments()).thenReturn(arguments2);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        Invocation mock = Mockito.mock(Object.class);
        Mockito.when(invocation1.getMock()).thenReturn(mock);
        Mockito.when(invocation2.getMock()).thenReturn(mock);

        invocationMatcher1.matches(invocationMatcher2);
    }

    @Test
    public void testHasSimilarMethod_shouldReturnTrueWhenInvocationsHaveSameMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);

        Mockito.when(invocation1.getMethod()).thenReturn(method);
        Mockito.when(invocation2.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        assertTrue("Should return true when invocations have the same method", invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethod_shouldReturnFalseWhenInvocationsHaveDifferentMethods() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);

        Mockito.when(invocation1.getMethod()).thenReturn(method1);
        Mockito.when(invocation2.getMethod()).thenReturn(method2);
        Mockito.when(method1.getName()).thenReturn("testMethod1");
        Mockito.when(method2.getName()).thenReturn("testMethod2");

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        assertFalse("Should return false when invocations have different methods", invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testToString_shouldReturnCorrectStringRepresentation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Method method = Mockito.mock(Method.class);

        Mockito.when(invocation.getMethod()).thenReturn(method);

        PrintSettings printSettings = new PrintSettings();
        String expected = invocation.toString(matchers, printSettings);

        String actual = invocationMatcher.toString();

        assertEquals("Should return the same string representation as the Invocation object", expected, actual);
    }

    @Test
    public void testCaptureArgumentsFrom_shouldCaptureArgumentsFromInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation i = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{1, "test"};

        Mockito.when(invocation.getArguments()).thenReturn(arguments);

        CapturesArguments captureArguments = Mockito.mock(CapturesArguments.class);

        for (int i = 0; i < arguments.length; i++) {
            Mockito.when(captureArguments.captureFrom((Object) Mockito.anyObject())).thenReturn(arguments[i]);
            invocationMatcher.captureArgumentsFrom(i);
        }

        for (Matcher matcher : invocationMatcher.getMatchers()) {
            if (matcher instanceof CapturesArguments) {
                CapturesArguments capturedArguments = (CapturesArguments) matcher;
                Object capturedArgument = capturedArguments.getValue();
                assertEquals("Should capture the correct argument", arguments[i], capturedArgument);
            }
        }
    }
}