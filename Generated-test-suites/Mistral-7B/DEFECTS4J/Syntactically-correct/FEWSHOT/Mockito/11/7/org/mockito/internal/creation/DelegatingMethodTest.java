package org.mockito.internal.creation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = getClass().getDeclaredMethod("testGetExceptionTypes");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = new Class[0];
        assertEquals(exceptionTypes, delegatingMethod.getExceptionTypes());

        Class<?>[] exceptionTypes2 = new Class[] {Exception1.class, Exception2.class};
        Method methodWithExceptions = getClass().getDeclaredMethod("testGetExceptionTypesWithExceptions");
        DelegatingMethod delegatingMethodWithExceptions = new DelegatingMethod(methodWithExceptions);
        assertEquals(exceptionTypes2, delegatingMethodWithExceptions.getExceptionTypes());
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

        Class<?>[] parameterTypes = new Class[0];
        assertEquals(parameterTypes, delegatingMethod.getParameterTypes());

        Class<?>[] parameterTypes2 = new Class[] {String.class, int.class};
        Method methodWithParameters = getClass().getDeclaredMethod("testGetParameterTypesWithParameters", String.class, int.class);
        DelegatingMethod delegatingMethodWithParameters = new DelegatingMethod(methodWithParameters);
        assertEquals(parameterTypes2, delegatingMethodWithParameters.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        Method method = getClass().getDeclaredMethod("testGetReturnType");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> returnType = Void.TYPE;
        assertEquals(returnType, delegatingMethod.getReturnType());

        Class<?> returnType2 = String.class;
        Method methodWithReturnType = getClass().getDeclaredMethod("testGetReturnTypeWithReturnType", String.class);
        DelegatingMethod delegatingMethodWithReturnType = new DelegatingMethod(methodWithReturnType);
        assertEquals(returnType2, delegatingMethodWithReturnType.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method method = getClass().getDeclaredMethod("testIsVarArgs");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertFalse(delegatingMethod.isVarArgs());

        Method methodWithVarArgs = getClass().getDeclaredMethod("testIsVarArgsWithVarArgs", String[].class);
        DelegatingMethod delegatingMethodWithVarArgs = new DelegatingMethod(methodWithVarArgs);
        assertTrue(delegatingMethodWithVarArgs.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = getClass().getDeclaredMethod("testIsAbstract");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertFalse(delegatingMethod.isAbstract());

        Method methodAbstract = getClass().getDeclaredMethod("testIsAbstractAbstract");
        DelegatingMethod delegatingMethodAbstract = new DelegatingMethod(methodAbstract);
        assertTrue(delegatingMethodAbstract.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = getClass().getDeclaredMethod("testGetExceptionTypes");
        Method method2 = getClass().getDeclaredMethod("testGetExceptionTypes");
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertTrue(delegatingMethod1.equals(delegatingMethod2));
        assertTrue(delegatingMethod1.equals(new DelegatingMethod(method1)));
        assertTrue(new DelegatingMethod(method1).equals(delegatingMethod1));

        Method method3 = getClass().getDeclaredMethod("testGetParameterTypes");
        assertFalse(delegatingMethod1.equals(new DelegatingMethod(method3)));
        assertFalse(new DelegatingMethod(method3).equals(delegatingMethod1));
    }

    @Test
    public void testHashCode() {
        Method method = getClass().getDeclaredMethod("testHashCode");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        int hashCode = delegatingMethod.hashCode();
        assertEquals(hashCode, delegatingMethod.hashCode());
        assertEquals(hashCode, new DelegatingMethod(method).hashCode());
        assertNotSame(hashCode, new DelegatingMethod(getClass().getDeclaredMethod("testGetJavaMethod")).hashCode());
    }
}