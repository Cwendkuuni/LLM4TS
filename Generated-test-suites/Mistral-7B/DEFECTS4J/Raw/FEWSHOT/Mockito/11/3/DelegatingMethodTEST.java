package org.mockito.internal.creation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { String.class, int.class }) {
            {
                setExceptionTypes(new Class[] { IllegalArgumentException.class, NullPointerException.class });
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertArrayEquals(new Class<?>[] { IllegalArgumentException.class, NullPointerException.class }, exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { String.class, int.class }) {
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method javaMethod = delegatingMethod.getJavaMethod();
        assertSame(method, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { String.class, int.class }) {
            {
                setName("testName");
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals("testName", delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { String.class, int.class }) {
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertArrayEquals(new Class<?>[] { String.class, int.class }, parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { String.class, int.class }) {
            {
                setReturnType(boolean.class);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(boolean.class, delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { String[].class }) {
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isVarArgs());

        method = new Method(Object.class.getName(), "testMethod", new Class[] { String.class }) {
        };
        delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { String.class, int.class }) {
            {
                setAccessible(true);
                setModifiers(method.getModifiers() | Modifier.ABSTRACT);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isAbstract());

        method = new Method(Object.class.getName(), "testMethod", new Class[] { String.class, int.class }) {
            {
                setAccessible(true);
                setModifiers(method.getModifiers() & ~Modifier.ABSTRACT);
            }
        };
        delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = new Method(Object.class.getName(), "testMethod1", new Class[] { String.class, int.class }) {
        };
        Method method2 = new Method(Object.class.getName(), "testMethod2", new Class[] { String.class, int.class }) {
        };
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertFalse(delegatingMethod1.equals(delegatingMethod2));

        method2 = new Method(Object.class.getName(), "testMethod1", new Class[] { String.class, int.class }) {
        };
        delegatingMethod2 = new DelegatingMethod(method2);
        assertFalse(delegatingMethod1.equals(delegatingMethod2));

        delegatingMethod2 = new DelegatingMethod(method1);
        assertTrue(delegatingMethod1.equals(delegatingMethod2));

        Object obj = new Object();
        assertFalse(delegatingMethod1.equals(obj));
    }

    @Test
    public void testHashCode() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { String.class, int.class }) {
        };
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        assertEquals(System.identityHashCode(method), delegatingMethod1.hashCode());

        Method method2 = new Method(Object.class.getName(), "testMethod", new Class[] { String.class, int.class }) {
        };
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertEquals(System.identityHashCode(method2), delegatingMethod2.hashCode());

        assertFalse(delegatingMethod1.hashCode() == delegatingMethod2.hashCode());
    }
}