package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.mockito.InstantiationException;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_NullOuterInstance_NoArgConstructor() {
        Class<String> stringClass = String.class;
        InstantiationException exception = assertThrows(InstantiationException.class, () -> new ConstructorInstantiator(null).newInstance(stringClass));
        assertEquals("Unable to create mock instance of 'String'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testNewInstance_NullOuterInstance_HasArgConstructor() {
        Class<Person> personClass = Person.class;
        InstantiationException exception = assertThrows(InstantiationException.class, () -> new ConstructorInstantiator(null).newInstance(personClass));
        assertEquals("Unable to create mock instance of 'Person'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testNewInstance_NotNullOuterInstance_NoArgConstructor() {
        Object outerInstance = mock(OuterClass.class);
        Class<String> stringClass = String.class;
        String instance = new ConstructorInstantiator(outerInstance).newInstance(stringClass);
        assertInstanceOf(String.class, instance);
    }

    @Test
    public void testNewInstance_NotNullOuterInstance_HasArgConstructor() {
        Object outerInstance = mock(OuterClass.class);
        Class<Person> personClass = Person.class;
        Person instance = new ConstructorInstantiator(outerInstance).newInstance(personClass);
        assertInstanceOf(Person.class, instance);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClass_NoSuchConstructor() {
        Object outerInstance = mock(OuterClass.class);
        Class<Person> personClass = Person.class;
        new ConstructorInstantiator(outerInstance).withOuterClass(personClass);
    }

    @Test
    public void testParamsException_NullClass() {
        Exception e = new RuntimeException();
        InstantiationException exception = new ConstructorInstantiator().paramsException(null, e);
        assertEquals("Unable to create mock instance of null.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testParamsException_NullException() {
        Class<Person> personClass = Person.class;
        InstantiationException exception = new ConstructorInstantiator().paramsException(personClass, null);
        assertEquals("Unable to create mock instance of 'Person'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testNoArgConstructor_NullClass() {
        InstantiationException exception = assertThrows(InstantiationException.class, () -> new ConstructorInstantiator().noArgConstructor(null));
        assertEquals("Unable to create mock instance of null.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
    }

    @Test(expected = InstantiationException.class)
    public void testNoArgConstructor_Exception() {
        Class<Person> personClass = Person.class;
        InstantiationException exception = new ConstructorInstantiator().noArgConstructor(personClass);
        fail("Unexpected instantiation success.");
    }

    static class OuterClass {
    }

    static class Person {
        //Empty constructor for testing purposes
    }
}