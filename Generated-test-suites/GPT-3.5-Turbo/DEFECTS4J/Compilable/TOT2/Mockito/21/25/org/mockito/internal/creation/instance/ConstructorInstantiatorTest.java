package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        Object instance = instantiator.newInstance(Object.class);
        assertNotNull(instance);
    }

    @Test
    public void testNewInstanceWithoutOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Object instance = instantiator.newInstance(Object.class);
        assertNotNull(instance);
    }

    @Test
    public void testWithOuterClassExceptionHandling() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new Object());
        try {
            instantiator.newInstance(String.class);
            fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            // Exception caught as expected
        }
    }

    @Test
    public void testNoArgConstructorExceptionHandling() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(String.class);
            fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            // Exception caught as expected
        }
    }

}