import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.DelegatingMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.Assert.*;

public class DelegatingMethodTest {

    private Method mockMethod;

    @Before
    public void setUp() throws NoSuchMethodException {
        mockMethod = DelegatingMethod.class.getMethod("getExceptionTypes");
    }

    @Test
    public void testGetExceptionTypes() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();

        assertEquals(Arrays.class, exceptionTypes.getClass());
    }

    @Test
    public void testGetJavaMethod() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);

        Method javaMethod = delegatingMethod.getJavaMethod();

        assertEquals(mockMethod, javaMethod);
    }

    @Test
    public void testGetName() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);

        String name = delegatingMethod.getName();

        assertEquals("getExceptionTypes", name);
    }

    @Test
    public void testGetParameterTypes() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();

        assertEquals(Arrays.class, parameterTypes.getClass());
    }

    @Test
    public void testGetReturnType() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);

        Class<?> returnType = delegatingMethod.getReturnType();

        assertEquals(Class.class, returnType.getClass());
    }

    @Test
    public void testIsVarArgs() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);

        boolean isVarArgs = delegatingMethod.isVarArgs();

        assertFalse(isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method abstractMethod = null;
        try {
            abstractMethod = DelegatingMethod.class.getMethod("isAbstract");
        } catch (NoSuchMethodException e) {
            fail("Method 'isAbstract' not found");
        }

        DelegatingMethod delegatingMethod = new DelegatingMethod(abstractMethod);

        boolean isAbstract = delegatingMethod.isAbstract();

        assertTrue(isAbstract);
    }

    @Test
    public void testEquals() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);
        DelegatingMethod sameMethod = new DelegatingMethod(mockMethod);
        Method javaMethod = delegatingMethod.getJavaMethod();

        boolean equalsSameObject = delegatingMethod.equals(delegatingMethod);
        boolean equalsSameMethod = delegatingMethod.equals(sameMethod);
        boolean equalsJavaMethod = delegatingMethod.equals(javaMethod);
        boolean equalsNull = delegatingMethod.equals(null);

        assertTrue(equalsSameObject);
        assertTrue(equalsSameMethod);
        assertFalse(equalsJavaMethod);
        assertFalse(equalsNull);
    }

    @Test
    public void testHashCode() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);

        int hashCode = delegatingMethod.hashCode();

        assertEquals(1, hashCode);
    }
}