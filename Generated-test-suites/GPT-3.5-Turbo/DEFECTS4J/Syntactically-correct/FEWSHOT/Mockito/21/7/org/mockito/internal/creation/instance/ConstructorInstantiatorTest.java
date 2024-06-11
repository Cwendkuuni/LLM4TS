package org.mockito.internal.creation.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ConstructorInstantiatorTest {
    
    @Test
    public void testConstructorInstantiatorWithOuterClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        assertNotNull(constructorInstantiator);
    }
    
    @Test
    public void testConstructorInstantiatorWithNullOuterClass() {
        Object outerClassInstance = null;
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        assertNotNull(constructorInstantiator);
    }
    
    @Test
    public void testNewInstanceWithOuterClass() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Object newInstance = constructorInstantiator.newInstance(Object.class);
        assertNotNull(newInstance);
        assertEquals(Object.class, newInstance.getClass());
    }
    
    @Test(expected = InstantationException.class)
    public void testNewInstanceWithOuterClassThrowsException() {
        Object outerClassInstance = new Object();
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        constructorInstantiator.newInstance(String.class);
    }
    
    @Test
    public void testNewInstanceWithNullOuterClass() {
        Object outerClassInstance = null;
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        Object newInstance = constructorInstantiator.newInstance(Object.class);
        assertNotNull(newInstance);
        assertEquals(Object.class, newInstance.getClass());
    }
    
    @Test(expected = InstantationException.class)
    public void testNewInstanceWithNullOuterClassThrowsException() {
        Object outerClassInstance = null;
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
        constructorInstantiator.newInstance(String.class);
    }
    
    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        Object newInstance = constructorInstantiator.noArgConstructor(Object.class);
        assertNotNull(newInstance);
        assertEquals(Object.class, newInstance.getClass());
    }
    
    @Test(expected = InstantationException.class)
    public void testNoArgConstructorThrowsException() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        constructorInstantiator.noArgConstructor(String.class);
    }
}