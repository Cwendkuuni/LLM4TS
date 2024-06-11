package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.Test;

public class DelegatingMethodTest {
    @Test
    public void testConstructor() throws Exception {
        Method method = String.class.getDeclaredMethod("length");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method, delegatingMethod.getJavaMethod());
    }

    @Test(expected = AssertionError.class)
    public void testConstructor_nullMethod() {
        new DelegatingMethod(null);
    }

    @Test
    public void testGetExceptionTypes() throws Exception {
        Method method = String.class.getDeclaredMethod("charAt", int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertEquals(1, exceptionTypes.length);
        assertEquals(IndexOutOfBoundsException.class, exceptionTypes[0]);
    }

    @Test
    public void testGetName() throws Exception {
        Method method = String.class.getDeclaredMethod("substring", int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals("substring", delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() throws Exception {
        Method method = String.class.getDeclaredMethod("indexOf", String.class, int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertEquals(2, parameterTypes.length);
        assertEquals(String.class, parameterTypes[0]);
        assertEquals(int.class, parameterTypes[1]);
    }

    @Test
    public void testGetReturnType() throws Exception {
        Method method = String.class.getDeclaredMethod("length");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(int.class, delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() throws Exception {
        Method method = String.class.getDeclaredMethod("split", String.class, int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsNotVarArgs() throws Exception {
        Method method = String.class.getDeclaredMethod("contains", CharSequence.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() throws Exception {
        Method method = AbstractClass.class.getDeclaredMethod("abstractMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isAbstract());
    }

    @Test
    public void testIsNotAbstract() throws Exception {
        Method method = ConcreteClass.class.getDeclaredMethod("concreteMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals_sameObject() throws Exception {
        Method method = String.class.getDeclaredMethod("length");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.equals(delegatingMethod));
    }

    @Test
    public void testEquals_differentClass() throws Exception {
        Method method = String.class.getDeclaredMethod("length");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.equals(method));
    }

    @Test
    public void testEquals_equalMethod() throws Exception {
        Method method1 = String.class.getDeclaredMethod("length");
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);

        Method method2 = String.class.getDeclaredMethod("length");
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testEquals_notEqualMethod() throws Exception {
        Method method1 = String.class.getDeclaredMethod("charAt", int.class);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);

        Method method2 = String.class.getDeclaredMethod("substring", int.class);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertFalse(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() throws Exception {
        Method method = String.class.getDeclaredMethod("length");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(1, delegatingMethod.hashCode());
    }
}

abstract class AbstractClass {
    abstract void abstractMethod();
}

class ConcreteClass {
    void concreteMethod() {
    }
}