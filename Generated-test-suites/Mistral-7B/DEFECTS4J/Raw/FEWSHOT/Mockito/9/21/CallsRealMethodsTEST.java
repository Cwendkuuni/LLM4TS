package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.InvocationOnMock;

public class CallsRealMethodsTest {

    @Test
    public void testCallRealMethod() {
        Object realObject = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testObject = mock(TestClass.class);
        when(testObject.realMethod(any(String.class))).thenReturn(answer);

        String input = "test";
        Object result = testObject.methodUnderTest(input);

        verify(realObject).realMethod(input);
        assertEquals(input, result);
    }

    @Test
    public void testCallRealMethodWithArguments() {
        Object realObject = mock(TestClassWithMethods.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClassWithMethods testObject = mock(TestClassWithMethods.class);
        when(testObject.realMethodWithArguments(anyString(), anyInt())).thenReturn(answer);

        String inputString = "testString";
        int inputInt = 1;
        Object result = testObject.methodUnderTest(inputString, inputInt);

        verify(realObject).realMethodWithArguments(inputString, inputInt);
        assertEquals(inputString, result);
    }

    @Test
    public void testCallRealMethodWithReturnValue() {
        Object realObject = mock(TestClassWithReturnValue.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClassWithReturnValue testObject = mock(TestClassWithReturnValue.class);
        when(testObject.realMethodWithReturnValue()).thenReturn(answer);

        Object result = testObject.methodUnderTest();

        verify(realObject).realMethodWithReturnValue();
        assertEquals(realObject, result);
    }

    @Test
    public void testCallRealMethodThrowsException() {
        Object realObject = mock(TestClassWithException.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClassWithException testObject = mock(TestClassWithException.class);
        when(testObject.realMethodThrowsException()).thenReturn(answer);

        try {
            testObject.methodUnderTest();
            fail("Expected Exception");
        } catch (Exception e) {
            verify(realObject).realMethodThrowsException();
        }
    }

    private interface TestClass {
        Object realMethod(String arg);
    }

    private interface TestClassWithMethods {
        Object realMethodWithArguments(String arg1, int arg2);
    }

    private interface TestClassWithReturnValue {
        Object realMethodWithReturnValue();
    }

    private interface TestClassWithException {
        void realMethodThrowsException() throws Exception;
    }

    private static class TestClassUnderTest {
        public Object methodUnderTest(Object arg) {
            return realMethod((String) arg);
        }

        private Object realMethod(String arg) {
            return callRealMethod(realMethod, arg);
        }

        private Object callRealMethod(Method method, Object arg) {
            try {
                return method.invoke(this, new Object[]{arg});
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}