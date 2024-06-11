package org.mockito.internal.creation.instance;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.creation.instance.ConstructorInstantiator;

public class ConstructorInstantiatorTest {

    @Test
    public void testConstructorInstantiator() {
       // creating ConstructorInstantiator object
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        
        // checking if the outerClassInstance is null
        assertNull("Outer Class Instance should be null", constructorInstantiator.outerClassInstance);
    }

    @Test
    public void testNewInstance_withNullOuterClass() {
        // creating ConstructorInstantiator object
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        
        // creating class object
        Class<?> cls = null;
        
        // checking if newInstance() with null outerClassInstance returns null
        assertNull("newInstance() with null outerClassInstance should return null", constructorInstantiator.newInstance(cls));
    }

    @Test
    public void testNewInstance_withNonNullOuterClass() {
        // creating constructorInstantiator object
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
        
        // creating class object
        Class<?> cls = null;
        
        // checking if newInstance() with non-null outerClassInstance returns null
        assertNull("newInstance() with non-null outerClassInstance should return null", constructorInstantiator.newInstance(cls));
    }

    @Test
    public void testWithOuterClass() {
        // creating constructorInstantiator object
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
        
        // creating class object
        Class<?> cls = null;
        
        // checking if withOuterClass() returns null
        assertNull("withOuterClass() should return null", constructorInstantiator.withOuterClass(cls));
    }

    @Test
    public void testParamsException() {
        // creating class object
        Class<?> cls = null;
        
        // checking if paramsException() throws InstantationException
        try {
            ConstructorInstantiator.paramsException(cls, new Exception());
            fail("paramsException() should throw InstantationException");
        } catch (InstantationException e) {
            // success
        }
    }

    @Test
    public void testNoArgConstructor() {
        // creating class object
        Class<?> cls = null;
        
        // checking if noArgConstructor() throws InstantationException
        try {
            ConstructorInstantiator.noArgConstructor(cls);
            fail("noArgConstructor() should throw InstantationException");
        } catch (InstantationException e) {
            // success
        }
    }

}