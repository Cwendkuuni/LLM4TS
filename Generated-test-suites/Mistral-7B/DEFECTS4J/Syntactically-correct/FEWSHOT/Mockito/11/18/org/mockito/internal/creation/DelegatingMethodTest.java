package org.mockito.internal.creation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = MockitoTestUtils.getTestMethod("testAdd", TestClassWithMethods.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertArrayEquals(new Class[0], exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = MockitoTestUtils.getTestMethod("testAdd", TestClassWithMethods.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Method javaMethod = delegatingMethod.getJavaMethod();
        assertEquals(method, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = MockitoTestUtils.getTestMethod("testAdd", TestClassWithMethods.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String name = delegatingMethod.getName();
        assertEquals(method.getName(), name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = MockitoTestUtils.getTestMethod("testAdd", TestClassWithMethods.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertArrayEquals(new Class[]{int.class, int.class}, parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = MockitoTestUtils.getTestMethod("testAdd", TestClassWithMethods.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> returnType = delegatingMethod.getReturnType();
        assertEquals(int.class, returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = MockitoTestUtils.getTestMethod("testAddVarargs", TestClassWithMethods.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean isVarArgs = delegatingMethod.isVarArgs();
        assertTrue(isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method abstractMethod = MockitoTestUtils.getTestMethod("testAbstractMethod", TestAbstractClass.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(abstractMethod);

        boolean isAbstract = delegatingMethod.isAbstract();
        assertTrue(isAbstract);
    }

    @Test
    public void testEquals() {
        Method method1 = MockitoTestUtils.getTestMethod("testAdd", TestClassWithMethods.class);
        Method method2 = MockitoTestUtils.getTestMethod("testAdd", TestClassWithMethods.class);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        boolean equals = delegatingMethod1.equals(delegatingMethod2);
        assertTrue(equals);
    }

    @Test
    public void testEqualsDifferentMethods() {
        Method method1 = MockitoTestUtils.getTestMethod("testAdd", TestClassWithMethods.class);
        Method method2 = MockitoTestUtils.getTestMethod("testSubtract", TestClassWithMethods.class);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        boolean equals = delegatingMethod1.equals(delegatingMethod2);
        assertFalse(equals);
    }

    @Test
    public void testEqualsNull() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(MockitoTestUtils.getTestMethod("testAdd", TestClassWithMethods.class));

        boolean equals = delegatingMethod.equals(null);
        assertFalse(equals);
    }

    @Test
    public void testHashCode() {
        Method method = MockitoTestUtils.getTestMethod("testAdd", TestClassWithMethods.class);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);

        int hashCode1 = delegatingMethod1.hashCode();
        int hashCode2 = delegatingMethod2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }
}

class TestClassWithMethods {
    public void testAdd(int i, int j) {
        // Method body
    }

    public void testAddVarargs(int... integers) {
        // Method body
    }
}

class TestAbstractClass {
    public abstract void abstractMethod();
}

class MockitoTestUtils {
    public static Method getTestMethod(String name, Class<?> clazz) {
        try {
            return clazz.getMethod(name);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}