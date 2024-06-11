package org.mockito.internal.creation;

import org.junit.Test;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;

import java.lang.reflect.Method;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = getTestMethod("testAdd", int[].class, int.class, int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();

        assertArrayEquals(new Class[0], exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = getTestMethod("testAdd", int[].class, int.class, int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Method javaMethod = delegatingMethod.getJavaMethod();

        assertEquals(method, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = getTestMethod("testAdd", int[].class, int.class, int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(method.getName(), delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method method = getTestMethod("testAdd", int[].class, int.class, int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();

        assertArrayEquals(new Class[]{int[].class, int.class, int.class}, parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = getTestMethod("testAdd", int[].class, int.class, int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(method.getReturnType(), delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method method = getTestMethod("testAdd", int[].class, int.class, int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertFalse(delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = getAbstractTestMethod("testAbstractMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertTrue(delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = getTestMethod("testAdd", int[].class, int.class, int.class);
        Method method2 = getTestMethod("testAdd", int[].class, int.class, int.class);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testEqualsDifferentMethods() {
        Method method1 = getTestMethod("testAdd", int[].class, int.class, int.class);
        Method method2 = getTestMethod("testSubtract", int[].class, int.class, int.class);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertFalse(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testEqualsNull() {
        Method method = getTestMethod("testAdd", int[].class, int.class, int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertFalse(delegatingMethod.equals(null));
    }

    @Test
    public void testHashCode() {
        Method method1 = getTestMethod("testAdd", int[].class, int.class, int.class);
        Method method2 = getTestMethod("testAdd", int[].class, int.class, int.class);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        int hashCode1 = delegatingMethod1.hashCode();
        int hashCode2 = delegatingMethod2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    private Method getTestMethod(String name, Class<?>... parameterTypes) {
        try {
            return Class.forName("org.mockito.junit.runners.MockitoJUnitRunner$1")
                    .getDeclaredMethod(name, parameterTypes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Method getAbstractTestMethod(String name) {
        try {
            return Class.forName("java.lang.String")
                    .getDeclaredMethod(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}