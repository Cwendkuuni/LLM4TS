package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.reflection.Reflection.newInstance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithNullOuterClassInstanceAndParameterlessClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        assertNull(instantiator.newInstance(String.class));
    }

    @Test
    public <T> void testNewInstanceWithNullOuterClassInstanceAndClassWithParameterlessConstructor(Class<T> cls) {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        T instance = instantiator.newInstance(cls);
        assertInstanceOf(cls, instance);
    }

    @Test(expected = InstantiationException.class)
    public <T> void testNewInstanceWithNullOuterClassInstanceAndClassWithNoParameterlessConstructor(Class<T> cls) {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        instantiator.newInstance(cls);
    }

    @Test
    public <T> void testNewInstanceWithNotNullOuterClassInstanceAndParameterlessClass(Class<T> cls) {
        Object outerClassInstance = newInstance(Object.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        T instance = instantiator.newInstance(cls);
        assertInstanceOf(cls, instance);
    }

    @Test(expected = InstantiationException.class)
    public <T> void testNewInstanceWithNotNullOuterClassInstanceAndClassWithNoParameterlessConstructor(Class<T> cls) {
        Object outerClassInstance = newInstance(Object.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        instantiator.newInstance(cls);
    }

    @Test
    public <T> void testNewInstanceWithNotNullOuterClassInstanceAndClassWithConstructorThatTakesInstanceAsParameter(Class<T> cls) throws Exception {
        Object outerClassInstance = newInstance(Object.class);
        Constructor<T> constructor = cls.getDeclaredConstructor(Object.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        T instance = instantiator.newInstance(cls);
        assertInstanceOf(cls, instance);
        assertInstanceOf(outerClassInstance.getClass(), constructor.newInstance(outerClassInstance).getClass().getDeclaredConstructors()[0].getParameterTypes()[0]);
    }

    @Test(expected = InstantiationException.class)
    public <T> void testNewInstanceWithNotNullOuterClassInstanceAndClassWithConstructorThatThrowsException(Class<T> cls) throws Exception {
        Object outerClassInstance = newInstance(Object.class);
        Constructor<T> constructor = cls.getDeclaredConstructor(Object.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        instantiator.newInstance(cls);
    }

    @Test
    public <T> void testWithOuterClassWithParameterlessClass(Class<T> cls) {
        Object outerClassInstance = newInstance(Object.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        T instance = instantiator.withOuterClass(cls);
        assertInstanceOf(cls, instance);
    }

    @Test(expected = InstantiationException.class)
    public <T> void testWithOuterClassWithClassThatHasNoParameterlessConstructor(Class<T> cls) {
        Object outerClassInstance = newInstance(Object.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        instantiator.withOuterClass(cls);
    }

    @Test(expected = InstantiationException.class)
    public <T> void testWithOuterClassWithClassThatThrowsExceptionInConstructor(Class<T> cls) throws Exception {
        Object outerClassInstance = newInstance(Object.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        instantiator.withOuterClass(cls);
    }

    @Test
    public <T> void testParamsExceptionWithNullOuterClassInstanceAndClassWithNoParameterlessConstructor(Class<T> cls) {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.newInstance(cls);
        } catch (InstantiationException e) {
            InstantiationException expectedException = ConstructorInstantiatorTest.paramsException(cls, new InstantiationException("Test Exception", new Exception()));
            assertInstanceOf(expectedException.getClass(), e.getClass());
            assertInstanceOf(expectedException.getMessage(), e.getMessage());
        }
    }

    @Test
    public <T> void testParamsExceptionWithNotNullOuterClassInstanceAndClassWithNoParameterlessConstructor(Class<T> cls) {
        Object outerClassInstance = newInstance(Object.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        try {
            instantiator.newInstance(cls);
        } catch (InstantiationException e) {
            InstantiationException expectedException = ConstructorInstantiatorTest.paramsException(cls, new InstantiationException("Test Exception", new Exception()));
            assertInstanceOf(expectedException.getClass(), e.getClass());
            assertInstanceOf(expectedException.getMessage(), e.getMessage());
        }
    }

    private static <T> InstantiationException paramsException(Class<T> cls, InstantiationException expectedException) {
        return new InstantiationException("Unexpected InstantiationException", expectedException);
    }
}