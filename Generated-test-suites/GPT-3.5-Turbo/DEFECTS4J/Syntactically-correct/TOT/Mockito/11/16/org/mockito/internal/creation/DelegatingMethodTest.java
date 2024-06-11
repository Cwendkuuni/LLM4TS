import org.junit.Test;
import org.mockito.internal.invocation.MockitoMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = null;
        try {
            method = DelegatingMethod.class.getMethod("getExceptionTypes");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();

        assertNotNull(exceptionTypes);
        assertEquals(0, exceptionTypes.length);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = null;
        try {
            method = DelegatingMethod.class.getMethod("getJavaMethod");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method javaMethod = delegatingMethod.getJavaMethod();

        assertNotNull(javaMethod);
        assertEquals("getJavaMethod", javaMethod.getName());
    }

    @Test
    public void testGetName() {
        Method method = null;
        try {
            method = DelegatingMethod.class.getMethod("getName");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String name = delegatingMethod.getName();

        assertNotNull(name);
        assertEquals("getName", name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = null;
        try {
            method = DelegatingMethod.class.getMethod("getParameterTypes");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();

        assertNotNull(parameterTypes);
        assertEquals(0, parameterTypes.length);
    }

    @Test
    public void testGetReturnType() {
        Method method = null;
        try {
            method = DelegatingMethod.class.getMethod("getReturnType");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> returnType = delegatingMethod.getReturnType();

        assertNotNull(returnType);
        assertEquals(Method.class, returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = null;
        try {
            method = DelegatingMethod.class.getMethod("isVarArgs");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isVarArgs = delegatingMethod.isVarArgs();

        assertFalse(isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = null;
        try {
            method = DelegatingMethod.class.getMethod("isAbstract");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isAbstract = delegatingMethod.isAbstract();

        assertFalse(isAbstract);
    }

    @Test
    public void testEqualsSameObject() {
        Method method = null;
        try {
            method = DelegatingMethod.class.getMethod("equals", Object.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean equals = delegatingMethod.equals(delegatingMethod);

        assertTrue(equals);
    }

    @Test
    public void testEqualsDelegatingMethodWithSameMethod() {
        Method method = null;
        try {
            method = DelegatingMethod.class.getMethod("equals", Object.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);

        boolean equals = delegatingMethod1.equals(delegatingMethod2);

        assertTrue(equals);
    }

    @Test
    public void testEqualsDifferentObjects() {
        Method method1 = null;
        Method method2 = null;
        try {
            method1 = DelegatingMethod.class.getMethod("equals", Object.class);
            method2 = DelegatingMethod.class.getMethod("hashCode");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        boolean equals = delegatingMethod1.equals(delegatingMethod2);

        assertFalse(equals);
    }

    @Test
    public void testHashCode() {
        Method method = null;
        try {
            method = DelegatingMethod.class.getMethod("hashCode");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        int hashCode = delegatingMethod.hashCode();

        assertEquals(1, hashCode);
    }

}