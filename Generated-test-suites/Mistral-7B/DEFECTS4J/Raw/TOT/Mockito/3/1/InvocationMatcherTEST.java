import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockitoCore;
import org.mockito.stub.Stubber;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private MockitoCore mockitoCore;
    private Stubber stubber;
    private Object mockObject;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
        stubber = mockitoCore.stubber();
        mockObject = mock(Object.class);
    }

    @Test
    public void testInvocationMatcher_simpleMethod_noMatchers() {
        Invocation invocation = new InvocationOnMock(mockObject) {
            @Override
            public Method getMethod() {
                return new Method(Object.class.getMethod("voidMethod"));
            }
        };
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation testInvocation = new InvocationOnMock(mockObject) {
            @Override
            public Method getMethod() {
                return new Method(Object.class.getMethod("voidMethod"));
            }
        };

        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher testInvocationMatcher = new InvocationMatcher(testInvocation, matchers);

        assertThat(invocationMatcher.matches(testInvocationMatcher), is(true));
    }

    @Test
    public void testInvocationMatcher_simpleMethod_matchers() {
        Invocation invocation = new InvocationOnMock(mockObject) {
            @Override
            public Method getMethod() {
                return new Method(Object.class.getMethod("voidMethod", int.class));
            }
        };
        Object[] arguments = new Object[]{1};

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.anyInt());
        InvocationMatcher testInvocationMatcher = new InvocationMatcher(invocation, matchers);

        testInvocationMatcher.captureArgumentsFrom(invocation);
        verify(mockObject).voidMethod(1);

        assertThat(invocationMatcher.matches(testInvocationMatcher), is(true));
    }

    @Test
    public void testInvocationMatcher_differentMethod() {
        Invocation invocation = new InvocationOnMock(mockObject) {
            @Override
            public Method getMethod() {
                return new Method(Object.class.getMethod("voidMethodA", int.class));
            }
        };
        Object[] arguments = new Object[]{1};

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.anyInt());
        InvocationMatcher testInvocationMatcher = new InvocationMatcher(invocation, matchers);

        testInvocationMatcher.captureArgumentsFrom(invocation);
        verify(mockObject).voidMethodA(1);

        assertThat(invocationMatcher.matches(testInvocationMatcher), is(false));
    }

    @Test
    public void testInvocationMatcher_sameMethod_differentArguments() {
        Invocation invocation = new InvocationOnMock(mockObject) {
            @Override
            public Method getMethod() {
                return new Method(Object.class.getMethod("voidMethod", int.class));
            }
        };
        Object[] arguments1 = new Object[]{1};
        Object[] arguments2 = new Object[]{2};

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.anyInt());
        InvocationMatcher testInvocationMatcher1 = new InvocationMatcher(invocation, matchers);
        InvocationMatcher testInvocationMatcher2 = new InvocationMatcher(invocation, matchers);

        testInvocationMatcher1.captureArgumentsFrom(invocation);
        testInvocationMatcher2.captureArgumentsFrom(invocation);
        verify(mockObject).voidMethod(1);

        assertThat(invocationMatcher.matches(testInvocationMatcher1), is(true));
        assertThat(invocationMatcher.matches(testInvocationMatcher2), is(false));
    }

    @Test
    public void testInvocationMatcher_methodWithVarArgs() {
        Invocation invocation = new InvocationOnMock(mockObject) {
            @Override
            public Method getMethod() {
                return new Method(Object.class.getMethod("voidMethod", int.class, int[].class));
            }
        };
        Object[] arguments1 = new Object[]{1, new int[]{1, 2}};
        Object[] arguments2 = new Object[]{1, new int[]{2, 3}};

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.anyInt(), ArgumentMatchers.arrayContaining(ArgumentMatchers.anyInt()));
        InvocationMatcher testInvocationMatcher1 = new InvocationMatcher(invocation, matchers);
        InvocationMatcher testInvocationMatcher2 = new InvocationMatcher(invocation, matchers);

        testInvocationMatcher1.captureArgumentsFrom(invocation);
        testInvocationMatcher2.captureArgumentsFrom(invocation);
        verify(mockObject).voidMethod(1, new int[]{1, 2});

        assertThat(invocationMatcher.matches(testInvocationMatcher1), is(true));
        assertThat(invocationMatcher.matches(testInvocationMatcher2), is(false));
    }

    @Test
    public void testInvocationMatcher_sameMethod_sameArguments() {
        Invocation invocation = new InvocationOnMock(mockObject) {
            @Override
            public Method getMethod() {
                return new Method(Object.class.getMethod("voidMethod", int.class));
            }
        };
        Object[] arguments = new Object[]{1};

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.anyInt());
        InvocationMatcher testInvocationMatcher1 = new InvocationMatcher(invocation, matchers);
        InvocationMatcher testInvocationMatcher2 = new InvocationMatcher(invocation, matchers);

        testInvocationMatcher1.captureArgumentsFrom(invocation);
        testInvocationMatcher2.captureArgumentsFrom(invocation);
        verify(mockObject).voidMethod(1);

        InvocationMatcherWithSameArguments matchersMatcher = new InvocationMatcherWithSameArguments(invocation, arguments);
        InvocationMatcher testInvocationMatcherMatcher1 = new InvocationMatcherWithSameArguments(invocation, arguments);
        InvocationMatcher testInvocationMatcherMatcher2 = new InvocationMatcherWithSameArguments(invocation, arguments);

        testInvocationMatcherMatcher1.captureArgumentsFrom(invocation);
        testInvocationMatcherMatcher2.captureArgumentsFrom(invocation);
        verify(mockObject).voidMethod(1);

        assertThat(invocationMatcher.matches(testInvocationMatcher1), is(true));
        assertThat(invocationMatcher.matches(testInvocationMatcher2), is(true));
        assertThat(matchersMatcher.matches(testInvocationMatcherMatcher1), is(true));
        assertThat(matchersMatcher.matches(testInvocationMatcherMatcher2), is(true));
    }

    private static class InvocationMatcherWithSameArguments extends InvocationMatcher {
        private final Object[] arguments;

        public InvocationMatcherWithSameArguments(Invocation invocation, Object[] arguments) {
            super(invocation);
            this.arguments = arguments;
        }

        @Override
        public boolean matches(InvocationMatcher other) {
            InvocationMatcher otherMatcher = (InvocationMatcher) other;
            return super.matches(otherMatcher) && new ArgumentsComparator().argumentsMatch(this, otherMatcher.getInvocation().getArguments()) && Arrays.deepEquals(arguments, otherMatcher.getInvocation().getArguments());
        }

        @Override
        public void captureArgumentsFrom(Invocation invocation) {
            super.captureArgumentsFrom(invocation);
            // No-op
        }
    }
}