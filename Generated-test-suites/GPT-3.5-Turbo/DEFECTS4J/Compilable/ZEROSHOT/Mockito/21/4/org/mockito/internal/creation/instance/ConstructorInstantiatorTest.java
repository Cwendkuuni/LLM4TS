package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstanceWithOuterClass() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
        Object instance = constructorInstantiator.newInstance(Object.class);
        assertNotNull(instance);
        assertEquals(Object.class, instance.getClass());
    }
    
    @Test
    public void testNewInstanceWithoutOuterClass() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        Object instance = constructorInstantiator.newInstance(Object.class);
        assertNotNull(instance);
        assertEquals(Object.class, instance.getClass());
    }

}