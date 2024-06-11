package org.mockito.internal.creation;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {
    private Method method;

    public DelegatingMethodTest() {
        try {
            method = DelegatingMethod.class.getMethod("equals", Object.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConstructor() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Assert.assertEquals(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetExceptionTypes() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Assert.assertEquals(method.getExceptionTypes(), delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testGetName() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Assert.assertEquals(method.getName(), delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Assert.assertEquals(method.getParameterTypes(), delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Assert.assertEquals(method.getReturnType(), delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Assert.assertEquals(method.isVarArgs(), delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Assert.assertEquals((method.getModifiers() & Modifier.ABSTRACT) != 0, delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        Assert.assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Assert.assertEquals(1, delegatingMethod.hashCode());
    }
}