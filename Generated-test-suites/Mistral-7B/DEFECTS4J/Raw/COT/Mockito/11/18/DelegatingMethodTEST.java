package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Method;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() throws NoSuchMethodException {
        Method method = DelegatingMethod.class.getDeclaredMethod("testGetExceptionTypes");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertArrayEquals(new Class[0], exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() throws NoSuchMethodException {
        Method method = DelegatingMethod.class.getDeclaredMethod("testGetJavaMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Method javaMethod = delegatingMethod.getJavaMethod();
        assertEquals(method, javaMethod);
    }

    @Test
    public void testGetName() throws NoSuchMethodException {
        Method method = DelegatingMethod.class.getDeclaredMethod("testGetName");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String name = delegatingMethod.getName();
        assertEquals(method.getName(), name);
    }

    @Test
    public void testGetParameterTypes() throws NoSuchMethodException {
        Method method = DelegatingMethod.class.getDeclaredMethod("testGetParameterTypes", int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertArrayEquals(new Class[]{int.class}, parameterTypes);
    }

    @Test
    public void testGetReturnType() throws NoSuchMethodException {
        Method method = DeleggingMethod.class.getDeclaredMethod("testGetReturnType");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> returnType = delegatingMethod.getReturnType();
        assertNotNull(returnType);
    }

    @Test
    public void testIsAbstract() throws NoSuchMethodException {
        Method method = DelegatingMethod.class.getDeclaredMethod("testIsAbstract");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertFalse(delegatingMethod.isAbstract());
    }

    @Test
    public void testEqualsWithNullObject() {
        Method method = DelegatingMethod.class.getDeclaredMethod("testGetJavaMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertFalse(delegatingMethod.equals(null));
    }

    @Test
    public void testEqualsWithDifferentMethod() {
        Method method1 = DelegatingMethod.class.getDeclaredMethod("testGetJavaMethod");
        Method method2 = DelegagingMethod.class.getDeclaredMethod("testGetExceptionTypes");
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertFalse(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testEqualsWithSameMethod() {
        Method method = DelegatingMethod.class.getDeclaredMethod("testGetJavaMethod");
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);

        assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testEqualsWithSameObject() {
        Method method = DelegatingMethod.class.getDeclaredMethod("testGetJavaMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertTrue(delegatingMethod.equals(delegatingMethod));
    }

    @Test(expected = NoSuchMethodException.class)
    public void testGetExceptionTypesWithNonExistentMethod() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(null);
        delegatingMethod.getExceptionTypes();
    }

    @Test
    public void testHashCode() {
        Method method = DelegatingMethod.class.getDeclaredMethod("testGetJavaMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        int hashCode = delegatingMethod.hashCode();
        assertEquals(hashCode, hashCode);
    }

    @Test
    public void testThrowIllegalArgumentExceptionWithNullMethod() {
        assertThrows(IllegalArgumentException.class, () -> new DelegatingMethod(null));
    }
}