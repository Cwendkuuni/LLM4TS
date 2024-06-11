package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.exceptions.base.InstantiationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithParameterlessClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Class<Object> cls = Object.class;
        Object instance = instantiator.newInstance(cls);
        Assert.assertNotNull(instance);
    }

    @Test
    public void testNewInstanceWithClassThatHasParameterlessConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Class<MyClass> cls = MyClass.class;
        MyClass instance = (MyClass) instantiator.newInstance(cls);
        Assert.assertNotNull(instance);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithClassThatHasNoParameterlessConstructor_ThrowException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Class<NoArgConstructorException> cls = NoArgConstructorException.class;
        instantiator.newInstance(cls);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithClassThatHasExceptions_ThrowParamsException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Class<HasExceptions> cls = HasExceptions.class;
        try {
            instantiator.newInstance(cls);
        } catch (InstantiationException e) {
            throw new InstantiationException("Expected params exception", e);
        }
    }

    @Test
    public void testWithOuterClassWithParameterlessClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<Object> cls = Object.class;
        Object instance = instantiator.newInstance(cls);
        Assert.assertNotNull(instance);
    }

    @Test
    public void testWithOuterClassWithClassThatHasParameterlessConstructor() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<MyClass> cls = MyClass.class;
        MyClass instance = (MyClass) instantiator.newInstance(cls);
        Assert.assertNotNull(instance);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClassWithClassThatHasNoParameterlessConstructor() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<NoArgConstructorException> cls = NoArgConstructorException.class;
        instantiator.newInstance(cls);
    }

    @Test(expected = InstantiationException.class)
    public void testWithOuterClassWithClassThatHasExceptions() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<HasExceptions> cls = HasExceptions.class;
        try {
            instantiator.newInstance(cls);
        } catch (Exception e) {
            throw new InstantiationException("Expected params exception", e);
        }
    }

    @Test
    public void testParamsException() {
        InstantiationException exception = new ConstructorInstantiator().paramsException(MyClass.class, new Exception());
        Assert.assertEquals("Unable to create mock instance of 'MyClass'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", exception.getMessage());
    }

    @Test
    public void testNoArgConstructor() {
        Object instance = ConstructorInstantiator.noArgConstructor(Object.class);
        Assert.assertNotNull(instance);
    }

    @Test(expected = InstantiationException.class)
    public void testNoArgConstructorWithException() {
        try {
            ConstructorInstantiator.noArgConstructor(MyClass.class);
        } catch (Exception e) {
            throw new InstantiationException("Expected exception", e);
        }
    }
}