package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.mockito.InvocationOnMock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CallsRealMethodsTest {

    @Test
    public void testCallsRealMethod() {
        Object realObject = mock(Object.class);
        CallsRealMethods answer = new CallsRealMethods();

        Object result = answer.answer(new InvocationOnMock(realObject, new Object[0], new Class[0], 0L));
        verify(realObject).callRealMethod();
        assertEquals(result, realObject.callRealMethod());
    }

    @Test
    public void testCallsRealMethodWithArguments() {
        Object realObject = mock(Object.class);
        CallsRealMethods answer = new CallsRealMethods();
        Object[] arguments = new Object[]{1, 2, 3};

        Object result = answer.answer(new InvocationOnMock(realObject, arguments, new Class[0], 0L));
        verify(realObject).callRealMethod(arguments);
        assertEquals(result, realObject.callRealMethod(arguments));
    }

    @Test
    public void testCallsRealMethodWithThrowable() {
        Object realObject = mock(Object.class);
        CallsRealMethods answer = new CallsRealMethods();

        try {
            answer.answer(new InvocationOnMock(realObject, new Object[0], new Class[0], 1L));
            fail("Expecting an exception");
        } catch (Throwable ignored) {
            verify(realObject).callRealMethod();
        }
    }

    @Test
    public void testCallsRealMethodWithMultipleArgumentsAndThrowable() {
        Object realObject = mock(Object.class);
        CallsRealMethods answer = new CallsRealMethods();
        Object[] arguments = new Object[]{1, 2, 3};

        try {
            answer.answer(new InvocationOnMock(realObject, arguments, new Class[0], 1L));
            fail("Expecting an exception");
        } catch (Throwable ignored) {
            verify(realObject).callRealMethod(arguments);
        }
    }
}