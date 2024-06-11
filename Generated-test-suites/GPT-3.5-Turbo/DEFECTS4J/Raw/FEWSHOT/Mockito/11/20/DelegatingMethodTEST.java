package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {
    @Test
    public void testConstructor() {
        Class<?>[] params = {Method.class};
        Method method = Util.getMethod("getMethod", params);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertNotNull(delegatingMethod);
    }

    @Test
    public void testGetExceptionTypes() {
        Class<?>[] params = {};
        Method method = Util.getMethod("getExceptionTypes", params);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] expected = {};
        Class<?>[] actual = delegatingMethod.getExceptionTypes();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetJavaMethod() {
        Class<?>[] params = {};
        Method method = Util.getMethod("getJavaMethod", params);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Method expected = method;
        Method actual = delegatingMethod.getJavaMethod();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetName() {
        Class<?>[] params = {};
        Method method = Util.getMethod("getName", params);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String expected = "getName";
        String actual = delegatingMethod.getName();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetParameterTypes() {
        Class<?>[] params = {};
        Method method = Util.getMethod("getParameterTypes", params);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] expected = {};
        Class<?>[] actual = delegatingMethod.getParameterTypes();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetReturnType() {
        Class<?>[] params = {};
        Method method = Util.getMethod("getReturnType", params);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> expected = Object.class;
        Class<?> actual = delegatingMethod.getReturnType();

        assertEquals(expected, actual);
    }

    @Test
    public void testIsVarArgs() {
        Class<?>[] params = {};
        Method method = Util.getMethod("isVarArgs", params);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean expected = false;
        boolean actual = delegatingMethod.isVarArgs();

        assertEquals(expected, actual);
    }

    @Test
    public void testIsAbstract() {
        Class<?>[] params = {};
        Method method = Util.getMethod("isAbstract", params);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean expected = false;
        boolean actual = delegatingMethod.isAbstract();

        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Class<?>[] params = {Object.class};
        Method method = Util.getMethod("equals", params);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);

        boolean expected = true;
        boolean actual = delegatingMethod1.equals(delegatingMethod2);

        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        Class<?>[] params = {};
        Method method = Util.getMethod("hashCode", params);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        int expected = 1;
        int actual = delegatingMethod.hashCode();

        assertEquals(expected, actual);
    }
}