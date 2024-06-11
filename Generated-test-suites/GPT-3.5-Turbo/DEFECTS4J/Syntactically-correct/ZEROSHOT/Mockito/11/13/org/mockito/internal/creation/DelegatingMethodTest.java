import org.junit.Test;
import static org.junit.Assert.*;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(null);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertNull(exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method javaMethod = new Method();
        DelegatingMethod delegatingMethod = new DelegatingMethod(javaMethod);
        Method result = delegatingMethod.getJavaMethod();
        assertEquals(javaMethod, result);
    }

    @Test
    public void testGetName() {
        Method javaMethod = new Method();
        DelegatingMethod delegatingMethod = new DelegatingMethod(javaMethod);
        String name = delegatingMethod.getName();
        assertEquals("method", name);
    }

    @Test
    public void testGetParameterTypes() {
        Method javaMethod = new Method();
        DelegatingMethod delegatingMethod = new DelegatingMethod(javaMethod);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertNull(parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method javaMethod = new Method();
        DelegatingMethod delegatingMethod = new DelegatingMethod(javaMethod);
        Class<?> returnType = delegatingMethod.getReturnType();
        assertEquals(void.class, returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method javaMethod = new Method();
        DelegatingMethod delegatingMethod = new DelegatingMethod(javaMethod);
        boolean isVarArgs = delegatingMethod.isVarArgs();
        assertFalse(isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method javaMethod = new Method();
        DelegatingMethod delegatingMethod = new DelegatingMethod(javaMethod);
        boolean isAbstract = delegatingMethod.isAbstract();
        assertFalse(isAbstract);
    }

    @Test
    public void testEquals() {
        Method javaMethod = new Method();
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(javaMethod);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(javaMethod);
        assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(null);
        int hashCode = delegatingMethod.hashCode();
        assertEquals(1, hashCode);
    }
}