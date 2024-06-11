package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterInstance() {
        Class<String> stringClass = String.class;
        assertNull(new ConstructorInstantiator(null).newInstance(stringClass));
    }

    @Test
    public void testNewInstanceWithNullClass() {
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        assertNull(constructorInstantiator.newInstance(null));
    }

    @Test
    public void testNewInstanceWithParameterlessConstructor() {
        Class<String> stringClass = String.class;
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        String instance = constructorInstantiator.newInstance(stringClass);
        assertInstanceOf(String.class, instance);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithExceptionThrownByParameterlessConstructor() {
        Class<Something> somethingClass = Something.class;
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        constructorInstantiator.newInstance(somethingClass);
    }

    @Test
    public void testNewInstanceWithOneArgConstructor() {
        Class<Adder> adderClass = Adder.class;
        Object outerClassInstance = spy(new Object());
        Constructor<Adder> constructor = adderClass.getDeclaredConstructor(Object.class);
        Adder instance = new ConstructorInstantiator(outerClassInstance).newInstance(adderClass);
        assertInstanceOf(Adder.class, instance);
        assertInstanceOf(Object.class, instance.getOuterClass());
        assertEquals(outerClassInstance, instance.getOuterClass());
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithExceptionThrownByConstructorWithOneArg() {
        Class<Adder> adderClass = Adder.class;
        Class<String> stringClass = String.class;
        Object outerClassInstance = mock(Object.class);
        Constructor<Adder> constructor = adderClass.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);
        try {
            new ConstructorInstantiator(outerClassInstance).newInstance(adderClass);
        } catch (IllegalAccessException e) {
            throw new InstantiationException("Unable to create mock instance of 'Adder'.\n" +
                    "Please ensure that the outer instance has correct type and that the target class has a constructor with no arguments.\n" +
                    "Caused by: java.lang.IllegalAccessException: " + constructor.getName() + " (class org.mockito.internal.creation.instance.ConstructorInstantiator can not access a member of class org.mockito.internal.creation.instance.Adder with modifiers private)", e);
        }
    }

    @Test
    public void testWithOuterClass() {
        Class<Adder> adderClass = Adder.class;
        Object outerClassInstance = spy(new Object());
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Constructor<Adder> constructor = adderClass.getDeclaredConstructor(Object.class);
        constructor.setAccessible(true);
        Adder instance = constructorInstantiator.withOuterClass(adderClass);
        assertInstanceOf(Adder.class, instance);
        assertInstanceOf(Object.class, instance.getOuterClass());
        assertEquals(outerClassInstance, instance.getOuterClass());
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClassWithExceptionThrownByConstructor() {
        Class<Adder> adderClass = Adder.class;
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Constructor<Adder> constructor = adderClass.getDeclaredConstructor(Object.class);
        constructor.setAccessible(true);
        try {
            constructorInstantiator.withOuterClass(adderClass);
        } catch (IllegalAccessException e) {
            throw new InstantiationException("Unable to create mock instance of 'Adder'.\n" +
                    "Please ensure that the outer instance has correct type and that the target class has a constructor with no arguments.\n" +
                    "Caused by: java.lang.IllegalAccessException: " + constructor.getName() + " (class org.mockito.internal.creation.instance.ConstructorInstantiator can not access a member of class org.mockito.internal.creation.instance.Adder with modifiers private)", e);
        }
    }

    @Test
    public void testParamsException() {
        Class<Something> somethingClass = Something.class;
        Throwable cause = new RuntimeException();
        InstantiationException exception = new ConstructorInstantiator().paramsException(somethingClass, cause);
        assertEquals("Unable to create mock instance of 'Something'.\n" +
                "Please ensure that the outer instance has correct type and that the target class has parameter-less constructor.\n" +
                "Caused by: java.lang.RuntimeException", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testNoArgConstructor() {
        Class<String> stringClass = String.class;
        Object instance = new ConstructorInstantiator().noArgConstructor(stringClass);
        assertInstanceOf(String.class, instance);
    }

    @Test(expected = InstantiationException.class)
    public void testNoArgConstructorWithExceptionThrown() {
        Class<Something> somethingClass = Something.class;
        InstantiationException exception = new ConstructorInstantiator().noArgConstructor(somethingClass);
        assertEquals("Unable to create mock instance of 'Something'.\n" +
                "Please ensure it has parameter-less constructor.", exception.getMessage());
    }
}

class Adder {
    Object outerClass;

    Adder(Object outerClass) {
        this.outerClass = outerClass;
    }

    Object getOuterClass() {
        return outerClass;
    }
}

class Something {
    //empty class
}