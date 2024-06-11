package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new MockitoMethod(DelegatingMethodTest.class.getMethod("testGetExceptionTypes", int.class, int[].class)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();

        Assert.assertArrayEquals(new Class<?>[]{Exception.class}, exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new MockitoMethod(DelegatingMethodTest.class.getMethod("testGetJavaMethod")).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Assert.assertEquals(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() {
        Method method = new MockitoMethod(DelegatingMethodTest.class.getMethod("testGetName")).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Assert.assertEquals("testGetName", delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new MockitoMethod(DelegatingMethodTest.class.getMethod("testGetParameterTypes", int.class, int[].class)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();

        Assert.assertArrayEquals(new Class<?>[]{int.class, int[].class}, parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = new MockitoMethod(DelegatingMethodTest.class.getMethod("testGetReturnType")).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Assert.assertEquals(void.class, delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method method = new MockitoMethod(DelegatingMethodTest.class.getMethod("testIsVarArgs", int[].class)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Assert.assertTrue(delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = new MockitoMethod(DelegatingMethodTest.class.getDeclaredMethod("testIsAbstract")).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Assert.assertFalse(delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = new MockitoMethod(DelegatingMethodTest.class.getMethod("testEquals", Object.class)).getJavaMethod();
        Method method2 = new MockitoMethod(DelegatingMethodTest.class.getMethod("testEquals", Object.class)).getJavaMethod();
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        Assert.assertTrue(delegatingMethod1.equals(delegatingMethod2));

        Object notDelegatingMethod = new Object();
        Assert.assertFalse(delegatingMethod1.equals(notDelegatingMethod));

        Method differentMethod = new MockitoMethod(DelegatingMethodTest.class.getMethod("testGetExceptionTypes", int.class, int[].class)).getJavaMethod();
        DelegatingMethod differentDelegatingMethod = new DelegatingMethod(differentMethod);
        Assert.assertFalse(delegatingMethod1.equals(differentDelegatingMethod));
    }

    @Test
    public void testHashCode() {
        Method method1 = new MockitoMethod(DelegatingMethodTest.class.getMethod("testHashCode")).getJavaMethod();
        Method method2 = new MockitoMethod(DelegatingMethodTest.class.getMethod("testHashCode")).getJavaMethod();
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        Assert.assertEquals(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());

        Assert.assertNotEquals(delegatingMethod1.hashCode(), 1);
    }
}