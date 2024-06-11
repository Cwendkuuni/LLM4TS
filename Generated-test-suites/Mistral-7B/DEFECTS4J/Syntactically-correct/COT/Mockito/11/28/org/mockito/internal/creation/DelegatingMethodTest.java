package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.DelegatingMethod;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        Assert.assertArrayEquals(new Class[0], exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method javaMethod = delegatingMethod.getJavaMethod();
        Assert.assertSame(method, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = new Method("testMethod", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String name = delegatingMethod.getName();
        Assert.assertEquals(method.getName(), name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method("testMethod", new Class[]{int.class, String.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        Assert.assertArrayEquals(new Class[]{int.class, String.class}, parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method("testMethod", new Class[0], new Class[]{String.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> returnType = delegatingMethod.getReturnType();
        Assert.assertEquals(String.class, returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method("testMethod", new Class[]{int.class, String[].class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isVarArgs = delegatingMethod.isVarArgs();
        Assert.assertTrue(isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method("testMethod", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isAbstract = delegatingMethod.isAbstract();
        Assert.assertFalse(isAbstract);
    }

    @Test(expected = AssertionError.class)
    public void testEquals() {
        Method method1 = new Method(() -> {}, new Class[0]);
        Method method2 = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        delegatingMethod1.equals(delegatingMethod2);
    }

    @Test
    public void testHashCode() {
        Method method = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        Method method2 = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        Assert.assertNotSame(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());
    }
}