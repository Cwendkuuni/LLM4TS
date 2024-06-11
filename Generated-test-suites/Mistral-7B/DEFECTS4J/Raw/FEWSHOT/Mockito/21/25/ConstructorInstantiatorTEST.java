package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InstantiationStrategy;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
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
        String instance = new ConstructorInstantiator(outerClassInstance).newInstance(stringClass);
        assertInstanceOf(String.class, instance);
    }

    @Test
    public void testNewInstanceWithConstructorThatTakesOneParam() {
        Object outerClassInstance = mock(Object.class);
        Class<String> stringClass = String.class;

        Constructor<String> constructor = stringClass.getDeclaredConstructor(Object.class);
        constructor.setAccessible(true);

        String instance = new ConstructorInstantiator(outerClassInstance).newInstance(stringClass);
        assertInstanceOf(String.class, instance);
        assertEquals(outerClassInstance, instance);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithClassThatHasNoConstructor() {
        Class<String> stringClass = String.class;
        new ConstructorInstantiator(null).newInstance(stringClass);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithClassThatHasExceptionInConstructor() {
        Object outerClassInstance = mock(Object.class);
        Class<Throwable> throwableClass = Throwable.class;
        try {
            new ConstructorInstantiator(outerClassInstance).newInstance(throwableClass);
        } catch (InstantiationException e) {
            throw e;
        }
    }

    private InstantiationException assertThrows(Class<? extends Throwable> expectedType, Runnable runnable) {
        try {
            runnable.run();
            fail("Expected exception of type " + expectedType.getName() + " not thrown.");
        } catch (Throwable throwable) {
            if (expectedType.isInstance(throwable)) {
                return (InstantiationException) throwable;
            }
            throw new AssertionError("Expected exception of type " + expectedType.getName() + ", but got " + throwable.getClass().getName() + " : " + throwable.getMessage());
        }
    }
}