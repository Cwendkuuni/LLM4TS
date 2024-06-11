import org.junit.Test;
import org.mockito.internal.creation.DelegatingMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        
        assertNotNull(exceptionTypes);
        assertEquals(0, exceptionTypes.length);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method javaMethod = delegatingMethod.getJavaMethod();
        
        assertNotNull(javaMethod);
        assertEquals(method, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String name = delegatingMethod.getName();
        
        assertNotNull(name);
        assertEquals("testMethod", name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        
        assertNotNull(parameterTypes);
        assertEquals(0, parameterTypes.length);
    }

    @Test
    public void testGetReturnType() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> returnType = delegatingMethod.getReturnType();
        
        assertNotNull(returnType);
        assertEquals(String.class, returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isVarArgs = delegatingMethod.isVarArgs();
        
        assertFalse(isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isAbstract = delegatingMethod.isAbstract();
        
        assertFalse(isAbstract);
    }

    @Test
    public void testEquals() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        
        assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        int hashCode = delegatingMethod.hashCode();
        
        assertEquals(1, hashCode);
    }

    private Method getTestMethod() {
        try {
            return DelegatingMethodTest.class.getMethod("testMethod");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void testMethod() {
        // Test method
    }

}