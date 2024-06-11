package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class CallsRealMethodsTest {

    @Test
    public void testCallRealMethod() {
        Object target = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass();
        TestClass spy = mock(TestClass.class, callRealMethod());

        verify(spy).realMethod();

        assertEquals(testClass.realMethod(), answer.answer(new InvocationOnMock(target, testClass, "realMethod", new Object[0])));
    }

    @Test
    public void testCallRealMethodWithArguments() {
        Object target = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass();
        TestClass spy = mock(TestClass.class, callRealMethod());

        verify(spy).realMethodWithArguments(1, "arg1");

        Object[] args = new Object[]{1, "arg1"};
        assertEquals(testClass.realMethodWithArguments(1, "arg1"), answer.answer(new InvocationOnMock(target, testClass, "realMethodWithArguments", args)));
    }

    @Test
    public void testCallRealMethodReturnsNull() {
        Object target = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass();

        assertEquals(null, answer.answer(new InvocationOnMock(target, testClass, "realMethodReturnsNull", new Object[0])));
        verify(testClass).realMethodReturnsNull();
    }

    @Test(expected = Throwable.class)
    public void testCallRealMethodThrowsException() {
        Object target = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass();
        TestClass spy = mock(TestClass.class, callRealMethod());

        verify(spy).realMethodThrowsException();

        answer.answer(new InvocationOnMock(target, testClass, "realMethodThrowsException", new Object[0]));
    }

    interface TestClass {
        void realMethod();
        void realMethodWithArguments(int arg1, String arg2);
        String realMethodReturnsNull();
        void realMethodThrowsException() throws Exception;
    }
}