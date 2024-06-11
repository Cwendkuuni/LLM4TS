package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DelegatingMethodTest {
    
    private static final Method MOCKITO_METHOD = getMockitoMethod();
    
    private static Method getMockitoMethod() {
        try {
            return MockitoMethod.class.getMethod("getExceptionTypes");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Test
    public void testGetExceptionTypes() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(MOCKITO_METHOD);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertEquals(MOCKITO_METHOD.getExceptionTypes(), exceptionTypes);
    }
    
    @Test
    public void testGetJavaMethod() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(MOCKITO_METHOD);
        Method javaMethod = delegatingMethod.getJavaMethod();
        assertEquals(MOCKITO_METHOD, javaMethod);
    }
    
    @Test
    public void testGetName() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(MOCKITO_METHOD);
        String name = delegatingMethod.getName();
        assertEquals(MOCKITO_METHOD.getName(), name);
    }
    
    @Test
    public void testGetParameterTypes() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(MOCKITO_METHOD);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertEquals(MOCKITO_METHOD.getParameterTypes(), parameterTypes);
    }
    
    @Test
    public void testGetReturnType() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(MOCKITO_METHOD);
        Class<?> returnType = delegatingMethod.getReturnType();
        assertEquals(MOCKITO_METHOD.getReturnType(), returnType);
    }
    
    @Test
    public void testIsVarArgs() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(MOCKITO_METHOD);
        boolean isVarArgs = delegatingMethod.isVarArgs();
        assertEquals(MOCKITO_METHOD.isVarArgs(), isVarArgs);
    }
    
    @Test
    public void testIsAbstract() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(MOCKITO_METHOD);
        boolean isAbstract = delegatingMethod.isAbstract();
        assertFalse(isAbstract);
    }
    
    @Test
    public void testEquals() {
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(MOCKITO_METHOD);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(MOCKITO_METHOD);
        assertTrue(delegatingMethod1.equals(delegatingMethod2));
        
        Method method = getMockitoMethod();
        assertTrue(delegatingMethod1.equals(method));
    }
    
    @Test
    public void testHashCode() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(MOCKITO_METHOD);
        int hashCode = delegatingMethod.hashCode();
        assertEquals(1, hashCode);
    }
}