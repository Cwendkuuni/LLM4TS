package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {
    @Test
    public void testConstructor() throws Exception {
        Method method = DummyClass.class.getDeclaredMethod("dummyMethod", String.class, int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertNotNull(delegatingMethod);
    }

    @Test(expected = AssertionError.class)
    public void testConstructorWithNullMethod() throws Exception {
        DelegatingMethod delegatingMethod = new DelegatingMethod(null);
    }

    @Test
    public void testGetExceptionTypes() throws Exception {
        Method method = DummyClass.class.getDeclaredMethod("dummyMethod", String.class, int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertEquals(0, exceptionTypes.length);
    }

    @Test
    public void testGetJavaMethod() throws Exception {
        Method method = DummyClass.class.getDeclaredMethod("dummyMethod", String.class, int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method javaMethod = delegatingMethod.getJavaMethod();
        assertEquals(method, javaMethod);
    }

    @Test
    public void testGetName() throws Exception {
        Method method = DummyClass.class.getDeclaredMethod("dummyMethod", String.class, int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String name = delegatingMethod.getName();
        assertEquals("dummyMethod", name);
    }

    @Test
    public void testGetParameterTypes() throws Exception {
        Method method = DummyClass.class.getDeclaredMethod("dummyMethod", String.class, int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertEquals(2, parameterTypes.length);
        assertEquals(String.class, parameterTypes[0]);
        assertEquals(int.class, parameterTypes[1]);
    }

    @Test
    public void testGetReturnType() throws Exception {
        Method method = DummyClass.class.getDeclaredMethod("dummyMethod", String.class, int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> returnType = delegatingMethod.getReturnType();
        assertEquals(void.class, returnType);
    }

    @Test
    public void testIsVarArgs() throws Exception {
        Method method = DummyClass.class.getDeclaredMethod("dummyMethod", String[].class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsNotVarArgs() throws Exception {
        Method method = DummyClass.class.getDeclaredMethod("dummyMethod", String.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() throws Exception {
        Method method = DummyClass.class.getDeclaredMethod("abstractMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isAbstract());
    }

    @Test
    public void testIsNotAbstract() throws Exception {
        Method method = DummyClass.class.getDeclaredMethod("concreteMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() throws Exception {
        Method method = DummyClass.class.getDeclaredMethod("dummyMethod", String.class, int.class);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testNotEquals() throws Exception {
        Method method1 = DummyClass.class.getDeclaredMethod("dummyMethod", String.class, int.class);
        Method method2 = DummyClass.class.getDeclaredMethod("dummyMethod", int.class, String.class);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertFalse(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() throws Exception {
        Method method = DummyClass.class.getDeclaredMethod("dummyMethod", String.class, int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(1, delegatingMethod.hashCode());
    }

    private static class DummyClass {
        public void dummyMethod(String str, int num) {}

        public void dummyMethod(String... strs) {}

        public void abstractMethod();

        public void concreteMethod() {}
    }
}