import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMockAdapter;
import org.mockito.internal.invocation.InvocationMatcher;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@RunWith(org.mockito.runners.JUnit4.class)
public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatcherList() {
        Invocation mockInvocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        Method method = mockInvocation.getMethod();

        invocationMatcher.assertEquals(method, invocationMatcher.getMethod());
        invocationMatcher.assertEquals(mockInvocation, invocationMatcher.getInvocation());
        invocationMatcher.assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocationAndMatcherList() {
        Invocation mockInvocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = new LinkedList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, matchers);
        Method method = mockInvocation.getMethod();

        invocationMatcher.assertEquals(method, invocationMatcher.getMethod());
        invocationMatcher.assertEquals(mockInvocation, invocationMatcher.getInvocation());
        invocationMatcher.assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        InvocationOnMock mockInvocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        Method method = mockInvocation.getMethod();

        invocationMatcher.assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        InvocationOnMock mockInvocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);

        invocationMatcher.assertEquals(mockInvocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        InvocationOnMock mockInvocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = new LinkedList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, matchers);

        invocationMatcher.assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        InvocationOnMock mockInvocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = new LinkedList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, matchers);

        String expected = "InvocationMatcher{invocation=InvocationOnMock{}, matchers=[]}";
        String actual = invocationMatcher.toString();

        invocationMatcher.assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromWithVarargs() {
        InvocationOnMock mockInvocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);

        invocationMatcher.captureArgumentsFrom(mockInvocation);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        InvocationOnMock mockInvocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = new LinkedList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, matchers);

        Object[] rawArguments = new Object[2];
        rawArguments[0] = "foo";
        rawArguments[1] = 42;

        Method method = Mockito.method(String.class, "foo", Integer.TYPE);
        mockInvocation.setRawArguments(rawArguments);
        mockInvocation.setMethod(method);

        for (int i = 0; i < matchers.size(); i++) {
            Matcher matcher = Mockito.mock(Matcher.class);
            matchers.set(i, matcher);

            if (i == 0) {
                Mockito.doReturn("bar").when(matcher).match(Mockito.anyString());
            } else {
                Mockito.doReturn(43).when(matcher).match(Mockito.anyInt());
            }
        }

        invocationMatcher.captureArgumentsFrom(mockInvocation);

        for (int i = 0; i < matchers.size(); i++) {
            Matcher matcher = matchers.get(i);
            Object capturedArgument = null;

            if (i == 0) {
                capturedArgument = mockInvocation.getArgumentAt(0, String.class);
                Mockito.verify(matcher).captureFrom(capturedArgument);
            } else {
                capturedArgument = mockInvocation.getArgumentAt(1, Integer.TYPE);
                Mockito.verify(matcher).captureFrom(capturedArgument);
            }
        }
    }

    @Test
    public void testHasSimilarMethodWithSameMethod() {
        InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
        InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.method(String.class, "testMethod", new Class[0]);
        mockInvocation1.setMethod(method);
        mockInvocation2.setMethod(method);

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        matcher1.assertTrue(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethods() {
        InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
        InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method1 = Mockito.method(String.class, "testMethod1", new Class[0]);
        Method method2 = Mockito.method(String.class, "testMethod2", new Class[0]);
        mockInvocation1.setMethod(method1);
        mockInvocation2.setMethod(method2);

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        matcher1.assertFalse(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testHasSameMethodWithSameMethods() {
        InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
        InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.method(String.class, "testMethod", new Class[0]);
        mockInvocation1.setMethod(method);
        mockInvocation2.setMethod(method);

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        matcher1.assertTrue(matcher1.hasSameMethod(matcher2));
    }

    @Test
    public void testHasSameMethodWithDifferentMethods() {
        InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
        InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method1 = Mockito.method(String.class, "testMethod1", new Class[0]);
        Method method2 = Mockito.method(String.class, "testMethod2", new Class[0]);
        mockInvocation1.setMethod(method1);
        mockInvocation2.setMethod(method2);

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        matcher1.assertFalse(matcher1.hasSameMethod(matcher2));
    }

    @Test
    public void testSafelyArgumentsMatchWithMatchingArguments() {
        InvocationOnMock mockInvocation = Mockito.mock(InvocationOnMock.class);
        Object[] rawArguments = new Object[2];
        rawArguments[0] = "foo";
        rawArguments[1] = 42;

        Method method = Mockito.method(String.class, "testMethod", new Class[0]);
        mockInvocation.setRawArguments(rawArguments);
        mockInvocation.setMethod(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        Object[] actualArguments = new Object[2];
        actualArguments[0] = "foo";
        actualArguments[1] = 42;

        invocationMatcher.assertTrue(invocationMatcher.safelyArgumentsMatch(actualArguments));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchWithMismatchingArguments() {
        InvocationOnMock mockInvocation = Mockito.mock(InvocationOnMock.class);
        Object[] rawArguments = new Object[2];
        rawArguments[0] = "foo";
        rawArguments[1] = 42;

        Method method = Mockito.method(String.class, "testMethod", new Class[0]);
        mockInvocation.setRawArguments(rawArguments);
        mockInvocation.setMethod(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        Object[] actualArguments = new Object[2];
        actualArguments[0] = 42;
        actualArguments[1] = "foo";

        invocationMatcher.assertFalse(invocationMatcher.safelyArgumentsMatch(actualArguments));
    }
}