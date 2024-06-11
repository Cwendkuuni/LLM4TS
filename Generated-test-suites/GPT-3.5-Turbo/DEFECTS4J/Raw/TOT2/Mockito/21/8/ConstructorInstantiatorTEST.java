package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testConstructorInstantiator() {
        Object outerInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerInstance);
        assertNotNull(constructorInstantiator);
    }

    @Test
    public void testNewInstanceWithNullOuterInstance() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        Object obj = constructorInstantiator.newInstance(Object.class);
        assertNotNull(obj);
        assertTrue(obj instanceof Object);
    }

    @Test
    public void testNewInstanceWithNonNullOuterInstance() {
        Object outerInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerInstance);
        Object obj = constructorInstantiator.newInstance(Object.class);
        assertNotNull(obj);
        assertTrue(obj instanceof Object);
    }

    @Test(expected = InstantiationException.class)
    public void testParamsException() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        constructorInstantiator.withOuterClass(Object.class);
    }

    @Test(expected = InstantiationException.class)
    public void testNoArgConstructorException() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        constructorInstantiator.noArgConstructor(String.class);
    }
}