package org.mockito.internal.creation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.MockitoMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

public class DelegatingMethodTest {

    private DelegatingMethod delegatingMethod;
    private Method method;

    @Before
    public void setUp() throws Exception {
        method = MyClass.class.getDeclaredMethod("myMethod");
        delegatingMethod = new DelegatingMethod(method);
    }

    @Test
    public void testGetExceptionTypes() {
        Class<?>[] expected = method.getExceptionTypes();
        Class<?>[] actual = delegatingMethod.getExceptionTypes();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetJavaMethod() {
        Method expected = method;
        Method actual = delegatingMethod.getJavaMethod();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetName() {
        String expected = method.getName();
        String actual = delegatingMethod.getName();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetParameterTypes() {
        Class<?>[] expected = method.getParameterTypes();
        Class<?>[] actual = delegatingMethod.getParameterTypes();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetReturnType() {
        Class<?> expected = method.getReturnType();
        Class<?> actual = delegatingMethod.getReturnType();

        assertEquals(expected, actual);
    }

    @Test
    public void testIsVarArgs() {
        boolean expected = method.isVarArgs();
        boolean actual = delegatingMethod.isVarArgs();

        assertEquals(expected, actual);
    }

    @Test
    public void testIsAbstract() {
        boolean expected = (method.getModifiers() & Modifier.ABSTRACT) != 0;
        boolean actual = delegatingMethod.isAbstract();

        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        DelegatingMethod otherMethod = new DelegatingMethod(method);
        boolean expected = method.equals(otherMethod);
        boolean actual = delegatingMethod.equals(otherMethod);

        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        int expected = 1;
        int actual = delegatingMethod.hashCode();

        assertEquals(expected, actual);
    }
}

class MyClass {
    public void myMethod(){}
}