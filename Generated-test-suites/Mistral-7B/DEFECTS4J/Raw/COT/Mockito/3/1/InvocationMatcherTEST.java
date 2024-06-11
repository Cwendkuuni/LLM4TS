import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.ArgumentsProcessor;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Location;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyList() {
        Invocation invocation = new MockitoTestHelper().createMock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertMatchersEmpty(invocationMatcher);
    }

    @Test
    public void testConstructorWithInvocationAndListOfMatchers() {
        Invocation invocation = new MockitoTestHelper().createMock(Invocation.class);
        Matcher matcher1 = new ArgumentsMatcher("arg1");
        Matcher matcher2 = new ArgumentsMatcher("arg2");
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertMatchersNotEmpty(invocationMatcher);
        assertMatchers(invocationMatcher, matcher1, matcher2);
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = new MockitoTestHelper().createMock(Invocation.class);
        Method method = MockitoTestHelper.createMockMethod();
        when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method returnedMethod = invocationMatcher.getMethod();

        assertEquals(method, returnedMethod);
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = new MockitoTestHelper().createMock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation returnedInvocation = invocationMatcher.getInvocation();

        assertEquals(invocation, returnedInvocation);
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = new MockitoTestHelper().createMock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(new ArgumentsMatcher("arg1"), new ArgumentsMatcher("arg2"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> returnedMatchers = invocationMatcher.getMatchers();

        assertEquals(matchers, returnedMatchers);
    }

    @Test
    public void testToString() {
        Invocation invocation = new MockitoTestHelper().createMock(Invocation.class);
        Matcher matcher1 = new ArgumentsMatcher("arg1");
        Matcher matcher2 = new ArgumentsMatcher("arg2");
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String toString = invocationMatcher.toString();

        String expected = "InvocationMatcher:\n" +
                "\tinvocation:\n" +
                "\t\tInvocation:\n" +
                "\t\t\tmock: MockitoMock\n" +
                "\t\t\tmethod:\n" +
                "\t\t\t\tname: <method name>\n" +
                "\t\t\targuments:\n" +
                "\t\t\t\t[ArgumentsMatcher: arg1, ArgumentsMatcher: arg2]\n";
        String pattern = new PrintSettings().printPattern();

        assertTrue(toString.matches(pattern));
    }

    @Test
    public void testMatches() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(MockitoTestHelper.createMock(Invocation.class), Arrays.asList(new ArgumentsMatcher("arg1")));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(MockitoTestHelper.createMock(Invocation.class), Arrays.asList(new ArgumentsMatcher("arg2")));
        Invocation invocation = MockitoTestHelper.createMock(Invocation.class);

        when(invocation.getMock()).thenReturn(MockitoTestHelper.createMock(Object.class));
        when(invocation.getMethod()).thenReturn(MockitoTestHelper.createMockMethod());

        when(invocation.getArguments()).thenReturn(new Object[0]);

        assertTrue(invocationMatcher1.matches(invocation));
        assertFalse(invocationMatcher1.matches(invocationMatcher2));
        assertFalse(invocationMatcher2.matches(invocationMatcher1));
        assertFalse(invocationMatcher1.matches(new Object()));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(MockitoTestHelper.createMock(Invocation.class), Arrays.asList(new ArgumentsMatcher("arg1")));

        Object[] actualArgs = new Object[0];
        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchThrowsException() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(MockitoTestHelper.createMock(Invocation.class), Arrays.asList(new ArgumentsMatcher("arg1")));

        Object[] actualArgs = new Object[]{"exception"};
        invocationMatcher.safelyArgumentsMatch(actualArgs);
    }

    @Test
    public void testHasSimilarMethod() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(MockitoTestHelper.createMock(Invocation.class), Arrays.asList(new ArgumentsMatcher("arg1")));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(MockitoTestHelper.createMock(Invocation.class), Arrays.asList(new ArgumentsMatcher("arg2")));
        Invocation invocation = MockitoTestHelper.createMock(Invocation.class);

        when(invocation.getMock()).thenReturn(MockitoTestHelper.createMock(Object.class));
        when(invocation.getMethod()).thenReturn(MockitoTestHelper.createMockMethod());

        Method method1 = MockitoTestHelper.createMockMethod();
        Method method2 = MockitoTestHelper.createMockMethod();

        when(method1.getName()).thenReturn("method1");
        when(method2.getName()).thenReturn("method2");

        when(method1.getParameterTypes()).thenReturn(new Class[]{String.class, int.class});
        when(method2.getParameterTypes()).thenReturn(new Class[]{int.class, String.class});

        when(invocation.getMethod()).thenReturn(method1);
        when(invocationMatcher2.getMethod()).thenReturn(method2);

        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethod() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(MockitoTestHelper.createMock(Invocation.class), Arrays.asList(new ArgumentsMatcher("arg1")));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(MockitoTestHelper.createMock(Invocation.class), Arrays.asList(new ArgumentsMatcher("arg1")));
        Invocation invocation = MockitoTestHelper.createMock(Invocation.class);

        when(invocation.getMock()).thenReturn(MockitoTestHelper.createMock(Object.class));
        when(invocation.getMethod()).thenReturn(MockitoTestHelper.createMockMethod());

        Method method1 = MockitoTestHelper.createMockMethod();
        Method method2 = MockitoTestHelper.createMockMethod();

        when(method1.getName()).thenReturn("method1");
        when(method2.getName()).thenReturn("method1");

        when(method1.getParameterTypes()).thenReturn(new Class[]{String.class, int.class});
        when(method2.getParameterTypes()).thenReturn(new Class[]{int.class, String.class});

        when(invocation.getMethod()).thenReturn(method1);
        when(invocationMatcher2.getMethod()).thenReturn(method2);

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testGetLocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(MockitoTestHelper.createMock(Invocation.class), Arrays.asList(new ArgumentsMatcher("arg1")));
        Location location = MockitoTestHelper.createMock(Location.class);

        when(invocationMatcher.getLocation()).thenReturn(location);

        Location returnedLocation = invocationMatcher.getLocation();

        assertEquals(location, returnedLocation);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(MockitoTestHelper.createMock(Invocation.class));
        Invocation invocation = MockitoTestHelper.createMock(Invocation.class);

        Matcher matcher = Mockito.mock(CapturesArguments.class);

        when(invocation.getArgumentAt(Mockito.anyInt(), Mockito.eq(Object.class))).thenReturn("arg");

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(matcher).captureFrom("arg");
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(MockitoTestHelper.createMock(Invocation.class), MockitoTestHelper.createMock(Invocation.class));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, invocationMatchers.size());

        for (InvocationMatcher invocationMatcher : invocationMatchers) {
            assertMatchersEmpty(invocationMatcher);
        }
    }

    private void assertMatchersEmpty(InvocationMatcher invocationMatcher) {
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertEquals(0, matchers.size());
    }

    private void assertMatchers(InvocationMatcher invocationMatcher, Matcher... matchers) {
        List<Matcher> matchersList = invocationMatcher.getMatchers();

        for (Matcher matcher : matchers) {
            assertEquals(matcher, matchersList.get(0));
            matchersList.remove(0);
        }

        assertEquals(0, matchersList.size());
    }

    private static class ArgumentsMatcher implements Matcher<Object[]>, CapturesArguments {
        private final String argumentName;

        public ArgumentsMatcher(String argumentName) {
            this.argumentName = argumentName;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("arguments[");
            description.appendValue(argumentName);
            description.appendText("]");
        }

        @Override
        public boolean matches(Object[] actual) {
            return Arrays.equals(actual, new Object[]{});
        }

        @Override
        public void captureFrom(Invocation invocation) {
            verify(this).matches(invocation.getArguments());
        }
    }

    private static class MockitoTestHelper {
        public MockitoMock createMock(Class clazz) {
            return Mockito.mock(clazz);
        }

        public Method createMockMethod() {
            return Mockito.mock(Method.class);
        }
    }
}