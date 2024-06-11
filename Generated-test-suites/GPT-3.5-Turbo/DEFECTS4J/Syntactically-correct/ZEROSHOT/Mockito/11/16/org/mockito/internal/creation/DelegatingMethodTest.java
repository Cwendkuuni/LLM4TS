package org.mockito.internal.creation;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.MockitoMethod;

import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        Assert.assertNotNull(exceptionTypes);
    }
    
    @Test
    public void testGetJavaMethod() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method javaMethod = delegatingMethod.getJavaMethod();
        Assert.assertNotNull(javaMethod);
    }
    
    @Test
    public void testGetName() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String name = delegatingMethod.getName();
        Assert.assertNull(name);
    }
    
    @Test
    public void testGetParameterTypes() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        Assert.assertNotNull(parameterTypes);
    }
    
    @Test
    public void testGetReturnType() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> returnType = delegatingMethod.getReturnType();
        Assert.assertNotNull(returnType);
    }
    
    @Test
    public void testIsVarArgs() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isVarArgs = delegatingMethod.isVarArgs();
        Assert.assertFalse(isVarArgs);
    }
    
    @Test
    public void testIsAbstract() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isAbstract = delegatingMethod.isAbstract();
        Assert.assertFalse(isAbstract);
    }
    
    @Test
    public void testEquals() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean equals = delegatingMethod.equals(null);
        Assert.assertFalse(equals);
    }
    
    @Test
    public void testHashCode() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        int hashCode = delegatingMethod.hashCode();
        Assert.assertEquals(1, hashCode);
    }

}