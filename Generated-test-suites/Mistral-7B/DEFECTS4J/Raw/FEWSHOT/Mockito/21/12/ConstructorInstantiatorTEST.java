package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_NullOuterClassInstance_NoArgConstructor() {
        Class<String> stringClass = String.class;
        InstantiationException exception = assertThrows(InstantiationException.class, () -> new ConstructorInstantiator(null).newInstance(stringClass));
        assertEquals("Unable to create mock instance of 'String'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testNewInstance_NullOuterClassInstance_HasArgConstructor() {
        Class<Person> personClass = Person.class;
        InstantiationException exception = assertThrows(InstantiationException.class, () -> new ConstructorInstantiator(null).newInstance(personClass));
        assertEquals("Unable to create mock instance of 'Person'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testNewInstance_NotNullOuterClassInstance_NoArgConstructor() {
        Object outerClassInstance = mock(SomeClass.class);
        Class<String> stringClass = String.class;
        String result = new ConstructorInstantiator(outerClassInstance).newInstance(stringClass);
        assertInstanceOf(String.class, result);
    }

    @Test
    public void testNewInstance_NotNullOuterClassInstance_HasArgConstructor() {
        Object outerClassInstance = mock(SomeClass.class);
        Class<Person> personClass = Person.class;
        Person result = new ConstructorInstantiator(outerClassInstance).newInstance(personClass);
        assertInstanceOf(Person.class, result);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_InvalidClass_NoArgConstructor() {
        new ConstructorInstantiator(null).newInstance(String[].class);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_InvalidClass_HasArgConstructor() {
        Object outerClassInstance = mock(SomeClass.class);
        new ConstructorInstantiator(outerClassInstance).newInstance(String[].class);
    }

    @Test
    public void testWithOuterClass_NoArgConstructor() {
        Object outerClassInstance = mock(SomeClass.class);
        Class<String> stringClass = String.class;
        String result = new ConstructorInstantiator(outerClassInstance).withOuterClass(stringClass).newInstance();
        assertInstanceOf(String.class, result);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClass_NoArgConstructor_NoSuchConstructor() {
        Object outerClassInstance = mock(SomeClass.class);
        Class<Person> personClass = Person.class;
        new ConstructorInstantiator(outerClassInstance).withOuterClass(personClass);
    }

    @Test
    public void testWithOuterClass_HasArgConstructor() {
        Object outerClassInstance = mock(SomeClass.class);
        Class<Person> personClass = Person.class;
        Constructor<Person> constructor = personClass.getDeclaredConstructor(String.class);
        Person result = new ConstructorInstantiator(outerClassInstance).withOuterClass(personClass).newInstance("John Doe");
        assertInstanceOf(Person.class, result);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClass_HasArgConstructor_NoSuchConstructor() {
        Object outerClassInstance = mock(SomeClass.class);
        Class<Person> personClass = Person.class;
        new ConstructorInstantiator(outerClassInstance).withOuterClass(personClass);
    }

    @Test
    public void testParamsException_NullClass() {
        Exception e = new Exception();
        InstantiationException exception = new ConstructorInstantiator().paramsException(null, e);
        assertNull(exception);
    }

    @Test
    public void testParamsException_NotNullClass() {
        Exception e = new Exception();
        InstantiationException exception = new ConstructorInstantiator().paramsException(String.class, e);
        assertEquals("Unable to create mock instance of 'String'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testNoArgConstructor_NullClass() {
        InstantiationException exception = assertThrows(InstantiationException.class, ConstructorInstantiator::noArgConstructor);
        assertEquals("Unable to create mock instance of 'null'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testNoArgConstructor_ValidClass() {
        String result = ConstructorInstantiator.noArgConstructor(String.class);
        assertInstanceOf(String.class, result);
    }
}