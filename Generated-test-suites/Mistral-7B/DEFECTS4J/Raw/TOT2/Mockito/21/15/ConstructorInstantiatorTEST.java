package org.mockito.internal.creation.instance.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.instance.ConstructorInstantiator;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance() {
        Object outerClassInstance = new Object() {};
        ConstructorInstantiator instantiation = new ConstructorInstantiator(outerClassInstance);
        Class<String> stringClass = String.class;
        String instance = instantiation.newInstance(stringClass);
        Assert.assertNotNull(instance);
    }

    @Test
    public void testNewInstanceWithNullOuterClassInstance() {
        ConstructorInstantiator instantiation = new ConstructorInstantiator(null);
        Class<String> stringClass = String.class;
        try {
            instantiation.newInstance(stringClass);
            fail();
        } catch (InstantiationException e) {
            String expectedMessage = "Unable to create mock instance of 'String'.\nPlease ensure that the outer instance has correct type";
            String actualMessage = e.getMessage();
            Assert.assertEquals(expectedMessage, actualMessage);
            Throwable expectedCause = new IllegalArgumentException("outerClassInstance is marked non-null but is null");
            Throwable actualCause = e.getCause();
            Assert.assertSame(expectedCause, actualCause);
        }
    }

    @Test
    public void testNewInstanceWithNullClass() {
        Object outerClassInstance = new Object() {};
        ConstructorInstantiator instantiation = new ConstructorInstantiator(outerClassInstance);
        try {
            instantiation.newInstance(null);
            fail();
        } catch (NullPointerException e) {
            String expectedMessage = "cls is marked non-null but is null";
            String actualMessage = e.getMessage();
            Assert.assertEquals(expectedMessage, actualMessage);
        }
    }

    @Test
    public void testNewInstanceWithClassNoArgConstructor() {
        Object outerClassInstance = new Object() {};
        ConstructorInstantiator instantiation = new ConstructorInstantiator(outerClassInstance);
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        try {
            instantiation.newInstance(stringBuilderClass);
            fail();
        } catch (InstantiationException e) {
            String expectedMessage = "Unable to create mock instance of 'StringBuilder'.\nPlease ensure it has a parameter-less constructor.";
            String actualMessage = e.getMessage();
            Assert.assertEquals(expectedMessage, actualMessage);
        }
    }

    @Test
    public void testNewInstanceWithClassArgConstructor() {
        Object outerClassInstance = new Object() {};
        ConstructorInstantiator instantiation = new ConstructorInstantiator(outerClassInstance);
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        Class<Integer> integerClass = Integer.class;
        try {
            Constructor<StringBuilder> constructor = stringBuilderClass.getDeclaredConstructor(integerClass);
            instantiation.newInstance(stringBuilderClass, new Object() {});
            fail();
        } catch (InstantiationException e) {
            String expectedMessage = "Unable to create mock instance of 'StringBuilder'.\nPlease ensure it has a parameter-less constructor.";
            String actualMessage = e.getMessage();
            Assert.assertEquals(expectedMessage, actualMessage);
        }
    }

    @Test
    public void testNewInstanceWithConstructorException() {
        Object outerClassInstance = new Object() {};
        ConstructorInstantiator instantiation = new ConstructorInstantiator(outerClassInstance);
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        try {
            Constructor<StringBuilder> constructor = stringBuilderClass.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            instantiation.newInstance(stringBuilderClass, "Test");
            fail();
        } catch (InstantiationException e) {
            InstantiationException expectedException = new InstantiationException("Unable to create mock instance of 'StringBuilder'.\nPlease ensure that the outer instance has correct type and that the target class has a parameter-less constructor.", new RuntimeException("Test ConstructorException"));
            InstantiationException actualException = (InstantiationException) e;
            String expectedMessage = expectedException.getMessage();
            String actualMessage = actualException.getMessage();
            Assert.assertEquals(expectedMessage, actualMessage);
            Throwable expectedCause = new RuntimeException("Test ConstructorException");
            Throwable actualCause = actualException.getCause();
            Assert.assertSame(expectedCause, actualCause);
        }
    }
}