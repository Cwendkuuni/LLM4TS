import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.InvocationOnMockImpl;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.util.collections.Arrays;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private InvocationOnMock invocationOnMock;
    private Method method;

    @Before
    public void setup() {
        method = Mockito.method(String.class, String.class);
        invocationOnMock = new InvocationOnMockImpl(method, new Location(), new Object[0]);
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndEmptyListOfMatchers() {
        invocationMatcher = new InvocationMatcher(invocationOnMock);
        assertNotNull(invocationMatcher);
        assertTrue(invocationMatcher.getMatchers().isEmpty());
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndListOfMatchers() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.isA(String.class), ArgumentMatchers.is(123));
        invocationMatcher = new InvocationMatcher(invocationOnMock, matchers);
        assertNotNull(invocationMatcher);
        assertFalse(invocationMatcher.getMatchers().isEmpty());
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetMethod() {
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        assertEquals(invocationOnMock, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.isA(String.class), ArgumentMatchers.is(123));
        invocationMatcher = new InvocationMatcher(invocationOnMock, matchers);
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.isA(String.class), ArgumentMatchers.is(123));
        invocationMatcher = new InvocationMatcher(invocationOnMock, matchers);
        String expected = String.format("InvocationMatcher[%s]", invocationOnMock.toString());
        String actual = invocationMatcher.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.isA(String.class), ArgumentMatchers.is(123));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocationOnMock, matchers);
        InvocationOnMock invocationOnMock2 = Mockito.mock(String.class);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocationOnMock2);
        assertTrue(invocationMatcher1.matches(invocationOnMock));
        assertFalse(invocationMatcher1.matches(invocationOnMock2));

        InvocationMatcher invocationMatcher3 = new InvocationMatcher(invocationOnMock, matchers);
        InvocationMatcher invocationMatcher4 = new InvocationMatcher(invocationOnMock, matchers);
        assertTrue(invocationMatcher3.matches(invocationMatcher4));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.isA(String.class), ArgumentMatchers.is(123));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationOnMock, matchers);
        Object[] arguments = new Object[]{1L, "test"};
        assertTrue(invocationMatcher.safelyArgumentsMatch(arguments));
    }

    @Test
    public void testHasSimilarMethod() {
        String methodName1 = "testMethod";
        String methodName2 = "testMethod";
        String methodName3 = "testOtherMethod";       
        
        Method method1 = Mockito.method(String.class, String.class);
        Method method2 = Mockito.method(String.class, String.class);
        Method method3 = Mockito.method(Integer.class, String.class);

        InvocationOnMock invocationOnMock1 = new InvocationOnMockImpl(method1, new Location(), new Object[0]);
        InvocationOnMock invocationOnMock2 = new InvocationOnMockImpl(method2, new Location(), new Object[0]);
        InvocationOnMock invocationOnMock3 = new InvocationOnMockImpl(method3, new Location(), new Object[0]);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocationOnMock1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocationOnMock2);
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(invocationOnMock3);

        //same method, same mock, unverified
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher1));
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));

        //different method names
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher3));

        //different mocks
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher3));

        //same method name, different arguments
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher3));
    }

    @Test(expected = AssertionError.class)
    public void testHasSameMethodWithDifferentInvocations() {
        Method method1 = Mockito.method(String.class, String.class);
        Method method2 = Mockito.method(Integer.class, String.class);

        InvocationOnMock invocationOnMock1 = new InvocationOnMockImpl(method1, new Location(), new Object[]{1L, "test"});
        InvocationOnMock invocationOnMock2 = new InvocationOnMockImpl(method2, new Location(), new Object[]{2L, "test"});

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocationOnMock1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocationOnMock2);
        invocationMatcher1.hasSameMethod(invocationMatcher2);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        List<Matcher> matchers = Arrays.asList(new ArgumentsMatcher("1"), new ArgumentsMatcher("2"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationOnMock, matchers);

        Object[] arguments = new Object[]{1L, "test"};
        invocationMatcher.captureArgumentsFrom(invocationOnMock);

        List<Matcher> capturedMatchers = invocationMatcher.getMatchers();
        List<Matcher> expectedMatchers = Arrays.asList(new ArgumentsMatcher(Arrays.toString(arguments)).and(instanceOf(String.class)));
        assertEquals(expectedMatchers, capturedMatchers);
    }

    @Test(expected = AssertionError.class)
    public void testMatchesWithDifferentMocks() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.isA(String.class), ArgumentMatchers.is(123));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(Mockito.mock(String.class), matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(Mockito.mock(Integer.class), matchers);
        invocationMatcher1.matches(invocationMatcher2);
    }

    @Test(expected = AssertionError.class)
    public void testMatchesWithDifferentMethods() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.isA(String.class), ArgumentMatchers.is(123));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(Mockito.mock(String.class), mockMethod1());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(Mockito.mock(String.class), mockMethod2());
        invocationMatcher1.matches(invocationMatcher2);
    }

    private Method mockMethod1() {
        return Mockito.spy(Mockito.method(String.class, String.class));
    }

    private Method mockMethod2() {
        return Mockito.spy(Mockito.method(Integer.class, String.class));
    }

    private static class ArgumentsMatcher implements Matcher<Object[]> {

        private final String expected;

        public ArgumentsMatcher(String expected) {
            this.expected = expected;
        }

        @Override
        public boolean matches(Object[] arguments) {
            String actual = Arrays.toString(arguments);
            return expected.equals(actual);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("expected: ").appendValue(expected);
        }
    }
}