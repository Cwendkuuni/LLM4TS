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
        Method method = new Method(TestClass.class, "testMethod", new Class[]{Integer.TYPE, String.class}) {
            {
                setAccessible(true);
                setExceptionTypes(new Class[]{NullPointerException.class, RuntimeException.class});
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertArrayEquals(new Class[]{NullPointerException.class, RuntimeException.class}, exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(TestClass.class, "testMethod", new Class[]{Integer.TYPE, String.class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() {
        Method method = new Method(TestClass.class, "testMethod", new Class[]{Integer.TYPE, String.class}) {
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
        Method method = new Method(TestClass.class, "testMethod", new Class[]{Integer.TYPE, String.class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertArrayEquals(new Class[]{Integer.TYPE, String.class}, delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method(TestClass.class, "testMethod", new Class[]{Integer.TYPE, String.class}) {
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
        Method method = new Method(TestClass.class, "testMethod", new Class[]{Integer.TYPE, String[].class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isVarArgs());

        method = new Method(TestClass.class, "testMethod", new Class[]{Integer.TYPE}) {
            {
                setAccessible(true);
            }
        };
        delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method(TestClass.class, "testMethod", new Class[]{Integer.TYPE, String.class}) {
            {
                setAccessible(true);
                setModifiers(method.getModifiers() | Modifier.ABSTRACT);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isAbstract());

        method = new Method(TestClass.class, "testMethod", new Class[]{Integer.TYPE, String.class}) {
            {
                setAccessible(true);
            }
        };
        delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = new Method(TestClass.class, "testMethod1", new Class[0]) {
            {
                setAccessible(true);
            }
        };
        Method method2 = new Method(TestClass.class, "testMethod2", new Class[0]) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertFalse(delegatingMethod1.equals(delegatingMethod2));

        delegatingMethod1 = new DelegatingMethod(method1);
        delegatingMethod2 = new DelegatingMethod(method1);
        assertTrue(delegatingMethod1.equals(delegatingMethod2));

        method1 = new Method(TestClass.class, "testMethod1", new Class[0]);
        delegatingMethod1 = new DelegatingMethod(method1);
        delegatingMethod2 = new DelegatingMethod(method1);
        assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() {
        Method method = new Method(TestClass.class, "testMethod", new Class[0]) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        assertEquals(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());
    }

    private static class TestClass {
        void testMethod(int i, String s) {
        }

        void testMethodWithDifferentName() {
        }

        void testMethod1() {
        }
    }
}