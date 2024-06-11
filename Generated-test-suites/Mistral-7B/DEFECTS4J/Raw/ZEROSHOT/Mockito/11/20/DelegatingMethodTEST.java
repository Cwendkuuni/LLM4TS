package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.MockitoMethod;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Method;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.util.reflection.Reflections.getModifiersExact;

@RunWith(MockitoJUnitRunner.class)
public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() throws NoSuchMethodException {
        Method method = DelegatingMethodTest.class.getMethod("testMethod", int.class, String.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertNull(exceptionTypes);

        method = DelegatingMethodTest.class.getMethod("testMethodWithException", int.class, String.class);
        delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes2 = delegatingMethod.getExceptionTypes();
        assertArrayEquals(new Class<?>[]{Exception.class}, exceptionTypes2);
    }

    @Test
    public void testGetJavaMethod() throws NoSuchMethodException {
        Method method = DelegatingMethodTest.class.getMethod("testMethod", int.class, String.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method javaMethod = delegatingMethod.getJavaMethod();
        assertEquals(method, javaMethod);
    }

    @Test
    public void testGetName() throws NoSuchMethodException {
        Method method = DelegatingMethodTest.class.getMethod("testMethod", int.class, String.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String name = delegatingMethod.getName();
        assertEquals("testMethod", name);
    }

    @Test
    public void testGetParameterTypes() throws NoSuchMethodException {
        Method method = DelegatingMethodTest.class.getMethod("testMethod", int.class, String.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertArrayEquals(new Class<?>[]{int.class, String.class}, parameterTypes);
    }

    @Test
    public void testGetReturnType() throws NoSuchMethodException {
        Method method = DelegatingMethodTest.class.getMethod("testMethod", int.class, String.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> returnType = delegatingMethod.getReturnType();
        assertEquals(void.class, returnType);

        method = DelegatingMethodTest.class.getMethod("testMethodWithReturnType", int.class, String.class);
        delegatingMethod = new DelegatingMethod(method);
        returnType = delegatingMethod.getReturnType();
        assertEquals(String.class, returnType);
    }

    @Test
    public void testIsVarArgs() throws NoSuchMethodException {
        Method method = DelegatingMethodTest.class.getMethod("testMethodWithVarArgs", int.class, int[].class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isVarArgs());

        method = DelegatingMethodTest.class.getMethod("testMethod", int.class, String.class);
        delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() throws NoSuchMethodException {
        Method method = DelegatingMethodTest.class.getMethod("testMethod", int.class, String.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.isAbstract());

        method = DelegatingMethodTest.class.getMethod("testAbstractMethod", int.class);
        delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = DelegatingMethodTest.class.getMethod("testMethod1", int.class);
        Method method2 = DelegatingMethodTest.class.getMethod("testMethod1", int.class);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertTrue(delegatingMethod1.equals(delegatingMethod2));

        Object notDelegatingMethod = new Object();
        assertFalse(delegatingMethod1.equals(notDelegatingMethod));

        Method method3 = DelegatingMethodTest.class.getMethod("testMethod2", int.class, String.class);
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(method3);
        assertFalse(delegatingMethod1.equals(delegatingMethod3));

        Method method4 = method1;
        DelegatingMethod delegatingMethod4 = new DelegatingMethod(method4);
        assertTrue(delegatingMethod1.equals(delegatingMethod4));
    }

    @Test
    public void testHashCode() {
        Method method1 = DelegatingMethodTest.class.getMethod("testMethod1", int.class);
        Method method2 = DelegatingMethodTest.class.getMethod("testMethod1", int.class);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertEquals(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());

        Object notDelegatingMethod = new Object();
        assertNotEquals(delegatingMethod1.hashCode(), notDelegatingMethod.hashCode());

        Method method3 = DeleggingMethodTest.class.getMethod("testMethod2", int.class, String.class);
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(method3);
        assertNotEquals(delegatingMethod1.hashCode(), delegatingMethod3.hashCode());

        Method method4 = method1;
        DelegatingMethod delegatingMethod4 = new DelegatingMethod(method4);
        assertEquals(delegatingMethod1.hashCode(), delegatingMethod4.hashCode());
    }

    private void testMethod(int a, String b) {
    }

    private void testMethodWithException(int a, String b) throws Exception {
        throw new Exception();
    }

    private void testMethodWithVarArgs(int a, int... b) {
    }

    private void testMethod1(int a) {
    }

    private void testAbstractMethod(int a) {
        // abstract method
    }
}