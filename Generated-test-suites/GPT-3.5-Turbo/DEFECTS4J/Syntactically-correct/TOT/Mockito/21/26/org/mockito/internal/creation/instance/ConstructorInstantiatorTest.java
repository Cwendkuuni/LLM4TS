
package org.mockito.internal.creation.instance;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;

public class ConstructorInstantiatorTest {

    private static final String DUMMY_STRING = "dummy";

    private ConstructorInstantiator instantiator;

    @Before
    public void setup() {
        instantiator = new ConstructorInstantiator(null);
    }

    @Test
    public void testNewInstance_noArgConstructor() {
        // Typical use case: Class with parameter-less constructor
        // Arrange
        ClassWithNoArgConstructor expected = new ClassWithNoArgConstructor();

        // Act
        ClassWithNoArgConstructor actual = instantiator.newInstance(ClassWithNoArgConstructor.class);

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNewInstance_noArgConstructor_failure() {
        // Error scenario: Class with parameter-less constructor throws exception
        // Arrange

        // Act & Assert
        // Assert that an exception is thrown when instantiating a class with parameter-less constructor
        Assert.assertThrows(InstantationException.class, () ->
                instantiator.newInstance(ClassWithNoArgConstructorThrowable.class)
        );
    }

    @Test
    public void testNewInstance_withOuterClass() {
        // Typical use case: Class with constructor that requires outer class instance
        // Arrange
        OuterClass outerClassInstance = new OuterClass(DUMMY_STRING);
        ClassWithOuterClassConstructor expected = outerClassInstance.new ClassWithOuterClassConstructor();

        // Act
        ClassWithOuterClassConstructor actual = instantiator.newInstance(ClassWithOuterClassConstructor.class);

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNewInstance_withOuterClass_failure() {
        // Error scenario: Class with constructor that requires outer class instance but no outer class instance provided
        // Arrange

        // Act & Assert
        // Assert that an exception is thrown when instantiating a class with outer class constructor
        Assert.assertThrows(InstantationException.class, () ->
                instantiator.newInstance(ClassWithOuterClassConstructor.class)
        );
    }

    @Test
    public void testNewInstance_withOuterClass_notMatchingTypes() {
        // Error scenario: Provided outer class instance type does not match the required type for the inner class constructor
        // Arrange
        ClassWithOuterClassConstructor expected = null;

        // Act
        ClassWithOuterClassConstructor actual = instantiator.newInstance(ClassWithOuterClassConstructor.class);

        // Assert
        Assert.assertNotEquals(expected, actual);
    }

    @Test
    public void testParamsException() {
        // Error scenario: Exception occurs when creating mock instance with parameters
        // Arrange
        Exception expected = new Exception();

        // Act
        InstantationException actual = instantiator.paramsException(ClassWithNoArgConstructor.class, expected);

        // Assert
        Assert.assertEquals(expected, actual.getCause());
        Assert.assertEquals("Unable to create mock instance of 'ClassWithNoArgConstructor'.\nPlease ensure it has parameter-less constructor.",
                actual.getMessage());
    }

    @Test
    public void testParamsException_withOuterClass() {
        // Error scenario: Exception occurs when creating mock instance with outer class parameter
        // Arrange
        Exception expected = new Exception();

        // Act
        InstantationException actual = instantiator.paramsException(ClassWithOuterClassConstructor.class, expected);

        // Assert
        Assert.assertEquals(expected, actual.getCause());
        Assert.assertEquals("Unable to create mock instance of 'ClassWithOuterClassConstructor'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.",
                actual.getMessage());
    }
}

class ClassWithNoArgConstructor {

}

class ClassWithNoArgConstructorThrowable {
    public ClassWithNoArgConstructorThrowable() {
        throw new RuntimeException();
    }
}

class OuterClass {
    private final String value;

    public OuterClass(String value) {
        this.value = value;
    }

    class ClassWithOuterClassConstructor {
        public ClassWithOuterClassConstructor() {
            // Empty constructor
        }
    }
}
