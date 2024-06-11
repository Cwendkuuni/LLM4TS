package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_NullOuterClassInstance_ReturnsNoArgConstructor() {
        Class<String> stringClass = String.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);

        String result = instantiator.newInstance(stringClass);
        assertInstanceOf(String.class, result);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_NullOuterClassInstance_ThrowsException_WhenClassHasNoArgConstructor() {
        Class<MockitoSessionSettings> settingsClass = MockitoSessionSettings.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);

        instantiator.newInstance(settingsClass);
    }

    @Test
    public void testNewInstance_NotNullOuterClassInstance_ReturnsInstanceWithOuterClass() {
        Object outerClassInstance = mock(SomeClass.class);
        Class<SomeInnerClass> innerClass = SomeInnerClass.class;

        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        SomeInnerClass result = instantiator.newInstance(innerClass);

        assertInstanceOf(SomeInnerClass.class, result);
        assertInstanceOf(SomeClass.class, result.getOuterClass());
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_NotNullOuterClassInstance_ThrowsException_WhenClassHasNoConstructor() {
        Object outerClassInstance = mock(SomeClass.class);
        Class<SomeNonExistentClass> nonExistentClass = SomeNonExistentClass.class;

        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);

        instantiator.newInstance(nonExistentClass);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_NotNullOuterClassInstance_ThrowsException_WhenConstructorThrowsException() {
        Object outerClassInstance = mock(SomeClass.class);
        Class<SomeThrownExceptionClass> thrownExceptionClass = SomeThrownExceptionClass.class;

        Constructor<SomeThrownExceptionClass> constructor = thrownExceptionClass.getDeclaredConstructor(outerClassInstance.getClass());

        try {
            constructor.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);

        instantiator.newInstance(thrownExceptionClass);
    }

    @Test
    public void testParamsException_ClassHasExceptionMessage() {
        Class<SomeClassWithMessage> someClassWithMessage = SomeClassWithMessage.class;

        InstantiationException exception = new ConstructorInstantiator().paramsException(someClassWithMessage, new RuntimeException());

        String expectedMessage = "Unable to create mock instance of '" + someClassWithMessage.getSimpleName() + "'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.";

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testParamsException_ClassHasNoMessage() {
        Class<SomeClassNoMessage> someClassNoMessage = SomeClassNoMessage.class;

        InstantiationException exception = new ConstructorInstantiator().paramsException(someClassNoMessage, new RuntimeException());

        String expectedMessage = "Unable to create mock instance of '" + someClassNoMessage.getSimpleName() + "'.\nPlease ensure it has parameter-less constructor.";

        assertEquals(expectedMessage, exception.getMessage());
    }

    private static class SomeClass {
    }

    private static class SomeInnerClass {
        SomeClass outerClass;

        SomeInnerClass(SomeClass outerClass) {
            this.outerClass = outerClass;
        }

        SomeClass getOuterClass() {
            return outerClass;
        }
    }

    private static class SomeNonExistentClass {
    }

    private static class SomeThrownExceptionClass {
        SomeClass outerClass;

        SomeThrownExceptionClass(SomeClass outerClass) {
            this.outerClass = outerClass;
        }
    }

    private static class SomeClassWithMessage {
        SomeClass outerClass;

        SomeClassWithMessage(SomeClass outerClass) {
            this.outerClass = outerClass;
        }
    }

    private static class SomeClassNoMessage {
        SomeClass outerClass;

        SomeClassNoMessage(SomeClass outerClass) {
            this.outerClass = outerClass;
        }
    }
}