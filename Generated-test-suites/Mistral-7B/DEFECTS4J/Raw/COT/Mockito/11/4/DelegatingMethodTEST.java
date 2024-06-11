package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.DelegatingMethod;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method(Object.class.getName(), "methodWithExceptions", new Class[]{Integer.TYPE, String.class});
        method[0] = Integer[].class;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        Assert.assertArrayEquals(new Class[] {Integer[].class}, exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(Object.class.getName(), "methodWithNoArguments", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method result = delegatingMethod.getJavaMethod();
        Assert.assertEquals(method, result);
    }

    @Test
    public void testGetName() {
        Method method = new Method(Object.class.getName(), "methodWithName", new Class[0]);
        method.setName("testMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String result = delegatingMethod.getName();
        Assert.assertEquals("testMethod", result);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method(Object.class.getName(), "methodWithParameters", new Class[]{Integer.TYPE, String.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] result = delegatingMethod.getParameterTypes();
        Class<?>[] expected = new Class[]{Integer.TYPE, String.class};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method(Object.class.getName(), "methodWithReturnType", new Class[0]);
        method.setReturnType(String.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> result = delegatingMethod.getReturnType();
        Assert.assertEquals(String.class, result);
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method("java.util.List", "addAll", new Class[]{Class.class, java.util.Collection.class});
        method.setGenericReturnType(void.class);
        method.setSignature(new Signature("(Ljava/lang/Class;Ljava/util/Collection;)V"));
        method.setParameterTypes(new Class[]{Class.class, java.util.Collection.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean result = delegatingMethod.isVarArgs();
        Assert.assertTrue(result);
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method("java.util.List", "addAll", new Class[]{Class.class, java.util.Collection.class});
        method.setAccessible(true);
        method.setModifiers(method.getModifiers() | Modifier.ABSTRACT);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean result = delegatingMethod.isAbstract();
        Assert.assertTrue(result);
    }

    @Test(expected = AssertionError.class)
    public void testEqualsNull() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("Object", "method", new Class[0]));
        Object o = null;
        boolean result = delegatingMethod.equals(o);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualsSameMethod() {
        Method method = new Method("Object", "method", new Class[0]);
        Method method2 = new Method("Object", "method", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean result = delegatingMethod.equals(new DelegatingMethod(method2));
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualsDifferentMethods() {
        Method method = new Method("Object", "method1", new Class[0]);
        Method method2 = new Method("Object", "method2", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean result = delegatingMethod.equals(new DelegatingMethod(method2));
        Assert.assertFalse(result);
    }

    @Test
    public void testHashCode() {
        Method method = new Method("Object", "method", new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        int hashCode1 = delegatingMethod1.hashCode();
        int hashCode2 = delegatingMethod2.hashCode();
        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCodeDifferentMethods() {
        Method method1 = new Method("Object", "method1", new Class[0]);
        Method method2 = new Method("Object", "method2", new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        int hashCode1 = delegatingMethod1.hashCode();
        int hashCode2 = delegatingMethod2.hashCode();
        Assert.assertNotSame(hashCode1, hashCode2);
    }
}