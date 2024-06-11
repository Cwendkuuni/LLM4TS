package org.mockito.internal.creation;

import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {

    @Test
    public void testConstructor() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetExceptionTypes() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertArrayEquals(method.getExceptionTypes(), delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testGetName() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method.getName(), delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertArrayEquals(method.getParameterTypes(), delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method.getReturnType(), delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method.isVarArgs(), delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals((method.getModifiers() & Modifier.ABSTRACT) != 0, delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(1, delegatingMethod.hashCode());
    }

    private Method getTestMethod() {
        try {
            return DelegatingMethodTest.class.getDeclaredMethod("testMethod");
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private void testMethod() {
        // Test method, do nothing
    }
}