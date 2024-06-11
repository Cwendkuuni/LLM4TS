package org.mockito.internal.creation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {
    @Test
    public void testGetExceptionTypes() {
        Method testMethod = getTestMethod();

        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();

        assertEquals(testMethod.getExceptionTypes(), exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method testMethod = getTestMethod();

        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        Method javaMethod = delegatingMethod.getJavaMethod();

        assertEquals(testMethod, javaMethod);
    }

    @Test
    public void testGetName() {
        Method testMethod = getTestMethod();

        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        String name = delegatingMethod.getName();

        assertEquals(testMethod.getName(), name);
    }

    @Test
    public void testGetParameterTypes() {
        Method testMethod = getTestMethod();

        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();

        assertEquals(testMethod.getParameterTypes(), parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method testMethod = getTestMethod();

        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        Class<?> returnType = delegatingMethod.getReturnType();

        assertEquals(testMethod.getReturnType(), returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method testMethod = getTestMethod();

        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        boolean isVarArgs = delegatingMethod.isVarArgs();

        assertEquals(testMethod.isVarArgs(), isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method testMethod = getTestMethod();

        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        boolean isAbstract = delegatingMethod.isAbstract();

        assertEquals(Modifier.isAbstract(testMethod.getModifiers()), isAbstract);
    }

    @Test
    public void testEquals() {
        Method testMethod = getTestMethod();

        DelegatingMethod delegatingMethod1 = new DelegatingMethod(testMethod);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(testMethod);

        assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() {
        Method testMethod = getTestMethod();

        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        int hashCode = delegatingMethod.hashCode();

        assertEquals(1, hashCode);
    }

    private Method getTestMethod() {
        try {
            return DelegatingMethodTest.class.getMethod("testMethod");
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public void testMethod() {
        // test method implementation
    }
}