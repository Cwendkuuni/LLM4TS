import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = getMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] expected = method.getExceptionTypes();
        Class<?>[] actual = delegatingMethod.getExceptionTypes();
        assertArrayEquals("getExceptionTypes() should return the same array as the underlying method", expected, actual);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = getMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method expected = method;
        Method actual = delegatingMethod.getJavaMethod();
        assertEquals("getJavaMethod() should return the same method as the underlying method", expected, actual);
    }

    @Test
    public void testGetName() {
        Method method = getMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String expected = method.getName();
        String actual = delegatingMethod.getName();
        assertEquals("getName() should return the same name as the underlying method", expected, actual);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = getMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] expected = method.getParameterTypes();
        Class<?>[] actual = delegatingMethod.getParameterTypes();
        assertArrayEquals("getParameterTypes() should return the same array as the underlying method", expected, actual);
    }

    @Test
    public void testGetReturnType() {
        Method method = getMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> expected = method.getReturnType();
        Class<?> actual = delegatingMethod.getReturnType();
        assertEquals("getReturnType() should return the same return type as the underlying method", expected, actual);
    }

    @Test
    public void testIsVarArgs() {
        Method method = getMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean expected = method.isVarArgs();
        boolean actual = delegatingMethod.isVarArgs();
        assertEquals("isVarArgs() should return the same value as the underlying method", expected, actual);
    }

    @Test
    public void testIsAbstract() {
        Method method = getMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean expected = (method.getModifiers() & Modifier.ABSTRACT) != 0;
        boolean actual = delegatingMethod.isAbstract();
        assertEquals("isAbstract() should return the same value as the underlying method", expected, actual);
    }

    @Test
    public void testEquals() {
        Method method = getMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean expected = method.equals(method);
        boolean actual = delegatingMethod.equals(method);
        assertEquals("equals() should return the same value as the underlying method's equals() method", expected, actual);
    }

    @Test
    public void testHashCode() {
        Method method = getMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        int expected = 1;
        int actual = delegatingMethod.hashCode();
        assertEquals("hashCode() should always return 1", expected, actual);
    }

    private Method getMethod() {
        try {
            return DelegatingMethod.class.getDeclaredMethod("getReturnType");
        } catch (NoSuchMethodException e) {
            fail("Failed to get method due to: " + e.getMessage());
        }
        return null;
    }
}