package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.Before;
import org.junit.Test;

public class DelegatingMethodTest {
    private DelegatingMethod delegatingMethod;

    @Before
    public void setup() throws Exception {
        Method method = DelegatingMethod.class.getDeclaredMethod("getName");
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
        assertEquals("getName", javaMethod.getName());
    }

    @Test
    public void testGetName() {
        String name = delegatingMethod.getName();
        assertEquals("getName", name);
    }

    @Test
    public void testGetParameterTypes() {
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertEquals(0, parameterTypes.length);
    }

    @Test
    public void testGetReturnType() {
        Class<?> returnType = delegatingMethod.getReturnType();
        assertEquals(String.class, returnType);
    }

    @Test
    public void testIsVarArgs() {
        boolean isVarArgs = delegatingMethod.isVarArgs();
        assertFalse(isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        boolean isAbstract = delegatingMethod.isAbstract();
        assertFalse(isAbstract);
    }

    @Test
    public void testEquals() throws Exception {
        Method method = DelegatingMethod.class.getDeclaredMethod("getName");
        DelegatingMethod other = new DelegatingMethod(method);
        assertTrue(delegatingMethod.equals(other));
    }

    @Test
    public void testHashCode() {
        int hashCode = delegatingMethod.hashCode();
        assertEquals(1, hashCode);
    }
}