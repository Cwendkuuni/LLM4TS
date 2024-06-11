package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import org.mockito.exceptions.base.InstantiationException;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceNullOuterClass() {
        assertNull(new ConstructorInstantiator(null).newInstance(String.class));
    }

    @Test
    public void testNewInstanceString() {
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        String instance = instantiator.newInstance(String.class);
        assertInstanceOf(String.class, instance);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceNoArgConstructorExceptionString() {
        new ConstructorInstantiator(null).newInstance(String.class);
    }

    @Test
    public void testWithOuterClassString() {
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        String instance = instantiator.withOuterClass(String.class);
        assertInstanceOf(String.class, instance);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClassConstructorExceptionString() {
        Object outerClassInstance = mock(Object.class);
        Class<?> stringClass = String.class;
        try {
            Constructor<?> constructor = stringClass.getDeclaredConstructor(outerClassInstance.getClass());
            new ConstructorInstantiator(outerClassInstance).withOuterClass(stringClass);
        } catch (NoSuchMethodException e) {
            throw new InstantiationException("Unexpected constructor exception", e);
        }
    }

    @Test
    public void testParamsExceptionString() {
        assertInstanceOf(InstantiationException.class, new ConstructorInstantiator(null).paramsException(String.class, new Exception()));
    }

    @Test
    public void testNoArgConstructorString() {
        String instance = new ConstructorInstantiator(null).noArgConstructor(String.class);
        assertInstanceOf(String.class, instance);
    }

    @Test(expected = InstantiationException.class)
    public void testNoArgConstructorExceptionString() {
        new ConstructorInstantiator(null).noArgConstructor(String.class);
    }

    @Test
    public void testNewInstanceObject() {
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Object instance = instantiator.newInstance(Object.class);
        assertInstanceOf(Object.class, instance);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceNoArgConstructorExceptionObject() {
        new ConstructorInstantiator(null).newInstance(Object.class);
    }

    @Test
    public void testWithOuterClassObject() {
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Object instance = instantiator.withOuterClass(Object.class);
        assertInstanceOf(Object.class, instance);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClassConstructorExceptionObject() {
        Object outerClassInstance = mock(Object.class);
        try {
            Constructor<?> constructor = Object.class.getDeclaredConstructor(outerClassInstance.getClass());
            new ConstructorInstantiator(outerClassInstance).withOuterClass(Object.class);
        } catch (NoSuchMethodException e) {
            throw new InstantiationException("Unexpected constructor exception", e);
        }
    }
}