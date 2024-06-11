package org.mockito.internal.creation;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {
    @Test
    public void testConstructor() throws NoSuchMethodException {
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertNotNull(delegatingMethod);
    }

    @Test(expected = AssertionError.class)
    public void testConstructor_NullMethod() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(null);
    }

    @Test
    public void testGetExceptionTypes() throws NoSuchMethodException {
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] expectedExceptionTypes = {NullPointerException.class};
        Class<?>[] actualExceptionTypes = delegatingMethod.getExceptionTypes();

        assertNotNull(actualExceptionTypes);
        assertEquals(expectedExceptionTypes.length, actualExceptionTypes.length);
        assertEquals(expectedExceptionTypes[0], actualExceptionTypes[0]);
    }

    @Test
    public void testGetJavaMethod() throws NoSuchMethodException {
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Method actualMethod = delegatingMethod.getJavaMethod();

        assertNotNull(actualMethod);
        assertEquals(method, actualMethod);
    }

    @Test
    public void testGetName() throws NoSuchMethodException {
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String expectedName = "myMethod";
        String actualName = delegatingMethod.getName();

        assertNotNull(actualName);
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetParameterTypes() throws NoSuchMethodException {
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] expectedParameterTypes = {int.class, String.class};
        Class<?>[] actualParameterTypes = delegatingMethod.getParameterTypes();

        assertNotNull(actualParameterTypes);
        assertEquals(expectedParameterTypes.length, actualParameterTypes.length);
        assertEquals(expectedParameterTypes[0], actualParameterTypes[0]);
        assertEquals(expectedParameterTypes[1], actualParameterTypes[1]);
    }

    @Test
    public void testGetReturnType() throws NoSuchMethodException {
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> expectedReturnType = String.class;
        Class<?> actualReturnType = delegatingMethod.getReturnType();

        assertNotNull(actualReturnType);
        assertEquals(expectedReturnType, actualReturnType);
    }

    @Test
    public void testIsVarArgs() throws NoSuchMethodException {
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean isVarArgs = delegatingMethod.isVarArgs();

        assertFalse(isVarArgs);
    }

    @Test
    public void testIsAbstract() throws NoSuchMethodException {
        Method method = MyClass.class.getMethod("myAbstractMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean isAbstract = delegatingMethod.isAbstract();

        assertTrue(isAbstract);
    }

    @Test
    public void testEquals() throws NoSuchMethodException {
        Method method1 = MyClass.class.getMethod("myMethod", int.class, String.class);
        Method method2 = MyClass.class.getMethod("myMethod", int.class, String.class);

        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        boolean areEqual = delegatingMethod1.equals(delegatingMethod2);
        boolean areNotEqual = delegatingMethod1.equals(null);

        assertTrue(areEqual);
        assertFalse(areNotEqual);
    }

    @Test
    public void testHashCode() throws NoSuchMethodException {
        Method method = MyClass.class.getMethod("myMethod", int.class, String.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        int hashCode = delegatingMethod.hashCode();

        assertNotEquals(0, hashCode);
    }

    private static class MyClass {
        public void myMethod(int arg1, String arg2) throws NullPointerException {}

        public abstract void myAbstractMethod();
    }
}