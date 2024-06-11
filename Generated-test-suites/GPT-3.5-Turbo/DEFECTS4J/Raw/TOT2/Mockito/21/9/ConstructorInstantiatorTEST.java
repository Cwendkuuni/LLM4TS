package org.mockito.internal.creation.instance;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConstructorInstantiatorTest {

    @Test
    public void testConstructorInstantiator() {
        Object obj = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(obj);
        assertNotNull(instantiator);
        assertEquals(instantiator.outerClassInstance, obj);
    }

    @Test
    public void testNewInstanceWithNullOuterClassInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        assertNotNull(instantiator.newInstance(Object.class));
    }

    @Test
    public void testNewInstanceWithOuterClassInstance() {
        Object obj = new Object();
        ConstructorInstantiator instantiator = new ConstructorInstantiator(obj);
        assertNotNull(instantiator.newInstance(obj.getClass()));
    }

    @Test
    public void testParamsException() {
        try {
            ConstructorInstantiator.paramsException(Object.class, new Exception());
        } catch (InstantationException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testNoArgConstructor() {
        assertNotNull(ConstructorInstantiator.noArgConstructor(Object.class));
    }
}