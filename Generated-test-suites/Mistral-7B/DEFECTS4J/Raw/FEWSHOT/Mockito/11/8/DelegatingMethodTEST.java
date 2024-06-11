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
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] {String.class, int.class}) {
            {
                setAccessible(true);
                setExceptionTypes(new Class[] {IllegalArgumentException.class, RuntimeException.class});
            }
        };

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();

        assertArrayEquals(new Class<?>[] {IllegalArgumentException.class, RuntimeException.class}, exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] {String.class, int.class}) {
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
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] {String.class, int.class}) {
            {
                setAccessible(true);
                setName("testMethodWithDifferentName");
            }
        };

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals("testMethod", delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] {String.class, int.class}) {
            {
                setAccessible(true);
            }
        };

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();

        assertArrayEquals(new Class<?>[] {String.class, int.class}, parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] {String.class, int.class}) {
            {
                setAccessible(true);
                setReturnType(String.class);
            }
        };

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(String.class, delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method method1 = new Method(Object.class.getName(), "testMethodWithVarArgs", new Class[] {String[].class});
        Method method2 = new Method(Object.class.getName(), "testMethodWithoutVarArgs", new Class[] {String.class});

        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertTrue(delegatingMethod1.isVarArgs());
        assertFalse(delegatingMethod2.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method abstractMethod = new Method(Object.class.getName(), "testMethod", new Class[] {}) {
            {
                setAccessible(true);
                setModifiers(Modifier.ABSTRACT);
            }
        };

        DelegatingMethod delegatingMethod = new DelegatingMethod(abstractMethod);

        assertTrue(delegatingMethod.isAbstract());

        Method nonAbstractMethod = new Method(Object.class.getName(), "testMethod", new Class[] {});
        DelegatingMethod nonAbstractDelegatingMethod = new DelegatingMethod(nonAbstractMethod);

        assertFalse(nonAbstractDelegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = new Method(Object.class.getName(), "testMethod1", new Class[] {});
        Method method2 = new Method(Object.class.getName(), "testMethod2", new Class[] {});

        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertFalse(delegatingMethod1.equals(delegatingMethod2));

        Method sameMethod = new Method(Object.class.getName(), "testMethod1", new Class[] {});
        DelegatingMethod sameDelegatingMethod = new DelegatingMethod(sameMethod);

        assertTrue(delegatingMethod1.equals(sameDelegatingMethod));

        assertEquals(method1, delegatingMethod1.getJavaMethod());
        assertEquals(method1, sameDelegatingMethod.getJavaMethod());
    }

    @Test
    public void testHashCode() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] {});

        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);

        int hashCode1 = delegatingMethod1.hashCode();
        int hashCode2 = delegatingMethod2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }
}