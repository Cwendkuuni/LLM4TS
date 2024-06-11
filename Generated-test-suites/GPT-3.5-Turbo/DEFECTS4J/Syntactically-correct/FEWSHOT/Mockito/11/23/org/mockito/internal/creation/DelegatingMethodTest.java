package org.mockito.internal.creation;

import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {

    @Test
    public void testConstructor() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertNotNull(delegatingMethod);
    }

    @Test
    public void testGetExceptionTypes() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] expected = null;
        Class<?>[] result = delegatingMethod.getExceptionTypes();
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method expected = null;
        Method result = delegatingMethod.getJavaMethod();
        assertEquals(expected, result);
    }

    @Test
    public void testGetName() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String expected = null;
        String result = delegatingMethod.getName();
        assertEquals(expected, result);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] expected = null;
        Class<?>[] result = delegatingMethod.getParameterTypes();
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetReturnType() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> expected = null;
        Class<?> result = delegatingMethod.getReturnType();
        assertEquals(expected, result);
    }

    @Test
    public void testIsVarArgs() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean expected = false;
        boolean result = delegatingMethod.isVarArgs();
        assertEquals(expected, result);
    }

    @Test
    public void testIsAbstract() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean expected = false;
        boolean result = delegatingMethod.isAbstract();
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Object o = null;
        boolean expected = false;
        boolean result = delegatingMethod.equals(o);
        assertEquals(expected, result);
    }

    @Test
    public void testHashCode() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        int expected = 1;
        int result = delegatingMethod.hashCode();
        assertEquals(expected, result);
    }
}