package org.mockito.internal.creation;

import static org.junit.Assert.*;

import org.junit.Test;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = getClass().getMethod("testGetExceptionTypes", int[].class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();

        assertEquals(exceptionTypes.length, 0);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = getClass().getMethod("testGetJavaMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(delegatingMethod.getJavaMethod(), method);
    }

    @Test
    public void testGetName() {
        Method method = getClass().getMethod("testGetName");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(delegatingMethod.getName(), "testGetName");
    }

    @Test
    public void testGetParameterTypes() {
        Method method = getClass().getMethod("testGetParameterTypes", int.class, int[].class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertEquals(parameterTypes.length, 2);
        assertEquals(parameterTypes[0].getName(), "int");
        assertEquals(parameterTypes[1].getName(), "int[]");
    }

    @Test
    public void testGetReturnType() {
        Method method = getClass().getMethod("testGetReturnType");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(delegatingMethod.getReturnType(), Void.TYPE);
    }

    @Test
    public void testIsVarArgs() {
        Method method = getClass().getMethod("testGetExceptionTypes", int[].class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertFalse(delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = getClass().getMethod("testIsAbstract");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertFalse(delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = getClass().getMethod("testEquals", Object.class);
        Method method2 = getClass().getMethod("testEquals", Object.class);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertTrue(delegatingMethod1.equals(delegatingMethod1));
        assertTrue(delegatingMethod1.equals(delegatingMethod2));
        assertTrue(delegatingMethod2.equals(delegatingMethod1));
        assertFalse(delegatingMethod1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        Method method = getClass().getMethod("testHashCode");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        int hashCode = delegatingMethod.hashCode();
        assertEquals(hashCode, 1);
    }
}