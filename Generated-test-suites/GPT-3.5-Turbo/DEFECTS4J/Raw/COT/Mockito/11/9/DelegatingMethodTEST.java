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
        Method mockMethod = MockitoMethod.class.getMethod("getName");
        delegatingMethod = new DelegatingMethod(mockMethod);
    }

    @Test
    public void testGetExceptionTypes() {
        Class<?>[] expected = {};
        Class<?>[] result = delegatingMethod.getExceptionTypes();
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetJavaMethod() {
        Method expected = MockitoMethod.class.getMethod("getName");
        Method result = delegatingMethod.getJavaMethod();
        assertEquals(expected, result);
    }

    @Test
    public void testGetName() {
        String expected = "getName";
        String result = delegatingMethod.getName();
        assertEquals(expected, result);
    }

    @Test
    public void testGetParameterTypes() {
        Class<?>[] expected = {};
        Class<?>[] result = delegatingMethod.getParameterTypes();
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetReturnType() {
        Class<?> expected = String.class;
        Class<?> result = delegatingMethod.getReturnType();
        assertEquals(expected, result);
    }

    @Test
    public void testIsVarArgs() {
        boolean result = delegatingMethod.isVarArgs();
        assertFalse(result);
    }

    @Test
    public void testIsAbstract() {
        boolean result = delegatingMethod.isAbstract();
        assertFalse(result);
    }

    @Test
    public void testEquals() {
        Method mockMethod = MockitoMethod.class.getMethod("getName");
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(mockMethod);
        assertTrue(delegatingMethod.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() {
        int expected = 1;
        int result = delegatingMethod.hashCode();
        assertEquals(expected, result);
    }
}