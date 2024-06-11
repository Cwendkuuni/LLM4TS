package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.lang.reflect.Method;

public class DelegatingMethodTest {
    @Test
    public void testGetExceptionTypes() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(null, delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testGetJavaMethod() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(null, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(null, delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(null, delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(null, delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(false, delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(false, delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Object o = null;
        assertEquals(true, delegatingMethod.equals(o));
    }

    @Test
    public void testHashCode() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(1, delegatingMethod.hashCode());
    }
}