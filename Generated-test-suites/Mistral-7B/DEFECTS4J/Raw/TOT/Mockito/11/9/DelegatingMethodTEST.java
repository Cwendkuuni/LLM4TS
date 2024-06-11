package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import static org.junit.Assert.*;
import static org.mockito.internal.util.reflection.Whitebox.*;

@RunWith(MockitoJUnitRunner.class)
public class DelegatingMethodTest {

    @Test
    public void testGetName() {
        Method internalMethod = getClass().getDeclaredMethod("testMethod", int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(internalMethod);

        String expectedName = "testMethod";
        String actualName = delegatingMethod.getName();

        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetParameterTypes() {
        Method internalMethod = getClass().getDeclaredMethod("testMethod", int.class, String.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(internalMethod);

        Class<?>[] expectedTypes = {int.class, String.class};
        Class<?>[] actualTypes = delegatingMethod.getParameterTypes();

        assertArrayEquals(expectedTypes, actualTypes);
    }

    @Test
    public void testEquals() {
        Method method1 = getClass().getDeclaredMethod("testMethod1", int.class);
        Method method2 = getClass().getDeclaredMethod("testMethod1", int.class);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testNotEquals() {
        Method method1 = getClass().getDeclaredMethod("testMethod1", int.class);
        Method method2 = getClass().getDeclaredMethod("testMethod2", String.class);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertFalse(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() {
        Method method1 = getClass().getDeclaredMethod("testMethod1", int.class);
        Method method2 = getClass().getDeclaredMethod("testMethod2", String.class);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        int expectedHashCode1 = method1.hashCode();
        int expectedHashCode2 = method2.hashCode();
        int actualHashCode1 = delegatingMethod1.hashCode();
        int actualHashCode2 = delegatingMethod2.hashCode();

        assertEquals(expectedHashCode1, actualHashCode1);
        assertEquals(expectedHashCode2, actualHashCode2);
        assertNotEquals(actualHashCode1, actualHashCode2);
    }
}