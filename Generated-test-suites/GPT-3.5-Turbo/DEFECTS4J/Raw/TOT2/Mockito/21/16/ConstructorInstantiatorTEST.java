package org.mockito.internal.creation.instance;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_NoOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Object result = instantiator.newInstance(String.class);
        assertNotNull(result);
        assertTrue(result instanceof String);
    }

    @Test
    public void testNewInstance_WithOuterClass() {
        String outerInstance = "outer";
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerInstance);
        Object result = instantiator.newInstance(Integer.class);
        assertNotNull(result);
        assertTrue(result instanceof Integer);
    }

    @Test
    public void testWithOuterClass_Exception() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.withOuterClass(String.class);
            fail("Expected InstantationException");
        } catch (InstantationException e) {
            // Success
        }
    }

    @Test
    public void testNoArgConstructor_Exception() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        try {
            instantiator.noArgConstructor(Integer.class);
            fail("Expected InstantationException");
        } catch (InstantationException e) {
            // Success
        }
    }
}