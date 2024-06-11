package org.junit.test;

import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClassInstance() {
        Class<String> stringClass = String.class;
        Instantiator instantiator = new ConstructorInstantiator(null);
        String instance = instantiator.newInstance(stringClass);
        assertNotNull(instance);
        assertInstanceOf(String.class, instance);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNullClass() {
        Object outerClassInstance = new Object();
        Instantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        instantiator.newInstance(null);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithoutParameterlessConstructor() {
        Class<Person> personClass = Person.class;
        Instantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(personClass);
    }

    @Test
    public <T> void testNewInstanceWithParameterlessConstructor() {
        Class<String> stringClass = String.class;
        Instantiator instantiator = new ConstructorInstantiator(null);
        String instance = instantiator.newInstance(stringClass);
        assertNotNull(instance);
        assertInstanceOf(String.class, instance);
    }

    @Test
    public void testWithOuterClass() {
        Class<Person> personClass = Person.class;
        Object outerClassInstance = new Object();
        Instantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Person instance = instantiator.newInstance(personClass);
        assertNotNull(instance);
        assertInstanceOf(Person.class, instance);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClassInvalidConstructor() {
        Class<Person> personClass = Person.class;
        Object outerClassInstance = new Object();
        Instantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        try {
            instantiator.withOuterClass(Integer.class);
        } catch (InstantiationException e) {
            throw e;
        } catch (Exception e) {
            throw new InstantiationException("Unexpected exception", e);
        }
    }

    @Test
    public void testParamsException() {
        Class<Person> personClass = Person.class;
        InstantiationException exception = ConstructorInstantiator.paramsException(personClass, new RuntimeException());
        String message = exception.getMessage();
        String expectedMessage = "Unable to create mock instance of 'Person'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.";
        assertNotNull(message);
        assertNotNull(exception.getCause());
        assertFalse(message.isEmpty());
        assertTrue(message.contains(expectedMessage));
    }

    @Test
    public void testNoArgConstructor() {
        Class<String> stringClass = String.class;
        String instance = ConstructorInstantiator.noArgConstructor(stringClass);
        assertNotNull(instance);
        assertInstanceOf(String.class, instance);
    }

    @Test(expected = InstantiationException.class)
    public void testNoArgConstructorWithException() {
        Class<Person> personClass = Person.class;
        try {
            ConstructorInstantiator.noArgConstructor(personClass);
        } catch (InstantiationException e) {
            throw e;
        } catch (Exception e) {
            throw new InstantiationException("Unexpected exception", e);
        }
    }
}

class Person {}