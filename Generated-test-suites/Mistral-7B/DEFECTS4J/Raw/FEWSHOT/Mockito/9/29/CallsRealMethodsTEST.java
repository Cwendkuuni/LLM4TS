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
            public int methodWithIntReturnType() {
                return 42;
            }
        };

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setTarget(testClass);
        invocation.setMethod(TestClass.class.getMethod("methodWithIntReturnType"));

        assertEquals(42, answer.answer(invocation).intValue());
        verify(testClass).methodWithIntReturnType();
    }

    @Test
    public void testCallRealMethodWithVoidReturnType() {
        Object mock = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass() {
            @Override
            public void methodWithVoidReturnType() {
                System.out.println("Method with void return type called.");
            }
        };

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setTarget(testClass);
        invocation.setMethod(TestClass.class.getMethod("methodWithVoidReturnType"));

        answer.answer(invocation);
        verify(testClass).methodWithVoidReturnType();
    }

    @Test
    public void testCallRealMethodWithArrayReturnType() {
        Object mock = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass() {
            @Override
            public String[] methodWithArrayReturnType() {
                return new String[]{"foo", "bar"};
            }
        };

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setTarget(testClass);
        invocation.setMethod(TestClass.class.getMethod("methodWithArrayReturnType"));

        String[] result = (String[])answer.answer(invocation);
        assertEquals("foo", result[0]);
        assertEquals("bar", result[1]);
        verify(testClass).methodWithArrayReturnType();
    }

    @Test(expected = AssertionError.class)
    public void testCallRealMethodThrowsCheckedException() {
        Object mock = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass() {
            @Override
            public int methodWithCheckedException() throws Exception {
                throw new Exception("Test exception.");
            }
        };

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setTarget(testClass);
        invocation.setMethod(TestClass.class.getMethod("methodWithCheckedException"));

        try {
            answer.answer(invocation);
        } catch (Exception e) {
            assertEquals("Test exception.", e.getMessage());
            throw e;
        }
        verify(testClass).methodWithCheckedException();
    }

    @Test(expected = RuntimeException.class)
    public void testCallRealMethodThrowsUncheckedException() {
        Object mock = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass() {
            @Override
            public void methodWithUncheckedException() throws RuntimeException {
                throw new RuntimeException("Test exception.");
            }
        };

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setTarget(testClass);
        invocation.setMethod(TestClass.class.getMethod("methodWithUncheckedException"));

        answer.answer(invocation);
        verify(testClass).methodWithUncheckedException();
    }

    interface TestClass {
        int methodWithIntReturnType() throws Exception;
        void methodWithVoidReturnType();
        String[] methodWithArrayReturnType();
        void methodWithCheckedException() throws Exception;
        void methodWithUncheckedException() throws RuntimeException;
    }
}