package org.mockito.internal.invocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.InvocationOnMockImpl;
import org.mockito.matchers.ArgThat;
import org.mockito.mock.MockitoMock;
import org.mockito.reporting.PrintingException;
import org.mockito.verification.VerificationMode;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void shouldCreateInvocationMatcherForSingleMatcher() {
        // given
        Object mock = mock(TestClass.class);
        Method method = TestClass.class.getMethod("methodWithOneArgument", String.class);
        List<Matcher> matchers = Arrays.asList(argThat(new ArgumentMatcher<String>() {
            @Override
            public boolean matches(String arg) {
                return arg.equals("expected");
            }
        }));

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(mock, method), matchers);

        // then
        assertEquals("expected", invocationMatcher.getInvocation().getArguments()[0]);
    }

    @Test
    public void shouldCreateInvocationMatcherForEmptyMatchers() {
        // given
        Object mock = mock(TestClass.class);
        Method method = TestClass.class.getMethod("methodWithNoArguments", (Class[]) null);

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(mock, method));

        // then
        assertNotNull(invocationMatcher);
        assertEquals(0, invocationMatcher.getMatchers().size());
    }

    @Test
    public void shouldCreateInvocationMatcherForMultipleMatchers() {
        // given
        Object mock = mock(TestClass.class);
        Method method = TestClass.class.getMethod("methodWithTwoArguments", String.class, int.class);
        List<Matcher> matchers = Arrays.asList(argThat(new ArgumentMatcher<String>() {
            @Override
            public boolean matches(String arg) {
                return arg.equals("expectedString");
            }
        }), argThat(new ArgumentMatcher<Integer>() {
            @Override
            public boolean matches(Integer arg) {
                return arg.equals(42);
            }
        }));

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(mock, method), matchers);

        // then
        assertEquals(2, invocationMatcher.getMatchers().size());
    }

    @Test
    public void shouldMatchInvocationWithSameMethodAndArguments() {
        // given
        Object mock = mock(TestClass.class);
        Method method = TestClass.class.getMethod("methodWithOneArgument", String.class);
        List<Matcher> matchers = Arrays.asList(argThat(new ArgumentMatcher<String>() {
            @Override
            public boolean matches(String arg) {
                return arg.equals("expected");
            }
        }));

        // when
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new InvocationOnMockImpl(mock, method), matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMockImpl(mock, method), matchers);

        // then
        assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void shouldNotMatchInvocationWithDifferentMethod() {
        // given
        Object mock = mock(TestClass.class);
        Method method1 = TestClass.class.getMethod("methodWithOneArgumentString", String.class);
        Method method2 = TestClass.class.getMethod("methodWithOneArgumentInt", int.class);
        List<Matcher> matchers = Arrays.asList(argThat(new ArgumentMatcher<Object>() {
            @Override
            public boolean matches(Object arg) {
                return arg.equals("expected");
            }
        }));

        // when
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new InvocationOnMockImpl(mock, method1), matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMockImpl(mock, method2), matchers);

        // then
        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void shouldNotMatchInvocationWithDifferentMock() {
        // given
        Object mock1 = mock(TestClass.class);
        Object mock2 = mock(TestClass.class);
        Method method = TestClass.class.getMethod("methodWithOneArgumentString", String.class);
        List<Matcher> matchers = Arrays.asList(argThat(new ArgumentMatcher<String>() {
            @Override
            public boolean matches(String arg) {
                return arg.equals("expected");
            }
        }));

        // when
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new InvocationOnMockImpl(mock1, method), matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMockImpl(mock2, method), matchers);

        // then
        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test(expected = PrintingException.class)
    public void shouldFailToPrintInvocationWithTooManyArguments() {
        // given
        Object mock = mock(TestClass.class);
        Method method = TestClass.class.getMethod("methodWithTooManyArguments", String[].class);
        List<Matcher> matchers = Arrays.asList(argThat(new ArgumentMatcher<String[]>() {
            @Override
            public boolean matches(String[] arg) {
                return arg.length == 2;
            }
        }));

        // when
        new InvocationMatcher(new InvocationOnMockImpl(mock, method), matchers).toString();

        // then
        fail("Printing exception not thrown");
    }

    private static class TestClass {
        public String methodWithOneArgument(String arg) {
            return null;
        }

        public String methodWithOneArgumentString(String arg) {
            return null;
        }

        public String methodWithOneArgumentInt(int arg) {
            return null;
        }

        public String methodWithTwoArguments(String arg1, int arg2) {
            return null;
        }

        public String methodWithTooManyArguments(String[] args) {
            return null;
        }
    }
}