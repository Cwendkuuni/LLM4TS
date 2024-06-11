package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        MyClass obj = instantiator.newInstance(MyClass.class);
        assertNotNull(obj);
    }

    @Test
    public void testWithOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new OuterClass());
        MyClass obj = instantiator.newInstance(MyClass.class);
        assertNotNull(obj);
    }

    @Test(expected = InstantationException.class)
    public void testNoArgConstructorException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(ClassWithNoArgConstructor.class);
    }

    @Test(expected = InstantationException.class)
    public void testWithOuterClassException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(new OuterClass());
        instantiator.newInstance(ClassWithNoArgConstructor.class);
    }

    private static class MyClass {
        // Class with parameter-less constructor
    }

    private static class ClassWithNoArgConstructor {
        public ClassWithNoArgConstructor(int value) {
            // Class with constructor that takes arguments
        }
    }

    private static class OuterClass {
        // Outer class used for testing withOuterClass method
    }
}