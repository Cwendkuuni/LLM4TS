import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Object mock;

    @Before
    public void setup() {
        mock = mock(TestClass.class);
        invocationMatcher = new InvocationMatcher(new InvocationOnMock(mock, new MethodTestClass(), new Object[0]), Collections.emptyList());
    }

    @Test
    public void testGetMethod() {
        Method method = invocationMatcher.getMethod();
        assertThat(method.getName(), is("methodName"));
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = invocationMatcher.getInvocation();
        assertThat(invocation.getMock(), sameInstance(mock));
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString());
        InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(new InvocationOnMock(mock, new MethodTestClass(), new Object[0]), matchers);
        List<Matcher> matchersFromInvocationMatcher = invocationMatcherWithMatchers.getMatchers();
        assertThat(matchersFromInvocationMatcher, sameInstance(matchers));
    }

    @Test
    public void testToString() {
        String expected = "InvocationMatcher(methodName())";
        String actual = invocationMatcher.toString();
        assertThat(actual, is(expected));
    }

    @Test(expected = AssertionError.class)
    public void testMatchesWithDifferentMock() {
        InvocationMatcher invocationMatcherForMockA = new InvocationMatcher(new InvocationOnMock(mockA, new MethodTestClass(), new Object[0]), Collections.emptyList());
        InvocationMatcher invocationMatcherForMockB = new InvocationMatcher(new InvocationOnMock(mockB, new MethodTestClass(), new Object[0]), Collections.emptyList());
        invocationMatcherForMockA.matches(invocationMatcherForMockB.getInvocation());
    }

    @Test(expected = AssertionError.class)
    public void testMatchesWithDifferentMethod() {
        InvocationMatcher invocationMatcherForMethodA = new InvocationMatcher(new InvocationOnMock(mock, new MethodTestClassA(), new Object[0]), Collections.emptyList());
        InvocationMatcher invocationMatcherForMethodB = new InvocationMatcher(new InvocationOnMock(mock, new MethodTestClassB(), new Object[0]), Collections.emptyList());
        invocationMatcherForMethodA.matches(invocationMatcherForMethodB.getInvocation());
    }

    @Test
    public void testMatchesWithUnverifiedInvocation() {
        InvocationMatcher invocationMatcherForMockAndMethod = new InvocationMatcher(new InvocationOnMock(mock, new MethodTestClass(), new Object[0]), Collections.emptyList());
        InvocationOnMock unverifiedInvocation = new InvocationOnMock(mock, new MethodTestClass(), new Object[0]);
        unverifiedInvocation.reset();
        boolean matches = invocationMatcherForMockAndMethod.matches(unverifiedInvocation);
        assertTrue(matches);
    }

    @Test
    public void testMatchesWithSameMethodAndArguments() {
        InvocationMatcher invocationMatcherForMockAndMethodAndArguments = new InvocationMatcher(new InvocationOnMock(mock, new MethodTestClass(), new Object[]{1, "test"}), Collections.emptyList());
        InvocationMatcher invocationMatcherForMockAndMethodAndArguments2 = new InvocationMatcher(new InvocationOnMock(mock, new MethodTestClass(), new Object[]{1, "test"}), Collections.emptyList());
        boolean matches = invocationMatcherForMockAndMethodAndArguments.matches(invocationMatcherForMockAndMethodAndArguments2.getInvocation());
        assertTrue(matches);
    }

    @Test
    public void testMatchesWithDifferentArguments() {
        InvocationMatcher invocationMatcherForMockAndMethodAndArguments1 = new InvocationMatcher(new InvocationOnMock(mock, new MethodTestClass(), new Object[]{1, "test"}), Collections.emptyList());
        InvocationMatcher invocationMatcherForMockAndMethodAndArguments2 = new InvocationMatcher(new InvocationOnMock(mock, new MethodTestClass(), new Object[]{2, "test2"}), Collections.emptyList());
        boolean matches = invocationMatcherForMockAndMethodAndArguments1.matches(invocationMatcherForMockAndMethodAndArguments2.getInvocation());
        assertFalse(matches);
    }

    @Test
    public void testHasSimilarMethod() {
        InvocationMatcher invocationMatcherForMockAndMethod1 = new InvocationMatcher(new InvocationOnMock(mock, new MethodTestClass(), new Object[0]), Collections.emptyList());
        InvocationMatcher invocationMatcherForMockAndMethod2 = new InvocationMatcher(new InvocationOnMock(mock, new MethodTestClass(), new Object[0]), Collections.emptyList());
        boolean hasSimilarMethod = invocationMatcherForMockAndMethod1.hasSimilarMethod(invocationMatcherForMockAndMethod2.getInvocation());
        assertTrue(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethods() {
        InvocationMatcher invocationMatcherForMockAndMethod1 = new InvocationMatcher(new InvocationOnMock(mock, new MethodTestClass(), new Object[0]), Collections.emptyList());
        InvocationMatcher invocationMatcherForMockAndMethod2 = new InvocationMatcher(new InvocationOnMock(mock, new MethodTestClass2(), new Object[0]), Collections.emptyList());
        boolean hasSimilarMethod = invocationMatcherForMockAndMethod1.hasSimilarMethod(invocationMatcherForMockAndMethod2.getInvocation());
        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString());
        InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(new InvocationOnMock(mock, new MethodTestClass(), new Object[0]), matchers);
        Object[] mockArguments = new Object[]{1, "test"};
        InvocationOnMock invocationWithArguments = new InvocationOnMock(mock, new MethodTestClass(), mockArguments);
        invocationMatcherWithMatchers.captureArgumentsFrom(invocationWithArguments);
        for (Matcher matcher : matchers) {
            if (matcher instanceof ArgumentCaptor) {
                ArgumentCaptor argumentCaptor = (ArgumentCaptor) matcher;
                Object capturedArgument = argumentCaptor.getValue();
                if (matcher.constraintClass().isInstance(mockArguments[0])) {
                    assertThat(capturedArgument, is(mockArguments[0]));
                } else if (matcher.constraintClass().isInstance(mockArguments[1])) {
                    assertThat(capturedArgument, is(mockArguments[1]));
                }
            }
        }
    }

    @Test
    public void testCreateFromInvocations() {
        List<Invocation> invocations = Arrays.asList(new InvocationOnMock(mock, new MethodTestClass(), new Object[]{1, "test"}), new InvocationOnMock(mock, new MethodTestClass(), new Object[]{2, "test2"}));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        InvocationMatcher invocationMatcherForFirstInvocation = invocationMatchers.get(0);
        InvocationMatcher invocationMatcherForSecondInvocation = invocationMatchers.get(1);
        Invocation invocationForFirstInvocation = invocationMatcherForFirstInvocation.getInvocation();
        Invocation invocationForSecondInvocation = invocationMatcherForSecondInvocation.getInvocation();
        assertTrue(invocationMatcherForFirstInvocation.matches(invocationForFirstInvocation));
        assertTrue(invocationMatcherForSecondInvocation.matches(invocationForSecondInvocation));
    }

    private static class MethodTestClass {
        public void methodName(int arg1, String arg2) {
            // Empty method implementation
        }
    }

    private static class MethodTestClassA {
        public void methodNameA(int arg1, String arg2) {
            // Empty method implementation
        }
    }

    private static class MethodTestClassB {
        public void methodNameB(String arg1, int arg2) {
            // Empty method implementation
        }
    }

    private static class MethodTestClass2 {
        public void methodName2(int arg1, String arg2, int arg3) {
            // Empty method implementation
        }
    }
}

class ArgumentsComparator {
    public boolean argumentsMatch(InvocationMatcher expected, InvocationMatcher actual) {
        return argumentsMatch(expected, actual.getInvocation().getArguments());
    }

    public boolean argumentsMatch(InvocationMatcher expected, Object[] actualArguments) {
        return Arrays.deepEquals(expected.getInvocation().getArguments(), actualArguments);
    }
}

class TestClass {
    public void methodName(int arg1, String arg2) {
        // Empty method implementation
    }
}