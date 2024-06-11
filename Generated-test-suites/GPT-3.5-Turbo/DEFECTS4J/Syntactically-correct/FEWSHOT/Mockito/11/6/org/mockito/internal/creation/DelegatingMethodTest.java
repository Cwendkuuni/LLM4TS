package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import java.lang.reflect.Method;

public class DelegatingMethodTest {
    
    @Test
    public void testGetExceptionTypes() {
        Method method = null; // replace with method object
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        Class<?>[] expected = {}; // replace with expected exception types
        Class<?>[] actual = delegatingMethod.getExceptionTypes();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetJavaMethod() {
        Method method = null; // replace with method object
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        Method expected = null; // replace with expected method object
        Method actual = delegatingMethod.getJavaMethod();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetName() {
        Method method = null; // replace with method object
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        String expected = ""; // replace with expected name
        String actual = delegatingMethod.getName();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetParameterTypes() {
        Method method = null; // replace with method object
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        Class<?>[] expected = {}; // replace with expected parameter types
        Class<?>[] actual = delegatingMethod.getParameterTypes();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetReturnType() {
        Method method = null; // replace with method object
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        Class<?> expected = null; // replace with expected return type
        Class<?> actual = delegatingMethod.getReturnType();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsVarArgs() {
        Method method = null; // replace with method object
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        boolean expected = false; // replace with expected value
        boolean actual = delegatingMethod.isVarArgs();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsAbstract() {
        Method method = null; // replace with method object
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        boolean expected = false; // replace with expected value
        boolean actual = delegatingMethod.isAbstract();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals() {
        Method method = null; // replace with method object
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        Object obj = null; // replace with object to compare
        boolean expected = false; // replace with expected value
        boolean actual = delegatingMethod.equals(obj);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHashCode() {
        Method method = null; // replace with method object
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        int expected = 1; // replace with expected value
        int actual = delegatingMethod.hashCode();
        
        assertEquals(expected, actual);
    }
}