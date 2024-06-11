package org.mockito.internal.creation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method(TestClass.class, "methodWithExceptions", new Class[]{String.class, Integer.class}) {
            {
                setExceptionTypes(new Class[]{IllegalArgumentException.class, RuntimeException.class});
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertArrayEquals(new Class[]{IllegalArgumentException.class, RuntimeException.class}, exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(TestClass.class, "methodWithReturnType", new Class[]{String.class, Integer.class}) {
            {
                setReturnType(String.class);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertSame(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() {
        Method method = new Method(TestClass.class, "methodWithName", new Class[]{String.class, Integer.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals("methodWithName", delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method(TestClass.class, "methodWithParameters", new Class[]{String.class, Integer.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertArrayEquals(new Class[]{String.class, Integer.class}, delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method(TestClass.class, "methodWithReturnType", new Class[]{String.class, Integer.class}) {
            {
                setReturnType(Integer.class);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(Integer.class, delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method(TestClass.class, "methodWithVarArgs", new Class[]{String[].class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertTrue(delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method(TestClass.class, "method", new Class[]{});
        method.setAccessible(true);
        method.setModifiers(method.getModifiers() | Modifier.ABSTRACT);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertTrue(delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = new Method(TestClass.class, "equalMethods", new Class[]{String.class, Integer.class});
        Method method2 = new Method(TestClass.class, "equalMethods", new Class[]{String.class, Integer.class});
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testEqualsNull() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(TestClass.class, "method", new Class[]{}));

        assertFalse(delegatingMethod.equals(null));
    }

    @Test
    public void testEqualsDifferentMethods() {
        Method method1 = new Method(TestClass.class, "equalMethods", new Class[]{String.class, Integer.class});
        Method method2 = new Method(TestClass.class, "differentMethods", new Class[]{String.class, Integer.class});
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertFalse(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() {
        Method method = new Method(TestClass.class, "method", new Class[]{});
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);

        assertEquals(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());
    }

    @Test
    public void testHashCodeDifferentMethods() {
        Method method1 = new Method(TestClass.class, "method1", new Class[]{});
        Method method2 = new Method(TestClass.class, "method2", new Class[]{});
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertNotSame(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());
    }
}

class TestClass {
    void method() {
    }

    void method(String s) {
    }

    void method(String[] strings) {
    }

    void methodWithName(String name) {
    }

    void methodWithExceptions(String s, Integer i) throws IllegalArgumentException, RuntimeException {
    }

    void methodWithReturnType(String s, Integer i) {
        return "";
    }

    void methodWithVarArgs(String[] strings) {
    }

    void equalMethods(String s, Integer i) {
    }
}