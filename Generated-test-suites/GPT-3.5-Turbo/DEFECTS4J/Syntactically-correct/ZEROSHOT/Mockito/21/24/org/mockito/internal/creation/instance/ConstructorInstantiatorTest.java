package org.mockito.internal.creation.instance;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator.InstantationException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ConstructorInstantiatorTest {

    private ConstructorInstantiator constructorInstantiator;
    private Object outerClassInstance;

    @Before
    public void setUp() {
        outerClassInstance = mock(Object.class);
        constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
    }

    @Test
    public void testNewInstance_withNullOuterClassInstance() {
        ConstructorInstantiator instance = new ConstructorInstantiator(null);
        InstantationException exception = assertThrows(InstantationException.class, () -> instance.newInstance(Object.class));
        assertEquals("Unable to create mock instance of 'Object'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testNewInstance_withNonNullOuterClassInstance() {
        InstantationException exception = assertThrows(InstantationException.class, () -> constructorInstantiator.newInstance(Object.class));
        assertEquals("Unable to create mock instance of 'Object'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testNoArgConstructor() {
        InstantationException exception = assertThrows(InstantationException.class, () -> constructorInstantiator.noArgConstructor(Object.class));
        assertEquals("Unable to create mock instance of 'Object'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testWithOuterClass() {
        InstantationException exception = assertThrows(InstantationException.class, () -> constructorInstantiator.withOuterClass(Object.class));
        assertEquals("Unable to create mock instance of 'Object'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", exception.getMessage());
    }
}