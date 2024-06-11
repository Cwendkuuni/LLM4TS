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
        Method method = getClass().getDeclaredMethod("testGetExceptionTypes");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = new Class<?>[0];
        assertArrayEquals(exceptionTypes, delegatingMethod.getExceptionTypes());

        method = getClass().getDeclaredMethod("testGetExceptionTypes", String.class);
        delegatingMethod = new DelegatingMethod(method);
        Class<?>[] expectedExceptionTypes = new Class<?>[]{String.class};
        assertArrayEquals(expectedExceptionTypes, delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testGetJavaMethod() {
        Method method = getClass().getDeclaredMethod("testGetJavaMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() {
        Method method = getClass().getDeclaredMethod("testGetName");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method.getName(), delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method method = getClass().getDeclaredMethod("testGetParameterTypes");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = new Class<?>[0];
        assertArrayEquals(parameterTypes, delegatingMethod.getParameterTypes());

        method = getClass().getDeclaredMethod("testGetParameterTypes", int.class, String.class);
        delegatingMethod = new DelegatingMethod(method);
        Class<?>[] expectedParameterTypes = new Class<?>[]{int.class, String.class};
        assertArrayEquals(expectedParameterTypes, delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        Method method = getClass().getDeclaredMethod("testGetReturnType");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(Void.TYPE, delegatingMethod.getReturnType());

        method = getClass().getDeclaredMethod("testGetReturnType", int.class);
        delegatingMethod = new DelegatingMethod(method);
        assertEquals(Integer.TYPE, delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method method1 = getClass().getDeclaredMethod("testIsVarArgs1");
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        assertFalse(delegatingMethod1.isVarArgs());

        Method method2 = getClass().getDeclaredMethod("testIsVarArgs2", String[].class);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertTrue(delegatingMethod2.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method1 = getClass().getDeclaredMethod("testIsAbstract1");
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        assertFalse(delegatingMethod1.isAbstract());

        Method method2 = getClass().getDeclaredMethod("testIsAbstract2");
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertTrue(delegatingMethod2.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = getClass().getDeclaredMethod("testEquals1");
        Method method2 = getClass().getDeclaredMethod("testEquals2");
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertFalse(delegatingMethod1.equals(delegatingMethod2));

        Method method3 = method1;
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(method3);
        assertTrue(delegatingMethod1.equals(delegatingMethod3));
    }

    @Test
    public void testHashCode() {
        Method method = getClass().getDeclaredMethod("testHashCode");
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        assertEquals(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());
    }
}