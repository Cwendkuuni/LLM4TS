package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.DelegatingMethod;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[]{Integer.TYPE, String.class});
        Exception[] exceptionTypes = {NullPointerException.class, IllegalArgumentException.class};
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] actualExceptionTypes = delegatingMethod.getExceptionTypes();
        Assert.assertArrayEquals(exceptionTypes, actualExceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[]{Integer.TYPE, String.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method actualJavaMethod = delegatingMethod.getJavaMethod();
        Assert.assertEquals(method, actualJavaMethod);
    }

    @Test
    public void testGetName() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[]{Integer.TYPE, String.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String actualName = delegatingMethod.getName();
        Assert.assertEquals("testMethod", actualName);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[]{Integer.TYPE, String.class});
        Class<?>[] parameterTypes = {Integer.TYPE, String.class};
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] actualParameterTypes = delegatingMethod.getParameterTypes();
        Assert.assertArrayEquals(parameterTypes, actualParameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[]{Integer.TYPE, String.class});
        Class<?> returnType = void.class; // Assuming testMethod returns void in this case
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> actualReturnType = delegatingMethod.getReturnType();
        Assert.assertEquals(returnType, actualReturnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[]{Integer.TYPE, String[].class});
        boolean isVarArgs = true;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean actualIsVarArgs = delegatingMethod.isVarArgs();
        Assert.assertEquals(isVarArgs, actualIsVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[0]);
        boolean isAbstract = false;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean actualIsAbstract = delegatingMethod.isAbstract();
        Assert.assertEquals(isAbstract, actualIsAbstract);

        method = new Method("AbstractClass", "abstractMethod", new Class[0]);
        isAbstract = true;
        delegatingMethod = new DelegatingMethod(method);
        actualIsAbstract = delegatingMethod.isAbstract();
        Assert.assertEquals(isAbstract, actualIsAbstract);
    }

    @Test(expected = AssertionError.class)
    public void testEqualsNull() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("Class", "method", new Class[0]));
        delegatingMethod.equals(null);
    }

    @Test
    public void testEqualsSameMethod() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[]{Integer.TYPE, String.class});
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        boolean actual = delegatingMethod1.equals(delegatingMethod2);
        Assert.assertTrue(actual);
    }

    @Test
    public void testEqualsDifferentMethods() {
        Method method1 = new Method(Object.class.getName(), "testMethod1", new Class[]{Integer.TYPE, String.class});
        Method method2 = new Method(Object.class.getName(), "testMethod2", new Class[]{Integer.TYPE, String.class});
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        boolean actual = delegatingMethod1.equals(delegatingMethod2);
        Assert.assertFalse(actual);
    }

    @Test
    public void testHashCode() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[]{Integer.TYPE, String.class});
        int expectedHashCode = 123; // Arbitrary value for testing
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        int actualHashCode = delegatingMethod.hashCode();
        Assert.assertEquals(expectedHashCode, actualHashCode);
    }
}