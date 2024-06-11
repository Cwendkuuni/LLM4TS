import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = getTestMethod("getExceptionTypes");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        // Test functionality
        assertNotNull(exceptionTypes);
        // Potential edge case: No exception types
        assertEquals(0, exceptionTypes.length);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = getTestMethod("getJavaMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method javaMethod = delegatingMethod.getJavaMethod();
        // Test functionality
        assertNotNull(javaMethod);
        // Test equality with original method
        assertEquals(method, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = getTestMethod("getName");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String name = delegatingMethod.getName();
        // Test functionality
        assertNotNull(name);
        // Potential edge case: Empty method name
        assertEquals("", name);
        // Test equality with original method
        assertEquals(method.getName(), name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = getTestMethod("getParameterTypes");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        // Test functionality
        assertNotNull(parameterTypes);
        // Potential edge case: No parameter types
        assertEquals(0, parameterTypes.length);
    }

    @Test
    public void testGetReturnType() {
        Method method = getTestMethod("getReturnType");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> returnType = delegatingMethod.getReturnType();
        // Test functionality
        assertNotNull(returnType);
        // Test equality with original method
        assertEquals(method.getReturnType(), returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = getTestMethod("isVarArgs");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isVarArgs = delegatingMethod.isVarArgs();
        // Test functionality
        assertTrue(isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = getTestMethod("isAbstract");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isAbstract = delegatingMethod.isAbstract();
        // Test functionality
        assertTrue(isAbstract);
    }

    @Test
    public void testEquals() {
        Method method = getTestMethod("equals");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        // Test functionality
        assertTrue(delegatingMethod.equals(method));
        assertFalse(delegatingMethod.equals(null));
        assertFalse(delegatingMethod.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        Method method = getTestMethod("hashCode");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        int hashCode = delegatingMethod.hashCode();
        // Test functionality
        assertEquals(1, hashCode);
    }

    private Method getTestMethod(String methodName) {
        try {
            return DelegatingMethod.class.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }
}