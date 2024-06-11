import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(null);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertNotNull(exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method javaMethod = new Method();
        DelegatingMethod delegatingMethod = new DelegatingMethod(javaMethod);
        assertEquals(javaMethod, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() {
        Method javaMethod = new Method();
        DelegatingMethod delegatingMethod = new DelegatingMethod(javaMethod);
        assertEquals("method", delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method javaMethod = new Method();
        DelegatingMethod delegatingMethod = new DelegatingMethod(javaMethod);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertNotNull(parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method javaMethod = new Method();
        DelegatingMethod delegatingMethod = new DelegatingMethod(javaMethod);
        assertEquals(Method.class, delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method javaMethod = new Method();
        DelegatingMethod delegatingMethod = new DelegatingMethod(javaMethod);
        assertFalse(delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method javaMethod = new Method();
        DelegatingMethod delegatingMethod = new DelegatingMethod(javaMethod);
        assertFalse(delegatingMethod.isAbstract());
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
        Method javaMethod = new Method();
        DelegatingMethod delegatingMethod = new DelegatingMethod(javaMethod);
        assertEquals(1, delegatingMethod.hashCode());
    }
}