import org.junit.Test;
import org.mockito.invocation.Invocation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class InvocationMatcherTest {

    private class TestInvocation implements Invocation {
        private Object mock;
        private Method method;
        private Object[] arguments;

        public TestInvocation(Object mock, Method method, Object[] arguments) {
            this.mock = mock;
            this.method = method;
            this.arguments = arguments;
        }

        @Override
        public Object getMock() {
            return mock;
        }

        @Override
        public Method getMethod() {
            return method;
        }

        @Override
        public Object[] getArguments() {
            return arguments;
        }

        @Override
        public <T> T getArgument(int index) {
            return (T) arguments[index];
        }

        @Override
        public <T> T getArgument(int index, Class<T> type) {
            Object value = arguments[index];
            if (value != null && !type.isAssignableFrom(value.getClass())) {
                fail("Invalid argument type");
            }
            return (T) value;
        }

        @Override
        public <T> T getArgument(String name) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> T getArgument(String name, Class<T> type) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Class<?>[] getRawParameterTypes() {
            return method.getParameterTypes();
        }

        @Override
        public void markVerified() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isVerified() {
            throw new UnsupportedOperationException();
        }

        @Override
        public InvocationMatcher withMatcher(int position, Matcher<?> matcher) {
            throw new UnsupportedOperationException();
        }

        @Override
        public InvocationMatcher withMatchers(List<Matcher<?>> matchers) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Location getLocation() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object callRealMethod() {
            throw new UnsupportedOperationException();
        }

        @Override
        public AtomicBoolean isVerifiedInOrder() {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        Invocation invocation = new TestInvocation(null, null, null);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(null);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertSame(invocation, invocationMatcher.getInvocation());
        assertSame(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocation() {
        Invocation invocation = new TestInvocation(null, null, null);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertSame(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() throws NoSuchMethodException {
        Method method = getClass().getMethod("testGetMethod");

        Invocation invocation = new TestInvocation(null, method, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertSame(method, invocationMatcher.getMethod());
    }

    @Test
    public void testMatches() {
        Invocation invocation = new TestInvocation(null, null, null);
        Invocation actualInvocation = new TestInvocation(null, null, null);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.matches(actualInvocation);

        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = new TestInvocation(null, null, null);
        Invocation candidateInvocation = new TestInvocation(null, null, null);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidateInvocation);

        assertTrue(result);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = new TestInvocation(null, null, null);
        Invocation candidateInvocation = new TestInvocation(null, null, null);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSameMethod(candidateInvocation);

        assertTrue(result);
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new TestInvocation(null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertNull(invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new TestInvocation(null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        try {
            invocationMatcher.captureArgumentsFrom(invocation);
            fail("Exception should have been thrown");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(new TestInvocation(null, null, null));

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(1, invocationMatchers.size());
    }
}

