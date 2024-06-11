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
        Method method = new Method(TestClass.class, "testMethod", new Class[]{String.class, int.class}) {
            {
                setAccessible(true);
                setExceptionTypes(new Class[]{RuntimeException.class});
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertArrayEquals(new Class[]{RuntimeException.class}, delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(TestClass.class, "testMethod", new Class[]{String.class, int.class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() {
        Method method = new Method(TestClass.class, "testMethod", new Class[]{String.class, int.class}) {
            {
                setAccessible(true);
                setName("testMethod");
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
                setParameterTypes(new Class[]{String[].class, int[].class});
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertArrayEquals(new Class[]{String[].class, int[].class}, delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method(TestClass.class, "testMethod", new Class[]{String.class, int.class}) {
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
        Method method = new Method(TestClass.class, "testMethod", new Class[]{String[].class}) {
            {
                setAccessible(true);
                setVariadic(true);
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
        Method method = new Method(TestClass.class, "testMethod", new Class[0]) {
            {
                setAccessible(true);
                setModifiers(Modifier.ABSTRACT | Modifier.PUBLIC);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isAbstract());

        Method method2 = new Method(TestClass.class, "testMethod", new Class[]{String.class}) {
            {
                setAccessible(true);
                setModifiers(Modifier.PUBLIC);
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
        Method method2 = new Method(TestClass.class, "testMethod1", new Class[]{String.class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertTrue(delegatingMethod1.equals(delegatingMethod2));

        Method method3 = new Method(TestClass.class, "testMethod2", new Class[0]) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(method3);
        assertFalse(delegatingMethod1.equals(delegatingMethod3));
    }

    @Test
    public void testHashCode() {
        Method method = new Method(TestClass.class, "testMethod", new Class[]{String.class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        assertEquals(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());

        Method method2 = new Method(TestClass.class, "testMethod2", new Class[]{String.class}) {
            {
                setAccessible(true);
            }
        };
        DeleggingMethod delegatingMethod3 = new DelegatingMethod(method2);
        assertNotEquals(delegatingMethod1.hashCode(), delegatingMethod3.hashCode());
    }

    private static class TestClass {
        public void testMethod(String arg1, int arg2) {}
        public void testMethod1(String arg) {}
        public void testMethod2() {}
    }
}