package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.DelegatingMethod;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes_emptyArray() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(TestClass.class, "testMethod", new Class[0]));
        Class<?>[] actual = delegatingMethod.getExceptionTypes();
        Assert.assertArrayEquals(new Class[0], actual);
    }

    @Test
    public void testGetExceptionTypes_nonEmptyArray() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(TestClass.class, "testMethod", new Class[]{String.class, Integer.class}));
        Class<?>[] actual = delegatingMethod.getExceptionTypes();
        Class<?>[] expected = {Exception1.class, Exception2.class};
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetJavaMethod_nullArg() {
        Assert.assertThrows(NullPointerException.class, () -> new DelegatingMethod(null));
    }

    @Test
    public void testGetJavaMethod_validArg() {
        Method javaMethod = new Method(TestClass.class, "testMethod", new Class[]{String.class, Integer.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(javaMethod);
        Method actual = delegatingMethod.getJavaMethod();
        Assert.assertEquals(javaMethod, actual);
    }

    @Test
    public void testGetName_nullArg() {
        Assert.assertThrows(NullPointerException.class, () -> new DelegatingMethod(null));
    }

    @Test
    public void testGetName_validArg() {
        Method javaMethod = new Method(TestClass.class, "testMethod", new Class[]{String.class, Integer.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(javaMethod);
        String actual = delegatingMethod.getName();
        String expected = "testMethod";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetParameterTypes_emptyArray() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(TestClass.class, "testMethod", new Class[0]));
        Class<?>[] actual = delegatingMethod.getParameterTypes();
        Assert.assertArrayEquals(new Class[0], actual);
    }

    @Test
    public void testGetParameterTypes_nonEmptyArray() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(TestClass.class, "testMethod", new Class[]{String.class, Integer.class}));
        Class<?>[] actual = delegatingMethod.getParameterTypes();
        Class<?>[] expected = {String.class, Integer.class};
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetReturnType_voidMethod() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(TestClass.class, "testVoidMethod", new Class[0]));
        Class<?> actual = delegatingMethod.getReturnType();
        Assert.assertEquals(void.class, actual);
    }

    @Test
    public void testGetReturnType_nonVoidMethod() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(TestClass.class, "testMethod", new Class[]{String.class, Integer.class}));
        Class<?> actual = delegatingMethod.getReturnType();
        Class<?> expected = String.class;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsAbstract_abstractMethod() {
        Method javaMethod = new Method(TestClass.class, "testAbstractMethod", new Class[0]);
        javaMethod.setAccessible(true);
        javaMethod.setModifiers(javaMethod.getModifiers() | Modifier.ABSTRACT);
        DelegatingMethod delegatingMethod = new DelegatingMethod(javaMethod);
        boolean actual = delegatingMethod.isAbstract();
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsAbstract_nonAbstractMethod() {
        Method javaMethod = new Method(TestClass.class, "testMethod", new Class[]{String.class, Integer.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(javaMethod);
        boolean actual = delegatingMethod.isAbstract();
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEquals_sameMethod() {
        Method javaMethod = new Method(TestClass.class, "testMethod", new Class[]{String.class, Integer.class});
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(javaMethod);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(javaMethod);
        boolean actual = delegatingMethod1.equals(delegatingMethod2);
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEquals_differentClasses() {
        Method javaMethod1 = new Method(TestClass1.class, "testMethod", new Class[]{String.class, Integer.class});
        Method javaMethod2 = new Method(TestClass2.class, "testMethod", new Class[]{String.class, Integer.class});
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(javaMethod1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(javaMethod2);
        boolean actual = delegatingMethod1.equals(delegatingMethod2);
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEquals_differentMethods() {
        Method javaMethod1 = new Method(TestClass.class, "testMethod1", new Class[]{String.class, Integer.class});
        Method javaMethod2 = new Method(TestClass.class, "testMethod2", new Class[]{String.class, Integer.class});
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(javaMethod1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(javaMethod2);
        boolean actual = delegatingMethod1.equals(delegatingMethod2);
        boolean expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHashCode_sameMethods() {
        Method javaMethod = new Method(TestClass.class, "testMethod", new Class[]{String.class, Integer.class});
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(javaMethod);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(javaMethod);
        int actual1 = delegatingMethod1.hashCode();
        int actual2 = delegatingMethod2.hashCode();
        int expected = actual1;
        Assert.assertEquals(expected, actual2);
    }

    @Test
    public void testHashCode_differentMethods() {
        Method javaMethod1 = new Method(TestClass.class, "testMethod1", new Class[]{String.class, Integer.class});
        Method javaMethod2 = new Method(TestClass.class, "testMethod2", new Class[]{String.class, Integer.class});
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(javaMethod1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(javaMethod2);
        int actual1 = delegatingMethod1.hashCode();
        int actual2 = delegatingMethod2.hashCode();
        int expected = actual1;
        Assert.assertNotEquals(expected, actual2);
    }

    static class TestClass {
        public void testMethod(String arg1, int arg2) {}
        public void testVoidMethod() {}
        public abstract void testAbstractMethod();
    }

    static class TestClass1 {}

    static class TestClass2 {}

    static class Exception1 extends Exception {}

    static class Exception2 extends Exception {}
}