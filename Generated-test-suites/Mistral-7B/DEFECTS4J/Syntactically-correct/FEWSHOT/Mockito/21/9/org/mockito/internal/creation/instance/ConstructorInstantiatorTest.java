package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceof;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;

import org.junit.Test;
import org.mockito.InstantiationException;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClassInstance() {
        Class<String> stringClass = String.class;
        InstantiationException exception = assertThrows(InstantiationException.class, () -> new ConstructorInstantiator(null).newInstance(stringClass));
        assertEquals("Unable to create mock instance of 'String'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testNewInstanceWithParameterlessConstructor() {
        Object outerClassInstance = mock(Object.class);
        Class<String> stringClass = String.class;
        String actual = new ConstructorInstantiator(outerClassInstance).newInstance(stringClass);
        assertInstanceof(String.class, actual);
        assertEquals("", actual);
    }

    @Test
    public void testNewInstanceWithConstructorThatTakesOneParameter() {
        Object outerClassInstance = mock(Object.class);
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        InstantiationException exception = assertThrows(InstantiationException.class, () -> new ConstructorInstantiator(outerClassInstance).newInstance(stringBuilderClass));
        assertEquals("Unable to create mock instance of 'java.lang.StringBuilder'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testWithOuterClass() {
        Object outerClassInstance = mock(Object.class);
        Class<String> stringClass = String.class;
        String actual = new ConstructorInstantiator(outerClassInstance).withOuterClass(stringClass);
        assertInstanceof(String.class, actual);
        assertEquals("", actual);
    }

    @Test
    public void testParamsExceptionWithStringClass() {
        Object outerClassInstance = mock(Object.class);
        Class<String> stringClass = String.class;
        InstantiationException paramsException = new ConstructorInstantiator(outerClassInstance).paramsException(stringClass, new RuntimeException());
        assertEquals("Unable to create mock instance of 'String'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", paramsException.getMessage());
    }

    @Test
    public void testNoArgConstructor() {
        Class<String> stringClass = String.class;
        String actual = ConstructorInstantiator.noArgConstructor(stringClass);
        assertInstanceof(String.class, actual);
        assertEquals("", actual);
    }
}