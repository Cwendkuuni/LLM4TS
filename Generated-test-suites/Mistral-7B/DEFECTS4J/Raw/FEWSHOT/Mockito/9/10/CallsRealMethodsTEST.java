package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.InvocationOnMock;

public class CallsRealMethodsTest {

    @Test
    public void testCallRealMethod() {
        Object mock = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass();
        TestClass mockTestClass = (TestClass) mock;

        Object result = answer.answer(new InvocationOnMock(testClass, new Object[0]));
        assertEquals(result, testClass.realMethod());

        verify(mockTestClass).realMethod();
    }

    @Test
    public void testCallRealMethodWithArguments() {
        Object mock = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass();
        TestClass mockTestClass = (TestClass) mock;

        Object[] args = new Object[2];
        args[0] = "arg1";
        args[1] = 1L;

        Object result = answer.answer(new InvocationOnMock(testClass, args));
        assertEquals(result, testClass.realMethod(args[0], args[1]));

        verify(mockTestClass).realMethod(args[0], args[1]);
    }

    @Test
    public void testCallRealMethodWithThrowsException() {
        Object mock = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass();
        TestClass mockTestClass = (TestClass) mock;

        try {
            answer.answer(new InvocationOnMock(testClass, new Object[0], new RuntimeException("Test Exception")));
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            assertEquals("Test Exception", e.getMessage());
        }

        verify(mockTestClass).realMethodThrowsException();
    }

    static class TestClass {
        public int realMethod() {
            return 42;
        }

        public void realMethod(String arg1, Long arg2) {
            // Do something
        }

        public void realMethodThrowsException() {
            throw new RuntimeException("Test Exception");
        }
    }
}