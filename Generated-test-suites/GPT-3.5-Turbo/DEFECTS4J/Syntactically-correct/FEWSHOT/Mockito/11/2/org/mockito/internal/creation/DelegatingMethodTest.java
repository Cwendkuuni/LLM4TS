package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = SomeClass.class.getMethod("someMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        Class<?>[] expected = {};
        Class<?>[] actual = delegatingMethod.getExceptionTypes();
        
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testGetJavaMethod() {
        Method method = SomeClass.class.getMethod("someMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        Method expected = method;
        Method actual = delegatingMethod.getJavaMethod();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetName() {
        Method method = SomeClass.class.getMethod("someMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        String expected = "someMethod";
        String actual = delegatingMethod.getName();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetParameterTypes() {
        Method method = SomeClass.class.getMethod("someMethod", String.class, int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        Class<?>[] expected = {String.class, int.class};
        Class<?>[] actual = delegatingMethod.getParameterTypes();
        
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testGetReturnType() {
        Method method = SomeClass.class.getMethod("someMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        Class<?> expected = void.class;
        Class<?> actual = delegatingMethod.getReturnType();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsVarArgs() {
        Method method = SomeClass.class.getMethod("someMethod", String[].class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        boolean expected = true;
        boolean actual = delegatingMethod.isVarArgs();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsAbstract() {
        Method method = SomeClass.class.getMethod("someMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        boolean expected = false;
        boolean actual = delegatingMethod.isAbstract();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals() {
        Method method1 = SomeClass.class.getMethod("someMethod");
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method1);
        Method method2 = SomeClass.class.getMethod("otherMethod");
        
        assertTrue(delegatingMethod1.equals(method1));
        assertTrue(delegatingMethod1.equals(delegatingMethod2));
        assertFalse(delegatingMethod1.equals(method2));
    }
    
    @Test
    public void testHashCode() {
        Method method = SomeClass.class.getMethod("someMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        int expected = 1;
        int actual = delegatingMethod.hashCode();
        
        assertEquals(expected, actual);
    }
}

class SomeClass {
    public void someMethod() {}
    
    public void otherMethod() {}
}