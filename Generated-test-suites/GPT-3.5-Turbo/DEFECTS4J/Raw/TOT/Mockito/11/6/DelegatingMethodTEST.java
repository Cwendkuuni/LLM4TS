import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.DelegatingMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

public class DelegatingMethodTest {
    private DelegatingMethod delegatingMethod;

    @Before
    public void setUp() {
        Method mockMethod = createMockMethod();
        delegatingMethod = new DelegatingMethod(mockMethod);
    }

    private Method createMockMethod() {
        try {
            return this.getClass().getMethod("mockMethod");
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    @Test
    public void testGetExceptionTypes() {
        Class<?>[] expected = new Class<?>[0];
        Class<?>[] actual = delegatingMethod.getExceptionTypes();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetJavaMethod() {
        Method expected = createMockMethod();
        Method actual = delegatingMethod.getJavaMethod();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetName() {
        String expected = "mockMethod";
        String actual = delegatingMethod.getName();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetParameterTypes() {
        Class<?>[] expected = new Class<?>[0];
        Class<?>[] actual = delegatingMethod.getParameterTypes();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetReturnType() {
        Class<?> expected = void.class;
        Class<?> actual = delegatingMethod.getReturnType();

        assertEquals(expected, actual);
    }

    @Test
    public void testIsVarArgs() {
        boolean expected = false;
        boolean actual = delegatingMethod.isVarArgs();

        assertEquals(expected, actual);
    }

    @Test
    public void testIsAbstract() {
        boolean expected = false;
        boolean actual = delegatingMethod.isAbstract();

        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsWithSameObject() {
        boolean result = delegatingMethod.equals(delegatingMethod);

        assertTrue(result);
    }

    @Test
    public void testEqualsWithDifferentObject() {
        boolean result = delegatingMethod.equals("differentObject");

        assertFalse(result);
    }

    @Test
    public void testEqualsWithDelegatingMethodHavingSameInternalMethod() {
        DelegatingMethod otherMethod = new DelegatingMethod(createMockMethod());
        boolean result = delegatingMethod.equals(otherMethod);

        assertTrue(result);
    }

    @Test
    public void testEqualsWithDelegatingMethodHavingDifferentInternalMethod() {
        DelegatingMethod otherMethod = new DelegatingMethod(null);
        boolean result = delegatingMethod.equals(otherMethod);

        assertFalse(result);
    }

    @Test
    public void testEqualsWithMethodHavingSameInternalMethod() {
        Method otherMethod = createMockMethod();
        boolean result = delegatingMethod.equals(otherMethod);

        assertTrue(result);
    }

    @Test
    public void testEqualsWithMethodHavingDifferentInternalMethod() {
        Method otherMethod = null;
        boolean result = delegatingMethod.equals(otherMethod);

        assertFalse(result);
    }

    @Test
    public void testHashCode() {
        int expected = 1;
        int actual = delegatingMethod.hashCode();

        assertEquals(expected, actual);
    }
}