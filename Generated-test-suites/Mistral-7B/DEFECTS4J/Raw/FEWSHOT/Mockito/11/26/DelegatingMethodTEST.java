package org.mockito.internal.creation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Modifier;

import org.junit.Test;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { String.class, int.class });
        method.setExceptionTypes(new Class[] { IllegalStateException.class });

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();

        assertEquals(1, exceptionTypes.length);
        assertSame(IllegalStateException.class, exceptionTypes[0]);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { String.class, int.class });

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method javaMethod = delegatingMethod.getJavaMethod();

        assertSame(method, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { String.class, int.class });

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String name = delegatingMethod.getName();

        assertEquals("testMethod", name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { String.class, int.class });

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();

        assertArrayEquals(new Class[] { String.class, int.class }, parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { String.class, int.class });
        method.setReturnType(String.class);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> returnType = delegatingMethod.getReturnType();

        assertEquals(String.class, returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { String[].class });
        method.setAccessible(true);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean varArgs = delegatingMethod.isVarArgs();

        assertTrue(varArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method("Test", Modifier.ABSTRACT | Modifier.PUBLIC, Object.class, new Class[0]);

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isAbstract = delegatingMethod.isAbstract();

        assertTrue(isAbstract);
    }

    @Test
    public void testEquals() {
        Method method1 = new Method(Object.class.getName(), "testMethod1", new Class[] { String.class, int.class });
        Method method2 = new Method(Object.class.getName(), "testMethod2", new Class[] { int.class, String.class });

        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertFalse(delegatingMethod1.equals(delegatingMethod2));

        Method equalMethod = new Method(Object.class.getName(), "testMethod1", new Class[] { String.class, int.class });
        DelegatingMethod equalDelegatingMethod = new DelegatingMethod(equalMethod);

        assertTrue(delegatingMethod1.equals(delegatingMethod1));
        assertTrue(delegatingMethod1.equals(equalDelegatingMethod));
        assertTrue(equalDelegatingMethod.equals(delegatingMethod1));
        assertTrue(equalDelegatingMethod.equals(equalDelegatingMethod));
    }

    @Test
    public void testHashCode() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { String.class, int.class });

        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        int hashCode1 = delegatingMethod1.hashCode();

        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        int hashCode2 = delegatingMethod2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }
}