package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.stubbing.answers.CallsRealMethods.CALLS_REAL_METHODS_INSTANCE;

public class CallsRealMethodsTest {

    @Test
    public void testCallRealMethod() {
        Object target = mock(TestClass.class);
        CallsRealMethods callsRealMethods = CALLS_REAL_METHODS_INSTANCE;

        String result = (String) callsRealMethods.answer(new InvocationOnMock(target, "someMethod", new Object[0]));
        assertEquals("Real method call result", result);
    }

    @Test
    public void testCallRealMethodWithArguments() {
        Object target = mock(TestClass.class);
        CallsRealMethods callsRealMethods = CALLS_REAL_METHODS_INSTANCE;

        Integer result = (Integer) callsRealMethods.answer(new InvocationOnMock(target, "someMethodWithArguments", new Object[]{1, 2, 3}));
        assertEquals(6, result.intValue()); // Assuming someMethodWithArguments returns the sum of its arguments
    }

    @Test(expected = Throwable.class)
    public void testCallRealMethodWithException() {
        Object target = mock(TestClass.class);
        CallsRealMethods callsRealMethods = CALLS_REAL_METHODS_INSTANCE;

        callsRealMethods.answer(new InvocationOnMock(target, "someMethodThatThrowsAnException", new Object[0]));
    }

    @Test
    public void testCallRealMethodWithVoidReturnType() {
        Object target = mock(TestClass.class);
        CallsRealMethods callsRealMethods = CALLS_REAL_METHODS_INSTANCE;

        callsRealMethods.answer(new InvocationOnMock(target, "someMethodWithVoidReturnType", new Object[0]));

        // No need to assert anything here since void methods don't return any value
    }

    private interface TestClass {
        String someMethod();
        Integer someMethodWithArguments(int arg1, int arg2, int arg3);
        void someMethodWithVoidReturnType();
        int someMethodThatThrowsAnException() throws Exception;
    }
}