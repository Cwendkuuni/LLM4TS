import org.junit.Test;
import org.mockito.internal.creation.DelegatingMethod;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = null;
        try {
            method = DelegatingMethod.class.getDeclaredMethod("getExceptionTypes");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();

        assertNotNull(exceptionTypes);
        assertEquals("Expected 0 exception types", 0, exceptionTypes.length);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = null;
        try {
            method = DelegatingMethod.class.getDeclaredMethod("getJavaMethod");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method javaMethod = delegatingMethod.getJavaMethod();

        assertNotNull(javaMethod);
        assertEquals("Expected the method to be the same as the provided method", method, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = null;
        try {
            method = DelegatingMethod.class.getDeclaredMethod("getName");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String name = delegatingMethod.getName();

        assertNotNull(name);
        assertEquals("Expected the method name to be 'getName'", "getName", name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = null;
        try {
            method = DelegatingMethod.class.getDeclaredMethod("getParameterTypes");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();

        assertNotNull(parameterTypes);
        assertEquals("Expected 0 parameter types", 0, parameterTypes.length);
    }

    @Test
    public void testGetReturnType() {
        Method method = null;
        try {
            method = DelegatingMethod.class.getDeclaredMethod("getReturnType");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> returnType = delegatingMethod.getReturnType();

        assertNotNull(returnType);
        assertEquals("Expected the method return type to be 'Class<?>'", Class.class, returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = null;
        try {
            method = DelegatingMethod.class.getDeclaredMethod("isVarArgs");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isVarArgs = delegatingMethod.isVarArgs();

        assertFalse("Expected isVarArgs to be false", isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = null;
        try {
            method = DelegatingMethod.class.getDeclaredMethod("isAbstract");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isAbstract = delegatingMethod.isAbstract();

        assertFalse("Expected isAbstract to be false", isAbstract);
    }

    @Test
    public void testEquals() {
        Method method = null;
        try {
            method = DelegatingMethod.class.getDeclaredMethod("equals", Object.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertTrue("Expected equals to return true when comparing the same object", delegatingMethod.equals(delegatingMethod));
    }

    @Test
    public void testHashCode() {
        Method method = null;
        try {
            method = DelegatingMethod.class.getDeclaredMethod("hashCode");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        int hashCode = delegatingMethod.hashCode();

        assertEquals("Expected hashCode to return 1", 1, hashCode);
    }
}