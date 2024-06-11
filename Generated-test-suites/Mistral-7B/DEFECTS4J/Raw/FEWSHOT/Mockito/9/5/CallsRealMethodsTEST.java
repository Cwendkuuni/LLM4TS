package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class CallsRealMethodsTest {

    @Test
    public void testCallRealMethod() {
        Object mock = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass() {
            @Override
            public int method1() {
                return 42;
            }
        };

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setTarget(testClass);
        invocation.setMethod(TestClass.class.getMethod("method1"));

        Object result = answer.answer(invocation);

        assertEquals(42, result);
        verify(testClass).method1();
    }

    @Test
    public void testCallRealMethodWithArguments() {
        Object mock = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass() {
            @Override
            public int method1(int i) {
                return i * 2;
            }
        };

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setTarget(testClass);
        invocation.setMethod(TestClass.class.getMethod("method1", int.class));
        invocation.getArguments()[0] = 5;

        Object result = answer.answer(invocation);

        assertEquals(10, result);
        verify(testClass).method1(5);
    }

    @Test
    public void testCallRealMethodWithException() {
        Object mock = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass() {
            @Override
            public int method1() throws Exception {
                throw new RuntimeException("Test exception");
            }
        };

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setTarget(testClass);
        invocation.setMethod(TestClass.class.getMethod("method1"));

        try {
            answer.answer(invocation);
        } catch (RuntimeException e) {
            assertEquals("Test exception", e.getMessage());
        }

        verify(testClass).method1();
    }

    @Test
    public void testCallRealMethodOnStaticMethod() {
        TestClass testClass = TestClass.class;
        CallsRealMethods answer = new CallsRealMethods();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setTarget(testClass);
        invocation.setMethod(TestClass.class.getMethod("staticMethod"));

        Object result = answer.answer(invocation);

        assertEquals("Static method return value", result);
        verify(testClass).staticMethod();
    }

    @Test
    public void testCallRealMethodOnPrivateMethod() {
        TestClass testClass = new TestClass() {
            private int privateMethod() {
                return 42;
            }
        };

        CallsRealMethods answer = new CallsRealMethods();

        TestClass mock = mock(TestClass.class);
        doReturn(testClass).when(mock).getPrivateInstance();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setTarget(mock);
        invocation.setMethod(TestClass.class.getDeclaredMethod("privateMethod"));
        invocation.getMethod().setAccessible(true);

        Object result = answer.answer(invocation);

        assertEquals(42, result);
        verify(testClass).privateMethod();
    }

    private static class TestClass {
        public int method1() {
            return 42;
        }

        public int method1(int i) {
            return i * 2;
        }

        public static String staticMethod() {
            return "Static method return value";
        }

        private int privateMethod() {
            return 42;
        }
    }
}