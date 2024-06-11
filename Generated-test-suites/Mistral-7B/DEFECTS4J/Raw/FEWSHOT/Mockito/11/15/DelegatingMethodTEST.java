package org.mockito.internal.creation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;

import org.junit.Test;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new MethodTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        Class<?>[] expectedExceptionTypes = method.getExceptionTypes();

        assertArrayEquals(expectedExceptionTypes, exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new MethodTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Method javaMethod = delegatingMethod.getJavaMethod();
        assertEquals(method, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = new MethodTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String name = delegatingMethod.getName();
        assertEquals(method.getName(), name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new MethodTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        Class<?>[] expectedParameterTypes = method.getParameterTypes();

        assertArrayEquals(expectedParameterTypes, parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = new MethodTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> returnType = delegatingMethod.getReturnType();
        assertEquals(method.getReturnType(), returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = new MethodTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean isVarArgs = delegatingMethod.isVarArgs();
        assertEquals(method.isVarArgs(), isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = new MethodTestAbstract();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean isAbstract = delegatingMethod.isAbstract();
        assertTrue(method.isAbstract() == isAbstract);
    }

    @Test
    public void testEquals() {
        Method method1 = new MethodTestMethod();
        Method method2 = new MethodTestMethod();
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        Object otherObject = new Object();

        assertFalse(delegatingMethod1.equals(otherObject));
        assertFalse(delegatingMethod1.equals(delegatingMethod2));
        assertFalse(delegatingMethod2.equals(otherObject));

        assertFalse(delegatingMethod1.equals(method1));
        assertTrue(delegatingMethod1.equals(method1));
        assertTrue(delegatingMethod1.equals(delegatingMethod1));
    }

    @Test
    public void testHashCode() {
        Method method = new MethodTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        int hashCode = delegatingMethod.hashCode();
        assertEquals(method.hashCode(), hashCode);
        assertEquals(hashCode, hashCode);
    }

    @Test
    public void testHashCodeDifferentMethods() {
        Method method1 = new MethodTestMethod();
        Method method2 = new MethodTestMethodWithDifferentName();
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        int hashCode1 = delegatingMethod1.hashCode();
        int hashCode2 = delegatingMethod2.hashCode();

        assertFalse(hashCode1 == hashCode2);
    }

    private static class MethodTestMethod {
        public int test() {
            return 0;
        }
    }

    private static class MethodTestMethodWithDifferentName {
        public void test() {
            return;
        }
    }

    private static class MethodTestAbstract {
        protected abstract int test();
    }
}