package org.mockito.internal.creation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.Test;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { String.class, int.class });
        method.setExceptionTypes(new Class[] { IllegalArgumentException.class, RuntimeException.class });
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertArrayEquals(new Class[] { IllegalArgumentException.class, RuntimeException.class }, exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { String.class, int.class });
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method javaMethod = delegatingMethod.getJavaMethod();

        assertSame(method, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { String.class, int.class });
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals("testMethod", delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { String.class, int.class });
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertArrayEquals(new Class[] { String.class, int.class }, delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { String.class, int.class });
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(Void.TYPE, delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { String[].class });
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertTrue(delegatingMethod.isVarArgs());

        method = new Method(Object.class.getName(), "testMethod", new Class[] { String.class, int.class });
        delegatingMethod = new DelegatingMethod(method);

        assertFalse(delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method("AbstractClass", "testMethod", new Class[] { String.class, int.class });
        Modifier[] modifiers = new Modifier[] { Modifier.ABSTRACT };
        method.setModifiers(Modifier.combine(method.getModifiers(), modifiers));
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertTrue(delegatingMethod.isAbstract());

        method = new Method(Object.class.getName(), "testMethod", new Class[] { String.class, int.class });
        delegatingMethod = new DelegatingMethod(method);

        assertFalse(delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = new Method(Object.class.getName(), "testMethod1", new Class[] { String.class, int.class });
        Method method2 = new Method(Object.class.getName(), "testMethod2", new Class[] { String.class, int.class });
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertFalse(delegatingMethod1.equals(delegatingMethod2));

        Method method3 = method1;
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(method3);

        assertTrue(delegatingMethod1.equals(delegatingMethod3));
    }

    @Test
    public void testHashCode() {
        Method method = new Method(Object.class.getName(), "testMethod", new Class[] { String.class, int.class });
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);

        assertEquals(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());
    }

    @Test
    public void testNull() {
        assertNull(new DelegatingMethod(null));
    }
}