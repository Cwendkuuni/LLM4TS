package org.mockito.internal.creation;

import org.junit.Test;
import org.mockito.internal.invocation.MockitoMethod;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = null; // TODO: Create a method object
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        Class<?>[] expected = null; // TODO: Define expected exception types
        Class<?>[] actual = delegatingMethod.getExceptionTypes();
        
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testGetJavaMethod() {
        Method method = null; // TODO: Create a method object
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        Method expected = null; // TODO: Define expected Java method
        Method actual = delegatingMethod.getJavaMethod();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetName() {
        Method method = null; // TODO: Create a method object
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        String expected = null; // TODO: Define expected name
        String actual = delegatingMethod.getName();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetParameterTypes() {
        Method method = null; // TODO: Create a method object
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        Class<?>[] expected = null; // TODO: Define expected parameter types
        Class<?>[] actual = delegatingMethod.getParameterTypes();
        
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testGetReturnType() {
        Method method = null; // TODO: Create a method object
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        Class<?> expected = null; // TODO: Define expected return type
        Class<?> actual = delegatingMethod.getReturnType();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsVarArgs() {
        Method method = null; // TODO: Create a method object
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        boolean expected = false; // TODO: Define expected value
        boolean actual = delegatingMethod.isVarArgs();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsAbstract() {
        Method method = null; // TODO: Create a method object
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        boolean expected = false; // TODO: Define expected value
        boolean actual = delegatingMethod.isAbstract();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals() {
        Method method = null; // TODO: Create a method object
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        Object obj = null; // TODO: Create an object for comparison
        boolean expected = false; // TODO: Define expected value
        boolean actual = delegatingMethod.equals(obj);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHashCode() {
        Method method = null; // TODO: Create a method object
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        int expected = 1;
        int actual = delegatingMethod.hashCode();
        
        assertEquals(expected, actual);
    }
    
}