import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

public class DelegatingMethodTest {

    private Method testMethod;

    @Before
    public void setUp() throws Exception {
        testMethod = DelegatingMethod.class.getMethod("getName");
    }

    @Test
    public void testGetExceptionTypes() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        assertArrayEquals(testMethod.getExceptionTypes(), delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testGetJavaMethod() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        assertEquals(testMethod, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        assertEquals("getName", delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        assertArrayEquals(testMethod.getParameterTypes(), delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        assertEquals(testMethod.getReturnType(), delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        assertEquals(testMethod.isVarArgs(), delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        assertEquals(Modifier.isAbstract(testMethod.getModifiers()), delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(testMethod);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(testMethod);
        assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        assertEquals(1, delegatingMethod.hashCode());
    }
}