package org.mockito.internal.creation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.Test;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { int.class, String[].class }) {
            {
                setAccessible(true);
            }
        };

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertArrayEquals(new Class[]{}, exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { int.class, String[].class }) {
            {
                setAccessible(true);
            }
        };

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Method javaMethod = delegatingMethod.getJavaMethod();
        assertEquals(method, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { int.class, String[].class }) {
            {
                setAccessible(true);
                setName("testName");
            }
        };

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String name = delegatingMethod.getName();
        assertEquals("testName", name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { int.class, String[].class }) {
            {
                setAccessible(true);
            }
        };

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertArrayEquals(new Class[] { int.class, String[].class }, parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { int.class, String[].class }) {
            {
                setAccessible(true);
                setReturnType(String.class);
            }
        };

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> returnType = delegatingMethod.getReturnType();
        assertEquals(String.class, returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { int.class, String[].class, int[].class }) {
            {
                setAccessible(true);
            }
        };

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean varArgs = delegatingMethod.isVarArgs();
        assertTrue(varArgs);

        method.setParameterTypes(new Class[] { int.class, String[].class });
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        assertFalse(delegatingMethod2.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { int.class, String[].class }) {
            {
                setAccessible(true);
                setModifiers(method.getModifiers() | Modifier.ABSTRACT);
            }
        };

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean abstractMethod = delegatingMethod.isAbstract();
        assertTrue(abstractMethod);

        method.setModifiers(method.getModifiers() & ~Modifier.ABSTRACT);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        assertFalse(delegatingMethod2.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = new Method(Object.class.getName(), "testMethod1", new Class[] { int.class, String[].class }) {
            {
                setAccessible(true);
            }
        };
        Method method2 = new Method(Object.class.getName(), "testMethod2", new Class[] { int.class, String[].class }) {
            {
                setAccessible(true);
            }
        };

        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertFalse(delegatingMethod1.equals(delegatingMethod2));

        assertFalse(delegatingMethod1.equals(null));
        assertFalse(delegatingMethod1.equals(""));
        assertFalse(delegatingMethod1.equals(new Object()));

        Method method3 = new Method(Object.class.getName(), "testMethod1", new Class[] { int.class, String[].class }) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(method3);

        assertTrue(delegatingMethod1.equals(delegatingMethod3));

        assertTrue(delegatingMethod1.equals(method1));
        assertTrue(method1.equals(delegatingMethod1));

        Method method4 = method1;
        DelegatingMethod delegatingMethod4 = new DelegatingMethod(method4);

        assertTrue(delegatingMethod1.equals(delegatingMethod4));
    }

    @Test
    public void testHashCode() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { int.class, String[].class }) {
            {
                setAccessible(true);
            }
        };

        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);

        int hashCode1 = delegatingMethod1.hashCode();
        int hashCode2 = delegatingMethod2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }
}