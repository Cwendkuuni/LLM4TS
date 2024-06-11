package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class CallsRealMethodsTest {

    private static class TestClass {
        public String method1() {
            return "method1";
        }

        public int method2(int a, int b) {
            return a + b;
        }
    }

    @Test
    public void testAnswer() throws Throwable {
        TestClass testObject = new TestClass();
        CallsRealMethods answer = new CallsRealMethods();

        InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation1.callRealMethod()).thenAnswer(answer);
        String result1 = (String) invocation1.callRealMethod();
        assertEquals("method1", result1);

        InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation2.callRealMethod()).thenAnswer(answer);
        int result2 = (int) invocation2.callRealMethod(2, 3);
        assertEquals(5, result2);
    }
}