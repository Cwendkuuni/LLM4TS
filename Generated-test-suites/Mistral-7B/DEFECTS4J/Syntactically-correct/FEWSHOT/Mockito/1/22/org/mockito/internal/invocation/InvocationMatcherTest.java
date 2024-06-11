package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockitoMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = createInvocation("m1(int, String)", new Object[]{1, "hello"});
        Invocation invocation2 = createInvocation("m1(int, String)", new Object[]{1, "hello"});
        Invocation invocation3 = createInvocation("m2(int, String)", new Object[]{1, "hello"});

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(invocation3);

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher3));
    }

    @Test
    public void testMatches() {
        Invocation invocation = createInvocation("m(int, String)", new Object[]{1, "hello"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation testInvocation1 = createInvocation("m(int, String)", new Object[]{1, "hello"});
        Invocation testInvocation2 = createInvocation("m(int, String)", new Object[]{1, "world"});

        assertTrue(invocationMatcher.matches(testInvocation1));
        assertFalse(invocationMatcher.matches(testInvocation2));
    }

    @Test
    public void testToString() {
        Invocation invocation = createInvocation("m(int, String)", new Object[]{1, "hello"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        String expected = "InvocationMatcher [invocation=Invocation [method=public void org.mockito.test.TestClass.m(int, java.lang.String), arguments=[1, hello]], matchers=[], mock=TestClassMock$1@558e80]";
        String actual = invocationMatcher.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testCaptureArguments() {
        Invocation invocation = createInvocation("m(int, String)", new Object[]{1, "hello"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Object capturedArgument = new Object();
        Matcher matcher = argThat(isInstanceOf(Captured.class));

        invocationMatcher.getMatchers().add(matcher);
        invocationMatcher.captureArgumentsFrom(invocation);

        assertTrue(matcher.matches(capturedArgument));
    }

    @Test
    public void testCaptureArgumentsVarargs() {
        Invocation invocation = createInvocation("m(int, String...)", new Object[]{1, new Object[]{1, 2, 3}});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Object[] capturedArguments = new Object[3];
        Matcher matcher = allOf(isInstanceOf(Captured[].class), isA(Object[].class));

        invocationMatcher.getMatchers().add(matcher);
        invocationMatcher.captureArgumentsFrom(invocation);

        for (int i = 0; i < 3; i++) {
            assertEquals(i + 1, invocationMatcher.getMatchers().get(0).getArguments()[i]);
        }
    }

    private Invocation createInvocation(String description, Object[] arguments) {
        try {
            Method method = MockitoMethod.getTestClassMethod(TestClass.class, description);
            return new Invocation(method, new Object[0], arguments, null, null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    static class TestClass {
        public void m(int i, String s) {}
        public void m(int i, String... strings) {}

        static class TestClassMock {
            Invocation invocation;

            TestClassMock(Invocation invocation) {
                this.invocation = invocation;
            }
        }
    }
}