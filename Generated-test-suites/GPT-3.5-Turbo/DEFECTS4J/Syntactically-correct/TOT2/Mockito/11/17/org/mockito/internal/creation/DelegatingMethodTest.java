import static org.junit.Assert.*;

import org.junit.Test;
import java.lang.reflect.Method;
import org.mockito.internal.creation.DelegatingMethod;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = null; // Initialize method
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertArrayEquals(method.getExceptionTypes(), delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testGetJavaMethod() {
        Method method = null; // Initialize method
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() {
        Method method = null; // Initialize method
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method.getName(), delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method method = null; // Initialize method
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertArrayEquals(method.getParameterTypes(), delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        Method method = null; // Initialize method
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method.getReturnType(), delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method method = null; // Initialize method
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method.isVarArgs(), delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = null; // Initialize method
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals((method.getModifiers() & Modifier.ABSTRACT) != 0, delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method = null; // Initialize method
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.equals(method));
    }

    @Test
    public void testHashCode() {
        Method method = null; // Initialize method
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(1, delegatingMethod.hashCode());
    }
}