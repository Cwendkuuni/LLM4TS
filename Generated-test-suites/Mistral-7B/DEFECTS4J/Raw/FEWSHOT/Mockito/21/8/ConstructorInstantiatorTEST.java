package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceNullOuterClassInstance() {
        Class<Object> cls = Object.class;
        assertNull(new ConstructorInstantiator(null).newInstance(cls));
    }

    @Test
    public void testNewInstanceWithNullClass() {
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        assertNull(instantiator.newInstance(null));
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithClassWithoutNoArgConstructor() {
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<String[]> cls = String[].class;
        instantiator.newInstance(cls);
    }

    @Test
    public void testNewInstanceWithNoArgConstructor() {
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<String> cls = String.class;
        String result = instantiator.newInstance(cls);
        assertInstanceOf(String.class, result);
    }

    @Test
    public void testNewInstanceWithOuterClassConstructor() {
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<Foo> cls = Foo.class;
        Foo result = instantiator.newInstance(cls);
        assertInstanceOf(Foo.class, result);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithOuterClassConstructorThrowsException() {
        Object outerClassInstance = mock(Object.class);
        Class<Foo> cls = Foo.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        try {
            instantiator.withOuterClass(cls);
        } catch (Exception e) {
            instantiator.paramsException(cls, e);
            throw e;
        }
    }

    @Test
    public void testParamsException() {
        Object outerClassInstance = mock(Object.class);
        Class<Foo> cls = Foo.class;
        InstantiationException e = new InstantiationException("Test", new Exception());
        assertInstanceOf(InstantiationException.class, new ConstructorInstantiator(outerClassInstance).paramsException(cls, e));
    }
}

class Foo {
    //Empty class for testing
}