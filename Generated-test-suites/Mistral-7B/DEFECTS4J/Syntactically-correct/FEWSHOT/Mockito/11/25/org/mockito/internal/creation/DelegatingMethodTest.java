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
        Method method = new Method(TestClass.class, "testMethod", new Class[]{}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] expected = new Class[0];
        assertArrayEquals(expected, delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(TestClass.class, "testMethod", new Class[]{}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertSame(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() {
        Method method = new Method(TestClass.class, "testMethod", new Class[]{}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(method.getName(), delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method(TestClass.class, "testMethod", new Class[]{int.class, String.class}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] expected = new Class[]{int.class, String.class};
        assertArrayEquals(expected, delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method(TestClass.class, "testMethod", new Class[]{int.class, String.class}, new Class[]{int.class, String.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(method.getReturnType(), delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method method1 = new Method(TestClass.class, "testMethod1", new Class[]{}, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        assertFalse(delegatingMethod1.isVarArgs());

        Method method2 = new Method(TestClass.class, "testMethod2", new Class[]{String[].class}, new Class[0]);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertTrue(delegatingMethod2.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method1 = new Method(TestClass.class, "testMethod1", new Class[]{}, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        assertFalse(delegatingMethod1.isAbstract());

        Method method2 = new Method(AbstractTestClass.class, "testMethod", new Class[]{}, new Class[0]);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertTrue(delegatingMethod2.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = new Method(TestClass.class, "testMethod1", new Class[]{}, new Class[0]);
        Method method2 = new Method(TestClass.class, "testMethod1", new Class[]{}, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testEqualsDifferentMethods() {
        Method method1 = new Method(TestClass.class, "testMethod1", new Class[]{}, new Class[0]);
        Method method2 = new Method(TestClass.class, "testMethod2", new Class[]{}, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertFalse(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testEqualsNull() {
        Method method = new Method(TestClass.class, "testMethod", new Class[]{}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertFalse(delegatingMethod.equals(null));
    }

    @Test
    public void testHashCodeEquals() {
        Method method = new Method(TestClass.class, "testMethod", new Class[]{}, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);

        int hashCode1 = delegatingMethod1.hashCode();
        int hashCode2 = delegatingMethod2.hashCode();

        assertTrue(hashCode1 == hashCode2);
    }

    @Test
    public void testHashCodeDifferentMethods() {
        Method method1 = new Method(TestClass.class, "testMethod1", new Class[]{}, new Class[0]);
        Method method2 = new Method(TestClass.class, "testMethod2", new Class[]{}, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        int hashCode1 = delegatingMethod1.hashCode();
        int hashCode2 = delegatingMethod2.hashCode();

        assertFalse(hashCode1 == hashCode2);
    }

    private static class TestClass {
        public static int testMethod1() {
            return 0;
        }

        public static void testMethod2(String[] args) {
        }
    }

    private static abstract class AbstractTestClass {
        public abstract int testMethod();
    }
}