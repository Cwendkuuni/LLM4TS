
package org.mockito.internal.creation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DelegatingMethodTest {
    private DelegatingMethod delegatingMethod;

    @Before
    public void setUp() throws Exception {
        Method method = DelegatingMethodTest.class.getMethod("someTestMethod");
        delegatingMethod = new DelegatingMethod(method);
    }

    @Test
    public void testGetExceptionTypes() {
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertEquals(0, exceptionTypes.length);
    }

    @Test
    public void testGetJavaMethod() {
        Method javaMethod = delegatingMethod.getJavaMethod();
        assertNotNull(javaMethod);
        assertEquals("someTestMethod", javaMethod.getName());
    }

    @Test
    public void testGetName() {
        String name = delegatingMethod.getName();
        assertEquals("someTestMethod", name);
    }

    @Test
    public void testGetParameterTypes() {
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertEquals(0, parameterTypes.length);
    }

    @Test
    public void testGetReturnType() {
        Class<?> returnType = delegatingMethod.getReturnType();
        assertEquals(Void.TYPE, returnType);
    }

    @Test
    public void testIsVarArgs() {
        assertFalse(delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method abstractMethod = mock(Method.class);
        when(abstractMethod.getModifiers()).thenReturn(Modifier.ABSTRACT);

        DelegatingMethod delegatingMethod = new DelegatingMethod(abstractMethod);
        assertTrue(delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() throws NoSuchMethodException {
        Method method1 = DelegatingMethodTest.class.getMethod("someTestMethod");
        Method method2 = DelegatingMethodTest.class.getMethod("someTestMethod");

        delegatingMethod = new DelegatingMethod(method1);
        DelegatingMethod otherMethod = new DelegatingMethod(method2);

        assertTrue(delegatingMethod.equals(otherMethod));
    }

    @Test
    public void testHashCode() {
        int hashCode = delegatingMethod.hashCode();
        assertEquals(1, hashCode);
    }

    @Test
    public void testNullMethodParameter() {
        try {
            new DelegatingMethod(null);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Method cannot be null", e.getMessage());
        }
    }

    public void someTestMethod() {
        // Test method
    }
}

