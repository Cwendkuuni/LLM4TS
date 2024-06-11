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
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] {IntType.class, StringType.class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        Class<?>[] expectedExceptionTypes = new Class[] {ExceptionType.class, AnotherExceptionType.class};

        assertArrayEquals(expectedExceptionTypes, exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] {IntType.class, StringType.class}) {
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
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] {IntType.class, StringType.class}) {
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
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] {IntType.class, StringType.class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();

        assertArrayEquals(new Class[] {IntType.class, StringType.class}, parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] {IntType.class, StringType.class}) {
            {
                setAccessible(true);
                setReturnType(VoidType.TYPE);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> returnType = delegatingMethod.getReturnType();

        assertEquals(VoidType.TYPE, returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] {IntType[].class, StringType[].class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertTrue(delegatingMethod.isVarArgs());

        method = new Method(Object.class.getName(), "testMethod", new Class[] {IntType.class, StringType.class}) {
            {
                setAccessible(true);
            }
        };
        delegatingMethod = new DelegatingMethod(method);

        assertFalse(delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] {IntType.class, StringType.class}) {
            {
                setAccessible(true);
                setModifiers(method.getModifiers() | Modifier.ABSTRACT);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertTrue(delegatingMethod.isAbstract());

        method = new Method(Object.class.getName(), "testMethod", new Class[] {IntType.class, StringType.class}) {
            {
                setAccessible(true);
            }
        };
        delegatingMethod = new DelegatingMethod(method);

        assertFalse(delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = new Method(Object.class.getName(), "testMethod1", new Class[] {IntType.class, StringType.class}) {
            {
                setAccessible(true);
            }
        };
        Method method2 = new Method(Object.class.getName(), "testMethod1", new Class[] {IntType.class, StringType.class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertTrue(delegatingMethod1.equals(delegatingMethod2));

        Method method3 = new Method(Object.class.getName(), "testMethod2", new Class[] {IntType.class, StringType.class}) {
            {
                setAccessible(true);
            }
        };
        delegatingMethod1 = new DelegatingMethod(method1);
        delegatingMethod2 = new DelegatingMethod(method3);

        assertFalse(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] {IntType.class, StringType.class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);

        assertEquals(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());

        method = new Method(Object.class.getName(), "testMethodWithDifferentName", new Class[] {IntType.class, StringType.class}) {
            {
                setAccessible(true);
            }
        };
        delegatingMethod1 = new DelegatingMethod(method);
        delegatingMethod2 = new DelegatingMethod(method);

        assertNotEquals(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());
    }

    @Test
    public void testNullDelegatingMethod() {
        assertNull(new DelegatingMethod(null));
    }
}