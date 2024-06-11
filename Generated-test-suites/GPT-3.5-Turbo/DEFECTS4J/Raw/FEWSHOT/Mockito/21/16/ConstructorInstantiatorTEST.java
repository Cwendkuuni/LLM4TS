
package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class ConstructorInstantiatorTest {

    private ConstructorInstantiator constructorInstantiator;
    private Object outerClassInstance;

    @Before
    public void setup() {
        outerClassInstance = mock(Object.class);
        constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
    }

    @Test
    public void testNoArgConstructor() {
        // Setup
        Class<?> cls = Object.class;
        Object expected = mock(cls);

        // Mocking
        doReturn(expected).when(outerClassInstance).newInstance();

        // Test
        Object result = constructorInstantiator.newInstance(cls);

        // Verify
        assertNotNull(result);
        assertEquals(expected, result);
        verify(outerClassInstance, times(1)).newInstance();
        verifyNoMoreInteractions(outerClassInstance);
    }

    @Test
    public void testWithOuterClass() {
        // Setup
        Class<?> cls = Object.class;
        Object expected = mock(cls);
        Constructor<?> constructor = mock(Constructor.class);

        // Mocking
        doReturn(constructor).when(cls).getDeclaredConstructor(outerClassInstance.getClass());
        doReturn(expected).when(constructor).newInstance(outerClassInstance);

        // Test
        Object result = constructorInstantiator.newInstance(cls);

        // Verify
        assertNotNull(result);
        assertEquals(expected, result);
        verify(cls, times(1)).getDeclaredConstructor(outerClassInstance.getClass());
        verify(constructor, times(1)).newInstance(outerClassInstance);
        verifyNoMoreInteractions(cls, constructor);
    }

    @Test(expected = InstantationException.class)
    public void testWithOuterClass_unableToCreateMockInstance() {
        // Setup
        Class<?> cls = Object.class;
        InstantationException expectedException = new InstantationException("Test exception");
        Constructor<?> constructor = mock(Constructor.class);

        // Mocking
        doReturn(constructor).when(cls).getDeclaredConstructor(outerClassInstance.getClass());
        doThrow(expectedException).when(constructor).newInstance(outerClassInstance);

        // Test
        try {
            constructorInstantiator.newInstance(cls);
        } catch (InstantationException e) {
            // Verify
            assertEquals(expectedException, e);
            verify(cls, times(1)).getDeclaredConstructor(outerClassInstance.getClass());
            verify(constructor, times(1)).newInstance(outerClassInstance);
            verifyNoMoreInteractions(cls, constructor);
            throw e;
        }
    }

    @Test(expected = InstantationException.class)
    public void testNoArgConstructor_unableToCreateMockInstance() {
        // Setup
        Class<?> cls = Object.class;
        InstantationException expectedException = new InstantationException("Test exception");

        // Mocking
        doThrow(expectedException).when(cls).newInstance();

        // Test
        try {
            constructorInstantiator.newInstance(cls);
        } catch (InstantationException e) {
            // Verify
            assertEquals(expectedException, e);
            verify(cls, times(1)).newInstance();
            verifyNoMoreInteractions(cls);
            throw e;
        }
    }

}
