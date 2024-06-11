package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.InvocationOnMock;

public class CallsRealMethodsTest {

    @Test
    public void testCallsRealMethods() {
        Object realObject = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testObject = mock(TestClass.class, new CallsRealMethods());

        testObject.method1(realObject);
        verify(realObject).method1(testObject); // real method called

        testObject.method2();
        verify(testObject).method2(); // real method called
    }

    @Test
    public void testCallsRealMethodsWithArguments() {
        Object realObject = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testObject = mock(TestClass.class, new CallsRealMethods());

        testObject.method3(1, "foo", realObject);
        verify(realObject).method3(1, "foo", testObject); // real method called
    }

    @Test
    public void testCallsRealMethodsThrowsException() {
        Object realObject = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testObject = mock(TestClass.class, new CallsRealMethods());

        try {
            testObject.method4(new Exception("Test Exception"));
        } catch (Exception e) {
            assertEquals("Test Exception", e.getMessage());
            verify(realObject).method4(new Exception("Test Exception")); // real method called
        }
    }
}

class TestClass {
    public void method1(Object obj) {}
    public void method2() {}
    public void method3(int i, String s, Object obj) {}
    public void method4(Exception e) throws Exception {}
}