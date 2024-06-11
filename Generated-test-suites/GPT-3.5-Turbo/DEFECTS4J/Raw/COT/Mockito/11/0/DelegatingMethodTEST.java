import org.junit.Test;
import org.mockito.internal.invocation.MockitoMethod;

import java.lang.reflect.Method;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] expected = method.getExceptionTypes();
        Class<?>[] actual = delegatingMethod.getExceptionTypes();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Method expected = method;
        Method actual = delegatingMethod.getJavaMethod();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetName() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String expected = method.getName();
        String actual = delegatingMethod.getName();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] expected = method.getParameterTypes();
        Class<?>[] actual = delegatingMethod.getParameterTypes();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetReturnType() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> expected = method.getReturnType();
        Class<?> actual = delegatingMethod.getReturnType();

        assertEquals(expected, actual);
    }

    @Test
    public void testIsVarArgs() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean expected = method.isVarArgs();
        boolean actual = delegatingMethod.isVarArgs();

        assertEquals(expected, actual);
    }

    @Test
    public void testIsAbstract() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean expected = Modifier.isAbstract(method.getModifiers());
        boolean actual = delegatingMethod.isAbstract();

        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsWithDelegatingMethod() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);

        boolean expected = true;
        boolean actual = delegatingMethod1.equals(delegatingMethod2);

        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsWithMethod() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean expected = true;
        boolean actual = delegatingMethod.equals(method);

        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        int expected = 1;
        int actual = delegatingMethod.hashCode();

        assertEquals(expected, actual);
    }

    // Helper method to get a test Method instance
    private Method getTestMethod() {
        try {
            return MockitoMethod.class.getDeclaredMethod("getExceptionTypes");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}

