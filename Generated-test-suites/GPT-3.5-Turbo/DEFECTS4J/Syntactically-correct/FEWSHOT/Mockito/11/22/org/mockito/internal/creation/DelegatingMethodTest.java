package org.mockito.internal.creation;

import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {
    @Test
    public void testGetExceptionTypes() {
        //Test case 1
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] expectedOutput = null;
        assertArrayEquals(expectedOutput, delegatingMethod.getExceptionTypes());
        
        //Test case 2
        method = new Method();
        delegatingMethod = new DelegatingMethod(method);
        expectedOutput = new Class<?>[0];
        assertArrayEquals(expectedOutput, delegatingMethod.getExceptionTypes());
        
        //Test case 3
        //Add more test cases here
    }
    
    @Test
    public void testGetJavaMethod() {
        //Test case 1
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method expectedOutput = null;
        assertEquals(expectedOutput, delegatingMethod.getJavaMethod());
        
        //Test case 2
        method = new Method();
        delegatingMethod = new DelegatingMethod(method);
        expectedOutput = new Method();
        assertEquals(expectedOutput, delegatingMethod.getJavaMethod());
        
        //Test case 3
        //Add more test cases here
    }
    
    @Test
    public void testGetName() {
        //Test case 1
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String expectedOutput = null;
        assertEquals(expectedOutput, delegatingMethod.getName());
        
        //Test case 2
        method = new Method();
        delegatingMethod = new DelegatingMethod(method);
        String expectedOutput = "";
        assertEquals(expectedOutput, delegatingMethod.getName());
        
        //Test case 3
        //Add more test cases here
    }
    
    @Test
    public void testGetParameterTypes() {
        //Test case 1
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] expectedOutput = null;
        assertArrayEquals(expectedOutput, delegatingMethod.getParameterTypes());
        
        //Test case 2
        method = new Method();
        delegatingMethod = new DelegatingMethod(method);
        expectedOutput = new Class<?>[0];
        assertArrayEquals(expectedOutput, delegatingMethod.getParameterTypes());
        
        //Test case 3
        //Add more test cases here
    }
    
    @Test
    public void testGetReturnType() {
        //Test case 1
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> expectedOutput = null;
        assertEquals(expectedOutput, delegatingMethod.getReturnType());
        
        //Test case 2
        method = new Method();
        delegatingMethod = new DelegatingMethod(method);
        expectedOutput = new Class<?>();
        assertEquals(expectedOutput, delegatingMethod.getReturnType());
        
        //Test case 3
        //Add more test cases here
    }
    
    @Test
    public void testIsVarArgs() {
        //Test case 1
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean expectedOutput = false;
        assertEquals(expectedOutput, delegatingMethod.isVarArgs());
        
        //Test case 2
        method = new Method();
        delegatingMethod = new DelegatingMethod(method);
        boolean expectedOutput = false;
        assertEquals(expectedOutput, delegatingMethod.isVarArgs());
        
        //Test case 3
        //Add more test cases here
    }
    
    @Test
    public void testIsAbstract() {
        //Test case 1
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean expectedOutput = false;
        assertEquals(expectedOutput, delegatingMethod.isAbstract());
        
        //Test case 2
        method = new Method();
        delegatingMethod = new DelegatingMethod(method);
        boolean expectedOutput = false;
        assertEquals(expectedOutput, delegatingMethod.isAbstract());
        
        //Test case 3
        //Add more test cases here
    }
    
    @Test
    public void testEquals() {
        //Test case 1
        Method method = null;
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        Object delegatingMethod2 = null;
        boolean expectedOutput = false;
        assertEquals(expectedOutput, delegatingMethod1.equals(delegatingMethod2));
        
        //Test case 2
        method = new Method();
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        boolean expectedOutput = true;
        assertEquals(expectedOutput, delegatingMethod1.equals(delegatingMethod2));
        
        //Test case 3
        //Add more test cases here
    }
    
    @Test
    public void testHashCode() {
        //Test case 1
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        int expectedOutput = 1;
        assertEquals(expectedOutput, delegatingMethod.hashCode());
        
        //Test case 2
        method = new Method();
        delegatingMethod = new DelegatingMethod(method);
        int expectedOutput = 1;
        assertEquals(expectedOutput, delegatingMethod.hashCode());
        
        //Test case 3
        //Add more test cases here
    }
}