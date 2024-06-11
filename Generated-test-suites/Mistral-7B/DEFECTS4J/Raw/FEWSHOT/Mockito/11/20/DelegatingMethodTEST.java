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
        Method method = new Method(TestClass.class, "testMethod", new Class[] {Integer.TYPE, String.class}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] expectedExceptionTypes = new Class[] {IllegalArgumentException.class};
        assertArrayEquals(expectedExceptionTypes, delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(TestClass.class, "testMethod", new Class[] {Integer.TYPE, String.class}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() {
        Method method = new Method(TestClass.class, "testMethod", new Class[] {Integer.TYPE, String.class}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals("testMethod", delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method(TestClass.class, "testMethod", new Class[] {Integer.TYPE, String.class}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] expectedParameterTypes = new Class[] {Integer.TYPE, String.class};
        assertArrayEquals(expectedParameterTypes, delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method(TestClass.class, "testMethod", new Class[] {Integer.TYPE, String.class}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(Void.TYPE, delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method(TestClass.class, "testMethod", new Class[] {Integer.TYPE, String[].class}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertTrue(delegatingMethod.isVarArgs());

        Method method2 = new Method(TestClass.class, "testMethod", new Class[] {Integer.TYPE}, new Class[0]);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertFalse(delegatingMethod2.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method(AbstractTestClass.class, "testMethod", new Class[0], new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertTrue(delegatingMethod.isAbstract());

        Method method2 = new Method(TestClass.class, "testMethod", new Class[] {Integer.TYPE, String.class}, new Class[0]);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertFalse(delegatingMethod2.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = new Method(TestClass.class, "testMethod1", new Class[] {Integer.TYPE}, new Class[0]);
        Method method2 = new Method(TestClass.class, "testMethod1", new Class[] {Integer.TYPE}, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertTrue(delegatingMethod1.equals(delegatingMethod2));

        Method method3 = new Method(TestClass.class, "testMethod2", new Class[] {Integer.TYPE}, new Class[0]);
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(method3);

        assertFalse(delegatingMethod1.equals(delegatingMethod3));

        Object notAMethod = new Object();

        assertFalse(delegatingMethod1.equals(notAMethod));
    }

    @Test
    public void testHashCode() {
        Method method = new Method(TestClass.class, "testMethod", new Class[] {Integer.TYPE, String.class}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        int hashCode = delegatingMethod.hashCode();
        assertEquals(hashCode, delegatingMethod.hashCode());

        Method method2 = new Method(TestClass.class, "testMethod2", new Class[] {Integer.TYPE}, new Class[0]);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertNotEquals(hashCode, delegatingMethod2.hashCode());
    }

    static class TestClass {
        public void testMethod(int i, String s) {}

        public void testMethod1(int i) {}

        public void testMethod2(int i) {}
    }

    static abstract class AbstractTestClass {
        public abstract void testMethod();
    }
}