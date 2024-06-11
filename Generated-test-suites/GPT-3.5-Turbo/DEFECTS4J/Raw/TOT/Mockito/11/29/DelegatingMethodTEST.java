import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

public class DelegatingMethodTest {

    private Method method;

    @Before
    public void setUp() {
        method = null; // initialize method
    }

    @Test
    public void testGetExceptionTypes() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        Class<?>[] expected = new Class<?>[0];
        Class<?>[] actual = delegatingMethod.getExceptionTypes();
        
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetJavaMethod() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Method expected = method;
        Method actual = delegatingMethod.getJavaMethod();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetName() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String expected = null; // set expected name
        String actual = delegatingMethod.getName();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetParameterTypes() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] expected = new Class<?>[0];
        Class<?>[] actual = delegatingMethod.getParameterTypes();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetReturnType() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> expected = null; // set expected return type
        Class<?> actual = delegatingMethod.getReturnType();

        assertEquals(expected, actual);
    }

    @Test
    public void testIsVarArgs() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean expected = false; // set expected value
        boolean actual = delegatingMethod.isVarArgs();

        assertEquals(expected, actual);
    }

    @Test
    public void testIsAbstract() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean expected = false; // set expected value
        boolean actual = delegatingMethod.isAbstract();

        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Object obj = null; // set mock object
        boolean expected = false; // set expected value
        boolean actual = delegatingMethod.equals(obj);

        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        int expected = 1;
        int actual = delegatingMethod.hashCode();

        assertEquals(expected, actual);
    }
}