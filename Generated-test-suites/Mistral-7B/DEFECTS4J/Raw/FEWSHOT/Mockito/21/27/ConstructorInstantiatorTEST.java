package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertInstanceOf;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;

import org.junit.Test;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceNullOuterClass() {
        assertInstanceOf(Object.class, ConstructorInstantiator.noArgConstructor(Object.class));
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceNullOuterClassThrowsException() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        ConstructorInstantiator.noArgConstructor(String.class);
    }

    @Test
    public void testNewInstanceWithOuterClass() {
        Object outerInstance = mock(Object.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);

        String result = (String) instantiator.newInstance(String.class);
        assertInstanceOf(String.class, result);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithOuterClassThrowsException() throws Exception {
        Object outerInstance = mock(Object.class);
        Constructor<String> constructor = String.class.getDeclaredConstructor(outerInstance.getClass());
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        instantiator.withOuterClass(String.class);
    }

    @Test(expected = InstantiationException.class)
    public void testNewInstanceWithNonExistentConstructorThrowsException() {
        Object outerInstance = mock(Object.class);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);

        try {
            instantiator.withOuterClass(Integer.class);
        } catch (Exception e) {
            throw new InstantiationException("Unexpected Exception.", e);
        }
    }
}