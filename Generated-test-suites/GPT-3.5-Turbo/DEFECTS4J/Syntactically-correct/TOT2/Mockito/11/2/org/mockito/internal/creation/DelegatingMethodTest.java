import org.junit.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

public class DelegatingMethodTest {

    private Method testMethod = getClass().getMethods()[0];
    private DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

    @Test
    public void testGetExceptionTypes() {
        Class<?>[] expected = testMethod.getExceptionTypes();
        assertArrayEquals(expected, delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testGetJavaMethod() {
        assertEquals(testMethod, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() {
        assertEquals(testMethod.getName(), delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Class<?>[] expected = testMethod.getParameterTypes();
        assertArrayEquals(expected, delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        assertEquals(testMethod.getReturnType(), delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        assertEquals(testMethod.isVarArgs(), delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        assertEquals((testMethod.getModifiers() & Modifier.ABSTRACT) != 0, delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        DelegatingMethod anotherDelegatingMethod = new DelegatingMethod(testMethod);
        assertTrue(delegatingMethod.equals(anotherDelegatingMethod));
    }

    @Test
    public void testHashCode() {
        assertEquals(1, delegatingMethod.hashCode());
    }

    // Edge cases and error scenarios

    @Test(expected = AssertionError.class)
    public void testConstructorWithNullMethod() {
        new DelegatingMethod(null);
    }

    @Test
    public void testEqualsWithDifferentObject() {
        assertFalse(delegatingMethod.equals(new Object()));
    }

    @Test
    public void testHashCodeWithDifferentObject() {
        assertNotEquals(delegatingMethod.hashCode(), new Object().hashCode());
    }
}