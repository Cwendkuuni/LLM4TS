package org.mockito.internal.creation.instance;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;

public class ConstructorInstantiatorTest {

    @Test
    public void testNewInstance_noArgConstructor() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        
        // Test case for no-arg constructor
        String str = instantiator.newInstance(String.class);
        
        assertNotNull(str);
        assertEquals("", str);
    }
    
    @Test
    public void testNewInstance_withOuterClass() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator("outer");
        
        // Test case for constructor with outer class instance
        Integer integer = instantiator.newInstance(Integer.class);
        
        assertNotNull(integer);
        assertEquals(0, integer.intValue());
    }
    
    @Test
    public void testNewInstance_withOuterClass_exceptionThrown() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(123);
        
        // Test case for exception thrown when creating instance with invalid outer class instance
        try {
            instantiator.newInstance(String.class);
            fail("Expected InstantiationException to be thrown");
        } catch (InstantiationException e) {
            assertEquals("Unable to create mock instance of 'String'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }
    
    @Test
    public void testNewInstance_noArgConstructor_exceptionThrown() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        
        // Test case for exception thrown when creating instance with no-arg constructor
        try {
            instantiator.newInstance(Object.class);
            fail("Expected InstantiationException to be thrown");
        } catch (InstantiationException e) {
            assertEquals("Unable to create mock instance of 'Object'.\nPlease ensure it has parameter-less constructor.", e.getMessage());
        }
    }
}