package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.invocation.InvocationOnMock;

public class CallsRealMethodsTest {

    @Test
    public void testCallsRealMethods() {
        Object realObject = new Object() {
            public int method1() {
                return 42;
            }
        };

        CallsRealMethods answer = new CallsRealMethods();

        Object mockObject = mock(Object.class);
        when(mockObject).thenAnswer(answer);

        int result = ((InvocationOnMock) mockObject.method1()).callRealMethod();
        assertEquals(42, result);
    }

    @Test
    public void testCallsRealMethodsWithArguments() {
        Object realObject = new Object() {
            public int method1(int arg1, String arg2) {
                return arg1 + arg2.length();
            }
        };

        CallsRealMethods answer = new CallsRealMethods();

        Object mockObject = mock(Object.class);
        when(mockObject).thenAnswer(answer);

        int result = ((InvocationOnMock) mockObject.method1(1, "test")).callRealMethod();
        assertEquals(5, result);
    }

    @Test
    public void testCallsRealMethodsWithThrowable() {
        Object realObject = new Object() {
            public int method1() throws Exception {
                throw new Exception("Test Exception");
            }
        };

        CallsRealMethods answer = new CallsRealMethods();

        Object mockObject = mock(Object.class);
        when(mockObject).thenAnswer(answer);

        try {
            ((InvocationOnMock) mockObject.method1()).callRealMethod();
            fail();
        } catch (Exception e) {
            assertEquals("Test Exception", e.getMessage());
        }
    }

    @Test
    public void testCallsRealMethodsWithNull() {
        Object realObject = new Object() {
            public String method1() {
                return "Test String";
            }
        };

        CallsRealMethods answer = new CallsRealMethods();

        Object mockObject = mock(Object.class);
        when(mockObject).thenAnswer(answer);

        String result = (String) ((InvocationOnMock) mockObject.method1()).callRealMethod();
        assertNull(result);

        when(mockObject).thenReturn("Test String");
        assertEquals("Test String", mockObject.method1());
    }

    @Test
    public void testCallsRealMethodsWithStaticMethod() {
        Object realObject = new Object();

        CallsRealMethods answer = new CallsRealMethods();

        Class<?> clazz = realObject.getClass();
        Method method = clazz.getMethod("method1");

        Object mockObject = mockStatic(clazz);
        when(clazz.class, method).thenAnswer(answer);

        int result = (Integer) method.invoke(realObject);
        assertEquals(42, result.intValue());
    }
}