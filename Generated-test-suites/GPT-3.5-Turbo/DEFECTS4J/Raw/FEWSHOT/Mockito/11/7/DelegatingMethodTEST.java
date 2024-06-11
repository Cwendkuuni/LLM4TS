package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.MockitoMethod;

public class DelegatingMethodTest {
    
    private Method mockMethod;
    private DelegatingMethod delegatingMethod;
    
    @Before
    public void setUp() throws Exception {
        mockMethod = mock(Method.class);
        delegatingMethod = new DelegatingMethod(mockMethod);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(mockMethod, delegatingMethod.getJavaMethod());
    }
    
    @Test
    public void testGetExceptionTypes() {
        Class<?>[] exceptionTypes = new Class<?>[] { Exception.class };
        when(mockMethod.getExceptionTypes()).thenReturn(exceptionTypes);
        
        assertEquals(exceptionTypes, delegatingMethod.getExceptionTypes());
    }
    
    @Test
    public void testGetName() {
        String methodName = "testMethod";
        when(mockMethod.getName()).thenReturn(methodName);
        
        assertEquals(methodName, delegatingMethod.getName());
    }
    
    @Test
    public void testGetParameterTypes() {
        Class<?>[] parameterTypes = new Class<?>[] { String.class, Integer.class };
        when(mockMethod.getParameterTypes()).thenReturn(parameterTypes);
        
        assertEquals(parameterTypes, delegatingMethod.getParameterTypes());
    }
    
    @Test
    public void testGetReturnType() {
        Class<?> returnType = String.class;
        when(mockMethod.getReturnType()).thenReturn(returnType);
        
        assertEquals(returnType, delegatingMethod.getReturnType());
    }
    
    @Test
    public void testIsVarArgs() {
        when(mockMethod.isVarArgs()).thenReturn(true);
        
        assertTrue(delegatingMethod.isVarArgs());
        
        when(mockMethod.isVarArgs()).thenReturn(false);
        
        assertFalse(delegatingMethod.isVarArgs());
    }
    
    @Test
    public void testIsAbstract() {
        int modifiers = Modifier.ABSTRACT;
        when(mockMethod.getModifiers()).thenReturn(modifiers);
        
        assertTrue(delegatingMethod.isAbstract());
        
        modifiers = 0;
        when(mockMethod.getModifiers()).thenReturn(modifiers);
        
        assertFalse(delegatingMethod.isAbstract());
    }
    
    @Test
    public void testEquals() {
        Method otherMethod = mock(Method.class);
        MockitoMethod otherDelegatingMethod = new DelegatingMethod(otherMethod);
        
        when(mockMethod.equals(otherMethod)).thenReturn(true);
        
        assertTrue(delegatingMethod.equals(otherDelegatingMethod));
        
        when(mockMethod.equals(otherMethod)).thenReturn(false);
        
        assertFalse(delegatingMethod.equals(otherDelegatingMethod));
        
        assertFalse(delegatingMethod.equals(null));
        assertFalse(delegatingMethod.equals("test"));
    }
    
    @Test
    public void testHashCode() {
        int expectedHashCode = 1;
        
        assertEquals(expectedHashCode, delegatingMethod.hashCode());
    }
}