package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InstantiationException;

import java.lang.reflect.Constructor;

public class ConstructorInstantiatorTest {

    private Object outerClassInstance;

    @Before
    public void setup() {
        outerClassInstance = mock(Object.class);
    }

    @Test
    public void testNewInstance_NullOuterClass_NoArgConstructor() {
        ConstructorInstantiator instantIator = new ConstructorInstantiator(null);
        assertNull(instantIator.newInstance(String.class));
    }

    @Test
    public void testNewInstance_NullOuterClass_HasArgConstructor() {
        ConstructorInstantiator instantIator = new ConstructorInstantiator(null);
        try {
            instantIator.newInstance(String[].class);
            fail("Expected InstantiationException");
        } catch (InstantiationException expected) {
            // expected
        }
    }

    @Test
    public void testNewInstance_ValidOuterClass_NoArgConstructor() {
        ConstructorInstantiator instantIator = new ConstructorInstantiator(outerClassInstance);
        String mockedObject = instantIator.newInstance(String.class);
        assertInstanceOf(String.class, mockedObject);
    }

    @Test
    public void testNewInstance_ValidOuterClass_HasArgConstructor() {
        Class<Object> argType = Object.class;
        Constructor<Object> constructor = Object.class.getDeclaredConstructor(argType);

        ConstructorInstantiator instantIator = new ConstructorInstantiator(outerClassInstance);
        Object mockedObject = instantIator.newInstance(constructor.getParameterTypes()[0].getClass());
        assertInstanceOf(Object.class, mockedObject);
        constructor.newInstance(mockedObject);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClass_NoArgConstructor_ThrowsException() {
        ConstructorInstantiator instantIator = new ConstructorInstantiator(null);
        instantIator.withOuterClass(String.class);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClass_HasArgConstructor_NoMatchingConstructor_ThrowsException() {
        ConstructorInstantiator instantIator = new ConstructorInstantiator(outerClassInstance);
        instantIator.withOuterClass(Integer[].class);
    }

    @Test
    public void testWithOuterClass_HasArgConstructor_MatchesConstructor() {
        Class<Object> argType = Object.class;
        Constructor<Object> constructor = Object.class.getDeclaredConstructor(argType);

        ConstructorInstantiator instantIator = new ConstructorInstantiator(outerClassInstance);
        Object mockedObject = instantIator.withOuterClass(constructor.getClass());
        assertInstanceOf(Object.class, mockedObject);
        constructor.newInstance(mockedObject);
    }

    @Test(expected = InstantiationException.class)
    public void testNoArgConstructor_NoArgConstructor_ThrowsException() {
        ConstructorInstantiator instantIator = new ConstructorInstantiator(outerClassInstance);
        instantIator.noArgConstructor(String[].class);
    }

    @Test(expected = InstantiationException.class)
    public void testNoArgConstructor_HasArgConstructor_ThrowsException() {
        ConstructorInstantiator instantIator = new ConstructorInstantiator(outerClassInstance);
        instantIator.noArgConstructor(Integer.class);
    }

    @Test
    public void testNoArgConstructor_NoArgConstructor() {
        ConstructorInstantiator instantIator = new ConstructorInstantiator(outerClassInstance);
        String mockedObject = instantIator.noArgConstructor(String.class);
        assertInstanceOf(String.class, mockedObject);
    }
}