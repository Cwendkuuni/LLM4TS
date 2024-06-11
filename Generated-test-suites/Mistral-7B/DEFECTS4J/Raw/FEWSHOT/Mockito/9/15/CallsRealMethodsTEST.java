package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.stubbing.Answer.callRealMethod;

import org.junit.Test;
import org.mockito.InvocationOnMock;

public class CallsRealMethodsTest {
    @Test
    public void testCallsRealMethods() {
        Object realObject = mock(RealClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        RealClass mock = mock(RealClass.class, callRealMethod());

        mock.method1(realObject);
        mock.method2();

        InvocationOnMock invocation1 = new InvocationOnMock(realObject, "method1", new Object[0]);
        Object result1 = answer.answer(invocation1);
        assertEquals(invocation1.getMock(), result1);

        InvocationOnMock invocation2 = new InvocationOnMock(realObject, "method2", new Object[0]);
        Object result2 = answer.answer(invocation2);
        assertNull(result2);
    }

    @Test
    public void testCallsRealMethodsWithArguments() {
        Object realObject = mock(RealClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        RealClass mock = mock(RealClass.class, callRealMethod());

        mock.methodWithArgument(1, realObject);
        mock.methodWithArgument("foo", realObject);

        InvocationOnMock invocation1 = new InvocationOnMock(realObject, "methodWithArgument", new Object[]{1L});
        Object result1 = answer.answer(invocation1);
        assertEquals(1L, invocation1.getArguments()[0]);
        assertEquals(realObject, invocation1.getArguments()[1]);
        assertEquals(invocation1.getMock(), result1);

        InvocationOnMock invocation2 = new InvocationOnMock(realObject, "methodWithArgument", new Object[]{"foo"});
        Object result2 = answer.answer(invocation2);
        assertEquals("foo", invocation2.getArguments()[0]);
        assertEquals(realObject, invocation2.getArguments()[1]);
        assertEquals(invocation2.getMock(), result2);
    }
}

class RealClass {
    public void method1(Object arg) {
        // some implementation
    }

    public void method2() {
        // some implementation
    }

    public void methodWithArgument(int arg1, Object arg2) {
        // some implementation
    }

    public void methodWithArgument(String arg1, Object arg2) {
        // some implementation
    }
}