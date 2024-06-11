package org.mockito.internal.creation.instance;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class ConstructorInstantiatorTest {

    Object outerClassInstance = new Object();

    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        ClassA instance = constructorInstantiator.newInstance(ClassA.class);
        assertNotNull(instance);
    }

    @Test
    public void testWithOuterClass() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        ClassB instance = constructorInstantiator.newInstance(ClassB.class);
        assertNotNull(instance);
    }

    @Test
    public void testWithOuterClassException() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        try {
            ClassA instance = constructorInstantiator.newInstance(ClassA.class);
            fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'ClassA'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }

    @Test
    public void testNoArgConstructorException() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        try {
            ClassC instance = constructorInstantiator.newInstance(ClassC.class);
            fail("Expected InstantationException to be thrown");
        } catch (InstantationException e) {
            assertEquals("Unable to create mock instance of 'ClassC'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }

    public static class ClassA {
        // No-arg constructor
    }

    public static class ClassB {
        public ClassB(Object outerInstance) {
            // Constructor with outer class instance parameter
        }
    }

    public static class ClassC {
        public ClassC() {
            // No-arg constructor
            throw new RuntimeException("Exception in constructor");
        }
    }
}