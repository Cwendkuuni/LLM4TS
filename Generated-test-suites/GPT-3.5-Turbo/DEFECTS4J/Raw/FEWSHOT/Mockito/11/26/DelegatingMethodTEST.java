package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.internal.invocation.MockitoMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {
    @Test
    public void testConstructor() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertNotNull(delegatingMethod);
    }

    @Test
    public void testGetExceptionTypes() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertNotNull(exceptionTypes);
        assertEquals(0, exceptionTypes.length);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method javaMethod = delegatingMethod.getJavaMethod();
        assertEquals(method, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String name = delegatingMethod.getName();
        assertNotNull(name);
        assertEquals("", name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertNotNull(parameterTypes);
        assertEquals(0, parameterTypes.length);
    }

    @Test
    public void testGetReturnType() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> returnType = delegatingMethod.getReturnType();
        assertNull(returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isVarArgs = delegatingMethod.isVarArgs();
        assertFalse(isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isAbstract = delegatingMethod.isAbstract();
        assertFalse(isAbstract);
    }

    @Test
    public void testEquals() {
        Method method1 = null;
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);

        Method method2 = null;
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        boolean isEqual = delegatingMethod1.equals(delegatingMethod2);
        assertTrue(isEqual);
    }

    @Test
    public void testHashCode() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        int hashCode = delegatingMethod.hashCode();
        assertEquals(1, hashCode);
    }
}