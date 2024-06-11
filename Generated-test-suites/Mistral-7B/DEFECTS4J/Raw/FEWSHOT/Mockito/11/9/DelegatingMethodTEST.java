package org.mockito.internal.creation;

import org.junit.Test;
import org.mockito.internal.invocation.MockitoMethod;

import java.lang.reflect.Method;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method(TestClass.class, "testMethod", new Class[] {Integer.TYPE, String[].class});
        Method delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertArrayEquals(new Class[]{}, exceptionTypes);

        Method methodWithException = new Method(TestClass.class, "testMethodWithException", new Class[] {Integer.TYPE, String[].class, RuntimeException.class});
        DelegatingMethod delegatingMethodWithException = new DelegatingMethod(methodWithException);

        Class<?>[] exceptionTypesWithException = delegatingMethodWithException.getExceptionTypes();
        assertEquals(RuntimeException.class, exceptionTypesWithException[0]);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(TestClass.class, "testMethod", new Class[] {Integer.TYPE, String[].class});
        Method delegatingMethod = new DelegatingMethod(method);

        assertEquals(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() {
        Method method = new Method(TestClass.class, "testMethod", new Class[] {Integer.TYPE, String[].class});
        Method delegatingMethod = new DelegatingMethod(method);

        assertEquals("testMethod", delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method(TestClass.class, "testMethod", new Class[] {Integer.TYPE, String[].class});
        Method delegatingMethod = new DelegatingMethod(method);

        assertArrayEquals(new Class[] {Integer.TYPE, String[].class}, delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method(TestClass.class, "testMethod", new Class[] {Integer.TYPE, String[].class});
        Method delegatingMethod = new DelegatingMethod(method);

        assertEquals(void.class, delegatingMethod.getReturnType());

        Method methodWithReturnType = new Method(TestClass.class, "testMethodWithReturnType", new Class[] {Integer.TYPE, String[].class});
        Method delegatingMethodWithReturnType = new DelegatingMethod(methodWithReturnType);

        assertEquals(Integer.TYPE, delegatingMethodWithReturnType.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method(TestClass.class, "testMethod", new Class[] {Integer.TYPE, String[].class});
        Method delegatingMethod = new DelegatingMethod(method);

        assertFalse(delegatingMethod.isVarArgs());

        Method methodWithVarArgs = new Method(TestClass.class, "testMethodWithVarArgs", new Class[] {Integer.TYPE, String[].class, String[].class});
        Method delegatingMethodWithVarArgs = new DelegatingMethod(methodWithVarArgs);

        assertTrue(delegatingMethodWithVarArgs.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method(TestClass.class, "testMethod", new Class[] {Integer.TYPE, String[].class});
        Method delegatingMethod = new DelegatingMethod(method);

        assertFalse(delegatingMethod.isAbstract());

        Method methodWithAbstract = new Method(TestAbstractClass.class, "testMethod", new Class[]{});
        Method delegatingMethodWithAbstract = new DelegatingMethod(methodWithAbstract);

        assertTrue(delegatingMethodWithAbstract.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = new Method(TestClass.class, "testMethod1", new Class[] {Integer.TYPE, String[].class});
        Method method2 = new Method(TestClass.class, "testMethod2", new Class[] {Integer.TYPE, String[].class});
        Method method3 = new Method(TestClass.class, "testMethod1", new Class[] {Integer.TYPE, String[].class});
        Method method4 = new Method(TestClass.class, "testMethod1", new Class[] {String[].class, Integer.TYPE});
        Method delegatingMethod1 = new DelegatingMethod(method1);
        Method delegatingMethod2 = new DelegatingMethod(method2);
        Method delegatingMethod3 = new DelegatingMethod(method3);
        Method delegatingMethod4 = new DelegatingMethod(method4);

        assertFalse(delegatingMethod1.equals(delegatingMethod2));
        assertFalse(delegatingMethod1.equals(delegatingMethod3));
        assertFalse(delegatingMethod1.equals(delegatingMethod4));
        assertFalse(delegatingMethod2.equals(delegatingMethod3));
        assertFalse(delegatingMethod2.equals(delegatingMethod4));
        assertFalse(delegatingMethod3.equals(delegatingMethod4));

        assertTrue(delegatingMethod1.equals(delegatingMethod1));
        assertTrue(delegatingMethod1.equals(delegatingMethod3));
    }

    @Test
    public void testHashCode() {
        Method method1 = new Method(TestClass.class, "testMethod1", new Class[] {Integer.TYPE, String[].class});
        Method method2 = new Method(TestClass.class, "testMethod2", new Class[] {Integer.TYPE, String[].class});
        Method method3 = new Method(TestClass.class, "testMethod1", new Class[] {Integer.TYPE, String[].class});
        Method method4 = new Method(TestClass.class, "testMethod1", new Class[] {String[].class, Integer.TYPE});
        Method delegatingMethod1 = new DelegatingMethod(method1);
        Method delegatingMethod2 = new DelegatingMethod(method2);
        Method delegatingMethod3 = new DelegatingMethod(method3);
        Method delegatingMethod4 = new DelegatingMethod(method4);

        int hashCode1 = delegatingMethod1.hashCode();
        int hashCode2 = delegatingMethod2.hashCode();
        int hashCode3 = delegatingMethod3.hashCode();
        int hashCode4 = delegatingMethod4.hashCode();

        assertEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode4);
        assertNotEquals(hashCode2, hashCode3);
        assertNotEquals(hashCode2, hashCode4);
        assertNotEquals(hashCode3, hashCode4);
    }

    private static class TestClass {
        public void testMethod(int i, String[] args) {}

        public void testMethodWithException(int i, String[] args, RuntimeException e) throws RuntimeException {}

        public void testMethodWithReturnType(int i, String[] args) {
            return i + 1;
        }
    }

    private static abstract class TestAbstractClass {}
}