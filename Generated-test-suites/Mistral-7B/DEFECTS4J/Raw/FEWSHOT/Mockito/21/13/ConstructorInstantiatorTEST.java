package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

public class ConstructorInstantiatorTest {

    @Test
    public void testNullOuterClassInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        assertNull(instantiator.newInstance(String.class));
    }

    @Test
    public void testHasParameterlessConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(mock(Object.class));
        assertInstanceOf(String.class, instantiator.newInstance(String.class));
    }

    @Test(expected = InstantiationException.class)
    public void testHasNoParameterlessConstructor_StringClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(mock(Object.class));
        instantiator.newInstance(Object[].class);
    }

    @Test(expected = InstantiationException.class)
    public void testHasNoParameterlessConstructor_CustomClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(mock(Object.class));
        Class<CustomClass> customClass = CustomClass.class;

        try {
            instantiator.newInstance(customClass);
        } catch (InstantiationException e) {
            throw instantiator.paramsException(customClass, e);
        }
    }

    @Test
    public void testHasConstructorWithOneArgument() {
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);

        Class<CustomClassWithOneArgConstructor> customClass = CustomClassWithOneArgConstructor.class;

        try {
            CustomClassWithOneArgConstructor instance = instantiator.newInstance(customClass);
            assertInstanceOf(CustomClassWithOneArgConstructor.class, instance);
        } catch (InstantiationException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test(expected = InstantiationException.class)
    public void testHasNoConstructorWithOneArgument() {
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);

        Class<CustomClassWithNoOneArgConstructor> customClass = CustomClassWithNoOneArgConstructor.class;

        try {
            instantiator.withOuterClass(customClass);
        } catch (InvocationTargetException e) {
            throw instantiator.paramsException(customClass, e.getCause());
        } catch (InstantiationException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    private static class CustomClass {
        //empty class
    }

    private static class CustomClassWithOneArgConstructor {
        CustomClassWithOneArgConstructor(Object arg) {
            //empty constructor
        }
    }

    private static class CustomClassWithNoOneArgConstructor {
        //error: constructor in class CustomClassWithNoOneArgConstructor cannot be applied to given types;
        // required: org.mockito.internal.creation.instance.ConstructorInstantiator
        CustomClassWithNoOneArgConstructor() {
            //empty constructor
        }
    }
}