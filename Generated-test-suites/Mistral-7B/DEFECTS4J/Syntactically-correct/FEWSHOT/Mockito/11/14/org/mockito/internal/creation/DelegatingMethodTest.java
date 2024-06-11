package org.mockito.internal.creation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = getMethodExample();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertArrayEquals(new Class[0], delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testGetJavaMethod() {
        Method method = getMethodExample();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() {
        Method method = getMethodExample();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method.getName(), delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method method = getMethodExample();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertArrayEquals(method.getParameterTypes(), delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        Method method = getMethodExample();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method.getReturnType(), delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method method = getMethodExampleVarArgs();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isVarArgs());

        Method methodNoVarArgs = getMethodExampleNoVarArgs();
        DelegatingMethod delegatingMethodNoVarArgs = new DelegatingMethod(methodNoVarArgs);
        assertFalse(delegatingMethodNoVarArgs.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method abstractMethod = getMethodExampleAbstract();
        DelegatingMethod delegatingMethodAbstract = new DelegatingMethod(abstractMethod);
        assertTrue(delegatingMethodAbstract.isAbstract());

        Method nonAbstractMethod = getMethodExample();
        DelegatingMethod delegatingMethodNonAbstract = new DelegatingMethod(nonAbstractMethod);
        assertFalse(delegatingMethodNonAbstract.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = getMethodExample();
        Method method2 = getMethodExample();
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertTrue(delegatingMethod1.equals(delegatingMethod2));

        Method method3 = getMethodExampleDifferentName();
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(method3);
        assertFalse(delegatingMethod1.equals(delegatingMethod3));

        Object notInstance = new Object();
        assertFalse(delegatingMethod1.equals(notInstance));

        DelegatingMethod delegatingMethod4 = new DelegatingMethod(method1);
        assertTrue(delegatingMethod1.equals(delegatingMethod4));

        DelegatingMethod delegatingMethod5 = new DelegatingMethod(method1);
        assertTrue(delegatingMethod1.equals(delegatingMethod5));

        DelegatingMethod delegatingMethod6 = new DelegatingMethod(method2);
        assertFalse(delegatingMethod1.equals(delegatingMethod6));
    }

    @Test
    public void testHashCode() {
        Method method = getMethodExample();
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        assertEquals(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());
    }

    private Method getMethodExample() {
        return getClass().getDeclaredMethod("testGetExceptionTypes", new Class[0]);
    }

    private Method getMethodExampleVarArgs() {
        return getClass().getDeclaredMethod("testGetExceptionTypesVarArgs", new Class[0]);
    }

    private Method getMethodExampleNoVarArgs() {
        return getClass().getDeclaredMethod("testGetExceptionTypesNoVarArgs", new Class[0]);
    }

    private Method getMethodExampleAbstract() {
        return AbstractClassWithMethod.class.getDeclaredMethod("abstractMethod", new Class[0]);
    }

    private Method getMethodExampleDifferentName() {
        return getClass().getDeclaredMethod("testDifferentMethodName", new Class[0]);
    }

    static class AbstractClassWithMethod {
        abstract void abstractMethod();
    }
}