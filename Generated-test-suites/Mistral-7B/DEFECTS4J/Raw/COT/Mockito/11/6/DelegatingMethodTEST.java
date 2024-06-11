package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.DelegatingMethod;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method(() -> {}, new Class[0], new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        Assert.assertArrayEquals(new Class[0], exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(() -> {}, new Class[0], new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method javaMethod = delegatingMethod.getJavaMethod();
        Assert.assertEquals(method, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = new Method(() -> {}, new Class[0], new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String name = delegatingMethod.getName();
        Assert.assertEquals(method.getName(), name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method(() -> {}, new Class[]{Integer.TYPE, String.class}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        Assert.assertArrayEquals(new Class[]{Integer.TYPE, String.class}, parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method(() -> new Integer(1), new Class[0], new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> returnType = delegatingMethod.getReturnType();
        Assert.assertEquals(Integer.TYPE, returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method(() -> {}, new Class[0], new Class[]{Integer[].class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isVarArgs = delegatingMethod.isVarArgs();
        Assert.assertTrue(isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method(() -> {}, new Class[0], new Class[0]);
        Method abstractMethod = new Method(() -> {}, new Class[0], new Class[0]);
        Modifier modifier1 = Modifier.PUBLIC;
        Modifier modifier2 = Modifier.ABSTRACT;
        method.setModifiers(modifier1);
        abstractMethod.setModifiers(modifier1 | modifier2);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        boolean isAbstract1 = delegatingMethod1.isAbstract();
        Assert.assertFalse(isAbstract1);

        DelegatingMethod delegatingMethod2 = new DelegatingMethod(abstractMethod);
        boolean isAbstract2 = delegatingMethod2.isAbstract();
        Assert.assertTrue(isAbstract2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualsNull() {
        Method method = new Method(() -> {}, new Class[0], new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        delegatingMethod.equals(null);
    }

    @Test
    public void testHashCode() {
        Method method1 = new Method(() -> {}, new Class[0], new Class[0]);
        Method method2 = new Method(() -> {}, new Class[0], new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        int hashCode1 = delegatingMethod1.hashCode();
        int hashCode2 = delegatingMethod2.hashCode();
        Assert.assertEquals(hashCode1, hashCode2);
    }
}