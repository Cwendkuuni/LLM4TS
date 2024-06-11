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
        Class<?>[] exceptionTypes = new Class[0];
        Method method = MockitoTestUtils.createMockMethod("testMethod", int.class, exceptionTypes);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertArrayEquals(exceptionTypes, delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testGetJavaMethod() {
        Method method = MockitoTestUtils.createMockMethod("testMethod", int.class, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() {
        Method method = MockitoTestUtils.createMockMethod("testMethod", int.class, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals("testMethod", delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Class<?>[] parameterTypes = new Class[]{int.class, String.class};
        Method method = MockitoTestUtils.createMockMethod("testMethod", int.class, parameterTypes);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertArrayEquals(parameterTypes, delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        Method method = MockitoTestUtils.createMockMethod("testMethod", int.class, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(int.class, delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Class<?>[] parameterTypes = new Class[]{int[].class};
        Method method = MockitoTestUtils.createMockMethod("testMethod", void.class, parameterTypes);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isVarArgs());

        method = MockitoTestUtils.createMockMethod("testMethod", int.class, new Class[0]);
        delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = MockitoTestUtils.createMockMethod("testMethod", int.class, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.isAbstract());

        method = MockitoTestUtils.createMockMethod("testMethod", int.class, new Class[0]);
        method = MockitoTestUtils.modifyAccessibility(method, Modifier.ABSTRACT);
        delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isAbstract());
    }

    @Test
    public void testEqualsNull() {
        Method method = MockitoTestUtils.createMockMethod("testMethod", int.class, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.equals(null));
    }

    @Test
    public void testEqualsSameObject() {
        Method method = MockitoTestUtils.createMockMethod("testMethod", int.class, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.equals(delegatingMethod));
    }

    @Test
    public void testEqualsSameMethod() {
        Method method = MockitoTestUtils.createMockMethod("testMethod", int.class, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        Method method2 = MockitoTestUtils.createMockMethod("testMethod", int.class, new Class[0]);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testEqualsDifferentMethods() {
        Method method1 = MockitoTestUtils.createMockMethod("testMethod1", int.class, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        Method method2 = MockitoTestUtils.createMockMethod("testMethod2", int.class, new Class[0]);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertFalse(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() {
        Method method = MockitoTestUtils.createMockMethod("testMethod", int.class, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method.hashCode(), delegatingMethod.hashCode());
    }
}