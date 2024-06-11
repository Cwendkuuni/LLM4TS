package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Constructor;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClassInstance() {
        Class<String> stringClass = String.class;
        assertNull(new ConstructorInstantiator(null).newInstance(stringClass));
    }

    @Test
    public void testNewInstanceWithNullClass() {
        Object outerClassInstance = mock(Object.class);
        assertNull(new ConstructorInstantiator(outerClassInstance).newInstance(null));
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithClassWithoutNoArgConstructor() {
        Object outerClassInstance = mock(Object.class);
        Class<ComplexClass> complexClass = ComplexClass.class; // a class that does not have a no-arg constructor
        new ConstructorInstantiator(outerClassInstance).newInstance(complexClass);
    }

    @Test
    public void testNewInstanceWithClassAndNoArgConstructor() {
        Object outerClassInstance = mock(Object.class);
        Class<String> stringClass = String.class;
        String result = new ConstructorInstantiator(outerClassInstance).newInstance(stringClass);
        assertInstanceOf(String.class, result);
    }

    @Test
    public void testNewInstanceWithClassAndOuterClassConstructor() {
        Object outerClassInstance = mock(Object.class);
        Class<OuterClassWithConstructor> outerClassWithConstructor = OuterClassWithConstructor.class;
        OuterClassWithConstructor result = new ConstructorInstantiator(outerClassInstance).newInstance(outerClassWithConstructor);
        assertInstanceOf(OuterClassWithConstructor.class, result);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClassThrowsException() {
        Object outerClassInstance = mock(Object.class);
        Class<ComplexClass> complexClass = ComplexClass.class; // a class that does not have a constructor with outerClassInstance as parameter
        new ConstructorInstantiator(outerClassInstance).withOuterClass(complexClass);
    }

    @Test
    public void testWithOuterClassReturnsNewInstance() {
        Object outerClassInstance = mock(Object.class);
        Class<OuterClassWithConstructor> outerClassWithConstructor = OuterClassWithConstructor.class;
        OuterClassWithConstructor result = new ConstructorInstantiator(outerClassInstance).withOuterClass(outerClassWithConstructor);
        assertInstanceOf(OuterClassWithConstructor.class, result);
    }
}

class ComplexClass {
    // a complex class that does not have a no-arg constructor
}

class OuterClassWithConstructor {
    public OuterClassWithConstructor(Object outerInstance) {
        // constructor with outerClassInstance as parameter
    }
}