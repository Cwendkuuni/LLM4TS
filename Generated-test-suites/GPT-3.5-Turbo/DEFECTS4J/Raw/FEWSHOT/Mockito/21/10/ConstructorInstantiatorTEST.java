package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class ConstructorInstantiatorTest {

    private ConstructorInstantiator constructorInstantiator;

    @Before
    public void setUp() {
        constructorInstantiator = new ConstructorInstantiator(null);
    }

    @Test
    public void testNewInstanceWithNoArgConstructor() {
        // Testing with a class that has a parameter-less constructor
        MyClass myClass = constructorInstantiator.newInstance(MyClass.class);
        assertNotNull(myClass);
    }

    @Test(expected = InstantationException.class)
    public void testNewInstanceWithInvalidClass() {
        // Testing with a class that does not have a parameter-less constructor
        constructorInstantiator.newInstance(InvalidClass.class);
    }

    @Test
    public void testNewInstanceWithOuterClass() {
        // Testing with a class that has an outer class instance
        OuterClass outerClass = new OuterClass();
        constructorInstantiator = new ConstructorInstantiator(outerClass);
        MyClass myClass = constructorInstantiator.newInstance(MyClass.class);
        assertNotNull(myClass);
    }

    public static class MyClass {
        public MyClass() {
            // Parameter-less constructor
        }
    }

    public static class InvalidClass {
        public InvalidClass(int value) {
            // Constructor with parameter
        }
    }

    public static class OuterClass {
        // Outer class
    }
}