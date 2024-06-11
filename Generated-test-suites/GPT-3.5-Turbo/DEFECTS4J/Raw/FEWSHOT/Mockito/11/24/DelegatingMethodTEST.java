
package org.mockito.internal.creation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() throws NoSuchMethodException {
        Method method = DelegatingMethod.class.getDeclaredMethod("getExceptionTypes");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertEquals(0, exceptionTypes.length);
    }

    @Test
    public void testGetJavaMethod() throws NoSuchMethodException {
        Method method = DelegatingMethod.class.getDeclaredMethod("getJavaMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() throws NoSuchMethodException {
        Method method = DelegatingMethod.class.getDeclaredMethod("getName");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals("getName", delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() throws NoSuchMethodException {
        Method method = DelegatingMethod.class.getDeclaredMethod("getParameterTypes");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertEquals(0, parameterTypes.length);
    }

    @Test
    public void testGetReturnType() throws NoSuchMethodException {
        Method method = DelegatingMethod.class.getDeclaredMethod("getReturnType");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(Class.class, delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() throws NoSuchMethodException {
        Method method = DelegatingMethod.class.getDeclaredMethod("isVarArgs");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() throws NoSuchMethodException {
        Method method = DelegatingMethod.class.getDeclaredMethod("isAbstract");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() throws NoSuchMethodException {
        Method method1 = DelegatingMethod.class.getDeclaredMethod("equals", Object.class);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        assertTrue(delegatingMethod1.equals(delegatingMethod1));

        Method method2 = DelegatingMethod.class.getDeclaredMethod("equals", Object.class);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertFalse(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() throws NoSuchMethodException {
        Method method = DelegatingMethod.class.getDeclaredMethod("hashCode");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(1, delegatingMethod.hashCode());
    }
}
