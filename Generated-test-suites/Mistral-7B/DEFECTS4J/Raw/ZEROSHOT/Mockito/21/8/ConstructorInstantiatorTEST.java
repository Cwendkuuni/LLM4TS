package org.mockito.internal.creation.instance;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.InstantiationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.mockito.internal.util.reflection.Reflections.newInstance;

public class ConstructorInstantiatorTest {

    private Object outerClassInstance;
    private Instantiator instantiator;

    @Before
    public void setUp() {
        outerClassInstance = newInstance(Object.class);
        instantiator = new ConstructorInstantiator(outerClassInstance);
    }

    @Test
    public void testNullOuterClassInstance() {
        Constructor<ConstructorInstantiator> constructor = ConstructorInstantiator.class.getDeclaredConstructor();
        try {
            instantiator = constructor.newInstance();
            instantiateAndAssertThrowsException(NullPointerException.class, newClass(String.class), instantiator);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            // expected
        }
    }

    @Test
    public void testConstructorWithNoArguments() {
        T result = instantiator.newInstance(newClass(String.class));
        assertNotNull(result);
    }

    @Test
    public void testConstructorWithArguments() {
        Class<?> outerClass = Object.class;
        Constructor<Object> outerClassConstructor = outerClass.getDeclaredConstructor();
        Object outerClassInstanceWithArguments = newInstance(outerClass, new Object[]{});

        Instantiator instantiatorWithArguments = new ConstructorInstantiator(outerClassInstanceWithArguments);
        T result = instantiatorWithArguments.newInstance(newClass(String.class));
        assertNotNull(result);
    }

    @Test(expected = InstantiationException.class)
    public void testConstructorWithNoArgumentException() {
        instantiateAndAssertThrowsException(InstantiationException.class, newClass(String.class), instantiator);
    }

    @Test(expected = InstantiationException.class)
    public void testConstructorWithArgumentsException() {
        Instantiator instantiatorWithArguments = new ConstructorInstantiator(outerClassInstance);
        instantiateAndAssertThrowsException(InstantiationException.class, newClass(String.class, Integer.TYPE), instantiatorWithArguments);
    }

    private <T> Class<T> newClass(Class<?>... parameterTypes) {
        try {
            return (Class<T>) Class.forName("java.lang.String");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> void instantiateAndAssertThrowsException(Class<? extends Throwable> exceptionClass, Class<T> cls, Instantiator instantiator) {
        try {
            instantiator.newInstance(cls);
            fail("Expected " + exceptionClass.getName() + " to be thrown");
        } catch (InstantiationException e) {
            if (!(e.getCause() instanceof InstantiationException && e.getCause().getClass().isAssignableFrom(exceptionClass))) {
                throw e;
            }
        }
    }

}