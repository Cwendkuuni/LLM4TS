package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import java.lang.reflect.Constructor;

public class ConstructorInstantiatorTest {
    @Test
    public void testConstructorInstantiator() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        assertNotNull(instantiator);
        assertEquals(outerClassInstance, instantiator.outerClassInstance);
    }

    @Test
    public void testNewInstanceWithNullOuterClassInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Object result = instantiator.newInstance(Object.class);
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }

    @Test
    public void testNewInstanceWithNonNullOuterClassInstance() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Object result = instantiator.newInstance(Object.class);
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }

    @Test
    public void testNewInstanceWithNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Object result = instantiator.newInstance(NoArgConstructorClass.class);
        assertNotNull(result);
        assertTrue(result instanceof NoArgConstructorClass);
    }

    @Test
    public void testNewInstanceWithInvalidOuterClassInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(InvalidOuterClassClass.class);
            fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'InvalidOuterClassClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }

    class NoArgConstructorClass {
        public NoArgConstructorClass() {}

    }

    class InvalidOuterClassClass {
        public InvalidOuterClassClass(Object outerClassInstance) {}
    }
}