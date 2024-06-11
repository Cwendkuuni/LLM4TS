
package org.mockito.internal.creation.instance;

import org.junit.Test;
import java.lang.reflect.Constructor;

import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_withOuterClass_shouldReturnInstanceWithOuterClass() {
        // Create an outer class instance
        OuterClass outerClassInstance = new OuterClass();

        // Create an instance of ConstructorInstantiator
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        // Create a target class
        TargetClass targetClass = constructorInstantiator.newInstance(TargetClass.class);

        // Assert that the returned target class instance is not null
        assertNotNull(targetClass);
        // Assert that the target class instance has the correct outer class instance
        assertEquals(outerClassInstance, targetClass.getOuterClassInstance());
    }

    @Test
    public void testNewInstance_withNullOuterClass_shouldReturnInstanceWithoutOuterClass() {
        // Create an instance of ConstructorInstantiator with null outer class instance
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);

        // Create a target class
        TargetClass targetClass = constructorInstantiator.newInstance(TargetClass.class);

        // Assert that the returned target class instance is not null
        assertNotNull(targetClass);
        // Assert that the target class instance's outer class instance is null
        assertNull(targetClass.getOuterClassInstance());
    }

    @Test
    public void testNewInstance_withClassWithoutParameterlessConstructor_shouldThrowException() {
        // Create an outer class instance
        OuterClass outerClassInstance = new OuterClass();

        // Create an instance of ConstructorInstantiator
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        // Create a class without parameterless constructor
        ClassWithoutParameterlessConstructor classWithoutParameterlessConstructor = new ClassWithoutParameterlessConstructor();

        // Assert that InstantiationException is thrown when trying to create an instance
        assertThrows(InstantationException.class, () -> {
            constructorInstantiator.newInstance(classWithoutParameterlessConstructor);
        });
    }

    @Test
    public void testNewInstance_withInvalidOuterClassType_shouldThrowException() {
        // Create an invalid outer class instance
        InvalidOuterClass invalidOuterClassInstance = new InvalidOuterClass();

        // Create an instance of ConstructorInstantiator with invalid outer class instance
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(invalidOuterClassInstance);

        // Create a target class
        TargetClass targetClass = constructorInstantiator.newInstance(TargetClass.class);

        // Assert that InstantiationException is thrown when trying to create an instance
        assertThrows(InstantationException.class, () -> {
            constructorInstantiator.newInstance(TargetClass.class);
        });
    }

    private static class OuterClass {
    }

    private static class TargetClass {
        private final OuterClass outerClassInstance;

        public TargetClass(OuterClass outerClassInstance) {
            this.outerClassInstance = outerClassInstance;
        }

        public OuterClass getOuterClassInstance() {
            return outerClassInstance;
        }
    }

    private static class ClassWithoutParameterlessConstructor {
        public ClassWithoutParameterlessConstructor(int arg) {
        }
    }

    private static class InvalidOuterClass {
    }
}
