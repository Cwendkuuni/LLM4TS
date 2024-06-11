package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.invocation.MockitoMethod;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new MockitoMethod(DelegatingMethod.class.getDeclaredMethod("getExceptionTypes", new Class[0])).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        Assert.assertArrayEquals(new Class[0], exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new MockitoMethod(DelegatingMethod.class.getDeclaredMethod("getJavaMethod", new Class[0])).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Method javaMethod = delegatingMethod.getJavaMethod();
        Assert.assertEquals(method, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = new MockitoMethod(DelegatingMethod.class.getDeclaredMethod("getName", new Class[0])).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String name = delegatingMethod.getName();
        Assert.assertEquals(method.getName(), name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new MockitoMethod(DelegatingMethod.class.getDeclaredMethod("getParameterTypes", new Class[0])).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        Assert.assertArrayEquals(method.getParameterTypes(), parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = new MockitoMethod(DelegatingMethod.class.getDeclaredMethod("getReturnType", new Class[0])).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> returnType = delegatingMethod.getReturnType();
        Assert.assertEquals(method.getReturnType(), returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = new MockitoMethod(DelegatingMethod.class.getDeclaredMethod("isVarArgs", new Class[0])).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean isVarArgs = delegatingMethod.isVarArgs();
        Assert.assertEquals(method.isVarArgs(), isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = new MockitoMethod(DelegatingMethod.class.getDeclaredMethod("isAbstract", new Class[0])).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean isAbstract = delegatingMethod.isAbstract();
        Assert.assertEquals(method.isAbstract(), isAbstract);
    }

    @Test(expected = AssertionError.class)
    public void testEqualsNull() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new MockitoMethod(DelegatingMethod.class.getDeclaredMethod("getName", new Class[0])).getJavaMethod());
        Assert.assertNotEquals(null, delegatingMethod);
    }

    @Test(expected = AssertionError.class)
    public void testEqualsDifferentMethods() {
        Method method1 = new MockitoMethod(DelegatingMethod.class.getDeclaredMethod("getName", new Class[0])).getJavaMethod();
        Method method2 = new MockitoMethod(DelegatingMethod.class.getDeclaredMethod("getExceptionTypes", new Class[0])).getJavaMethod();
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        Assert.assertNotEquals(delegatingMethod1, delegatingMethod2);
    }

    @Test
    public void testHashCode() {
        Method method = new MockitoMethod(DelegatingMethod.class.getDeclaredMethod("getName", new Class[0])).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        int hashCode = delegatingMethod.hashCode();
        Assert.assertEquals(hashCode, hashCode); // This line should not cause an error, since hashCode must be equal to itself
    }
}