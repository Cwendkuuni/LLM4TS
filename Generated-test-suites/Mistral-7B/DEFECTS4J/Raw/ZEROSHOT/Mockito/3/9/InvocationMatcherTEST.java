import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockitoCore;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

    private MockitoCore mockitoCore;
    private InvocationMatcher invocationMatcher;
    private Invocation mockInvocation;
    private Invocation realInvocation;
    private List<Matcher> matchers;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
        mockInvocation = mock(InvocationOnMock.class);
        realInvocation = new InvocationMock();
        matchers = Arrays.asList(ArgumentMatchers.isA(String.class), ArgumentMatchers.isNull());
        invocationMatcher = new InvocationMatcher(mockInvocation, matchers);
    }

    @Test
    public void shouldCreateInvocationMatcherWithEmptyList() {
        InvocationMatcher emptyMatcher = new InvocationMatcher(mockInvocation);
        assertNotNull(emptyMatcher);
        verify(mockitoCore).checkArgument(mockInvocation != null, "invocation cannot be null");
    }

    @Test
    public void shouldCreateInvocationMatcherWithListOfMatchers() {
        InvocationMatcher matcherWithList = new InvocationMatcher(mockInvocation, matchers);
        assertNotNull(matcherWithList);
        verify(mockitoCore).checkArgument(mockInvocation != null, "invocation cannot be null");
        verify(mockitoCore).checkArgument(matchers != null, "matchers cannot be null");
    }

    @Test
    public void shouldReturnSameMethod() {
        InvocationMatcher sameMethodMatcher = new InvocationMatcher(mockInvocation);
        InvocationMatcher sameMethodMatcher2 = new InvocationMatcher(mockInvocation);
        assertTrue(sameMethodMatcher.hasSameMethod(sameMethodMatcher2));
    }

    @Test
    public void shouldReturnFalseWhenDifferentMethods() {
        InvocationMatcher differentMethodMatcher1 = new InvocationMatcher(mockInvocation);
        InvocationMatcher differentMethodMatcher2 = new InvocationMatcher(mockInvocation, new MethodTestMatcher());
        assertFalse(differentMethodMatcher1.hasSameMethod(differentMethodMatcher2));
    }

    @Test
    public void shouldReturnFalseWhenDifferentMocks() {
        InvocationMatcher differentMockMatcher1 = new InvocationMatcher(mockInvocation);
        InvocationMatcher differentMockMatcher2 = new InvocationMatcher(RealInvocationMock.getInstance());
        assertFalse(differentMockMatcher1.hasSameMethod(differentMockMatcher2));
    }

    @Test
    public void shouldReturnFalseWhenInvocationsAreVerified() {
        InvocationMatcher unverifiedMatcher = new InvocationMatcher(mockInvocation);
        InvocationMatcher verifiedMatcher = new InvocationMatcher(realInvocation);
        assertFalse(unverifiedMatcher.hasSameMethod(verifiedMatcher));
    }

    @Test
    public void shouldReturnFalseWhenArgumentsDontMatch() {
        Object[] arguments = new Object[]{1, 2, 3};
        InvocationMatcher matcherWithList = new InvocationMatcher(mockInvocation, matchers);
        InvocationMatcher differentArgumentsMatcher = new InvocationMatcher(mockInvocation, Arrays.asList(ArgumentMatchers.is("abc")));
        assertFalse(matcherWithList.matches(new InvocationMock(new MethodTest(), arguments)));
        assertFalse(differentArgumentsMatcher.matches(new InvocationMock(new MethodTest(), arguments)));
    }

    @Test
    public void shouldReturnTrueWhenInvocationsAreTheSame() {
        InvocationMatcher sameMatcher = new InvocationMatcher(mockInvocation);
        InvocationMatcher sameMatcher2 = new InvocationMatcher(mockInvocation);
        assertTrue(sameMatcher.matches(sameMatcher2));
    }

    @Test
    public void shouldReturnTrueWhenArgumentsMatch() {
        InvocationMatcher matcherWithList = new InvocationMatcher(mockInvocation, matchers);
        InvocationMock invocationMock = new InvocationMock(new MethodTest(), new Object[]{1, "mockito", null});
        assertTrue(matcherWithList.matches(invocationMock));
    }

    @Test
    public void shouldCaptureArguments() {
        InvocationMatcher matcherWithList = new InvocationMatcher(mockInvocation, matchers);
        InvocationMock invocationMock = new InvocationMock(new MethodTest(), new Object[]{1, "mockito", null});
        matcherWithList.captureArgumentsFrom(invocationMock);
        List<Object> capturedArguments = Arrays.asList(matchers.get(0).getRealValue(), matchers.get(1).getRealValue());
        assertThat(matchers.get(0).getCapturedValues(), is(capturedArguments.subList(0, 1)));
        assertThat(matchers.get(1).getCapturedValues(), is(capturedArguments.subList(1, 2)));
    }

    private static class MethodTest {
        public void test() {
        }
    }

    private static class MethodTestMatcher implements Matcher<Method> {
        @Override
        public boolean matches(Method method) {
            return method.getName().equals("test");
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("method with name test");
        }
    }

    private static class InvocationMock implements Invocation {
        private final Method method;
        private final Object[] rawArguments;

        public InvocationMock(Method method, Object[] rawArguments) {
            this.method = method;
            this.rawArguments = rawArguments;
        }

        public Method getMethod() {
            return method;
        }

        public Object[] getArguments() {
            return rawArguments;
        }

        public Object[] getRawArguments() {
            return rawArguments;
        }

        public Object getArgumentAt(int index, Class<?> type) {
            return rawArguments[index];
        }

        public Location getLocation() {
            return new Location() {};
        }

        public boolean isVerified() {
            return false;
        }
    }

    private static class RealInvocationMock extends InvocationMock {
        private static final RealInvocationMock INSTANCE = new RealInvocationMock();

        private RealInvocationMock() {
        }

        public static RealInvocationMock getInstance() {
            return INSTANCE;
        }
    }

    private static class ArgumentMatchersWrapper {
        public static List<Matcher<Object>> argumentsToMatchers(Object[] arguments) {
            return Collections.emptyList();
        }
    }

    private static class ArgumentsComparator {
        public boolean argumentsMatch(InvocationMatcher invocation1, InvocationMatcher invocation2) {
            return true;
        }

        public boolean argumentsMatch(InvocationMatcher invocationMatcher, Object[] invocation) {
            return true;
        }
    }

    private static class ArgumentsProcessor {
        public static List<Matcher<Object>> argumentsToMatchers(Object[] arguments) {
            return null;
        }
    }
}