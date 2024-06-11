package org.mockito.internal.creation.instance;
import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class ConstructorInstantiatorTest {

    // Test ConstructorInstantiator constructor
    @Test
    public void testConstructorInstantiator() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        assertNotNull(instantiator);
    }

    // Test newInstance method with no outer class instance
    @Test
    public void testNewInstanceNoOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        assertNotNull(instantiator.newInstance(String.class));
    }

    // Test newInstance method with outer class instance
    @Test
    public void testNewInstanceWithOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        assertNotNull(instantiator.newInstance(String.class));
    }

    // Test newInstance method with incorrect outer class instance
    @Test
    public void testNewInstanceWithIncorrectOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        try {
            instantiator.newInstance(Integer.class);
            fail("Expected InstantationException");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'Integer'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }

    // Test newInstance method with Exception in constructor
    @Test
    public void testNewInstanceWithException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(InvalidClass.class);
            fail("Expected InstantationException");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'InvalidClass'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }

    // Edge case: Class with no parameter-less constructor
    @Test
    public void testNewInstanceNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(ClassWithParameters.class);
            fail("Expected InstantationException");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'ClassWithParameters'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }
}

class InvalidClass {}

class ClassWithParameters {
    public ClassWithParameters(String param) {}
}