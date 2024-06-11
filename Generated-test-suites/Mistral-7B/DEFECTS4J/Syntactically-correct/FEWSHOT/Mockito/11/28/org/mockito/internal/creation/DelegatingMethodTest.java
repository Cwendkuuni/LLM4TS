package org.mockito.internal.creation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.Test;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = getClass().getMethod("testGetExceptionTypes", int[].class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertArrayEquals(new Class<?>[]{Exception.class}, exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = getClass().getMethod("testGetJavaMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Method javaMethod = delegatingMethod.getJavaMethod();
        assertSame(method, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = getClass().getMethod("testGetName");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String name = delegatingMethod.getName();
        assertEquals("testGetName", name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = getClass().getMethod("testGetParameterTypes", int.class, int[].class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertArrayEquals(new Class<?>[]{int.class, int[].class}, parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = getClass().getMethod("testGetReturnType");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> returnType = delegatingMethod.getReturnType();
        assertEquals(int.class, returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = getClass().getMethod("testIsVarArgs", int[].class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean varArgs = delegatingMethod.isVarArgs();
        assertTrue(varArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = getClass().getMethod("testIsAbstract");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean abstractMethod = delegatingMethod.isAbstract();
        assertFalse(abstractMethod);
    }

    @Test
    public void testEquals() {
        Method method1 = getClass().getMethod("testGetExceptionTypes", int[].class);
        Method method2 = getClass().getMethod("testGetExceptionTypes", int[].class);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        boolean equals = delegatingMethod1.equals(delegatingMethod2);
        assertTrue(equals);
    }

    @Test
    public void testHashCode() {
        Method method = getClass().getMethod("testGetExceptionTypes", int[].class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        int hashCode = delegatingMethod.hashCode();
        assertEquals(System.identityHashCode(delegatingMethod), hashCode);
    }

    @Test
    public void testEqualsWithNull() {
        Method method = getClass().getMethod("testGetExceptionTypes", int[].class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean equals = delegatingMethod.equals(null);
        assertFalse(equals);
    }

    @Test
    public void testEqualsWithDifferentMethod() {
        Method method1 = getClass().getMethod("testGetExceptionTypes", int[].class);
        Method method2 = getClass().getMethod("testGetReturnType");
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        boolean equals = delegatingMethod1.equals(delegatingMethod2);
        assertFalse(equals);
    }
}