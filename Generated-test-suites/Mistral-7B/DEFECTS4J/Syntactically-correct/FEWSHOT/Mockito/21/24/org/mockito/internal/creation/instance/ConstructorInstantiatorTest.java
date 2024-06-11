package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import org.mockito.exceptions.base.InstantiationException;

import java.lang.reflect.Constructor;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_NullOuterClassInstance_NoArgConstructor() {
        Class<String> stringClass = String.class;
        assertInstanceOf(String.class, ConstructorInstantiator.noArgConstructor(stringClass));
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_NullOuterClassInstance_HasArgConstructor_ThrowsException() {
        Class<String> stringClass = String.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(stringClass);
    }

    @Test
    public void testNewInstance_HasOuterClassInstance_NoArgConstructor() {
        Object outerClassInstance = mock(Object.class);
        Class<String> stringClass = String.class;
        assertInstanceOf(String.class, new ConstructorInstantiator(outerClassInstance).newInstance(stringClass));
    }

    @Test
    public void testNewInstance_HasOuterClassInstance_HasArgConstructor() {
        Object outerClassInstance = mock(Object.class);
        Class<String> stringClass = String.class;
        Constructor<String> constructor = stringClass.getDeclaredConstructor(Object.class);
        assertInstanceOf(String.class, new ConstructorInstantiator(outerClassInstance).newInstance(stringClass));
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstance_HasOuterClassInstance_InvalidConstructorType_ThrowsException() {
        Object outerClassInstance = mock(Object.class);
        Class<Number> numberClass = Number.class;
        new ConstructorInstantiator(outerClassInstance).newInstance(numberClass);
    }

    @Test
    public void testParamsException() {
        Class<String> stringClass = String.class;
        assertInstanceOf(InstantiationException.class, ConstructorInstantiator.paramsException(stringClass, new RuntimeException()));
    }
}