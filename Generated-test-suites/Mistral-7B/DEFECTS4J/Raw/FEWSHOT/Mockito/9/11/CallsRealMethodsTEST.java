package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class CallsRealMethodsTest {

    @Test
    public void testCallsRealMethods() {
        Object mockObject = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass();
        TestClass testClassMock = mockObject;

        // Test calling real methods on a real object
        Object result = testClass.realMethod();
        assertEquals(result, answer.answer(mock(InvocationOnMock.class).withRealMethod(testClass.realMethod())));

        // Test calling real methods on a mocked object
        testClassMock.realMethod();
        verify(testClassMock).realMethod();
    }

    @Test
    public void testCallsRealMethodsWithArguments() {
        Object mockObject = mock(TestClassWithArguments.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClassWithArguments testClass = new TestClassWithArguments();
        TestClassWithArguments testClassMock = mockObject;

        // Test calling real methods on a real object with arguments
        Object result = testClass.realMethodWithArguments("arg1", 1);
        assertEquals(result, answer.answer(mock(InvocationOnMock.class)
                .withRealMethod(testClass.realMethodWithArguments(eq("arg1"), eq(1)))
                .withArguments(argThat(invocation -> invocation.getArguments()[0].equals("arg1")))
                .withArguments(argThat(invocation -> invocation.getArguments()[1].equals(1)))));

        // Test calling real methods on a mocked object with arguments
        testClassMock.realMethodWithArguments("arg1", 1);
        verify(testClassMock).realMethodWithArguments(eq("arg1"), eq(1));
    }

    // Add more test cases as needed for other methods or constructors in the CallsRealMethods class
}

class TestClass {
    public Object realMethod() {
        return new Object();
    }
}

class TestClassWithArguments {
    public Object realMethodWithArguments(String arg1, int arg2) {
        return new Object();
    }
}