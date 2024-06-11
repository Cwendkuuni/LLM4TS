package org.mockito.internal.creation;

import static org.junit.Assert.*;

import org.junit.Test;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method(TestClass.class, "testMethod", new Class[] {Integer.TYPE, String.class}) {
            {
                setAccessible(true);
                setExceptionTypes(new Class[] {RuntimeException.class});
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertArrayEquals(new Class[]{RuntimeException.class}, exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(TestClass.class, "testMethod", new Class[] {Integer.TYPE, String.class}) {
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
        Method method = new Method(TestClass.class, "testMethod", new Class[] {Integer.TYPE, String.class}) {
            {
                setAccessible(true);
                setName("test_method");
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String name = delegatingMethod.getName();
        assertEquals("test_method", name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method(TestClass.class, "testMethod", new Class[] {Integer.TYPE, String.class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertArrayEquals(new Class[]{Integer.TYPE, String.class}, parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method(TestClass.class, "testMethod", new Class[] {Integer.TYPE, String.class}) {
            {
                setAccessible(true);
                setReturnType(Integer.TYPE);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> returnType = delegatingMethod.getReturnType();
        assertEquals(Integer.TYPE, returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method(TestClass.class, "testMethod", new Class[] {Integer.TYPE, String[].class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isVarArgs = delegatingMethod.isVarArgs();
        assertTrue(isVarArgs);

        method = new Method(TestClass.class, "testMethod", new Class[] {Integer.TYPE, String.class}) {
            {
                setAccessible(true);
            }
        };
        delegatingMethod = new DelegatingMethod(method);
        isVarArgs = delegatingMethod.isVarArgs();
        assertFalse(isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method(TestClass.class, "testMethod", new Class[] {Integer.TYPE, String.class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isAbstract = delegatingMethod.isAbstract();
        assertFalse(isAbstract);

        method = new Method(TestAbstractClass.class, "testMethod", new Class[] {}) {
            {
                setAccessible(true);
                setModifiers(method.getModifiers() | Modifier.ABSTRACT);
            }
        };
        delegatingMethod = new DelegatingMethod(method);
        isAbstract = delegatingMethod.isAbstract();
        assertTrue(isAbstract);
    }

    @Test
    public void testEquals() {
        Method method1 = new Method(TestClass1.class, "testMethod", new Class[] {Integer.TYPE, String.class}) {
            {
                setAccessible(true);
            }
        };
        Method method2 = new Method(TestClass2.class, "testMethod", new Class[] {Integer.TYPE, String.class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod method1Delegating = new DelegatingMethod(method1);
        DelegatingMethod method2Delegating = new DelegatingMethod(method2);
        assertFalse(method1Delegating.equals(method2Delegating));

        method1Delegating = new DelegatingMethod(method1);
        method2Delegating = new DelegatingMethod(method1);
        assertTrue(method1Delegating.equals(method2Delegating));

        method1Delegating = new DelegatingMethod(method1);
        Object sameMethodObject = method1;
        assertTrue(method1Delegating.equals(sameMethodObject));
    }

    @Test
    public void testHashCode() {
        Method method = new Method(TestClass.class, "testMethod", new Class[] {Integer.TYPE, String.class}) {
            {
                setAccessible(true);
            }
        };
        DelegatingMethod method1Delegating = new DelegatingMethod(method);
        DelegatingMethod method2Delegating = new DelegatingMethod(method);
        int hashCode1 = method1Delegating.hashCode();
        int hashCode2 = method2Delegating.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    private static class TestClass {
        public void testMethod(int i, String s) {}
    }

    private static class TestClass1 {}

    private static class TestClass2 {}

    private static class TestAbstractClass {
        public abstract void testMethod();
    }
}