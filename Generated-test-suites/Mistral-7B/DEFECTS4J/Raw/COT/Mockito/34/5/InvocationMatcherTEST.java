import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationOnMockWithMatcher;
import org.mockito.invocation.InvocationOnMockArguments;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(org.mockito.runners.JUnit4.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndEmptyMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocation.getMethod();
        assertTrue(invocationMatcher.matches(new InvocationOnMockWithMatcher(invocation, method, new Object[0])));
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(Mockito.isNull()), Mockito.argThat(Mockito.isInstanceOf(String.class)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Method method = invocation.getMethod();

        Object[] arguments = new Object[]{null, "test"};
        InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
        InvocationOnMockArguments invocationOnMockArguments = new InvocationOnMockArguments(invocationOnMock, method, arguments);

        invocationMatcher.captureArgumentsFrom(invocationOnMockArguments);

        assertTrue(invocationMatcher.matches(invocationOnMockArguments));
        assertTrue(matchers.get(0).matches(arguments[0]));
        assertTrue(matchers.get(1).matches(arguments[1]));
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(Mockito.isNull()), Mockito.argThat(Mockito.isInstanceOf(String.class)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(Mockito.isNull()), Mockito.argThat(Mockito.isInstanceOf(String.class)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String expected = "InvocationMatcher[this = InvocationImpl@6f14, method = MethodImpl@6ab1, arguments = [], matchers = [null, java.lang.String]]";
        String actual = invocationMatcher.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(Mockito.isNull()), Mockito.argThat(Mockito.isInstanceOf(String.class)));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers);
        Method method = Mockito.mock(Method.class);

        Mockito.when(invocation1.getMethod()).thenReturn(method);
        Mockito.when(invocation2.getMethod()).thenReturn(method);

        assertTrue(invocationMatcher1.matches(invocationMatcher1));
        assertTrue(invocationMatcher1.matches(invocationMatcher2));
        assertTrue(invocationMatcher2.matches(invocationMatcher1));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(Mockito.isNull()), Mockito.argThat(Mockito.isInstanceOf(String.class)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Object[] arguments1 = new Object[]{null, "test"};
        Object[] arguments2 = new Object[]{null, 1};

        assertFalse(invocationMatcher.safelyArgumentsMatch(arguments2));
        assertTrue(invocationMatcher.safelyArgumentsMatch(arguments1));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchWithThrowable() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(Mockito.isNull()));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Object[] arguments = new Object[]{new Exception("test")};

        invocationMatcher.safelyArgumentsMatch(arguments);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(Mockito.isNull()), Mockito.argThat(Mockito.isInstanceOf(String.class)));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);

        Mockito.when(invocation1.getMethod()).thenReturn(method1);
        Mockito.when(invocation2.getMethod()).thenReturn(method2);
        Mockito.when(method1.getName()).thenReturn("testMethod");
        Mockito.when(method2.getName()).thenReturn("testMethod");

        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(Mockito.isNull()), Mockito.argThat(Mockito.isInstanceOf(String.class)));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers);
        Method method = Mockito.mock(Method.class);

        Mockito.when(invocation1.getMethod()).thenReturn(method);
        Mockito.when(invocation2.getMethod()).thenReturn(method);

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(Mockito.isNull()), Mockito.argThat(Mockito.isInstanceOf(String.class)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Object[] arguments = new Object[]{null, "test"};

        InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
        InvocationOnMockArguments invocationOnMockArguments = new InvocationOnMockArguments(invocationOnMock, Mockito.mock(Method.class), arguments);

        invocationMatcher.captureArgumentsFrom(invocationOnMockArguments);

        assertEquals(arguments[0], matchers.get(0).getValue());
        assertEquals(arguments[1], matchers.get(1).getValue());
    }

    @Test
    public void testHasSameMethodWithOverloadedMethods() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(Mockito.isNull()), Mockito.argThat(Mockito.isInstanceOf(String.class)));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);

        Mockito.when(invocation1.getMethod()).thenReturn(method1);
        Mockito.when(invocation2.getMethod()).thenReturn(method2);

        Mockito.when(method1.getName()).thenReturn("testMethod");
        Mockito.when(method2.getName()).thenReturn("testMethod");

        Mockito.when(method1.getParameterTypes()).thenReturn(new Class[]{String.class});
        Mockito.when(method2.getParameterTypes()).thenReturn(new Class[]{Integer.class, String.class});

        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethodWithSameArguments() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(Mockito.isNull()), Mockito.argThat(Mockito.isInstanceOf(String.class)));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers);
        Method method = Mockito.mock(Method.class);

        Mockito.when(invocation1.getMethod()).thenReturn(method);
        Mockito.when(invocation2.getMethod()).thenReturn(method);

        Object[] arguments1 = new Object[]{null, "test"};
        Object[] arguments2 = new Object[]{arguments1};

        InvocationOnMock invocationOnMock1 = Mockito.mock(InvocationOnMock.class);
        InvocationOnMock invocationOnMock2 = Mockito.mock(InvocationOnMock.class);

        InvocationOnMockArguments invocationOnMockArguments1 = new InvocationOnMockArguments(invocationOnMock1, method, arguments1);
        InvocationOnMockArguments invocationOnMockArguments2 = new InvocationOnMockArguments(invocationOnMock2, method, arguments2);

        invocationMatcher1.captureArgumentsFrom(invocationOnMockArguments1);
        invocationMatcher2.captureArgumentsFrom(invocationOnMockArguments2);

        assertTrue(invocationMatcher1.matches(invocationMatcher2));
        assertTrue(invocationMatcher2.matches(invocationMatcher1));
    }
}