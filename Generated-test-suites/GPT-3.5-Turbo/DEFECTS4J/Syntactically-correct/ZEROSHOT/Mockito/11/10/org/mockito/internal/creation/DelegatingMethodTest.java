import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.MockitoMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

public class DelegatingMethodTest {

    private DelegatingMethod delegatingMethod;

    @Before
    public void setUp() throws Exception {
        Method method = DelegatingMethod.class.getMethod("getJavaMethod");
        delegatingMethod = new DelegatingMethod(method);
    }

    @Test
    public void testGetExceptionTypes() {
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();

        assertEquals(0, exceptionTypes.length);
    }

    @Test
    public void testGetJavaMethod() {
        Method javaMethod = delegatingMethod.getJavaMethod();

        assertNotNull(javaMethod);
        assertEquals("getJavaMethod", javaMethod.getName());
    }

    @Test
    public void testGetName() {
        String name = delegatingMethod.getName();

        assertEquals("getJavaMethod", name);
    }

    @Test
    public void testGetParameterTypes() {
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();

        assertEquals(0, parameterTypes.length);
    }

    @Test
    public void testGetReturnType() {
        Class<?> returnType = delegatingMethod.getReturnType();

        assertEquals(Method.class, returnType);
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
    public void testEquals() {
        DelegatingMethod other = new DelegatingMethod(delegatingMethod.getJavaMethod());

        assertTrue(delegatingMethod.equals(other));
    }

    @Test
    public void testHashCode() {
        int hashCode = delegatingMethod.hashCode();

        assertEquals(1, hashCode);
    }
}