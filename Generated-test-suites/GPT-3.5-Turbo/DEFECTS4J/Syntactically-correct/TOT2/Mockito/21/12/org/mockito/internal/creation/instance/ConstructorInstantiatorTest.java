package org.mockito.internal.creation.instance;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;

public class ConstructorInstantiatorTest {

    @Test
    public void testConstructorInstantiator() {
        // Test constructor for ConstructorInstantiator
        Object outerInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerInstance);
        assertNotNull(constructorInstantiator);
    }

    @Test
    public void testNewInstance() {
        // Test newInstance method with outerClassInstance as null
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        Object instance = constructorInstantiator.newInstance(Object.class);
        assertNotNull(instance);
    }

    @Test
    public void testWithOuterClass() {
        // Test withOuterClass method with outerClassInstance provided
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
        Object instance = constructorInstantiator.withOuterClass(Object.class);
        assertNotNull(instance);
    }
    
    @Test
    public void testParamsException() {
        // Test paramsException method with invalid class
        try {
            ConstructorInstantiator.paramsException(Integer.class, new Exception());
            fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'Integer'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testNoArgConstructor() {
        // Test noArgConstructor method with invalid class
        try {
            ConstructorInstantiator.noArgConstructor(Integer.class);
            fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'Integer'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }
}