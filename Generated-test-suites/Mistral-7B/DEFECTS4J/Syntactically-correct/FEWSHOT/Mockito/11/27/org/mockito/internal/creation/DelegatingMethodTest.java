package org.mockito.internal.creation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method(TestClass.class, "testMethod", new Class[]{String.class, int.class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = new Class[]{IllegalArgumentException.class, NullPointerException.class};
        assertArrayEquals(exceptionTypes, delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(TestClass.class, "testMethod", new Class[]{String.class, int.class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertSame(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() {
        Method method = new Method(TestClass.class, "testMethod", new Class[]{String.class, int.class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals("testMethod", delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method(TestClass.class, "testMethod", new Class[]{String.class, int.class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = new Class[]{String.class, int.class};
        assertArrayEquals(parameterTypes, delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method(TestClass.class, "testMethod", new Class[]{String.class, int.class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> returnType = int.class;
        assertSame(returnType, delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method(TestClass.class, "testMethod", new Class[]{String.class, int.class, String[].class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isVarArgs());

        Method method2 = new Method(TestClass.class, "testMethod", new Class[]{String.class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertFalse(delegatingMethod2.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method(TestClass.class, "testAbstractMethod", new Class[0]) {
            {
                setAccessible(true);
                setModifiers(Modifier.ABSTRACT);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isAbstract());

        Method method2 = new Method(TestClass.class, "testConcreteMethod", new Class[0]) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertFalse(delegatingMethod2.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = new Method(TestClass.class, "testMethod1", new Class[]{String.class}) {
            {
                setAccessible(true);
            }
        };
        Method method2 = new Method(TestClass.class, "testMethod2", new Class[]{String.class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertFalse(delegatingMethod1.equals(delegatingMethod2));

        Method methodSame = new Method(TestClass.class, "testMethodSame", new Class[]{String.class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethodSame = new DelegatingMethod(methodSame);
        assertTrue(delegatingMethod1.equals(delegatingMethodSame));

        DelegatingMethod delegatingMethodSameReference = delegatingMethodSame;
        assertTrue(delegatingMethod1.equals(delegatingMethodSameReference));
    }

    @Test
    public void testHashCode() {
        Method method1 = new Method(TestClass.class, "testMethod1", new Class[]{String.class}) {
            {
                setAccessible(true);
            }
        };
        Method method2 = new Method(TestClass.class, "testMethod2", new Class[]{String.class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        int hashCode1 = delegatingMethod1.hashCode();
        int hashCode2 = delegatingMethod2.hashCode();
        assertFalse(hashCode1 == hashCode2);
    }

    private static class TestClass {
        public void testMethod(String arg1, int arg2) {
        }

        public void testAbstractMethod() {
        }

        public void testMethodSame(String arg) {
        }

        public void testMethod1(String arg) {
        }

        public void testMethod2(String arg) {
        }
    }
}