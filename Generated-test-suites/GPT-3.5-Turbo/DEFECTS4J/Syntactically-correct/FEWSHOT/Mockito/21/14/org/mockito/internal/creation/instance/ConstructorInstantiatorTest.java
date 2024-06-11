package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class ConstructorInstantiatorTest {
    private final Object outerClassInstance = mock(Object.class);

    @Test
    public void testConstructorInstantiatorWithNullOuterClassInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        String expectedMessage = "Unable to create mock instance of '"
                + Object.class.getSimpleName() + "'.\nPlease ensure it has parameter-less constructor.";
        InstantationException exception = null;
  
        try {
            instantiator.newInstance(Object.class);
        } catch (InstantationException e) {
            exception = e;
        }
        
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testConstructorInstantiatorWithNonnullOuterClassInstanceAndNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Object mockInstance = instantiator.newInstance(Object.class);
        assertNull(mockInstance);
    }

    @Test
    public void testConstructorInstantiatorWithNonnullOuterClassInstanceAndInvalidConstructorParams() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        String expectedMessage = "Unable to create mock instance of '"
                + Object.class.getSimpleName() + "'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.";
        InstantationException exception = null;
  
        try {
            instantiator.newInstance(String.class);
        } catch (InstantationException e) {
            exception = e;
        }
        
        assertEquals(expectedMessage, exception.getMessage());
    }
}