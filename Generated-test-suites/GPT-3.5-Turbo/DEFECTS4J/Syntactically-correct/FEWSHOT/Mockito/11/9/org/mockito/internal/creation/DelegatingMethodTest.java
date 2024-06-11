package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {
    
    @Test
    public void testGetExceptionTypes() {
        Method method1 = null;
        DelegatingMethod dm1 = new DelegatingMethod(method1);
        assertEquals(method1.getExceptionTypes(), dm1.getExceptionTypes());
        
        Method method2 = new Method();
        DelegatingMethod dm2 = new DelegatingMethod(method2);
        assertEquals(method2.getExceptionTypes(), dm2.getExceptionTypes());
    }

    @Test
    public void testGetJavaMethod() {
        Method method1 = null;
        DelegatingMethod dm1 = new DelegatingMethod(method1);
        assertEquals(method1, dm1.getJavaMethod());

        Method method2 = new Method();
        DelegatingMethod dm2 = new DelegatingMethod(method2);
        assertEquals(method2, dm2.getJavaMethod());
    }

    @Test
    public void testGetName() {
        Method method1 = null;
        DelegatingMethod dm1 = new DelegatingMethod(method1);
        assertEquals(method1.getName(), dm1.getName());

        Method method2 = new Method();
        DelegatingMethod dm2 = new DelegatingMethod(method2);
        assertEquals(method2.getName(), dm2.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method method1 = null;
        DelegatingMethod dm1 = new DelegatingMethod(method1);
        assertEquals(method1.getParameterTypes(), dm1.getParameterTypes());

        Method method2 = new Method();
        DelegatingMethod dm2 = new DelegatingMethod(method2);
        assertEquals(method2.getParameterTypes(), dm2.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        Method method1 = null;
        DelegatingMethod dm1 = new DelegatingMethod(method1);
        assertEquals(method1.getReturnType(), dm1.getReturnType());

        Method method2 = new Method();
        DelegatingMethod dm2 = new DelegatingMethod(method2);
        assertEquals(method2.getReturnType(), dm2.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method method1 = null;
        DelegatingMethod dm1 = new DelegatingMethod(method1);
        assertEquals(method1.isVarArgs(), dm1.isVarArgs());

        Method method2 = new Method();
        DelegatingMethod dm2 = new DelegatingMethod(method2);
        assertEquals(method2.isVarArgs(), dm2.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method1 = null;
        DelegatingMethod dm1 = new DelegatingMethod(method1);
        assertEquals((method1.getModifiers() & Modifier.ABSTRACT) != 0, dm1.isAbstract());

        Method method2 = new Method();
        DelegatingMethod dm2 = new DelegatingMethod(method2);
        assertEquals((method2.getModifiers() & Modifier.ABSTRACT) != 0, dm2.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = null;
        DelegatingMethod dm1 = new DelegatingMethod(method1);
        assertTrue(dm1.equals(method1));

        Method method2 = new Method();
        DelegatingMethod dm2 = new DelegatingMethod(method2);
        assertTrue(dm2.equals(method2));

        DelegatingMethod dm3 = new DelegatingMethod(method2);
        assertTrue(dm2.equals(dm3));

        DelegatingMethod dm4 = new DelegatingMethod(null);
        assertFalse(dm4.equals(dm2));
      
        assertFalse(dm2.equals(null));
    }

    @Test
    public void testHashCode() {
        Method method1 = null;
        DelegatingMethod dm1 = new DelegatingMethod(method1);
        assertEquals(1, dm1.hashCode());

        Method method2 = new Method();
        DelegatingMethod dm2 = new DelegatingMethod(method2);
        assertEquals(1, dm2.hashCode());

        DelegatingMethod dm3 = new DelegatingMethod(null);
        assertEquals(1, dm3.hashCode());
    }
}