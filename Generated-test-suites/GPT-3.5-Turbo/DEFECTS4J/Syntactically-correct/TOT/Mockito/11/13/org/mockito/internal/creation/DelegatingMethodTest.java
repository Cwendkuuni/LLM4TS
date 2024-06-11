import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

public class DelegatingMethodTest {

    private DelegatingMethod delegatingMethod;
    private Method method;

    @Before
    public void setUp() throws Exception {
        method = DelegatingMethod.class.getDeclaredMethod("getExceptionTypes");
        delegatingMethod = new DelegatingMethod(method);
    }

    @Test
    public void testGetExceptionTypes() {
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertNotNull(exceptionTypes);
        assertEquals(0, exceptionTypes.length);
    }

    @Test
    public void testGetJavaMethod() {
        Method javaMethod = delegatingMethod.getJavaMethod();
        assertNotNull(javaMethod);
        assertEquals(method, javaMethod);
    }

    @Test
    public void testGetName() {
        String name = delegatingMethod.getName();
        assertNotNull(name);
        assertEquals("getExceptionTypes", name);
    }

    @Test
    public void testGetParameterTypes() {
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertNotNull(parameterTypes);
        assertEquals(0, parameterTypes.length);
    }

    @Test
    public void testGetReturnType() {
        Class<?> returnType = delegatingMethod.getReturnType();
        assertNotNull(returnType);
        assertEquals(Class[].class, returnType);
    }

    @Test
    public void testIsVarArgs() {
        boolean isVarArgs = delegatingMethod.isVarArgs();
        assertFalse(isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        boolean isAbstract = delegatingMethod.isAbstract();
        assertFalse(isAbstract);
    }

    @Test
    public void testEquals_WithDelegatingMethod() throws NoSuchMethodException {
        DelegatingMethod other = new DelegatingMethod(method);
        assertTrue(delegatingMethod.equals(other));
    }

    @Test
    public void testEquals_WithMethod() {
        assertTrue(delegatingMethod.equals(method));
    }

    @Test
    public void testHashCode() {
        int hashCode = delegatingMethod.hashCode();
        assertEquals(1, hashCode);
    }
}